package com.example.android.histogram.ImageManipulation;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.histogram.Matrix;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

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


    private Matrix getKernel(int size) {
        Matrix m = new Matrix(size, size);
        double stdv = 0.3;
        double r, s = 2.0 * stdv * stdv;
        double sum = 0.0; 
        int mid = size / 2;
        for (int x = -mid; x <= mid; x++) // Loop to generate 5x5 kernel
            for(int y = -mid; y <= mid; y++)
                sum += m.data[x + 2][y + 2] = (Math.exp(-(x * x + y * y) / s))/(Math.PI * s);


        for(int i = 0; i < size; ++i) // Loop to normalize the kernel
            for(int j = 0; j < size; ++j)
                m.data[i][j] /= sum;

        return m;
    }

    public Bitmap improveCurrentGaus() {
        int sz = 5;
        int mid = sz / 2;
        Matrix filter = getKernel(sz);
        Matrix image = new Matrix(Bm, mid);

        Bitmap bm_return = Bitmap.createBitmap(Bm.getWidth(), Bm.getHeight(), Bitmap.Config.ARGB_8888);

        for (int i = 0; i < Bm.getWidth() - mid; ++i)
            for (int j = 0;j < Bm.getHeight() - mid; ++j) {
                int color =  (int)filter.ComponentProd(image, i + mid, j + mid);
                bm_return.setPixel(i, j, Color.argb(255, color, color, color));
            }

        return bm_return;
        /*Bitmap bm_return = Bitmap.createBitmap(Bm.getWidth(), Bm.getHeight(), Bitmap.Config.ARGB_8888);
        double [] M = new double[ColorsCount];
        double M_2 = 0;
        double [] D = new double[ColorsCount];;
        double [] p = new double[ColorsCount];
        double n = MainRgb[0] / (double) (Bm.getHeight() * Bm.getWidth());
        M[0] =  n / ColorsCount;
        D[0] = n * ( n - 1) / ColorsCount;

        for (int i = 1; i < ColorsCount; ++i) {
            double pi = MainRgb[i] / (double) (Bm.getHeight() * Bm.getWidth());
            M[i] += M[i - 1] + pi * (i + 1) / ColorsCount;
            M_2 += i * i * pi / (ColorsCount * ColorsCount);
            D[i] += M_2 - M[i] * M[i];
        }*/

       /* for (int i = 0; i < ColorsCount; ++i) {
            double xi = MainRgb[i] / (double) (Bm.getHeight() * Bm.getWidth());

        }*/
        /*for (int i = 0; i < ColorsCount; ++i) {
            double xi = MainRgb[i] / (double) (Bm.getHeight() * Bm.getWidth());
            p[i] = MainRgb[i] * 1.0 / (Math.sqrt(D[i]) * Math.sqrt(2 * Math.PI)) * Math.exp(- Math.pow(xi - M[i], 2)/(2 * D[i]));
        }

        for (int i = 0; i < Bm.getWidth(); ++i)
            for (int j = 0;j <Bm.getHeight();++j) {
                int px = (int) (p[Color.red(Bm.getPixel(i, j))]);
                bm_return.setPixel(i, j, Color.argb(255, px, px, px));
            }

        return bm_return;*/
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
