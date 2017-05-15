package com.example.android.histogram;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.example.android.histogram.ImageManipulation.ColorImage;
import com.example.android.histogram.ImageManipulation.GrayFull;
import com.example.android.histogram.ImageManipulation.GrayFuzzy;
import com.example.android.histogram.ImageManipulation.GrayImage;
import com.example.android.histogram.ImageManipulation.ImageWork;
import com.jjoe64.graphview.GraphView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.*;

import static android.app.PendingIntent.getActivity;

public class HistogramPage extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1; // флаг запроса
    private static final int CAMERA_REQUEST = 2; // флаг запроса

    private  int IMAGE_SIZE_WIDTH ;
    private  int IMAGE_SIZE_HEIGHT;
    private  int MAIN_IMAGE_HEIGHT;
    private  int MAIN_IMAGE_WIDTH;

    private  GraphView gv_main, gv_hist_equal;
    private ImageWork Im_main, Im_hist_equal; // класс для работы с изображениями
    private Uri file;
    private String camPath;
    Bitmap BmMain, BmHistogramEqaulised;//, OriginalImage, NegativeImage;

   // private List<Map.Entry<String, String>> Colors = new LinkedList<>();
    private String UserName;

    ImageView Image1, Image2;
    Button btmActivate;
    private int imageType = 0;

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
            Image2 = (ImageView) findViewById(R.id.image_view_histogram_equalised);
            gv_main     = (GraphView) findViewById(R.id.graph_main);
            gv_hist_equal     = (GraphView) findViewById(R.id.graph_hist_equalised);

            btmActivate = (Button) findViewById(R.id.button_build_histogram);

            if (savedInstanceState != null) {

                BmMain = savedInstanceState.getParcelable("selectedImage1");
                BmHistogramEqaulised = savedInstanceState.getParcelable("selectedImage2");
                btmActivate.setVisibility(savedInstanceState.getInt("selectedActivate_buttom") == View.VISIBLE ? View.VISIBLE : View.INVISIBLE);
                Im_main = savedInstanceState.getParcelable("selectImageWorkMain");
                Im_hist_equal = savedInstanceState.getParcelable("selectImageWorkHistEqualise");

                camPath = savedInstanceState.getString("imagePathCamera");

                initImageWork(0);
                if (Im_hist_equal != null)
                    initImageWork(1);
            }
            else {
                setRandomImage();

            }

            UserName = getIntent().getStringExtra("user_name");

            TextView tw = (TextView) findViewById(R.id.welcome_user_1);
            tw.setText(getString(R.string.user_name, UserName));



        }
        catch (Exception e) {
            Toast.makeText(this, R.string.data_downloading_problem, Toast.LENGTH_SHORT).show();
        }
    }

    private  void setEqualisedData() {

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
        menu.add(0, 1, 0, "save Statistic");

        SubMenu sMenu = menu.addSubMenu(0, 2, 0, R.string.loadFrom);
            sMenu.add(0, 3, 0, R.string.gallery);
            if (checkCameraHardware(this))
                 sMenu.add(0, 4, 0, R.string.camera);
            sMenu.add(0, 5, 0, R.string.randomPhoto);


        return true;
    }


    private void initImageWork(int blockInit) {
        switch (blockInit) {
            case 0 :
                if (imageType == 0) {
                    BmMain = ImageWork.convertToGray(BmMain);

                    Im_main = new GrayFull(BmMain);
                }
                else
                    Im_main = new ColorImage(BmMain);

                Image1.setImageBitmap(BmMain);
                gv_main.removeAllSeries();
                Im_main.insertGgraph(gv_main);




                Image2.setImageBitmap(BmHistogramEqaulised = null);
                gv_hist_equal.setVisibility(View.GONE);
                Image2.setVisibility(View.GONE);
                btmActivate.setVisibility(View.VISIBLE);
                break;
            case 1 :
                BmHistogramEqaulised = Im_main.equalisedImage();
                if (imageType == 0)
                    Im_hist_equal = new GrayImage(BmHistogramEqaulised);
                else
                    Im_hist_equal = new ColorImage(BmHistogramEqaulised);

                Image2.setImageBitmap(BmHistogramEqaulised);
                gv_hist_equal.removeAllSeries();
                Im_hist_equal.insertGgraph(gv_hist_equal);


                gv_hist_equal.setVisibility(View.VISIBLE);
                Image2.setVisibility(View.VISIBLE);
                btmActivate.setVisibility(View.INVISIBLE);
                break;
        }

    }

    private void setRandomImage() {
        BmMain = BitmapFactory.decodeResource(getResources(), GetRamdomImage());
        initImageWork(0);
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
        savedInstanceState.putParcelable("selectedImage2", BmHistogramEqaulised);
        savedInstanceState.putInt("selectedActivate_buttom", btmActivate.getVisibility());
        savedInstanceState.putParcelable("selectImageWorkMain", Im_main);
        savedInstanceState.putParcelable("selectImageWorkHistEqualise", Im_hist_equal);
        savedInstanceState.putString("imagePathCamera", camPath);
    }

    public void BuilingHisogram(View v) {

        if (BmHistogramEqaulised == null)
            try {
                initImageWork(1);
                btmActivate.setVisibility(View.INVISIBLE);
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
    public static File createFile(String DirNmae, String Fname, Context context) {
        Boolean b = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        String file_path;
        if(b)
        {
            // yes SD-card is present
            file_path = Environment.getExternalStorageDirectory() + File.separator + DirNmae;
            Log.d("1", "was ues sd");
        }
        else
        {
            file_path = context.getFilesDir().getPath();
            Log.d("1", "was ues storage");
        }


        File dir = new File(file_path);

        if(!dir.exists())
            dir.mkdirs();
        Log.d("1", dir.getPath());
        return new File(dir, Fname);
    }


    private void createFileCSV(File fileN) {

    }
    private void SavePhoto(Bitmap bitmap, String fName) {
        Boolean b = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        File file = createFile(getResources().getString(R.string.app_name), getResources().getString(R.string.app_name) + fName == null ? getCurTime() : fName + ".png", this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.println(1, "sad", String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case 0:
                if (BmHistogramEqaulised != null)
                    SavePhoto(BmHistogramEqaulised, null);
                else
                    Toast.makeText(this, R.string.imageToSaveWasNotFound, Toast.LENGTH_LONG).show();

                return true;
            case 1:
               // ReverseColors(item);
                File f = createFile(getResources().getString(R.string.app_name) + File.separator + "Statistic", "or.csv", this);
                File f1 = createFile(getResources().getString(R.string.app_name) + File.separator + "Statistic", "fuzzy.csv", this);
                File f2 = createFile(getResources().getString(R.string.app_name) + File.separator + "Statistic", "full.csv", this);

                Bitmap fz = new GrayFuzzy(BmMain).equalisedImage();
                Bitmap fl = new GrayFull(BmMain).equalisedImage();
                ImageWork.saveStatistic(this, Im_main, new GrayImage(fz), new GrayImage(fl),
                        f, f1, f2);

                GalleryRefresh(f);
                GalleryRefresh(f1);
                GalleryRefresh(f2);
                SavePhoto(BmMain, "original");
                SavePhoto(fz, "fuzzy");
                SavePhoto(fl, "full");

                Toast.makeText(this, "was stat", Toast.LENGTH_SHORT).show();

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

        initImageWork(0);
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
