package com.example.android.histogram;

import android.graphics.*;
import android.graphics.drawable.RotateDrawable;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.List;
import java.util.Vector;

/**
 * Created by Анатолий on 17.04.2016.
 */
public class ImageWork {
    private final int colors_count = 256;

    private Bitmap bm;
    private int color;
    private int[] main_rgb = new int[colors_count];

    public  ImageWork(Bitmap bm, int c) {this.bm = bm; this.color = c;}

    // подсчитывает статистические данные по изображению
    private void CountHistogram() {
        for (int i = 0; i < bm.getHeight(); ++i)
            for (int j = 0;j <bm.getWidth();++j) {
                int pic = bm.getPixel(j, i);
                ++main_rgb[(int) (0.3 * Color.red(pic) + 0.59 * Color.green(pic) + 0.11 * Color.blue(pic))];
            }
    }

    // получаем гистограмму с заданной высотой и ширино  и цветовой гаммой
    public Bitmap get_gitogram(int heigh, int width, String Back_color, String Histogram_color) {
        Bitmap bm_return = Bitmap.createBitmap(heigh, width, Bitmap.Config.ARGB_8888);

        CountHistogram();

        return  BuildHistogram(bm_return, Back_color, Histogram_color);
    }

    // найти самый высокий пик
    private int FindMaxHeight() {
        int max = -Integer.MAX_VALUE;

        for (int i = 0; i < colors_count; ++i)
            if (max < main_rgb[i])
                max = main_rgb[i];

        return  max;
    }

  /*  public Bitmap resize_image(Bitmap old, int new_Height, int new_Width){

    }*/

// строит изобраение, которое будет подходящим для image view
    private Bitmap BuildHistogram(Bitmap bm_return, String Back_color, String Histogram_color) {
        int max = FindMaxHeight();
        int height = bm_return.getHeight();
        int width = bm_return.getWidth();

        int step = width / colors_count;
        bm_return.eraseColor(Color.parseColor(Back_color));
        Matrix matrix = new Matrix();
        Canvas c = new Canvas(bm_return);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(Color.parseColor(Histogram_color));


        for (int i = 0; i < colors_count; ++i)
            c.drawRect(i * step, 0, (1 + i) * step, (height * main_rgb[i]) / max, p);

        matrix.preScale(-1, 1);
        matrix.postRotate(180, width/2, height/2);
        bm_return = Bitmap.createBitmap(bm_return, 0,0,width, height, matrix, false);
        bm_return.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return bm_return;
    }
}