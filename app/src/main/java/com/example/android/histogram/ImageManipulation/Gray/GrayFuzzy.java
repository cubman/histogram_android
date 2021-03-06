package com.example.android.histogram.ImageManipulation.Gray;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Анатолий on 14.05.2017.
 */
public class GrayFuzzy extends GrayImage implements Parcelable {

    public GrayFuzzy(Bitmap Bm) {
        super(Bm);
    }

    protected GrayFuzzy(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GrayFuzzy> CREATOR = new Creator<GrayFuzzy>() {
        @Override
        public GrayFuzzy createFromParcel(Parcel in) {
            return new GrayFuzzy(in);
        }

        @Override
        public GrayFuzzy[] newArray(int size) {
            return new GrayFuzzy[size];
        }
    };

    @Override
    public Bitmap equalisedImage() {
        double [] h = new double[ColorsCount];

        int nu = 4;
        for (int i = 0; i < Bm.getWidth(); ++i)
            for (int j = 0;j <Bm.getHeight();++j) {
                int intnsive = Color.red(Bm.getPixel(i, j));
                for (int k = intnsive - nu; k <= intnsive + nu; ++k)
                    if (k >= 0 && k < ColorsCount)
                        h[k] += 1 - Math.abs(k - intnsive) / (double)nu;
            }

        double [] y = getFuzzyEqualisation(h);

        Bitmap bm_return = Bitmap.createBitmap(Bm.getWidth(), Bm.getHeight(), Bitmap.Config.ARGB_8888);

        for (int i = 0; i < Bm.getWidth(); ++i)
            for (int j = 0;j <Bm.getHeight();++j) {
                int px = (int) (y[Color.red(Bm.getPixel(i, j))]);
                bm_return.setPixel(i, j, Color.argb(255, px, px, px));
            }
        return bm_return;
    }
}
