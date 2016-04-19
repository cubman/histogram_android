package com.example.android.histogram;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
    ImageView image1, image2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_histogram_page);

        image1 = (ImageView) findViewById(R.id.image_view_main);
        image2 = (ImageView) findViewById(R.id.image_view_histogram);

        // first time the app is started get the bitmap from unknown source.
        if (savedInstanceState != null) {
            {
                bm_main = savedInstanceState.getParcelable("selectedImage1");
                bm_histogram = savedInstanceState.getParcelable("selectedImage2");
            }
            image1.setImageBitmap(bm_main);
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
        savedInstanceState.putParcelable("selectedImage1", bm_main);
        savedInstanceState.putParcelable("selectedImage2", bm_histogram);
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

    public void load_picture_from_gallary(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }


@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
        // Let's read picked image data - its URI
        Uri pickedImage = data.getData();
        // Let's read picked image path using content resolver
        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bm_main = BitmapFactory.decodeFile(imagePath);

            if  (bm_main.getWidth() > 1300 || bm_main.getHeight() > 1700)
            bm_main = Bitmap.createScaledBitmap(bm_main, Math.min(1600, bm_main.getWidth()), Math.min(2028, bm_main.getHeight()), true);
        image1.setImageBitmap(bm_main);

        // Do something with the bitmap


        // At the end remember to close the cursor or you will end with the RuntimeException!
        cursor.close();
        }
        }
}
