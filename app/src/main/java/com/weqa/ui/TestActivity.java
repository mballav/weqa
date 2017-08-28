package com.weqa.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.weqa.R;
import com.weqa.model.FloorplanImageInput;
import com.weqa.service.RetrofitBuilder;
import com.weqa.util.FloorplanImageAsyncTask;

import retrofit2.Retrofit;

public class TestActivity extends AppCompatActivity implements FloorplanImageAsyncTask.UpdateImage {

    ImageView floorplan;
    private static final String LOG_TAG = "YEZLO-LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        floorplan = (ImageView) findViewById(R.id.floorplan);
        getFloorplanImage();
    }

    private void getFloorplanImage() {
        Retrofit retrofit = RetrofitBuilder.getRetrofit();

        FloorplanImageAsyncTask runner = new FloorplanImageAsyncTask(retrofit, LOG_TAG, this);
        FloorplanImageInput input = new FloorplanImageInput();
        input.setFloorPlanId(2);
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input);
    }

    @Override
    public void updateUI(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Log.d("YEZLO_LOG", "Image Width: " + decodedByte.getWidth());
        Log.d("YEZLO_LOG", "Image Height: " + decodedByte.getHeight());
        floorplan.setImageBitmap(decodedByte);

        int width = floorplan.getWidth();
        int height = (int) (width*1.0*decodedByte.getHeight()/decodedByte.getWidth());
        floorplan.setMinimumHeight(height);
        floorplan.setMaxHeight(height);
    }

}
