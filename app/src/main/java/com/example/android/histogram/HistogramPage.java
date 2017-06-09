package com.example.android.histogram;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.*;
import android.widget.*;

import com.example.android.histogram.ActionChoose.ChooseAction;
import com.example.android.histogram.ActionChoose.ColorFullAction;
import com.example.android.histogram.ActionChoose.ColorFuzzyAction;
import com.example.android.histogram.ActionChoose.GrayFullAction;
import com.example.android.histogram.ActionChoose.GrayFuzzyAction;
import com.example.android.histogram.ImageManipulation.Color.ColorFull;
import com.example.android.histogram.ImageManipulation.Color.ColorFuzzy;
import com.example.android.histogram.ImageManipulation.Color.ColorImage;
import com.example.android.histogram.ImageManipulation.Gray.GrayFull;
import com.example.android.histogram.ImageManipulation.Gray.GrayFuzzy;
import com.example.android.histogram.ImageManipulation.Gray.GrayImage;
import com.example.android.histogram.ImageManipulation.ImageWork;
import com.jjoe64.graphview.GraphView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    Bitmap BmOriginal, BmMain, BmHistogramEqaulised;//, OriginalImage, NegativeImage;

    private SyncHistogram sh;

    private String UserName;

    ImageView Image1, Image2;
    protected Button btmActivate;
    private boolean allowFilter = false;
    protected Menu menu;

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
            sh = new SyncHistogram();

            if (savedInstanceState != null) {
                BmOriginal = savedInstanceState.getParcelable("OriginalImage");
                BmMain = savedInstanceState.getParcelable("selectedImage1");
                BmHistogramEqaulised = savedInstanceState.getParcelable("selectedImage2");
                btmActivate.setVisibility(savedInstanceState.getInt("selectedActivate_buttom") == View.VISIBLE ? View.VISIBLE : View.INVISIBLE);
                Im_main = savedInstanceState.getParcelable("selectImageWorkMain");
                Im_hist_equal = savedInstanceState.getParcelable("selectImageWorkHistEqualise");
                allowFilter = savedInstanceState.getBoolean("filterGray");
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

    private boolean checkCameraHardware(Context context) {
        return  context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_histogram, menu);
        menu.add(0, 0, 0, R.string.save);
        menu.add(0, 1, 0, "save statistic");

        SubMenu sMenu = menu.addSubMenu(0, 2, 0, R.string.loadFrom);
            sMenu.add(0, 3, 0, R.string.gallery);
            if (checkCameraHardware(this))
                sMenu.add(0, 4, 0, R.string.camera);
            sMenu.add(0, 5, 0, R.string.randomPhoto);

        SubMenu sMenu2 = menu.addSubMenu(0, 6, 0, R.string.GrayMenu);
            sMenu2.add(0, 7, 0, R.string.FullSubMenuGray);
            sMenu2.add(0, 8, 0, R.string.FuzzySubMenuGray);
            sMenu2.add(0, 9, 0, R.string.FilterSubMenuGray).setCheckable(true).setChecked(allowFilter);

        SubMenu sMenu3 = menu.addSubMenu(0, 10, 0, R.string.ColorMenu);
            sMenu3.add(0, 11, 0, R.string.FullSubMenuColor);
            sMenu3.add(0, 12, 0, R.string.FuzzySubMenuColor);

        this.menu = menu;
        return true;
    }


    private void initImageWork(int blockInit) {
        switch (blockInit) {
            case 0 :
                Image1.setImageBitmap(BmMain == null ? BmOriginal : BmMain);

                gv_main.setVisibility(View.GONE);
                gv_hist_equal.setVisibility(View.GONE);
                Image2.setVisibility(View.GONE);
                btmActivate.setVisibility(View.INVISIBLE);
                break;
            case 1 :
                try {
                    gv_main.removeAllSeries();
                    Im_main.insertGgraph(gv_main);

                    Image2.setImageBitmap(BmHistogramEqaulised);
                    gv_hist_equal.removeAllSeries();
                    Im_hist_equal.insertGgraph(gv_hist_equal);

                    gv_main.setVisibility(View.VISIBLE);
                    gv_hist_equal.setVisibility(View.VISIBLE);
                    Image2.setVisibility(View.VISIBLE);
                    btmActivate.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    Toast.makeText(this, R.string.data_downloading_problem, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void setRandomImage() {
        BmMain = BmOriginal = BitmapFactory.decodeResource(getResources(), GetRamdomImage());
        initImageWork(0);
    }
    // возвращает произвольную фоторафию из списка содержимого
    private int GetRamdomImage() {
        //int [] l = new int[]{R.drawable.test1,R.drawable.test2, R.drawable.test3, R.drawable.test4, R.drawable.test5, R.drawable.test6,  R.drawable.test7};
        int [] l = new int[]{R.drawable.test5};

        return l[new Random().nextInt(l.length)];
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable("OriginalImage", BmOriginal);
        savedInstanceState.putParcelable("selectedImage1", BmMain);
        savedInstanceState.putParcelable("selectedImage2", BmHistogramEqaulised);
        savedInstanceState.putInt("selectedActivate_buttom", btmActivate.getVisibility());
        savedInstanceState.putParcelable("selectImageWorkMain", Im_main);
        savedInstanceState.putParcelable("selectImageWorkHistEqualise", Im_hist_equal);
        savedInstanceState.putBoolean("filterGray", allowFilter);
        savedInstanceState.putString("imagePathCamera", camPath);
    }

    public void BuilingHisogram(View v) {
        // Кнопка активации, которая не активна
    }

    public void GalleryRefresh(File file)
    {
        MediaScannerConnection.scanFile(this,
                new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {

                    }
                }
        );
    }

    public static File createFile(String DirNmae, String Fname, Context context) {
        Boolean b = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        String file_path;
        if(b)
        {
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
        File f = new File(dir + File.separator + Fname);
        if (f.exists())
            f.delete();

        return new File(dir, Fname);
    }


    private void SavePhoto(Bitmap bitmap, String fName, boolean notify) {
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
           if (notify)
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
                    SavePhoto(BmHistogramEqaulised, null, true);
                else
                    Toast.makeText(this, R.string.imageToSaveWasNotFound, Toast.LENGTH_LONG).show();

                return true;
            case 1:
               // ReverseColors(item);
                new LoadingStatistic().execute(BmMain);

                return true;
            case 3:
                // Загрузка изображения из галереи
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                Im_hist_equal = null;
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

                    if (photoFile != null) {

                        file = Uri.fromFile(photoFile);
                        camPath = file.toString();

                        cameraPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);

                        startActivityForResult(cameraPickerIntent, CAMERA_REQUEST);
                        Im_hist_equal = null;
                    }
                }
                return true;
            case 5:
                // Загрузка изображения из имеющихся
                setRandomImage();
                Im_hist_equal = null;
                return true;

            case 7 :
                BmMain = ImageWork.convertToGray(BmOriginal);
                Im_main = new GrayFull(BmMain);

                initImageWork(0);

                new SyncHistogram().execute(new Pair<ChooseAction, Boolean>(new GrayFullAction(), menu.getItem(3).getSubMenu().getItem(2).isChecked()));
                return true;

            case 8 :
                BmMain = ImageWork.convertToGray(BmOriginal);
                Im_main = new GrayFuzzy(BmMain);

                initImageWork(0);
                new SyncHistogram().execute(new Pair<ChooseAction, Boolean>(new GrayFuzzyAction(), menu.getItem(3).getSubMenu().getItem(2).isChecked()));

                return true;

            case 9:
                item.setChecked(allowFilter = !allowFilter);

                return false;

            case 11:
                Im_main = new ColorFull(BmOriginal);

                initImageWork(0);
                new SyncHistogram().execute(new Pair<ChooseAction, Boolean>(new ColorFullAction(), false));
                return true;

            case 12:
                Im_main = new ColorFuzzy(BmOriginal);

                initImageWork(0);
                new SyncHistogram().execute(new Pair<ChooseAction, Boolean>(new ColorFuzzyAction(),false));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private File getFile() throws IOException {
        String imageFileName = "JPG_" + getCurTime() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        camPath = image.getAbsolutePath();
        return image;
    }

    private void getAndScalePhoto(Uri data) throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();

        BmOriginal = BitmapFactory.decodeStream(getContentResolver().openInputStream(data));


        if (BmOriginal.getHeight() > MAIN_IMAGE_HEIGHT || BmOriginal.getWidth() > MAIN_IMAGE_WIDTH) {
            options.inSampleSize = getResources().getInteger(R.integer.image_scale);
            BmOriginal = BitmapFactory.decodeStream(getContentResolver().openInputStream(data), null, options);
        }

        if (BmOriginal == null)
            Toast.makeText(this, R.string.image_was_not_downloaded, Toast.LENGTH_SHORT).show();

        BmMain = BmOriginal;
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

    private  class SyncHistogram extends AsyncTask<Pair<ChooseAction, Boolean>, Void, Bitmap> {

        ProgressDialog progressDialog;

        @Override
        protected Bitmap doInBackground(Pair<ChooseAction, Boolean>... p) {
            try {
                Im_hist_equal = p[0].first.returnNewImage(Im_main, p[0].second);
            } catch (Exception e) {
                Toast.makeText(HistogramPage.this, R.string.histogram_downloading_problem, Toast.LENGTH_SHORT).show();
            }

            return Im_hist_equal.getCurrentImage();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            BmHistogramEqaulised = result;

            Image1.setImageBitmap(Im_main.getCurrentImage());
            Image2.setImageBitmap(BmHistogramEqaulised);

            gv_hist_equal.removeAllSeries();
            Im_hist_equal.insertGgraph(gv_hist_equal);

            gv_main.removeAllSeries();
            Im_main.insertGgraph(gv_main);

            gv_main.setVisibility(View.VISIBLE);
            gv_hist_equal.setVisibility(View.VISIBLE);
            Image2.setVisibility(View.VISIBLE);
            btmActivate.setVisibility(View.INVISIBLE);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(HistogramPage.this);
                progressDialog.setMessage(getResources().getText(R.string.counting_Hist));
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
            }
        }
    }

    private  class LoadingStatistic extends AsyncTask<Bitmap, Void, Void> {
        @Override
        protected Void doInBackground(Bitmap... p) {
            Log.d("123", "3333");
            File f = createFile(getResources().getString(R.string.app_name) + File.separator + "Statistic", "original.csv", HistogramPage.this);
            File f1 = createFile(getResources().getString(R.string.app_name) + File.separator + "Statistic", "fuzzy.csv", HistogramPage.this);
            File f2 = createFile(getResources().getString(R.string.app_name) + File.separator + "Statistic", "full.csv", HistogramPage.this);

            Bitmap fz = new GrayFuzzy(p[0]).equalisedImage();
            Bitmap fl = new GrayFull(p[0]).equalisedImage();
            ImageWork.saveStatistic(HistogramPage.this, Im_main, new GrayImage(fz), new GrayImage(fl), f, f1, f2);

            GalleryRefresh(f);
            GalleryRefresh(f1);
            GalleryRefresh(f2);
            SavePhoto(p[0], "original", false);
            SavePhoto(fz, "fuzzy", false);
            SavePhoto(fl, "full", false);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            Toast.makeText(HistogramPage.this, getResources().getText(R.string.statistic_saved), Toast.LENGTH_SHORT).show();
        }

    }
}
