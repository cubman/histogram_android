package com.example.android.histogram.ImageManipulation;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.jjoe64.graphview.GraphView;

/**
 * Created by Анатолий on 13.05.2017.
 */
public class GrayImage extends ImageWork implements Parcelable {
    protected GrayImage(Parcel in) {
        super(in);
    }

    public GrayImage(Bitmap Bm)
    {
        super(Bm);
    }

    // подсчитывает статистические данные по изображению
    protected void CountHistogram() {
        for (int i = 0; i < Bm.getHeight(); ++i)
            for (int j = 0;j <Bm.getWidth();++j)
                ++MainRgb[Color.red(Bm.getPixel(j, i))];
    }


    public Bitmap equalisedImage() {
        int [] h = new int[ColorsCount]; // Generate the histogram
        int [] cumhistogram = new int[ColorsCount]; // Generate cumulative frequency histogram
        double dim = Bm.getHeight() * Bm.getWidth();
        double alpha = 255.0 / dim;
        int [] Sc = new int[ColorsCount];


        for (int i = 0; i < ColorsCount; ++i)
            h[i] = MainRgb[i];

        cumhistogram[0] = h[0];
        for (int i = 1; i < ColorsCount; ++i)
            cumhistogram[i] = h[i] + cumhistogram[i - 1];

        for (int i = 0; i < ColorsCount; ++i)
            Sc[i] = (int) Math.round( alpha * (double)cumhistogram[i]);

        Bitmap bm_return = Bitmap.createBitmap(Bm.getWidth(), Bm.getHeight(), Bitmap.Config.ARGB_8888);

        for (int i = 0; i < Bm.getWidth(); ++i)
            for (int j = 0;j <Bm.getHeight();++j) {
                int px = (int) (Sc[Color.red(Bm.getPixel(i, j))]);
                bm_return.setPixel(i, j, Color.argb(255, px, px, px));
        }
        return bm_return;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GrayImage> CREATOR = new Creator<GrayImage>() {
        @Override
        public GrayImage createFromParcel(Parcel in) {
            return new GrayImage(in);
        }

        @Override
        public GrayImage[] newArray(int size) {
            return new GrayImage[size];
        }
    };
}
