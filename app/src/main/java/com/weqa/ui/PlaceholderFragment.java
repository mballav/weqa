package com.weqa.ui;

/**
 * Created by Manish Ballav on 8/31/2017.
 */

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.weqa.R;
import com.weqa.model.HotspotCenter;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static String LOG_TAG = "WEQA-LOG";

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_ITEM_TYPE   = "item_type";
    private static final String ARG_LOCATIONS   = "locations";
    private static final String ARG_FLOOR_LEVEL   = "floor_level";
    private static final String ARG_IMAGE_BYTES = "image_bytes";
    private static final String ARG_HOTSPOT_COLOR = "hotspot_color";

    float hotspotSize;
    List<HotspotCenter> hotspotCenters = new ArrayList<HotspotCenter>();
    int originalBitmapWidth, originalBitmapHeight;

    public PlaceholderFragment() {
    }
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int itemTypeNumber, byte[] imageBytes,
                                                  String locations, String floorLevel, int hotspotColor) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_TYPE, itemTypeNumber);
        args.putString(ARG_LOCATIONS, locations);
        args.putInt(ARG_HOTSPOT_COLOR, hotspotColor);
        args.putByteArray(ARG_IMAGE_BYTES, imageBytes);
        args.putString(ARG_FLOOR_LEVEL, floorLevel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int itemTypeNumber = getArguments().getInt(ARG_ITEM_TYPE);
        String locations = getArguments().getString(ARG_LOCATIONS);
        String floorLevel = getArguments().getString(ARG_FLOOR_LEVEL);
        int hotspotColor = getArguments().getInt(ARG_HOTSPOT_COLOR);
        byte[] imageBytes = getArguments().getByteArray(ARG_IMAGE_BYTES);

        View rootView = inflater.inflate(R.layout.fragment_summary_floorplan, container, false);

        TextView floorNumberText = (TextView) rootView.findViewById(R.id.floorNumber);
        floorNumberText.setText("Floor " + floorLevel);

        TextView listIconText = (TextView) rootView.findViewById(R.id.listicontext);
        //listIconText.setTypeface(fontLatoLight);

        TextView tapToEnlarge = (TextView) rootView.findViewById(R.id.taptoenlarge);
        //tapToEnlarge.setTypeface(fontLatoLight);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        Log.d(LOG_TAG, "Image Width: " + decodedByte.getWidth());
        Log.d(LOG_TAG, "Image Height: " + decodedByte.getHeight());

        ImageView floorplan = (ImageView) rootView.findViewById(R.id.floorplan);

        originalBitmapWidth = decodedByte.getWidth();
        originalBitmapHeight = decodedByte.getHeight();

        hotspotSize = Math.min(originalBitmapWidth, originalBitmapHeight) / 15.0f;

        Bitmap copyBitmap = decodedByte.copy(Bitmap.Config.ARGB_8888, true);

        initHotspotData(locations);
        printHotspotData();

        drawHotspots(copyBitmap, hotspotColor);
        floorplan.setImageBitmap(copyBitmap);

        return rootView;
    }

    public void drawHotspots(Bitmap bMap, int hotspotColor) {

        Canvas canvas = new Canvas();
        canvas.setBitmap(bMap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(hotspotColor);
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


