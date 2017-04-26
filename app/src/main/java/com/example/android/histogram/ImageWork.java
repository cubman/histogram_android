package com.example.android.histogram;

import android.graphics.*;
import android.graphics.drawable.RotateDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Анатолий on 17.04.2016.
 */
public class ImageWork implements Parcelable  {
    private final int ColorsCount = 256;

    private Bitmap Bm;
    private int color;
    private int[] MainRgb = new int[ColorsCount];
    private double [] Normalised;

    protected ImageWork() {
        Bm = Bitmap.createBitmap(0,0, Bitmap.Config.ARGB_8888);
    }

    public  ImageWork(Bitmap Bm, int c) {
        this.Bm = Bm;
        this.color = c;
        CountHistogram();
        Normalised = normalise();
    }

    // подсчитывает статистические данные по изображению
    private void CountHistogram() {
        for (int i = 0; i < Bm.getHeight(); ++i)
            for (int j = 0;j <Bm.getWidth();++j) {
                int pic = Bm.getPixel(j, i);
                ++MainRgb[(int) (0.3 * Color.red(pic) + 0.59 * Color.green(pic) + 0.11 * Color.blue(pic))];
            }
    }

    // получаем гистограмму с заданной высотой и ширино  и цветовой гаммой
    public Bitmap get_gitogram(int heigh, int width, String Back_color, String Histogram_color) {
        Bitmap bm_return = Bitmap.createBitmap(heigh, width, Bitmap.Config.ARGB_8888);

        return  BuildHistogram(bm_return, Back_color, Histogram_color);
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
    // нормаль=изует гистограмму
    private double[] normalise() {
        double [] res = new double[MainRgb.length];
        int max = FindMaxHeight();
        for (int i = 0; i < ColorsCount; ++i)
            res[i] = MainRgb[i] / Double.valueOf(max);

        return  res;
    }
// строит изобраение, которое будет подходящим для image view
    private Bitmap BuildHistogram(Bitmap bm_return, String Back_color, String Histogram_color) {
        int height = bm_return.getHeight();
        int width = bm_return.getWidth();

        int step = width / ColorsCount;
        bm_return.eraseColor(Color.parseColor(Back_color));
        Matrix matrix = new Matrix();
        Canvas c = new Canvas(bm_return);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(Color.parseColor(Histogram_color));


        for (int i = 0; i < ColorsCount; ++i)
            c.drawRect(i * step, 0, (1 + i) * step, (int) (height  * Normalised[i]), p);

        matrix.preScale(-1, 1);
        matrix.postRotate(180, width/2, height/2);
        bm_return = Bitmap.createBitmap(bm_return, 0,0,width, height, matrix, false);
        bm_return.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return bm_return;
    }

    public static final Parcelable.Creator<ImageWork> CREATOR
            = new Parcelable.Creator<ImageWork>() {
        public ImageWork createFromParcel(Parcel in) {
            return new ImageWork(in);
        }

        public ImageWork[] newArray(int size) {
            return new ImageWork[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(MainRgb);
        dest.writeDoubleArray(Normalised);
    }

    private ImageWork(Parcel in) {
        in.readIntArray(MainRgb);
        in.readDoubleArray(Normalised);
    }
}
