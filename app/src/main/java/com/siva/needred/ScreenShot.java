package com.siva.needred;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ScreenShot extends AppCompatActivity{

    private View mMainView;
    private TextView mName;
    private TextView mHosital;
    private TextView mBlood;
    private TextView mMobile;
    private TextView mReq;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile);
        mName = (TextView) findViewById(R.id.profName);
//        mHosital = (TextView) findViewById(R.id.profHospital);
        mBlood = (TextView) findViewById(R.id.profBloodGroup);

        mReq = (TextView) findViewById(R.id.profRequirement);
        mMobile = (TextView) findViewById(R.id.profMobile);


        Intent myIntent = getIntent();
        final String name = myIntent.getStringExtra("name");
        final String blood = myIntent.getStringExtra("blood");
//        final String hospital = myIntent.getStringExtra("hospital");
        final String mobile = myIntent.getStringExtra("mobile");
        final String req = myIntent.getStringExtra("req");

        mName.setText(name);
//        mHosital.setText(hospital);
        mBlood.setText(blood);
        mMobile.setText(mobile);
        mReq.setText(req);
        takeScreenshot();

  //      screenShot(findViewById(R.id.screen));
    }
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
//            View v1 = getWindow().getDecorView().getRootView();
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            Log.e("Screenshot ",e.toString());
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }/*
    public void screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        String path = new File(
                android.os.Environment.getExternalStorageDirectory(),
                "devdeeds") + "/screenshot.jpg";
        savePic(bitmap,path);
    }
    public void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(strFileName);
            b.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


}