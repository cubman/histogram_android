package com.example.android.histogram;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.graphics.*;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.*;
import android.widget.*;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;
import java.io.*;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.*;
import static com.example.android.histogram.R.string.app_name;

public class HistogramPage extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1; // флаг запроса
    private static final int CAMERA_REQUEST = 2; // флаг запроса

    private  int IMAGE_SIZE_WIDTH ;
    private  int IMAGE_SIZE_HEIGHT;
    private  int MAIN_IMAGE_HEIGHT;
    private  int MAIN_IMAGE_WIDTH;

    private  GraphView gv;
    private ImageWork Im; // класс для работы с изображениями
    private Uri file;
    private String camPath;
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
            gv     = (GraphView) findViewById(R.id.graph);
            gv.removeAllSeries();
            btmActivate = (Button) findViewById(R.id.button_build_histogram);

            Colors.add(new AbstractMap.SimpleEntry<>("#F5F5F5","#000000"));
            Colors.add(new AbstractMap.SimpleEntry<>("#000000","#F5F5F5"));

            if (savedInstanceState != null) {
                {
                    BmMain = savedInstanceState.getParcelable("selectedImage1");
                    BmHistogram = savedInstanceState.getParcelable("selectedImage2");
                    OriginalImage = savedInstanceState.getParcelable("selectedImage_original");
                    NegativeImage = savedInstanceState.getParcelable("selectedImage_negative");
                    btmActivate.setVisibility(savedInstanceState.getInt("selectedActivate_buttom") == View.VISIBLE ? View.VISIBLE : View.INVISIBLE);
                    Im = savedInstanceState.getParcelable("selectImageWork");
                    if (Im != null) {
                        Im.insertGgraph(gv);

                        gv.setVisibility(View.VISIBLE);
                    } else {
                        gv.setVisibility(View.GONE);
                        Image2.setVisibility(View.GONE);
                    }

                    camPath = savedInstanceState.getString("imagePathCamera");
                }
                Image1.setImageBitmap(BmMain);
                Image2.setImageBitmap(BmHistogram);
            }
            else {
                setRandomImage();
                gv.setVisibility(View.GONE);
                Image2.setVisibility(View.GONE);
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


    private boolean checkCameraHardware(Context context) {
        return  context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show menu when menu button is pressed
        //MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_histogram, menu);
        menu.add(0, 0, 0, R.string.save);
        menu.add(0, 1, 0, R.string.histogram_activation).setCheckable(true);

        SubMenu sMenu = menu.addSubMenu(0, 2, 0, R.string.loadFrom);
            sMenu.add(0, 3, 0, R.string.gallery);
            if (checkCameraHardware(this))
                 sMenu.add(0, 4, 0, R.string.camera);
            sMenu.add(0, 5, 0, R.string.randomPhoto);


        return true;
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
        savedInstanceState.putParcelable("selectImageWork", Im);
        savedInstanceState.putString("imagePathCamera", camPath);
    }


    // рисует гистограмму
    public void BuilingHisogram(View v) {
        if (BmHistogram == null)
        try {
            BmMain = ((BitmapDrawable) Image1.getDrawable()).getBitmap();
            Im = new ImageWork(BmMain, R.color.background_main);
            gv.removeAllSeries();
            Im.insertGgraph(gv);

            OriginalImage = Im.get_gitogram(IMAGE_SIZE_WIDTH, IMAGE_SIZE_HEIGHT, Colors.get(0).getKey(), Colors.get(0).getValue());
            NegativeImage = Im.get_gitogram(IMAGE_SIZE_WIDTH, IMAGE_SIZE_HEIGHT, Colors.get(1).getKey(), Colors.get(1).getValue());

            if (hist_reverse)
                BmHistogram = NegativeImage;
            else BmHistogram = OriginalImage;

            Image2.setImageBitmap(BmHistogram);

            btmActivate.setVisibility(View.INVISIBLE);
            Image2.setVisibility(View.VISIBLE);
            gv.setVisibility(View.VISIBLE);
        }
        catch (Exception e) {
            Toast.makeText(this, R.string.histogram_downloading_problem, Toast.LENGTH_SHORT).show();
        }
    }

    private String fromInt(int val)
    {
        return String.valueOf(val);
    }

    public void GalleryRefresh(File file)
    {
        //this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        MediaScannerConnection.scanFile(this,
                new String[]{file.getAbsolutePath()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        //now visible in gallery
                    }
                }
        );
    }


    private void SavePhoto(Bitmap bitmap) {

        Boolean b = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        String file_path;
            if(b)
            {
                // yes SD-card is present
                file_path = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name);
                Log.d("1", "was ues sd");
            }
            else
            {
                file_path = this.getFilesDir().getPath();
                Log.d("1", "was ues storage");
            }


            File dir = new File(file_path);

            if(!dir.exists())
                dir.mkdirs();
            Log.d("1", dir.getPath());
            File file = new File(dir, getResources().getString(R.string.app_name) + getCurTime() + ".png");
            Log.d("2", file.getPath());
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            Log.d("1", "fos");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            Log.d("1", "saveds");
            fOut.flush();

            fOut.close();
            if (b)
               GalleryRefresh(file);
            Toast.makeText(this, R.string.imageWasSavedSuccesfully, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, R.string.imageWasSavedUnsuccesfully, Toast.LENGTH_LONG).show();
        }
    }


    private String getCurTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formDate = df.format(c.getTime());
        return formDate;
    }

    private void ReverseColors(MenuItem item) {
        hist_reverse = !hist_reverse;
        item.setChecked((hist_reverse));
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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.println(1, "sad", String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case 0:
                if (BmHistogram != null)
                    SavePhoto(BmHistogram);
                else
                    Toast.makeText(this, R.string.imageToSaveWasNotFound, Toast.LENGTH_LONG).show();

                return true;
            case 1:
                ReverseColors(item);

                return true;
            case 3:
                // Загрузка изображения из галереи
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                return true;
            case 4:

                // Загрузка изображения из камеры
                Intent cameraPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //getting uri of the file
                /*file = Uri.fromFile(CameraWork.getFile());

                //Setting the file Uri to my photo
                    cameraPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                Log.d("12345", "++++" + file.getPath());

                startActivityForResult(cameraPickerIntent, CAMERA_REQUEST);*/

                if (cameraPickerIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;

                    try {
                        photoFile = getFile();
                    } catch (Exception e) {
                        Log.d("12345", "++++" + "000000");
                    }
                    Log.d("12345", "++++" + "1");
                    // Continue only if the File was successfully created
                    if (photoFile != null) {

                        file = Uri.fromFile(photoFile);
                        camPath = file.toString();
                        Log.d("12345", "++++" + "2" + file.getPath() + " " + camPath);
                        cameraPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);

                        Log.d("12345", "++++" + "3");

                        startActivityForResult(cameraPickerIntent, CAMERA_REQUEST);
                    }
                }
                return true;
            case 5:
                // Загрузка изображения из имеющихся
                setRandomImage();
                btmActivate.setVisibility(View.VISIBLE);
                Image2.setVisibility(View.GONE);
                gv.setVisibility(View.GONE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private File getFile() throws IOException {
        String imageFileName = "JPEG_" + getCurTime() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        camPath = image.getAbsolutePath();
        return image;
    }

    private void getAndScalePhoto(Uri data) throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();

        BmMain = BitmapFactory.decodeStream(getContentResolver().openInputStream(data));


        if (BmMain.getHeight() > MAIN_IMAGE_HEIGHT || BmMain.getWidth() > MAIN_IMAGE_WIDTH) {
            options.inSampleSize = getResources().getInteger(R.integer.image_scale);
            BmMain = BitmapFactory.decodeStream(getContentResolver().openInputStream(data), null, options);
        }

        if (BmMain == null)
            Toast.makeText(this, R.string.image_was_not_downloaded, Toast.LENGTH_SHORT).show();

        Image1.setImageBitmap(BmMain);
        Image2.setImageBitmap(BmHistogram = null);

        btmActivate.setVisibility(View.VISIBLE);
        Image2.setVisibility(View.GONE);
        gv.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
                try {
                   getAndScalePhoto(data.getData());


                } catch (Exception e) {
                    Toast.makeText(this, R.string.gallery_problem, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                try {
                    Log.d("12345", "++++" + "2" +camPath);

                    getAndScalePhoto(Uri.parse(camPath));

            } catch (Exception e) {
            Toast.makeText(this, R.string.camera_problem, Toast.LENGTH_SHORT).show();

        }
        }
        }
}
