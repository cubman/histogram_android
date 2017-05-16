package com.example.android.histogram.ImageManipulation;

import android.content.Context;
import android.graphics.*;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by Анатолий on 17.04.2016.
 */
public abstract class ImageWork implements Parcelable {
    protected static final int ColorsCount = 256;

    protected Bitmap Bm;
    protected int[] MainRgb = new int[ColorsCount];
    protected double [] Normalised;

    protected ImageWork(Bitmap Bm) {
        this.Bm = Bm;
        CountHistogram();
        Normalised = normalise();
    }

    // нормализует гистограмму
    protected double[] normalise() {
        double [] res = new double[MainRgb.length];

        double max = FindMaxHeight();
        for (int i = 0; i < ColorsCount; ++i)
            res[i] = MainRgb[i] / max;

        return  res;
    }


    // получаем гистограмму с заданной высотой и ширино  и цветовой гаммой
    public Bitmap get_gitogram(int height, int width, String Back_color, String Histogram_color) {
        return  BuildHistogram(height, width, Back_color, Histogram_color);
    }

    private static void saveSt(Context context, ImageWork im, File fName) {
        try {
            FileWriter f = new FileWriter(fName);
            for (int i = 0; i < ColorsCount; ++i)
                f.write(String.format("%d;%d\n", i, im.MainRgb[i]));
            f.flush();
            f.close();
            Log.d("333", "was written");
        } catch (Exception e) {
            Log.d("333", "was not written");
           /// Toast.makeText(context, "was stat", Toast.LENGTH_SHORT).show();
        }
    }
    public static void saveStatistic(Context context, ImageWork original, ImageWork fuzzy, ImageWork full, File originalCSV, File fuzzyCSV, File fullCSV) {
       /* original.CountHistogram();
        fuzzy.CountHistogram();
        full.CountHistogram();*/

        saveSt(context, original, originalCSV);
        saveSt(context, fuzzy, fuzzyCSV);
        saveSt(context, full, fullCSV);
        Log.d("PATHNAME", originalCSV.getAbsolutePath());
    }

    // найти самый высокий пик
    private int FindMaxHeight() {
        int max = -Integer.MAX_VALUE;

        for (int i = 0; i < ColorsCount; ++i)
            if (max < MainRgb[i])
                max = MainRgb[i];

        return  max;
    }

    public  void insertGgraph(GraphView graph) {
        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(ColorsCount);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(1);

        DataPoint[] dp = new DataPoint[Normalised.length];

        for (int i = 0; i < Normalised.length; ++i)
            dp[i] = new DataPoint(i, Normalised[i]);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dp);
        graph.addSeries(series);

// styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX(), (int) (data.getX()), 100);
            }
        });

        series.setSpacing(0);

    }

// строит изобраение, которое будет подходящим для image view
    private Bitmap BuildHistogram(int height, int width, String Back_color, String Histogram_color) {
        Bitmap bm_return = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int step = width / ColorsCount;
        bm_return.eraseColor(Color.parseColor(Back_color));
        Matrix matrix = new Matrix();
        Canvas c = new Canvas(bm_return);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(Color.parseColor(Histogram_color));


        for (int i = 0; i < ColorsCount; ++i)
            c.drawRect(i * step, 0, (1 + i) * step, (int) (height  * Normalised[i]), p);

        matrix.preScale(-1, 1);
        matrix.postRotate(180, width / 2, height / 2);
        Bitmap bm = Bitmap.createBitmap(bm_return, 0, 0, width, height, matrix, false);
        bm.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return bm;
    }

    public static Bitmap convertToGray(Bitmap Bm) {
        Bitmap b = Bitmap.createBitmap(Bm.getWidth(), Bm.getHeight(), Bitmap.Config.ARGB_8888);
        for (int i = 0; i < b.getWidth(); ++i)
            for (int j = 0; j < b.getHeight(); ++j) {
                int pixel = Bm.getPixel(i, j);
                int clr = (int) (0.3 * Color.red(pixel) + 0.59 * Color.green(pixel) + 0.11 * Color.blue(pixel));
                b.setPixel(i, j, Color.argb(255, clr, clr, clr));//();
            }
        return b;
    }

    abstract public Bitmap equalisedImage();

    abstract protected void CountHistogram();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(MainRgb);
        dest.writeDoubleArray(Normalised);
    }

    protected ImageWork(Parcel in) {
        in.readIntArray(MainRgb);
        in.readDoubleArray(Normalised);
    }
}
