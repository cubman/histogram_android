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
import com.example.android.histogram.ActionChoose.ColorAction;
import com.example.android.histogram.ActionChoose.FullAction;
import com.example.android.histogram.ActionChoose.FuzzyAction;
import com.example.android.histogram.ImageManipulation.ColorImage;
import com.example.android.histogram.ImageManipulation.GrayFull;
import com.example.android.histogram.ImageManipulation.GrayFuzzy;
import com.example.android.histogram.ImageManipulation.GrayImage;
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

   // private List<Map.Entry<String, String>> Colors = new LinkedList<>();
    private String UserName;

    ImageView Image1, Image2;
    protected Button btmActivate;
    private int imageType = 0;
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

            if (savedInstanceState != null) {
                BmOriginal = savedInstanceState.getParcelable("OriginalImage");
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

    private boolean checkCameraHardware(Context context) {
        return  context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void addItem(MenuItem menuItem) {
        this.menu.add(menuItem.getGroupId(), menuItem.getItemId(), menuItem.getOrder(), menuItem.getTitle());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show menu when menu button is pressed
        //MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_histogram, menu);
        menu.add(0, 0, 0, R.string.save);
        menu.add(0, 1, 0, "save statistic");

        SubMenu sMenu = menu.addSubMenu(0, 2, 0, R.string.loadFrom);
            sMenu.add(0, 3, 0, R.string.gallery);
            if (checkCameraHardware(this))
                sMenu.add(0, 4, 0, R.string.camera);
            sMenu.add(0, 5, 0, R.string.randomPhoto);

        SubMenu sMenu2 = menu.addSubMenu(0, 6, 0, "Gray");
            sMenu2.add(0, 7, 0, "Full");
            sMenu2.add(0, 8, 0, "Fuzzy");
            sMenu2.add(0, 9, 0, "Filter").setCheckable(true).setChecked(false);

        SubMenu sMenu3 = menu.addSubMenu(0, 10, 0, "Color");
            sMenu3.add(0, 11, 0, "Full");

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
                   /* BmHistogramEqaulised = Im_main.equalisedImage();
                    if (imageType == 0) {
                        Im_hist_equal = new GrayImage(BmHistogramEqaulised);
                        BmHistogramEqaulised = ((GrayImage) Im_hist_equal).improveCurrentGaus();
                    }
                    else
                        Im_hist_equal = new ColorImage(BmHistogramEqaulised);



                    if (imageType == 0)
                        Im_hist_equal = new GrayImage(BmHistogramEqaulised);
                    else
                        Im_hist_equal = new ColorImage(BmHistogramEqaulised);*/

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
        BmOriginal = BitmapFactory.decodeResource(getResources(), GetRamdomImage());
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

        savedInstanceState.putParcelable("OriginalImage", BmOriginal);
        savedInstanceState.putParcelable("selectedImage1", BmMain);
        savedInstanceState.putParcelable("selectedImage2", BmHistogramEqaulised);
        savedInstanceState.putInt("selectedActivate_buttom", btmActivate.getVisibility());
        savedInstanceState.putParcelable("selectImageWorkMain", Im_main);
        savedInstanceState.putParcelable("selectImageWorkHistEqualise", Im_hist_equal);
        savedInstanceState.putString("imagePathCamera", camPath);
    }

    public void BuilingHisogram(View v) {

        /*if (BmHistogramEqaulised == null)
            try {
                initImageWork(1);
                btmActivate.setVisibility(View.INVISIBLE);
            }
            catch (Exception e) {
                Toast.makeText(this, R.string.histogram_downloading_problem, Toast.LENGTH_SHORT).show();
            }*/
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

            case 7 :
                BmMain = ImageWork.convertToGray(BmOriginal);
                Im_main = new GrayFull(BmMain);

                initImageWork(0);
                new SyncHistogram().execute(new Pair<ChooseAction, Boolean>(new FullAction(), menu.getItem(3).getSubMenu().getItem(2).isChecked()));
                return true;

            case 8 :
                BmMain = ImageWork.convertToGray(BmOriginal);
                Im_main = new GrayFuzzy(BmMain);

                initImageWork(0);
                new SyncHistogram().execute(new Pair<ChooseAction, Boolean>(new FuzzyAction(), menu.getItem(3).getSubMenu().getItem(2).isChecked()));
                return true;

            case 9:
                item.setChecked(!item.isChecked());

                return false;

            case 11:
                Im_main = new ColorImage(BmOriginal);

                initImageWork(0);
                new SyncHistogram().execute(new Pair<ChooseAction, Boolean>(new ColorAction(),false));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     SubMenu sMenu = menu.addSubMenu(0, 2, 0, R.string.loadFrom);
            sMenu.add(0, 3, 0, R.string.gallery);
            if (checkCameraHardware(this))
                 sMenu.add(0, 4, 0, R.string.camera);
            sMenu.add(0, 5, 0, R.string.randomPhoto);

        SubMenu sMenu2 = menu.addSubMenu(0, 6, 0, "Gray");
            sMenu2.add(0, 7, 0, "Full");
            sMenu2.add(0, 8, 0, "Fuzzy");
            sMenu2.add(0, 9, 0, "Filter").setCheckable(true).setChecked(false);

        SubMenu sMenu3 = menu.addSubMenu(0, 10, 0, "Color");
            sMenu3.add(0, 11, 0, "Full");
            */

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

        BmOriginal = BitmapFactory.decodeStream(getContentResolver().openInputStream(data));


        if (BmOriginal.getHeight() > MAIN_IMAGE_HEIGHT || BmOriginal.getWidth() > MAIN_IMAGE_WIDTH) {
            options.inSampleSize = getResources().getInteger(R.integer.image_scale);
            BmOriginal = BitmapFactory.decodeStream(getContentResolver().openInputStream(data), null, options);
        }

        if (BmOriginal == null)
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

    private  class SyncHistogram extends AsyncTask<Pair<ChooseAction, Boolean>, Void, Bitmap> {

        ProgressDialog progressDialog;

        @Override
        protected Bitmap doInBackground(Pair<ChooseAction, Boolean> ... p) {
            Log.d("123", "3333");

            // Synchronize code here
            try {
                Log.d("123", "3333");
                Im_hist_equal = p[0].first.returnNewImage(Im_main, p[0].second);

               /*

                if (imageType == 0)
                    Im_hist_equal = new GrayImage(bm);
                else
                    Im_hist_equal = new ColorImage(bm);*/


            }
            catch (Exception e) {
                Toast.makeText(HistogramPage.this, R.string.histogram_downloading_problem, Toast.LENGTH_SHORT).show();
            }
            Log.d("123", "3333");
            return  Im_hist_equal.getCurrentImage();
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

        ProgressDialog progressDialog;

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

            //
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
           /* if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
            Toast.makeText(HistogramPage.this, getResources().getText(R.string.statistic_saved), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
          /*  if (progressDialog == null) {
                progressDialog = new ProgressDialog(HistogramPage.this);
                progressDialog.setMessage(getResources().getText(R.string.statistic_Counting));
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
            }*/


        }
    }
}
