package com.example.android.histogram;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

public class histogram_page extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;
    private static final  int IMAGE_SIZE = 512;

    private Image_Work im;
    Bitmap bm_main, bm_histogram;

    private String user_name;

    Switch list_toggle;
    ImageView image1, image2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_histogram_page);

            image1 = (ImageView) findViewById(R.id.image_view_main);
            image2 = (ImageView) findViewById(R.id.image_view_histogram);


            if (savedInstanceState != null) {
                {
                    bm_main = savedInstanceState.getParcelable("selectedImage1");
                    bm_histogram = savedInstanceState.getParcelable("selectedImage2");
                }
                image1.setImageBitmap(bm_main);
                image2.setImageBitmap(bm_histogram);
            }
            else {
                bm_main = BitmapFactory.decodeResource(getResources(), get_ramdom_image());
                image1.setImageBitmap(bm_main);
                image2.setImageDrawable(null);
            }
            user_name = getIntent().getStringExtra("user_name");

            TextView tw = (TextView) findViewById(R.id.textView2);
            tw.setText(getString(R.string.user_name, user_name));

            list_toggle = (Switch) findViewById(R.id.switch1);
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
        catch (Exception e) {
            Toast.makeText(this, "Some problem dowloading datas", Toast.LENGTH_SHORT).show();
        }
    }

    private int get_ramdom_image() {
        int [] l = new int[]{R.drawable.test1,R.drawable.test2,R.drawable.test3};

        return l[new Random().nextInt(l.length)];
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("selectedImage1", bm_main);
        savedInstanceState.putParcelable("selectedImage2", bm_histogram);
    }

    public void builing_hisogram(View v) {
        try {
            bm_main = ((BitmapDrawable) image1.getDrawable()).getBitmap();
            im = new Image_Work(bm_main, R.color.background_main);

            bm_histogram = im.get_gitogram(IMAGE_SIZE, IMAGE_SIZE);
            image2.setImageBitmap(bm_histogram);
        }
        catch (Exception e) {
            Toast.makeText(this, "Histogram could be built", Toast.LENGTH_SHORT).show();
        }
    }

    // Кнопка "назад"
    public void back_to_menu(View v) {
        finish();
        super.onBackPressed();
    }

    // Загрузка изображения из галереи
    public void load_picture_from_gallary(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }


    // действия по запуску галереи
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    try {
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {

            Uri pickedImage = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();

            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bm_main = BitmapFactory.decodeFile(imagePath);

            if (bm_main.getWidth() > 950 || bm_main.getHeight() > 720)
                bm_main = Bitmap.createScaledBitmap(bm_main, (int) (bm_main.getWidth() * 0.2), (int) (bm_main.getHeight() * 0.2), false);
            image1.setImageBitmap(bm_main);


            cursor.close();
            image2.setImageBitmap(bm_histogram = null);
        }
    } catch (Exception e) {
        Toast.makeText(this, "Some problem in gallery generating", Toast.LENGTH_SHORT).show();
    }
}
}
