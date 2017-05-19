package com.example.android.histogram.ActionChoose;

import com.example.android.histogram.ImageManipulation.ImageWork;

/**
 * Created by Анатолий on 17.05.2017.
 */
public interface ChooseAction {
   public ImageWork returnNewImage(ImageWork iw, boolean shouldFilter);
}

