package com.example.android.histogram;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.List;
import java.util.Vector;

/**
 * Created by Анатолий on 17.04.2016.
 */
public class Image_Work {

    private Bitmap bm;
    private int[] main_rgb = new int[256];

    public  Image_Work(Bitmap bm) {this.bm = bm;}

    private void count_histogram() {
        for (int i = 0; i < bm.getWidth(); ++i)
            for (int j = 0;j < bm.getHeight();++j) {
                int pic = bm.getPixel(i, j);

                ++main_rgb[(int) (0.3 * Color.red(pic) + 0.59 * Color.green(pic) + 0.11 * Color.blue(pic))];
            }
    }

    public Bitmap get_gitogram(int heigh, int width) {
        Bitmap bm_return = Bitmap.createBitmap(heigh, width, Bitmap.Config.ARGB_8888);

       // count_histogram();

        bm_return.eraseColor(Color.RED);

        return bm_return;
    }
}
