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

   public HSI(int color) {
        double R = Color.red(color) / 255.0;
        double G = Color.green(color) / 255.0;
        double B = Color.blue(color) / 255.0;

        double MIN = Math.min(R, Math.min(G, B));

        I = (R + G + B) / 3;
        S = 1 - (3 / (R + G + B)) * MIN;

        H = Math.acos((0.5 * ((R - G) + (R - B))) / Math.sqrt((R - G) * (R - G) + (R - B) * (G - B))) * 180 / Math.PI;
        H = B > G ? 360 - H : H;

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
       }*/
    }

    public int toRGB() {
        double R = 0, G = 0, B = 0;

    /*   if (H < 0.0001) {
            Log.d("Nan", String.valueOf(H) + " " + String.valueOf(S) + " " + String.valueOf(I));
           B = G = I * (1 - S);
           R = I * (1 + ((S * Math.cos(H / 180 * Math.PI)) / (Math.cos((60 - H) / 180 * Math.PI))));
           //B = 3 * I - (G + B);
        } else*/
        if (Math.abs(H - 300) < 0.00001) {
            H -= 240;
            G = I * (1 - S);
            B = I * (1 + ((S * Math.cos(H / 180 * Math.PI)) / (Math.cos((60 - H) / 180 * Math.PI))));
            R = 3 * I - (G + B);
        } else
        if (0 <= H && H < 120) {
            B = I * (1 - S);
            R = I * (1 + ((S * Math.cos(H / 180 * Math.PI)) / (Math.cos((60 - H) / 180 * Math.PI))));
            G = 3 * I - (B + R);
        }
        else
        if (120 <= H && H < 240) {
            H -= 120;
            R = I * (1 - S);
            G = I * (1 + ((S * Math.cos(H / 180 * Math.PI)) / (Math.cos((60 - H) / 180 * Math.PI))));
            B = 3 * I - (R + G);
        }
        else
        if (240 <= H && H <= 360) {
            H -= 240;
            G = I * (1 - S);
            B = I * (1 + ((S * Math.cos(H / 180 * Math.PI)) / (Math.cos((60 - H) / 180 * Math.PI))));
            R = 3 * I - (G + B);
        }
        else  {
            R = G = B = I;
           // Log.d("Nan", String.valueOf(H) + " " + String.valueOf(S) + " " + String.valueOf(I) + " R = " + String.valueOf(r) + " G = " + String.valueOf(g) + " B = " + String.valueOf(b));
        }


        /*if (Math.abs(R - r) > 0.0001 || Math.abs(G - g) > 0.0001 ||Math.abs(B - b) > 0.0001) {
            Log.d("d1", String.valueOf(H) + " | " + String.valueOf(R) + " | " + String.valueOf(G) + " | " + String.valueOf(B));
            Log.d("d2", String.valueOf(H) + " | " + String.valueOf(r) + " | " + String.valueOf(g) + " | " + String.valueOf(b));
            Log.d("d3", String.valueOf(H) + " | " + String.valueOf(R - r) + " | " + String.valueOf(G - g) + " | " + String.valueOf(B - b));
        }*/
        return Color.argb(255, (int)(R * 255), (int)(G * 255), (int)(B * 255));
    }

    /*public HSI(int color) {
        double R = Color.red(color) / 255.0;
        double G = Color.green(color) / 255.0;
        double B = Color.blue(color) / 255.0;

        double MIN = Math.min(R, Math.min(G, B));

        I = (R + G + B) / 3;
        S = 1 - (3 / (R + G + B)) * MIN;
        H = Math.acos((0.5 * ((R - G) + (R - B))) / Math.sqrt((R - G) * (R - G) + (R - B) * (G - B))) * 180 / Math.PI;
        H = B > G ? 360 - H : H;

        r = R;
        g = G;
        b = B;
    }

    public int toRGB() {
        double R, G, B, W;
        double cos_h, cos_1047_h;
        H = H % 360.0; // cycle H around to 0-360 degrees
        H = 3.14159*H/(float)180; // Convert to radians.
        S = S>0?(S<1?S:1):0; // clamp S and I to interval [0,1]
        I = I>0?(I<1?I:1):0;

        if(H < 2.09439) {
            cos_h = Math.cos(H);
            cos_1047_h = Math.cos(1.047196667 - H);
            R = S*I/3*(1+cos_h/cos_1047_h);
            G = S*I/3*(1+(1-cos_h/cos_1047_h));
            B = 0;
            W = (1-S)*I;
        } else if(H < 4.188787) {
            H = H - 2.09439;
            cos_h = Math.cos(H);
            cos_1047_h = Math.cos(1.047196667 - H);
            G = S*I/3*(1+cos_h/cos_1047_h);
            B = S*I/3*(1+(1-cos_h/cos_1047_h));
            R = 0;
            W = (1-S)*I;
        } else {
            H = H - 4.188787;
            cos_h = Math.cos(H);
            cos_1047_h = Math.cos(1.047196667 - H);
            B = S*I/3*(1+cos_h/cos_1047_h);
            R = S*I/3*(1+(1-cos_h/cos_1047_h));
            G = 0;
            W = (1-S)*I;
        }

        if (Math.abs(R - r) > 0.0001 || Math.abs(G - g) > 0.0001 ||Math.abs(B - b) > 0.0001) {
            Log.d("d1", String.valueOf(H) + " | " + String.valueOf(R) + " | " + String.valueOf(G) + " | " + String.valueOf(B));
            Log.d("d2", String.valueOf(H) + " | " + String.valueOf(r) + " | " + String.valueOf(g) + " | " + String.valueOf(b));
            Log.d("d3", String.valueOf(H) + " | " + String.valueOf(R - r) + " | " + String.valueOf(G - g) + " | " + String.valueOf(B - b));
        }
        return Color.argb((int)(W * 255), (int)(R * 255), (int)(G * 255), (int)(B * 255));
    }*/
}
