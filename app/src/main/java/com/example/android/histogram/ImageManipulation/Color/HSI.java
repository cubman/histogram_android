package com.example.android.histogram.ImageManipulation.Color;

import android.graphics.Color;
import android.util.Log;

import com.example.android.histogram.Main;

/**
 * Created by Анатолий on 17.05.2017.
 */

public class HSI {
    public double H, S, I;
    private double r, g, b;

  /* public HSI(int color) {
        double R = Color.red(color) / 255.0;
        double G = Color.green(color) / 255.0;
        double B = Color.blue(color) / 255.0;

        double MIN = Math.min(R, Math.min(G, B));

        I = (R + G + B) / 3;

       if (Math.abs(R - G) < 0.0001 && Math.abs(R - B) < 0.0001) {
           S = H = 0;
       } else {
         //  S = 1 - (3 / (R + G + B)) * MIN;

           double t = 0.5 * ((R - G) + (R - B)) / Math.sqrt((R - G) * (R - G) + (R - B) * (G - B));
           if (t > 1)
               t = 1;
           if (t < -1)
               t = -1;
           H = Math.acos(t) * 180 / Math.PI;

           H = B > G ? 360 - H : H;

           if (R <= G && R <= B) S = 1 - 3*R;
           if (G <= R && G <= B ) S = 1 - 3*G;
           if (B <= R && B <= G) S = 1 - 3*B;
       }
        r = R;
        g = G;
        b = B;
      /* if (Math.abs(R - G) < 0.0001) {
           Log.d("Nan1", String.valueOf(H) + " " + String.valueOf(S) + " " + String.valueOf(I) + " R = " + String.valueOf(r) + " G = " + String.valueOf(g) + " B = " + String.valueOf(b));
        H = 0;
           S = 0.5;
       }*/
       /* else
       if (Math.abs(R - B) < 0.0001) {
          //H = 200;
           S = 0.8;
           Log.d("Nan2", String.valueOf(H) + " " + String.valueOf(S) + " " + String.valueOf(I) + " R = " + String.valueOf(r) + " G = " + String.valueOf(g) + " B = " + String.valueOf(b));
       } else
       if (Math.abs(B - G) < 0.0001) {
          // H = 200;
           S = 0.9;
           Log.d("Nan3", String.valueOf(H) + " " + String.valueOf(S) + " " + String.valueOf(I) + " R = " + String.valueOf(r) + " G = " + String.valueOf(g) + " B = " + String.valueOf(b));
       }
    }*/

  /*  public int toRGB() {
        double R = 0, G = 0, B = 0;

        double mm = 0.15;
        if (0 <= H && H < 120) {
            B = I * (1 - S);
            R = I * (1 + ((S * Math.cos(H / 180 * Math.PI)) / (Math.cos((60 - H) / 180 * Math.PI))));
            G = 3 * I - (B + R);
            if (Math.abs(r - R) > mm || Math.abs(g - G) > mm ||Math.abs(b - B) > mm )
                Log.d("rgb1", String.valueOf(255 * r) + " " + String.valueOf(255 *g) + " " + String.valueOf(255 *b) + "    " + String.valueOf(255 *R) + " " + String.valueOf(255 *G) + " " + String.valueOf(255 *B));

        }
        else
        if (120 <= H && H < 240) {
            H -= 120;
            R = I * (1 - S);
            G = I * (1 + ((S * Math.cos(H / 180 * Math.PI)) / (Math.cos((60 - H) / 180 * Math.PI))));
            B = 3 * I - (R + G);
            if (Math.abs(r - R) > mm || Math.abs(g - G) > mm ||Math.abs(b - B) > mm )
                Log.d("rgb2", String.valueOf(255 * r) + " " + String.valueOf(255 *g) + " " + String.valueOf(255 *b) + "    " + String.valueOf(255 *R) + " " + String.valueOf(255 *G) + " " + String.valueOf(255 *B));

        }
        else
        if (240 <= H && H <= 360) {
            H -= 240;
            G = I * (1 - S);
            B = I * (1 + ((S * Math.cos(H / 180 * Math.PI)) / (Math.cos((60 - H) / 180 * Math.PI))));
            R = 3 * I - (G + B);
            if (Math.abs(r - R) > mm || Math.abs(g - G) > mm ||Math.abs(b - B) > mm )
                Log.d("rgb3", String.valueOf(255 * r) + " " + String.valueOf(255 *g) + " " + String.valueOf(255 *b) + "    " + String.valueOf(255 *R) + " " + String.valueOf(255 *G) + " " + String.valueOf(255 *B));

        }
        else  {
            R = G = B = I;
            if (Math.abs(r - R) > mm || Math.abs(g - G) > mm ||Math.abs(b - B) > mm )
                Log.d("rgb4", String.valueOf(255 * r) + " " + String.valueOf(255 *g) + " " + String.valueOf(255 *b) + "    " + String.valueOf(255 *R) + " " + String.valueOf(255 *G) + " " + String.valueOf(255 *B));

            // Log.d("Nan", String.valueOf(H) + " " + String.valueOf(S) + " " + String.valueOf(I) + " R = " + String.valueOf(r) + " G = " + String.valueOf(g) + " B = " + String.valueOf(b));
        }


        if (Math.abs(R - r) > 0.0001 || Math.abs(G - g) > 0.0001 ||Math.abs(B - b) > 0.0001) {
            Log.d("d1", String.valueOf(H) + " | " + String.valueOf(R) + " | " + String.valueOf(G) + " | " + String.valueOf(B));
            Log.d("d2", String.valueOf(H) + " | " + String.valueOf(r) + " | " + String.valueOf(g) + " | " + String.valueOf(b));
            Log.d("d3", String.valueOf(H) + " | " + String.valueOf(R - r) + " | " + String.valueOf(G - g) + " | " + String.valueOf(B - b));
        }
        return Color.argb(255, (int)(R * 255), (int)(G * 255), (int)(B * 255));
    }*/

