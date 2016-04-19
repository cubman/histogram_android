package com.example.android.histogram;

import android.graphics.*;
import android.graphics.drawable.RotateDrawable;
import android.util.Log;

import java.util.List;
import java.util.Vector;

/**
 * Created by Анатолий on 17.04.2016.
 */
public class Image_Work {
    private final int colors_count = 256;

    private Bitmap bm;
    private int color;
    private int[] main_rgb = new int[colors_count];

    public  Image_Work(Bitmap bm, int c) {this.bm = bm; this.color = c;}

    private void count_histogram() {
        for (int i = 0; i < bm.getHeight(); ++i)
            for (int j = 0;j <bm.getWidth();++j) {
                int pic = bm.getPixel(j, i);
                ++main_rgb[(int) (0.3 * Color.red(pic) + 0.59 * Color.green(pic) + 0.11 * Color.blue(pic))];
            }
    }

    public Bitmap get_gitogram(int heigh, int width) {
        Bitmap bm_return = Bitmap.createBitmap(heigh, width, Bitmap.Config.ARGB_8888);

        count_histogram();

        return  build_histogram(bm_return);
    }

    private int find_max_height() {
        int max = -Integer.MAX_VALUE;

        for (int i = 0; i < colors_count; ++i)
            if (max < main_rgb[i])
                max = main_rgb[i];

        return  max;
    }

    private Bitmap build_histogram(Bitmap bm_return) {
        int max = find_max_height();
        int height = bm_return.getHeight();
        int width = bm_return.getWidth();

        int step = width / colors_count;
        Canvas c = new Canvas(bm_return);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(this.color);

        for (int i = 0; i < colors_count; ++i)
            c.drawRect(i * step, 2, (1 + i) * step, (height * main_rgb[i]) / max, p);

        c.rotate(90, height/2, width/2);

        return bm_return;
    }
}
