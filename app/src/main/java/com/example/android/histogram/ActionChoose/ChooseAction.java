package com.example.android.histogram.ActionChoose;

import android.graphics.Bitmap;

import com.example.android.histogram.ImageManipulation.ColorImage;
import com.example.android.histogram.ImageManipulation.GrayFuzzy;
import com.example.android.histogram.ImageManipulation.GrayImage;
import com.example.android.histogram.ImageManipulation.ImageWork;

/**
 * Created by Анатолий on 17.05.2017.
 */
public interface ChooseAction {
   public ImageWork returnNewImage(ImageWork iw, boolean shouldFilter);
}

