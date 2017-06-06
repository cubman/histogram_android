package com.example.android.histogram.ImageManipulation.Gray;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.histogram.ImageManipulation.ImageWork;

/**
 * Created by Анатолий on 14.05.2017.
 */
public class GrayFull extends GrayImage implements Parcelable {
    public GrayFull(Bitmap Bm) {
        super(Bm);
    }

    protected GrayFull(Parcel in) {
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

    public static final Creator<GrayFull> CREATOR = new Creator<GrayFull>() {
        @Override
        public GrayFull createFromParcel(Parcel in) {
            return new GrayFull(in);
        }

        @Override
        public GrayFull[] newArray(int size) {
            return new GrayFull[size];
        }
    };

    @Override
    public Bitmap equalisedImage() {
        int [] Sc = getFullEqualisation(MainRgb);

        Bitmap bm_return = Bitmap.createBitmap(Bm.getWidth(), Bm.getHeight(), Bitmap.Config.ARGB_8888);

        for (int i = 0; i < Bm.getWidth(); ++i)
            for (int j = 0;j <Bm.getHeight();++j) {
                int px = (Sc[Color.red(Bm.getPixel(i, j))]);
                bm_return.setPixel(i, j, Color.argb(255, px, px, px));
            }
        return bm_return;
    }
}
