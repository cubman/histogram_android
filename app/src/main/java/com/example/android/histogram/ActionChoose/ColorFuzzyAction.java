package com.example.android.histogram.ActionChoose;

import android.graphics.Bitmap;

import com.example.android.histogram.ImageManipulation.Color.ColorFuzzy;
import com.example.android.histogram.ImageManipulation.Color.ColorImage;
import com.example.android.histogram.ImageManipulation.ImageWork;

/**
 * Created by Анатолий on 17.05.2017.
 */
public class ColorFuzzyAction implements ChooseAction {
    public ImageWork returnNewImage(ImageWork iw, boolean shouldFilter) {
        Bitmap bm = iw.equalisedImage();
        ColorFuzzy Im_hist_equal = new ColorFuzzy(bm);

        return Im_hist_equal;
    }
}