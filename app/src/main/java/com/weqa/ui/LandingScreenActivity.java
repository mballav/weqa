package com.weqa.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.weqa.R;
import com.weqa.model.Authorization;
import com.weqa.model.CodeConstants;
import com.weqa.model.FloorPlan;
import com.weqa.model.FloorPlanDetail;
import com.weqa.model.FloorPlanDetailV2;
import com.weqa.model.FloorplanInputV2;
import com.weqa.model.FloorplanResponseV2;
import com.weqa.model.HotspotCenter;
import com.weqa.model.ItemTypeDetailV2;
import com.weqa.model.ResponseOrgBasedItemType;
import com.weqa.service.InstanceIdService;
import com.weqa.service.RetrofitBuilder;
import com.weqa.util.AuthorizationUtil;
import com.weqa.util.BuildingUtil;
import com.weqa.util.FloorplanV2AsyncTask;
import com.weqa.util.SharedPreferencesUtil;
import com.weqa.util.UIHelper;
import com.weqa.widget.SearchableSpinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;

public class LandingScreenActivity extends AppCompatActivity
        implements View.OnClickListener, FloorplanV2AsyncTask.UpdateFloorplan, AdapterView.OnItemSelectedListener {

    private static String LOG_TAG = "WEQA-LOG";

    private TabLayout mTabLayout;

    private final static String[] itemTypeHeadings = {"Desks", "Meeting Rooms", "Conference Rooms", "Focus Rooms"};
    private final static int[] itemTypeColors = {R.color.colorDarkGreen, R.color.colorDarkGreen,
                                                    R.color.colorDarkGreen, R.color.colorDarkGreen};
    private byte[] decodedString;
    private String floorLevel;
    private Map<Integer, String> locationsMap = new HashMap<Integer, String>();

    public static Typeface fontAwesome, fontLatoReg, fontLatoBlack, fontLatoLight;

    private SearchableSpinner spinner;
    private List items;
    private SharedPreferencesUtil util;
    private Authorization selectedBuilding;
    private int selectedBuildingId;

    private List<Authorization> compiledAuthList;

    private RelativeLayout landingScreenLayout;
    private ProgressBar progressBar;
    PhotoView floorplan;
    TextView floorNumberText;

    int originalBitmapWidth, originalBitmapHeight;
    float hotspotSize;
    List<HotspotCenter> hotspotCenters = new ArrayList<HotspotCenter>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        fontAwesome = Typeface.createFromAsset(this.getAssets(), "font/fontawesome-webfont.ttf");
        fontLatoBlack = Typeface.createFromAsset(this.getAssets(), "font/Lato-Black.ttf");
        fontLatoLight = Typeface.createFromAsset(this.getAssets(), "font/Lato-Light.ttf");
        fontLatoReg = Typeface.createFromAsset(this.getAssets(), "font/Lato-Regular.ttf");

        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        for (String itemType : itemTypeHeadings) {
            mTabLayout.addTab(mTabLayout.newTab().setText(itemType));
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                String locations = (locationsMap.get(position) == null) ? "" : locationsMap.get(position);
                updateFloorplanWithHotspots(locations, itemTypeColors[position]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        UIHelper.changeTabsFont(mTabLayout, fontLatoReg);

        floorNumberText = (TextView) findViewById(R.id.floorNumber);

        TextView cameraCircle = (TextView) findViewById(R.id.cameracircle);
        cameraCircle.setOnClickListener(this);

        spinner = (SearchableSpinner) findViewById(R.id.spinner);

        spinner.setPositiveButton("OK");
        spinner.setTitle("Buildings");

        util = new SharedPreferencesUtil(this);
        List<Authorization> authListOriginal = util.getAuthorizationInfo();
        compiledAuthList = AuthorizationUtil.removeDupliateBuildings(authListOriginal);

        items = new ArrayList();
        for (Authorization a : compiledAuthList) {
            String bName = AuthorizationUtil.getBuildingDisplayName(a);
            items.add(bName);
//            Log.d(LOG_TAG, "Inside onCreate(): " + a);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, items);
        spinner.setAdapter(arrayAdapter);

        BuildingUtil buildingUtil = new BuildingUtil(LOG_TAG, util, this);
        selectedBuilding = buildingUtil.getBuildingForSearchBar(compiledAuthList);
        selectedBuildingId = Integer.parseInt(selectedBuilding.getBuildingId());

        int index = compiledAuthList.indexOf(selectedBuilding);
        spinner.setSelection(index);

        landingScreenLayout = (RelativeLayout) findViewById(R.id.landingScreenContainer);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        spinner.setOnItemSelectedListener(this);

        floorplan = (PhotoView) findViewById(R.id.floorplan);

        updateUI();
    }

    private void fetchFloorplanAndAvailability() {
        Retrofit retrofit = RetrofitBuilder.getRetrofit();

        SharedPreferencesUtil util = new SharedPreferencesUtil(this);
        List<Authorization> authList = util.getAuthorizationInfo(selectedBuildingId);

        FloorplanV2AsyncTask runner = new FloorplanV2AsyncTask(retrofit, LOG_TAG, this);

        FloorplanInputV2 input = new FloorplanInputV2();
        input.setActionCodeItemType(CodeConstants.AC101);
        input.setActionCodeFloorPlan(CodeConstants.AC201);
        input.setBuildingId("" + selectedBuildingId);
        input.setUuid(InstanceIdService.getAppInstanceId(this));

        List<FloorPlan> floorplanList = new ArrayList<FloorPlan>();
        for (Authorization a : authList) {
            FloorPlan fp = new FloorPlan();
            fp.setFloorPlanId(Integer.parseInt(a.getFloorPlanId()));
            File floorplanFile = new File(Environment.getExternalStorageDirectory(),
                    "Pictures/floorplan_" + selectedBuildingId + "_" + a.getFloorPlanId());
            Log.d(LOG_TAG, "Checking if file exists: "
                    + "Pictures/floorplan_" + selectedBuildingId + "_" + a.getFloorPlanId());
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
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input, new Integer(selectedBuildingId));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(LOG_TAG, "Inside onItemSelected: NEW POSITION =========== " + position);

        int index = compiledAuthList.indexOf(selectedBuilding);
        if (index == position) return;

        selectedBuilding = compiledAuthList.get(position);
        selectedBuildingId = Integer.parseInt(selectedBuilding.getBuildingId());
        updateUI();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Log.d(LOG_TAG, "NOTHING SELECTED");
    }

    @Override
    public void onClick(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    public void updateUI() {
        landingScreenLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        fetchFloorplanAndAvailability();
    }

    @Override
    public void updateFloorplan(FloorplanResponseV2 fr) {

        FloorPlanDetailV2 f = fr.getFloorPlanDetails().get(0);

        Log.d(LOG_TAG, "Fetching floor level... buildingID: " + selectedBuildingId + ", floorplanID: " + f.getFloorPlanId());
        floorLevel = util.getFloorLevel(selectedBuildingId, f.getFloorPlanId().longValue());
        Log.d(LOG_TAG, "Floor LEVEL = " + floorLevel);

        floorNumberText.setText("Floor " + floorLevel);

        Map<Integer, Integer> itemTypeIdMap = new HashMap<Integer, Integer>();
        int i = 0;
        for (ResponseOrgBasedItemType ri : fr.getResponseOrgBasedItemType()) {
            itemTypeIdMap.put(ri.getItemTypeId(), i++);
        }

        locationsMap.clear();
        for (ItemTypeDetailV2 itd : f.getItemTypeDetail()) {
            locationsMap.put(itemTypeIdMap.get(itd.getItemTypeId()), itd.getImageLocation());
        }

        decodedString = null;
        if (f.getFloorImage() != null && f.getFloorImage().length() > 0) {
            String base64Image = f.getFloorImage();
            decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        }
        else {
            try {
                File floorplanFile = new File(Environment.getExternalStorageDirectory(),
                        "Pictures/floorplan_" + selectedBuildingId + "_" + f.getFloorPlanId().intValue());
                FileInputStream fis = new FileInputStream(floorplanFile);

                decodedString = new byte[(int) (floorplanFile.length())];

                fis.read(decodedString, 0, (int) (floorplanFile.length()));
                fis.close();
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        if (decodedString != null) {
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            Log.d(LOG_TAG, "Inside updateFloorplan(): decodedString.size() = " + decodedString.length);

            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            Log.d(LOG_TAG, "Image Width: " + decodedByte.getWidth());
            Log.d(LOG_TAG, "Image Height: " + decodedByte.getHeight());

            originalBitmapWidth = decodedByte.getWidth();
            originalBitmapHeight = decodedByte.getHeight();

            hotspotSize = Math.min(originalBitmapWidth, originalBitmapHeight) / 15.0f;

            floorplan.setImageBitmap(decodedByte);

            mTabLayout.getTabAt(0).select();
            String locations = (locationsMap.get(0) == null) ? "" : locationsMap.get(0);
            updateFloorplanWithHotspots(locations, itemTypeColors[0]);

            landingScreenLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        else {
            Toast.makeText(this, "Fatal Error... exiting!", Toast.LENGTH_LONG).show();
            this.finish();
        }

    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateFloorplanWithHotspots(String locations, int hotspotColor) {
        Matrix matrix = new Matrix();
        floorplan.getSuppMatrix(matrix);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        Bitmap copyBitmap = decodedByte.copy(Bitmap.Config.ARGB_8888, true);

        initHotspotData(locations);
        printHotspotData();

        drawHotspots(copyBitmap, hotspotColor);
        floorplan.setImageBitmap(copyBitmap);

        floorplan.setDisplayMatrix(matrix);
    }

    public void drawHotspots(Bitmap bMap, int hotspotColor) {

        Canvas canvas = new Canvas();
        canvas.setBitmap(bMap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(this, hotspotColor));
        paint.setAlpha(140);

        int bWidth = bMap.getWidth();
        int bHeight = bMap.getHeight();

        int halfSizeX = (int) (hotspotSize*bWidth/(2*originalBitmapWidth));
        int halfSizeY = (int) (hotspotSize*bHeight/(2*originalBitmapHeight));

        for (HotspotCenter c : hotspotCenters) {
            int startX = (int) ((c.x)*bWidth);
            int startY = (int) ((c.y)*bHeight);
            canvas.drawRect(startX-halfSizeX, startY-halfSizeY, startX+halfSizeX, startY+halfSizeY, paint);
        }
    }

    public void initHotspotData(String locations) {
        if (hotspotCenters.size() > 0) {
            hotspotCenters.removeAll(hotspotCenters);
        }
        if (locations.trim().length() == 0)
            return;
        String[] hotspotCoords = locations.split(",");
        for (String coords : hotspotCoords) {
            String[] xy = coords.split("_");
            float x = (float) (Integer.parseInt(xy[0])*1.0/originalBitmapWidth);
            float y = (float) (Integer.parseInt(xy[1])*1.0/originalBitmapHeight);
            HotspotCenter c = new HotspotCenter(x, y);
            hotspotCenters.add(c);
        }
    }

    private void printHotspotData() {
        Log.d(LOG_TAG, "HotspotSize = " + hotspotSize);
        for (HotspotCenter c : hotspotCenters) {
            Log.d(LOG_TAG, "Center: (" + c.x + ", " + c.y + ")");
        }
    }
}
