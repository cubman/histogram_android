package com.example.android.histogram.ActionChoose;

import android.graphics.Bitmap;

import com.example.android.histogram.ImageManipulation.ColorImage;
import com.example.android.histogram.ImageManipulation.GrayFull;
import com.example.android.histogram.ImageManipulation.ImageWork;

/**
 * Created by Анатолий on 17.05.2017.
 */
public class ColorAction implements ChooseAction {
    public ImageWork returnNewImage(ImageWork iw, boolean shouldFilter) {
        Bitmap bm = iw.equalisedImage();
        ColorImage Im_hist_equal = new ColorImage(bm);

        return Im_hist_equal;
    }
}