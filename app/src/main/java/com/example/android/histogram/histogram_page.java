package com.example.android.histogram;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class histogram_page extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;

    private Image_Work im;
    Bitmap bm_main, bm_histogram;
    private static final String KEY_COUNT = "COUNT";

    private String user_name;

    Switch list_toggle;
    ImageView image2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_histogram_page);

        image2 = (ImageView) findViewById(R.id.image_view_histogram);

        // first time the app is started get the bitmap from unknown source.
        if (savedInstanceState != null) {
            {
                bm_histogram = savedInstanceState.getParcelable("selectedImage");
            }
            image2.setImageBitmap(bm_histogram);
        }
            user_name = getIntent().getStringExtra("user_name");

            TextView tw = (TextView) findViewById(R.id.textView2);
            tw.setText(getString(R.string.user_name, user_name));

        list_toggle=(Switch)findViewById(R.id.switch1);
        list_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    image2.setVisibility(View.INVISIBLE);
                } else {
                    image2.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("selectedImage", bm_histogram);
    }

    public void builing_hisogram(View v) {
        ImageView im_v_1 = (ImageView)findViewById(R.id.image_view_main);
        ImageView im_v_2 = (ImageView)findViewById(R.id.image_view_histogram);

        bm_main = ((BitmapDrawable)im_v_1.getDrawable()).getBitmap();
        im = new Image_Work(bm_main, R.color.background_main);

        bm_histogram = im.get_gitogram(512, 512);
        im_v_2.setImageBitmap(bm_histogram);
    }

    public void back_to_menu(View v) {
        Intent questionIntent = new Intent(histogram_page.this,
                Main.class);

        System.exit(0);
        startActivity(questionIntent);

    }


}
