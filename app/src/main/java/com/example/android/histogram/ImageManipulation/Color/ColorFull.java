package com.example.android.histogram.ImageManipulation.Color;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.histogram.ImageManipulation.ImageWork;

/**
 * Created by Анатолий on 31.05.2017.
 */
public class ColorFull extends ColorImage implements Parcelable {
    public ColorFull(Bitmap Bm) {
        super(Bm);
    }

    protected ColorFull(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<ColorFull> CREATOR = new Creator<ColorFull>() {
        @Override
        public ColorFull createFromParcel(Parcel in) {
            return new ColorFull(in);
        }

        @Override
        public ColorFull[] newArray(int size) {
            return new ColorFull[size];
        }
    };

    @Override
    public Bitmap equalisedImage() {
        int [] h = new int[ColorsCount];

        for (int i = 0; i < Bm.getWidth(); ++i)
            for (int j = 0;j <Bm.getHeight();++j)
                ++h[(int)(new HSI(Bm.getPixel(i, j)).I * 255)];

        int [] Sc = getFullEqualisation(h);

        Bitmap bm_return = Bitmap.createBitmap(Bm.getWidth(), Bm.getHeight(), Bitmap.Config.ARGB_8888);

        for (int i = 0; i < Bm.getWidth(); ++i)
            for (int j = 0;j <Bm.getHeight();++j) {
                int cl = Bm.getPixel(i, j);
                HSI hsi = new HSI(cl);
                hsi.I = Sc[(int)(hsi.I * 255)] / 255.0;
                bm_return.setPixel(i, j, hsi.toRGB());
            }
        return bm_return;
    }

}
