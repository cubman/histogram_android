package com.example.android.histogram;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Анатолий on 16.05.2017.
 */
final public class Matrix {
    private final int M;             // number of rows
    private final int N;             // number of columns
    public double[][] data;   // M-by-N array

    // create M-by-N matrix of 0's
    public Matrix(int M, int N) {
        this.M = M;
        this.N = N;
        data = new double[M][N];
    }

    // create matrix based on 2d array
    public Matrix(double[][] data) {
        M = data.length;
        N = data[0].length;
        this.data = new double[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                this.data[i][j] = data[i][j];
    }

    public double ComponentProd(Matrix B, int startX, int startY) {
        double res = 0.0;

        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                res += B.data[i + startX][j + startY] * data[i][j];

        return res;
    }

    public Matrix(Bitmap b, int add) {
        this.M = b.getWidth();
        this.N = b.getHeight();
        data = new double[M + 2 * add][N + 2 * add];

        for (int i = add; i < M + add; i++)
            for (int j = add; j < N + add; j++)
                data[i][j] = Color.red(b.getPixel(i - add, j - add));

        for (int i = 0; i < M + add; ++i)
            for (int j = 0; j < add; ++j) {
                data[i][j] = data[i][j + add + 1];
                data[i][j + N + add] = data[i][j + N];
            }

        for (int i = 0; i < N + add; ++i)
            for (int j = 0; j < add; ++j) {
                data[j][i] = data[j + add + 1][i];
                data[j + M + add][i] = data[j + M][i];
            }
    }

    // return C = A * B
    public Matrix times(Matrix B) {
        Matrix A = this;
        if (A.N != B.M) throw new RuntimeException("Illegal matrix dimensions.");
        Matrix C = new Matrix(A.M, B.N);
        for (int i = 0; i < C.M; i++)
            for (int j = 0; j < C.N; j++)
                for (int k = 0; k < A.N; k++)
                    C.data[i][j] += (A.data[i][k] * B.data[k][j]);
        return C;
    }
}