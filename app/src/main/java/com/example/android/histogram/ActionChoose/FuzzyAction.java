package com.example.android.histogram.ActionChoose;

import android.graphics.Bitmap;

import com.example.android.histogram.ActionChoose.ChooseAction;
import com.example.android.histogram.ImageManipulation.GrayFuzzy;
import com.example.android.histogram.ImageManipulation.GrayImage;
import com.example.android.histogram.ImageManipulation.ImageWork;

public class FuzzyAction implements ChooseAction {
    public ImageWork returnNewImage(ImageWork iw, boolean shouldFilter) {
        Bitmap bm = iw.equalisedImage();
        GrayFuzzy Im_hist_equal = new GrayFuzzy(bm);
        if (shouldFilter)
            bm = Im_hist_equal.improveCurrentGaus();
       return new GrayFuzzy(bm);
    }
}
