package com.example.android.histogram.ImageManipulation.Color;

import android.graphics.Color;
import android.util.Log;

/**
 * Created by Анатолий on 17.05.2017.
 */

public class HSI {
    public double H, S, I;
    private double r, g, b;

    public HSI(int color) {
        double R = Color.red(color) / 255.0;
        double G = Color.green(color) / 255.0;
        double B = Color.blue(color) / 255.0;

        double MIN = Math.min(R, Math.min(G, B));

        I = (R + G + B) / 3;
        S = 1 - (3 / (R + G + B)) * MIN;
        if (Math.abs(S) < 0.0001) {
            H = 360;
        }
        else /*if (Math.abs(B - R) < 0.0001)
            H = 240;
        else
       if (Math.abs(R - G) < 0.0001)
            H = 0;
        else
        if (Math.abs(B - G) < 0.0001)
            H = 120;
        else*/
        {
            H = Math.acos((0.5 * ((R - G) + (R - B))) / Math.sqrt((R - G) * (R - G) + (R - B) * (G - B))) * 180 / Math.PI;
            H = B > G ? 360 - H : H;
        }
        // 0 120 240
        // 0 240 120
        // 120 0 240
        // 120 240 0
        // 240 120 0
        r = R;
        g = G;
        b = B;
    }

    public int toRGB() {
        double R = 0, G = 0, B = 0;

       /* if (H < 0.0001) {
            Log.d("Nan", String.valueOf(H) + " " + String.valueOf(S) + " " + String.valueOf(I));
            B = I * (1 - S);
            R = I * (1 + ((S * Math.cos(H / 180 * Math.PI)) / (Math.cos((60 - H) / 180 * Math.PI))));
            G = 3 * I - (B + R);
        } else*/
        if (0 < H && H <= 120) {
            B = I * (1 - S);
            R = I * (1 + ((S * Math.cos(H / 180 * Math.PI)) / (Math.cos((60 - H) / 180 * Math.PI))));
            G = 3 * I - (B + R);
        }
        else
        if (120 < H && H <= 240) {
            H -= 120;
            R = I * (1 - S);
            G = I * (1 + ((S * Math.cos(H / 180 * Math.PI)) / (Math.cos((60 - H) / 180 * Math.PI))));
            B = 3 * I - (R + G);
        }
        else
        if (240 < H && H <= 360) {
            H -= 240;
            G = I * (1 - S);
            B = I * (1 + ((S * Math.cos(H / 180 * Math.PI)) / (Math.cos((60 - H) / 180 * Math.PI))));
            R = 3 * I - (G + B);
        }
        else Log.d("H", String.valueOf(H));


       /* if (Math.abs(R - r) > 0.0001 || Math.abs(G - g) > 0.0001 ||Math.abs(B - b) > 0.0001) {
            Log.d("d1", String.valueOf(H) + " | " + String.valueOf(R) + " | " + String.valueOf(G) + " | " + String.valueOf(B));
            Log.d("d2", String.valueOf(H) + " | " + String.valueOf(r) + " | " + String.valueOf(g) + " | " + String.valueOf(b));
            Log.d("d3", String.valueOf(H) + " | " + String.valueOf(R - r) + " | " + String.valueOf(G - g) + " | " + String.valueOf(B - b));
        }*/
        return Color.argb(255, (int)(R * 255), (int)(G * 255), (int)(B * 255));
    }
}
