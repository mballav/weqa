package com.weqa.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.weqa.R;
import com.weqa.model.Authorization;
import com.weqa.model.CodeConstants;
import com.weqa.model.FloorPlan;
import com.weqa.model.FloorPlanDetail;
import com.weqa.model.FloorplanInput;
import com.weqa.model.FloorplanResponse;
import com.weqa.model.HotspotCenter;
import com.weqa.service.RetrofitBuilder;
import com.weqa.util.FloorplanAsyncTask;
import com.weqa.util.SharedPreferencesUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class FloorPlanActivity extends AppCompatActivity  implements View.OnClickListener, OnPhotoTapListener,
        FloorplanAsyncTask.UpdateFloorplan {

    private static final String LOG_TAG = "WEQA-LOG";

    float hotspotSize;

    List<HotspotCenter> hotspotCenters = new ArrayList<HotspotCenter>();

    boolean hotspotBooked = false;

    HotspotCenter previousHotspot = new HotspotCenter();
    PhotoView floorplan;

    int originalBitmapWidth, originalBitmapHeight;

    private int buildingId;
    private int itemTypeId;

    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan);

        Intent intent = getIntent();

        buildingId = intent.getIntExtra("buildingId", 0);
        itemTypeId = intent.getIntExtra("itemTypeId", 0);

        Log.d(LOG_TAG, "Item TYPE ID: " + itemTypeId);

        floorplan = (PhotoView) findViewById(R.id.floorplan);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);
        getFloorplanDetail();
    }

    private void getFloorplanDetail() {
        Retrofit retrofit = RetrofitBuilder.getRetrofit();

        SharedPreferencesUtil util = new SharedPreferencesUtil(this);
        List<Authorization> authList = util.getAuthorizationInfo(buildingId);

        FloorplanAsyncTask runner = new FloorplanAsyncTask(retrofit, LOG_TAG, this);

        FloorplanInput input = new FloorplanInput();
        input.setActionCode(CodeConstants.AC70);
        input.setItemTypeId(itemTypeId);

        List<FloorPlan> floorplanList = new ArrayList<FloorPlan>();
        for (Authorization a : authList) {
            FloorPlan fp = new FloorPlan();
            fp.setFloorPlanId(Integer.parseInt(a.getFloorPlanId()));
            File floorplanFile = new File(Environment.getExternalStorageDirectory(),
                    "Pictures/floorplan_" + buildingId + "_" + a.getFloorPlanId());
            Log.d(LOG_TAG, "Checking if file exists: "
                    + "Pictures/floorplan_" + buildingId + "_" + a.getFloorPlanId());
            if (floorplanFile.exists()) {
                Log.d(LOG_TAG, "File exists!");
                fp.setImageStatus(true);
            } else {
                Log.d(LOG_TAG, "File does NOT exist!");
                fp.setImageStatus(false);
            }
            floorplanList.add(fp);
        }
        input.setFloorPlan(floorplanList);
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input, new Integer(buildingId));
    }

    @Override
    public void updateFloorplan(FloorplanResponse response) {

        bar.setVisibility(View.GONE);

        Bitmap decodedByte = null;
        FloorPlanDetail f = response.getFloorPlanDetails().get(0);
        if (f.getFloorImage() != null && f.getFloorImage().length() > 0) {
            String base64Image = response.getFloorPlanDetails().get(0).getFloorImage();
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }
        else {
            try {
                File floorplanFile = new File(Environment.getExternalStorageDirectory(),
                        "Pictures/floorplan_" + buildingId + "_" + f.getFloorPlanId().intValue());
                FileInputStream fis = new FileInputStream(floorplanFile);

                byte[] temp = new byte[(int) (floorplanFile.length())];

                fis.read(temp, 0, (int) (floorplanFile.length()));
                fis.close();
                decodedByte = BitmapFactory.decodeByteArray(temp, 0, temp.length);
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        if (decodedByte != null) {
            Log.d(LOG_TAG, "Image Width: " + decodedByte.getWidth());
            Log.d(LOG_TAG, "Image Height: " + decodedByte.getHeight());

            floorplan.setImageBitmap(decodedByte);

            originalBitmapWidth = decodedByte.getWidth();
            originalBitmapHeight = decodedByte.getHeight();

            hotspotSize = Math.min(originalBitmapWidth, originalBitmapHeight) / 15.0f;

            Bitmap copyBitmap = decodedByte.copy(Bitmap.Config.ARGB_8888, true);

            initHotspotData(f);
            printHotspotData();

            drawHotspots(copyBitmap);
            floorplan.setImageBitmap(copyBitmap);

            floorplan.setOnPhotoTapListener(this);
            floorplan.setScaleLevels(0.5F, 2.5F, 4.5F);
            floorplan.setScale(0.5F);
        }

    }

    @Override
    public void onClick(View v) {
        try {
            File dataFile = new File(Environment.getExternalStorageDirectory(),
                    "Pictures/booked_hotspot_centers.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile, true));
            writer.write("Bitmap-width=" + originalBitmapWidth + "\n");
            writer.write("Bitmap-height=" + originalBitmapHeight + "\n");
            writer.write("Hotspot-size=" + hotspotSize + "\n");
            writer.write("Hotspot Center Coordinates\n");
            writer.write(previousHotspot.x + "," + previousHotspot.y + "\n");
            writer.close();
            Toast.makeText(v.getContext(), "Hotspot information saved!", Toast.LENGTH_SHORT).show();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void drawHotspots(Bitmap bMap) {

        Canvas canvas = new Canvas();
        canvas.setBitmap(bMap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(this, R.color.colorDarkGreen));
        paint.setAlpha(140);

        int bWidth = bMap.getWidth();
        int bHeight = bMap.getHeight();

        int halfSizeX = (int) (hotspotSize*bWidth/(2*originalBitmapWidth));
        int halfSizeY = (int) (hotspotSize*bHeight/(2*originalBitmapHeight));

        for (HotspotCenter c : hotspotCenters) {
            int startX = (int) ((c.x)*bWidth);
            int startY = (int) ((c.y)*bHeight);
            c.croppedBitmap = Bitmap.createBitmap(bMap,
                    startX-halfSizeX, startY-halfSizeY, 2*halfSizeX, 2*halfSizeY);
            canvas.drawRect(startX-halfSizeX, startY-halfSizeY, startX+halfSizeX, startY+halfSizeY, paint);
        }
    }

    public void initHotspotData(FloorPlanDetail f) {
        String hotspotCenterString = f.getImageLocation();
        String[] hotspotCoords = hotspotCenterString.split(",");
        for (String coords : hotspotCoords) {
            String[] xy = coords.split("_");
            float x = (float) (Integer.parseInt(xy[0])*1.0/originalBitmapWidth);
            float y = (float) (Integer.parseInt(xy[1])*1.0/originalBitmapHeight);
            HotspotCenter c = new HotspotCenter(x, y);
            hotspotCenters.add(c);
        }
    }

    private void printHotspotData() {
        Log.d(LOG_TAG, "OriginalBitmapWidth = " + originalBitmapWidth);
        Log.d(LOG_TAG, "OriginalBitmapHeight = " + originalBitmapHeight);
        Log.d(LOG_TAG, "HotspotSize = " + hotspotSize);
        for (HotspotCenter c : hotspotCenters) {
            Log.d(LOG_TAG, "Center: (" + c.x + ", " + c.y + ")");
        }
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        Matrix matrix = new Matrix();
        floorplan.getSuppMatrix(matrix);

        if (insideAvailableHotspot(x, y)) {
            if (hotspotBooked) {
                drawHotspot(previousHotspot, R.color.colorDarkGreen);
            }
            HotspotCenter c = getHotspotCenter(x, y);
            drawHotspot(c, R.color.colorDarkRed);
            hotspotBooked = true;
            previousHotspot.x = c.x;
            previousHotspot.y = c.y;
            previousHotspot.croppedBitmap = c.croppedBitmap;
        } else if (hotspotBooked) {
            HotspotCenter c2 = getHotspotCenter(x, y);
            if ((c2 != null) && previousHotspot.equals(c2)) {
                drawHotspot(previousHotspot, R.color.colorDarkGreen);
                hotspotBooked = false;
                previousHotspot.x = -1.0f;
                previousHotspot.y = -1.0f;
                previousHotspot.croppedBitmap = null;
            }
        }

        //after setImageBitmap()
        floorplan.setDisplayMatrix(matrix);
    }

    private HotspotCenter getHotspotCenter(float x, float y) {
        float halfSize = hotspotSize/2;
        for (HotspotCenter center : hotspotCenters) {
            if ((Math.abs(x - center.x)*originalBitmapWidth <= halfSize) && (Math.abs(y - center.y)*originalBitmapHeight <= halfSize)) {
                return center;
            }
        }
        return null;
    }

    private boolean anotherHotspotTooNear(float x, float y) {
        for (HotspotCenter center : hotspotCenters) {
            if ((Math.abs(x - center.x)*originalBitmapWidth < hotspotSize) && (Math.abs(y - center.y)*originalBitmapHeight < hotspotSize))
                return true;
        }
        return false;
    }

    private boolean insideAvailableHotspot(float x, float y) {
        float halfSize = hotspotSize/2.0f;
        for (HotspotCenter center : hotspotCenters) {
            if (center.equals(previousHotspot)) continue;
            if ((Math.abs(x - center.x)*originalBitmapWidth <= halfSize) && (Math.abs(y - center.y)*originalBitmapHeight <= halfSize))
                return true;
        }
        return false;
    }

    public void drawHotspot(HotspotCenter center, int color) {
        Bitmap floorplanBitmap = ((BitmapDrawable) floorplan.getDrawable()).getBitmap();

        Bitmap copyBitmap = floorplanBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas();
        canvas.setBitmap(copyBitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(this, color));
        paint.setAlpha(140);

        float bWidth = floorplanBitmap.getWidth();
        float bHeight = floorplanBitmap.getHeight();

        int halfSizeX = (int) (hotspotSize*bWidth/(2.0*originalBitmapWidth));
        int halfSizeY = (int) (hotspotSize*bHeight/(2.0*originalBitmapHeight));

        int startX = (int) (center.x*bWidth);
        int startY = (int) (center.y*bHeight);

        canvas.drawBitmap(center.croppedBitmap, (startX - halfSizeX), (startY - halfSizeY), null);
        canvas.drawRect(startX-halfSizeX, startY-halfSizeY, startX+halfSizeX, startY+halfSizeY, paint);

        floorplan.setImageBitmap(copyBitmap);

    }

}