    public HSI(int color) {
        double R = Color.red(color) / 255.0;
        double G = Color.green(color) / 255.0;
        double B = Color.blue(color) / 255.0;

        double MIN = Math.min(R, Math.min(G, B));

        I = (R + G + B) / 3;

        if (Math.abs(R - G) < 0.0001 && Math.abs(R - B) < 0.0001) {
            S = H = 0;
        } else {
              S = 1 - (3 / (R + G + B)) * MIN;

            double t = 0.5 * ((R - G) + (R - B)) / Math.sqrt((R - G) * (R - G) + (R - B) * (G - B));
            if (t > 1)
                t = 1;
            if (t < -1)
                t = -1;
            H = Math.acos(t) * 180 / Math.PI;

            H = B > G ? 360 - H : H;
        }

        r = R;
        g = G;
        b = B;
    }

    public int toRGB() {
        double R = 0, G = 0, B = 0;
        H = Math.PI * H / 180.0;
        S = S < 0 ? 0 : S > 1 ? 1 : S;
        I = I < 0 ? 0 : I > 1 ? 1 : I;

        if (Math.abs(S) < 0.0001)
            R = G = B = I;
        else {
            if (H >= 0 && H < 2 * Math.PI / 3) {
                B = (1 - S) / 3;
                R = (1 + S * Math.cos(H) / Math.cos(Math.PI / 3 - H)) / 3;
                G = 1 - R - B;
            } else if ((H >= 2 * Math.PI / 3) && (H < 4 * Math.PI / 3)) {
                H -= 2 * Math.PI / 3;
                R = (1 - S) / 3;
                G = (1 + S * Math.cos(H) / Math.cos(Math.PI / 3 - H)) / 3;
                B = 1 - R - G;
            } else if ((H >= 4 * Math.PI / 3) && (H < 2 * Math.PI)) {
                H -= 4 * Math.PI / 3;
                G = (1 - S) / 3;
                B = (1 + S * Math.cos(H) / Math.cos(Math.PI / 3 - H)) / 3;
                R = 1 - B - G;
            }
            R *= 3 * I;
            G *= 3 * I;
            B *= 3 * I;
        }

        R = R < 0 ? 0 : R > 1 ? 1 : R;
        G = G < 0 ? 0 : G > 1 ? 1 : G;
        B = B < 0 ? 0 : B > 1 ? 1 : B;

      /*  if (Math.abs(R - r) > 0.0001 || Math.abs(G - g) > 0.0001 ||Math.abs(B - b) > 0.0001) {
            Log.d("d1", String.valueOf(H) + " | " + String.valueOf(R) + " | " + String.valueOf(G) + " | " + String.valueOf(B));
            Log.d("d2", String.valueOf(H) + " | " + String.valueOf(r) + " | " + String.valueOf(g) + " | " + String.valueOf(b));
            Log.d("d3", String.valueOf(H) + " | " + String.valueOf(R - r) + " | " + String.valueOf(G - g) + " | " + String.valueOf(B - b));
        }*/
        return Color.rgb((int)(R * 255), (int)(G * 255), (int)(B * 255));
    }
}
