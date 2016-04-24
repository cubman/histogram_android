package com.example.android.histogram;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.graphics.*;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.*;
import android.widget.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

public class histogram_page extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1; // флаг запроса
    private  int IMAGE_SIZE_WIDTH ;
    private  int IMAGE_SIZE_HEIGHT;
    private  int MAIN_IMAGE_HEIGHT;
    private  int MAIN_IMAGE_WIDTH;

    private Image_Work im;                        // класс для работы с изображениями
    Bitmap bm_main, bm_histogram, original_image, negative_image;

    private List<Map.Entry<String, String>> colors = new LinkedList<>();
    private String user_name;

    Switch list_toggle;
    ImageView image1, image2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.activity_histogram_page);

            IMAGE_SIZE_WIDTH = getResources().getInteger(R.integer.histogram_width);
            IMAGE_SIZE_HEIGHT = (int)(IMAGE_SIZE_WIDTH * 0.75);

            MAIN_IMAGE_HEIGHT = getResources().getInteger(R.integer.main_inmage_max_height);
            MAIN_IMAGE_WIDTH = getResources().getInteger(R.integer.main_inmage_max_widtht);

            image1 = (ImageView) findViewById(R.id.image_view_main);
            image2 = (ImageView) findViewById(R.id.image_view_histogram);

            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
                image2.getLayoutParams().height = (int)getResources().getDimension(R.dimen.image_height);
                image2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            };

            colors.add(new AbstractMap.SimpleEntry<String, String>("#F5F5F5","#000000"));
            colors.add(new AbstractMap.SimpleEntry<String, String>("#000000","#F5F5F5"));

            if (savedInstanceState != null) {
                {
                    bm_main = savedInstanceState.getParcelable("selectedImage1");
                    bm_histogram = savedInstanceState.getParcelable("selectedImage2");
                    original_image = savedInstanceState.getParcelable("selectedImage_original");
                    negative_image = savedInstanceState.getParcelable("selectedImage_negative");
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

            TextView tw = (TextView) findViewById(R.id.welcome_user_1);
            tw.setText(getString(R.string.user_name, user_name));

            list_toggle = (Switch) findViewById(R.id.switch1);

            list_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (bm_histogram != null) {
                        bm_main = ((BitmapDrawable) image1.getDrawable()).getBitmap();
                        im = new Image_Work(bm_main, R.color.background_main);
                        if (isChecked)
                            // image2.setVisibility(View.INVISIBLE);
                            bm_histogram = negative_image;
                        else
                            // image2.setVisibility(View.VISIBLE);
                            bm_histogram = original_image;
                        image2.setImageBitmap(bm_histogram);

                    }

                }
            });
        }
        catch (Exception e) {
            Toast.makeText(this, "Some problem dowloading datas", Toast.LENGTH_SHORT).show();
        }
    }

    // возвращает произвольную фоторафию из списка содержимого
    private int get_ramdom_image() {
        int [] l = new int[]{R.drawable.test1,R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test5, R.drawable.test6,  R.drawable.test7};

        return l[new Random().nextInt(l.length)];
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable("selectedImage1", bm_main);
        savedInstanceState.putParcelable("selectedImage2", bm_histogram);
        savedInstanceState.putParcelable("selectedImage_original", original_image);
        savedInstanceState.putParcelable("selectedImage_negative", negative_image);
    }

    // рисует гистограмму
    public void builing_hisogram(View v) {
        if (bm_histogram == null)
        try {
            bm_main = ((BitmapDrawable) image1.getDrawable()).getBitmap();
            im = new Image_Work(bm_main, R.color.background_main);

            original_image = im.get_gitogram(IMAGE_SIZE_WIDTH, IMAGE_SIZE_HEIGHT, colors.get(0).getKey(), colors.get(0).getValue());
            negative_image = im.get_gitogram(IMAGE_SIZE_WIDTH, IMAGE_SIZE_HEIGHT, colors.get(1).getKey(), colors.get(1).getValue());

            if (list_toggle.isChecked())
                bm_histogram = negative_image;
            else bm_histogram = original_image;

            image2.setImageBitmap(bm_histogram);
        }
        catch (Exception e) {
            Toast.makeText(this, "Histogram could be built", Toast.LENGTH_SHORT).show();
        }
    }

   /* // Кнопка "назад"
    public void back_to_menu(View v) {
        finish();
        super.onBackPressed();
    }*/

    // Загрузка изображения из галереи
    public void load_picture_from_gallary(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                BitmapFactory.Options options = new BitmapFactory.Options();

                bm_main = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));


              if (bm_main.getHeight() > MAIN_IMAGE_HEIGHT || bm_main.getWidth() > MAIN_IMAGE_WIDTH) {
                    options.inSampleSize = getResources().getInteger(R.integer.image_scale);
                   bm_main = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                }

                if (bm_main == null)
                    Toast.makeText(this, "Image wasn't dowloaded!", Toast.LENGTH_SHORT).show();

                image1.setImageBitmap(bm_main);
                image2.setImageBitmap(bm_histogram = null);
            }
        }
        catch(Exception e){
                Toast.makeText(this, "Some problem in gallery generating!", Toast.LENGTH_SHORT).show();

            }
        }
}
