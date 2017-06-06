package com.example.android.histogram.ActionChoose;

import android.graphics.Bitmap;

import com.example.android.histogram.ImageManipulation.Color.ColorFull;
import com.example.android.histogram.ImageManipulation.Color.ColorFuzzy;
import com.example.android.histogram.ImageManipulation.ImageWork;

/**
 * Created by Анатолий on 31.05.2017.
 */
public class ColorFullAction implements ChooseAction {
    public ImageWork returnNewImage(ImageWork iw, boolean shouldFilter) {
        Bitmap bm = iw.equalisedImage();
        ColorFull Im_hist_equal = new ColorFull(bm);

        return Im_hist_equal;
    }
}
