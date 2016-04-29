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

public class HistogramPage extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1; // флаг запроса
    private  int IMAGE_SIZE_WIDTH ;
    private  int IMAGE_SIZE_HEIGHT;
    private  int MAIN_IMAGE_HEIGHT;
    private  int MAIN_IMAGE_WIDTH;

    private ImageWork Im;                        // класс для работы с изображениями
    Bitmap BmMain, BmHistogram, OriginalImage, NegativeImage;

    private List<Map.Entry<String, String>> Colors = new LinkedList<>();
    private String UserName;

    Switch ListToggle;
    ImageView Image1, Image2;
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

            Image1 = (ImageView) findViewById(R.id.image_view_main);
            Image2 = (ImageView) findViewById(R.id.image_view_histogram);

            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
                Image2.getLayoutParams().height = (int)getResources().getDimension(R.dimen.image_height);
                Image2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            };

            Colors.add(new AbstractMap.SimpleEntry<String, String>("#F5F5F5","#000000"));
            Colors.add(new AbstractMap.SimpleEntry<String, String>("#000000","#F5F5F5"));

            if (savedInstanceState != null) {
                {
                    BmMain = savedInstanceState.getParcelable("selectedImage1");
                    BmHistogram = savedInstanceState.getParcelable("selectedImage2");
                    OriginalImage = savedInstanceState.getParcelable("selectedImage_original");
                    NegativeImage = savedInstanceState.getParcelable("selectedImage_negative");
                }
                Image1.setImageBitmap(BmMain);
                Image2.setImageBitmap(BmHistogram);
            }
            else {
                BmMain = BitmapFactory.decodeResource(getResources(), GetRamdomImage());
                Image1.setImageBitmap(BmMain);
                Image2.setImageDrawable(null);
            }
            UserName = getIntent().getStringExtra("user_name");

            TextView tw = (TextView) findViewById(R.id.welcome_user_1);
            tw.setText(getString(R.string.user_name, UserName));

            ListToggle = (Switch) findViewById(R.id.switch1);

            ListToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (BmHistogram != null) {
                        BmMain = ((BitmapDrawable) Image1.getDrawable()).getBitmap();
                        Im = new ImageWork(BmMain, R.color.background_main);
                        if (isChecked)
                            // image2.setVisibility(View.INVISIBLE);
                            BmHistogram = NegativeImage;
                        else
                            // image2.setVisibility(View.VISIBLE);
                            BmHistogram = OriginalImage;
                        Image2.setImageBitmap(BmHistogram);

                    }

                }
            });
        }
        catch (Exception e) {
            Toast.makeText(this, R.string.data_downloading_problem, Toast.LENGTH_SHORT).show();
        }
    }

    // возвращает произвольную фоторафию из списка содержимого
    private int GetRamdomImage() {
        int [] l = new int[]{R.drawable.test1,R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test5, R.drawable.test6,  R.drawable.test7};

        return l[new Random().nextInt(l.length)];
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable("selectedImage1", BmMain);
        savedInstanceState.putParcelable("selectedImage2", BmHistogram);
        savedInstanceState.putParcelable("selectedImage_original", OriginalImage);
        savedInstanceState.putParcelable("selectedImage_negative", NegativeImage);
    }

    // рисует гистограмму
    public void BuilingHisogram(View v) {
        if (BmHistogram == null)
        try {
            BmMain = ((BitmapDrawable) Image1.getDrawable()).getBitmap();
            Im = new ImageWork(BmMain, R.color.background_main);

            OriginalImage = Im.get_gitogram(IMAGE_SIZE_WIDTH, IMAGE_SIZE_HEIGHT, Colors.get(0).getKey(), Colors.get(0).getValue());
            NegativeImage = Im.get_gitogram(IMAGE_SIZE_WIDTH, IMAGE_SIZE_HEIGHT, Colors.get(1).getKey(), Colors.get(1).getValue());

            if (ListToggle.isChecked())
                BmHistogram = NegativeImage;
            else BmHistogram = OriginalImage;

            Image2.setImageBitmap(BmHistogram);
        }
        catch (Exception e) {
            Toast.makeText(this, R.string.histogram_downloading_problem, Toast.LENGTH_SHORT).show();
        }
    }

   /* // Кнопка "назад"
    public void back_to_menu(View v) {
        finish();
        super.onBackPressed();
    }*/

    // Загрузка изображения из галереи
    public void LoadPictureFromGallery(View v) {
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

                BmMain = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));


              if (BmMain.getHeight() > MAIN_IMAGE_HEIGHT || BmMain.getWidth() > MAIN_IMAGE_WIDTH) {
                    options.inSampleSize = getResources().getInteger(R.integer.image_scale);
                  BmMain = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                }

                if (BmMain == null)
                    Toast.makeText(this, R.string.image_was_not_downloaded, Toast.LENGTH_SHORT).show();

                Image1.setImageBitmap(BmMain);
                Image2.setImageBitmap(BmHistogram = null);
            }
        }
        catch(Exception e){
                Toast.makeText(this, R.string.gallery_problem, Toast.LENGTH_SHORT).show();

            }
        }
}
