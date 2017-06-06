package com.example.android.histogram.ActionChoose;

import android.graphics.Bitmap;

import com.example.android.histogram.ImageManipulation.Gray.GrayFull;
import com.example.android.histogram.ImageManipulation.ImageWork;

/**
 * Created by Анатолий on 17.05.2017.
 */
public class GrayFullAction implements ChooseAction {
    public ImageWork returnNewImage(ImageWork iw, boolean shouldFilter) {
        Bitmap bm = iw.equalisedImage();
        GrayFull Im_hist_equal = new GrayFull(bm);
        if (shouldFilter)
            bm = Im_hist_equal.improveCurrentGaus();
        return new GrayFull(bm);
    }
}
