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
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.graphics.*;
import android.os.Environment;
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
import java.io.*;

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

    private boolean hist_reverse;

    ImageView Image1, Image2;
    Button btmActivate;
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
            btmActivate = (Button) findViewById(R.id.button_build_histogram);

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
                    btmActivate.setVisibility(savedInstanceState.getInt("selectedActivate_buttom") == View.VISIBLE ? View.VISIBLE : View.INVISIBLE);
                }
                Image1.setImageBitmap(BmMain);
                Image2.setImageBitmap(BmHistogram);
            }
            else {
                setRandomImage();
            }
            UserName = getIntent().getStringExtra("user_name");

            TextView tw = (TextView) findViewById(R.id.welcome_user_1);
            tw.setText(getString(R.string.user_name, UserName));

            hist_reverse = false;



        }
        catch (Exception e) {
            Toast.makeText(this, R.string.data_downloading_problem, Toast.LENGTH_SHORT).show();
        }
    }

    private void setRandomImage() {
        BmMain = BitmapFactory.decodeResource(getResources(), GetRamdomImage());
        Image1.setImageBitmap(BmMain);
        Image2.setImageBitmap(BmHistogram = null);

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
        savedInstanceState.putInt("selectedActivate_buttom", btmActivate.getVisibility());
    }

    // рисует гистограмму
    public void BuilingHisogram(View v) {
        if (BmHistogram == null)
        try {
            BmMain = ((BitmapDrawable) Image1.getDrawable()).getBitmap();
            Im = new ImageWork(BmMain, R.color.background_main);

            OriginalImage = Im.get_gitogram(IMAGE_SIZE_WIDTH, IMAGE_SIZE_HEIGHT, Colors.get(0).getKey(), Colors.get(0).getValue());
            NegativeImage = Im.get_gitogram(IMAGE_SIZE_WIDTH, IMAGE_SIZE_HEIGHT, Colors.get(1).getKey(), Colors.get(1).getValue());

            if (hist_reverse)
                BmHistogram = NegativeImage;
            else BmHistogram = OriginalImage;

            Image2.setImageBitmap(BmHistogram);

            btmActivate.setVisibility(View.INVISIBLE);
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


    public void saveContent(View v)
    {
        saveImage(BmHistogram);
    }


    private void saveImage(Bitmap finalBitmap) {
        try
        {
            File saveDir = new File("/sdcard/CameraExample/");

            if (!saveDir.exists())
            {
                saveDir.mkdirs();
            }

            FileOutputStream os = new FileOutputStream(String.format("/sdcard/CameraExample/%d.jpg", System.currentTimeMillis()));
            os.write(finalBitmap.getNinePatchChunk());
            os.close();
        }
        catch (Exception e)
        {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, 0, 0, R.string.save);
        menu.add(0, 1, 0, R.string.histogram_activation);
       // menu.getItem(1)
        SubMenu sMenu = menu.addSubMenu(0, 2, 0, R.string.loadFrom);
        sMenu.add(0, 3, 0, R.string.gallery);
        sMenu.add(0, 4, 0, R.string.camera);
        sMenu.add(0, 5, 0, R.string.randomPhoto);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.println(1, "sad", String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case 0:
                saveImage(BmMain);
                return true;
            case 1:
                hist_reverse = !hist_reverse;
                if (BmHistogram != null) {
                    BmMain = ((BitmapDrawable) Image1.getDrawable()).getBitmap();
                    Im = new ImageWork(BmMain, R.color.background_main);
                    if (hist_reverse)
                        // image2.setVisibility(View.INVISIBLE);
                        BmHistogram = NegativeImage;
                    else
                        // image2.setVisibility(View.VISIBLE);
                        BmHistogram = OriginalImage;
                    Image2.setImageBitmap(BmHistogram);
                }

                return true;
            case 3:
                // Загрузка изображения из галереи
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                return true;
            case 4:
                // Загрузка изображения из камеры
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
                return true;
            case 5:
                setRandomImage();
                btmActivate.setVisibility(View.VISIBLE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    // Загрузка изображения из галереи
    public void LoadPictureFromGallery(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
                try {
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

                    btmActivate.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Toast.makeText(this, R.string.gallery_problem, Toast.LENGTH_SHORT).show();

                }
            } else  {
                try {
                    BmMain = (Bitmap) data.getExtras().get("data");
                    Image1.setImageBitmap(BmMain);

                    Image2.setImageBitmap(BmHistogram = null);
                    btmActivate.setVisibility( View.VISIBLE);

            } catch (Exception e) {
            Toast.makeText(this, R.string.gallery_problem, Toast.LENGTH_SHORT).show();

        }
        }
        }
}
