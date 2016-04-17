package com.example.android.histogram;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.io.IOException;

public class histogram_page extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;

    private Image_Work im;
    private String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_histogram_page);

        user_name = getIntent().getStringExtra("user_name");

        TextView tw = (TextView) findViewById(R.id.textView2);
        tw.setText(getString(R.string.user_name, user_name));
    }

    public void builing_hisogram(View v) {
        ImageView im_v_1 = (ImageView)findViewById(R.id.image_view_main);
        ImageView im_v_2 = (ImageView)findViewById(R.id.image_view_histogram);
        im  = new Image_Work(im_v_1.getDrawingCache());
        im.get_gitogram(1512,1512);
       // im_v_2.setImageBitmap();
    }
    public void back_to_menu(View v) {
        Intent questionIntent = new Intent(histogram_page.this,
                Main.class);

        System.exit(0);
        startActivity(questionIntent);

    }

}
