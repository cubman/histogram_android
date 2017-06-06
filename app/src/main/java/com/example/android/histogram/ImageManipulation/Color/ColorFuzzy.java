package com.example.android.histogram.ImageManipulation.Color;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Анатолий on 31.05.2017.
 */
public class ColorFuzzy  extends ColorImage implements Parcelable {
    public ColorFuzzy(Parcel in) {
        super(in);
    }

    public ColorFuzzy(Bitmap Bm) {
        super(Bm);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ColorFuzzy> CREATOR = new Creator<ColorFuzzy>() {
        @Override
        public ColorFuzzy createFromParcel(Parcel in) {
            return new ColorFuzzy(in);
        }

        @Override
        public ColorFuzzy[] newArray(int size) {
            return new ColorFuzzy[size];
        }
    };

    @Override
    public Bitmap equalisedImage() {

        double [] h = new double[ColorsCount];

        int nu = 4;
        for (int i = 0; i < Bm.getWidth(); ++i)
            for (int j = 0;j <Bm.getHeight();++j) {
                int intnsive = (int)(new HSI(Bm.getPixel(i, j)).I * 255);
                for (int k = intnsive - nu; k <= intnsive + nu; ++k)
                    if (k >= 0 && k < ColorsCount)
                        h[k] += 1 - Math.abs(k - intnsive) / (double)nu;
            }

        double [] y = getFuzzyEqualisation(h);

        Bitmap bm_return = Bitmap.createBitmap(Bm.getWidth(), Bm.getHeight(), Bitmap.Config.ARGB_8888);

        for (int i = 0; i < Bm.getWidth(); ++i)
            for (int j = 0;j <Bm.getHeight();++j) {
                int cl = Bm.getPixel(i, j);
                HSI hsi = new HSI(cl);
                hsi.I = y[(int)(hsi.I * 255)] / 255.0;


                bm_return.setPixel(i, j, hsi.toRGB());
            }
        return bm_return;
    }
}
