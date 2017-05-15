package com.example.android.histogram.ImageManipulation;

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

        double [] hP = new double[ColorsCount];
        for (int i = 1; i < ColorsCount - 1; ++i)
            hP[i] = (h[i + 1] - h [i - 1]) / 2;

        List<Integer> m = new ArrayList<>();
        for (int i = 1; i < ColorsCount - 1; ++i)
            if (hP[i + 1] * hP[i - 1] < 0) {
                int last = -1;
                if (m.size() > 0)
                    last = m.get(m.size() - 1);
                if (i != last + 1)
                    m.add(i);
                else {
                    m.set(m.size() - 1, h[i] > h[last] ? i : last);
                }
            }

        //********************
        double [] factor = new double[m.size()];
        int left = 0;
        int right;
        double factor_sum = 0.0;
        for (int subHist = 0 ; subHist < m.size(); ++subHist) {
            if (subHist == 0)
                right = m.get(subHist);
            else
            if (subHist == m.size() - 1)
                right = ColorsCount;
            else
            {
                left = m.get(subHist) + 1;
                right = m.get(subHist + 1);
            }

            double heig = -1;
            double low = Bm.getHeight() * Bm.getWidth();
            double M = 0;

            for (int i = left; i < right; ++i) {
                M += h[i];
                if (h[i] > heig)
                    heig = h[i];
                if (h[i] < low)
                    low = h[i];
            }
            factor[subHist] = (heig - low) * Math.log10(M);
            factor_sum += factor[subHist];
        }

        double [] range = new double[m.size()];
        for (int subHist = 0 ; subHist < m.size(); ++subHist) {
            range[subHist] = (ColorsCount - 1) * factor[subHist] / factor_sum;
        }

        int [] start = new int[m.size() + 1];
        int [] stop = new int[m.size() + 1];
        start[0] = 0;
        stop[0] = (int)range[0];

        double range_sum = range[0];
        for (int subHist = 1 ; subHist < m.size(); ++subHist) {
            start[subHist] = (int)range_sum + 1;
            range_sum += range[subHist];
            stop[subHist] = (int)range_sum;
        }
        start[m.size()] = (int)range_sum;
        stop[m.size()] = ColorsCount - 1;

        //*********************
        double [] y = new double[ColorsCount];
        for (int i = 0; i < m.size(); ++i) {
            double Mi = 0;
            for (int j = start[i]; j <= stop[i]; ++j)
            {
                Mi += h[j];
            }
            for (int j = start[i]; j <= stop[i]; ++j)
            {
                int sumH = 0;
                for (int k = start[i]; k <= j; ++k)
                    sumH += h[k];

                y[j] = start[i] + range[i] * sumH / Mi;
            }
        }

        Bitmap bm_return = Bitmap.createBitmap(Bm.getWidth(), Bm.getHeight(), Bitmap.Config.ARGB_8888);

        for (int i = 0; i < Bm.getWidth(); ++i)
            for (int j = 0;j <Bm.getHeight();++j) {
                int px = (int) (y[Color.red(Bm.getPixel(i, j))]);
                bm_return.setPixel(i, j, Color.argb(255, px, px, px));
            }
        return bm_return;
    }
}
