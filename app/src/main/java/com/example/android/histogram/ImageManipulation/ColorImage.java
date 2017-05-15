package com.example.android.histogram.ImageManipulation;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Анатолий on 13.05.2017.
 */
public class ColorImage extends ImageWork implements Parcelable {
    protected ColorImage(Parcel in) {
        super(in);
    }

    public ColorImage(Bitmap Bm) {
        super(Bm);
    }
    // подсчитывает статистические данные по изображению
    protected void CountHistogram() {
        for (int i = 0; i < Bm.getHeight(); ++i)
            for (int j = 0;j <Bm.getWidth();++j) {
                int pic = Bm.getPixel(j, i);
                ++MainRgb[(int) (0.3 * Color.red(pic) + 0.59 * Color.green(pic) + 0.11 * Color.blue(pic))];
            }
    }


    public Bitmap equalisedImage() {
        return Bm;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ColorImage> CREATOR = new Creator<ColorImage>() {
        @Override
        public ColorImage createFromParcel(Parcel in) {
            return new ColorImage(in);
        }

        @Override
        public ColorImage[] newArray(int size) {
            return new ColorImage[size];
        }
    };
}
