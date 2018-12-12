

package chapter04;

import chapter01.RatingTable;
import chapter01.Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class MatrixDecomposition extends RatingTable {

    private double[][] U;
    private double[][] V;

    private int K;
    private int N;
    private int M;

    private int t;


    private Table<Integer> testTable;

    public MatrixDecomposition() {
        testTable = new Table<>();
    }

    public void importTestData(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        String line;
        while ((line = br.readLine()) != null) {
            String[] words = line.split("::", 0);
            int userId = Integer.parseInt(words[0]);
            int movieId = Integer.parseInt(words[1]);
            int rating = Integer.parseInt(words[2]);

            testTable.put(userId, movieId, rating);
        }

        br.close();
    }

    public double[][] getU() {
        return U;
    }

    public double[][] getV() {
        return V;
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
        initNMUV();
    }

    /*
        N, M, U, Vの初期化
     */
    public void initNMUV() {
        N = getUserNum() + 1;
        M = getMaxMoiveID() + 1;

        U = new double[K][N];
        V = new double[K][M];

        //全要素を初期化
        t = 0;

        Random random = new Random(0);
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < N; j++) {
                U[i][j] = random.nextDouble();
            }
            for (int j = 0; j < M; j++) {
                V[i][j] = random.nextDouble();
            }
        }
    }

    /*
            uの列ベクトルを返す
         */
    public double u(int i, int j) {
        return U[i][j];
    }

    /*
        uの更新
     */
    public void setU(int i, int j, double value) {
        U[i][j] = value;
    }


    /*
        vの列ベクトルを返す
     */
    public double v(int i, int j) {
        return V[i][j];
    }

    /*
        vの更新
     */
    public void setV(int i, int j, double value) {
        V[i][j] = value;
    }

    /*
        内積計算（u^T*v）
     */
    public double productUV(int ui, int vj) {
        double product = 0;
        for (int k = 0; k < K; k++) {
            product += u(k, ui) * v(k, vj);
        }
        return product;
    }

    // U,Vの更新
    public void update(double eta) {
        for (int i : getUserIdSet()) {
            for (int j : getWatchedMoviesSet(i)) {
                for (int k = 0; k < K; k++) {
                    setU(k, i, u(k, i) + eta * e(i, j) * v(k, j));
                    setV(k, j, v(k, j) + eta * e(i, j) * u(k, i));
                }
            }
        }

    }

    // U,Vの更新
    public void updaterMSE(double eta, double lamda) {
        for (int i : getUserIdSet()) {
            for (int j : getWatchedMoviesSet(i)) {
                for (int k = 0; k < K; k++) {
                    setU(k, i, u(k, i) + eta * (e(i, j) * v(k, j) - lamda * u(k, i)));
                    setV(k, j, v(k, j) + eta * (e(i, j) * u(k, i) - lamda * v(k, j)));
                }
            }
        }
    }

    /*
        s
     */
    public double s(int i, int j) {
        return get(i, j);
    }

    /*
        sの推定値
     */
    public double predictS(int i, int j) {
        return productUV(i, j);
    }

    /*
        差分
     */
    public double e(int i, int j) {
        return s(i, j) - predictS(i, j);
    }


    /*
        MSE
     */
    public double MSE() {
        double mse = 0;
        int r_size = 0;
        for (int i : getUserIdSet()) {
            r_size += getWatchedMoviesSet(i).size();
            for (int j : getWatchedMoviesSet(i)) {
                mse += (s(i, j) - predictS(i, j)) * (s(i, j) - predictS(i, j));
            }
        }
        return Math.sqrt(mse / (double) r_size);
    }

    /*
        rMSE
     */
    public double rMSE2(double lamda) {
        double uTu = 0;
        double vTv = 0;
        for (int k = 0; k < K; k++) {
            for (int i : getUserIdSet()) {
                uTu += u(k, i) * u(k, i);
            }
            for (int j : getMovieIdSet()) {
                vTv += v(k, j) * v(k, j);
            }
        }

        double mse = 0;
        for (int i : getUserIdSet()) {
            for (int j : getWatchedMoviesSet(i)) {
                mse += (s(i, j) - predictS(i, j)) * (s(i, j) - predictS(i, j));
            }
        }

        return (mse + lamda * (uTu + vTv)) / 2.0;
    }


    /*
        テストデータに対するMSE
     */
    public double testMSE() {
        double mse = 0;
        double num = 0;
        for (int i : getTestTable().getFirstKeySet()) {
            for (int j : getTestTable().getMapKeySet(i)) {
                mse += (getTestRating(i, j) - predictS(i, j)) * (getTestRating(i, j) - predictS(i, j));
                num++;
            }
        }
        return Math.sqrt(mse / num);
    }

    /*
         確率勾配法によるU,Vの更新
      */
    public void rSGD(double lamda, double eta) {
        double mse = rMSE2(lamda);
        int count = 0;
        while (true) {
            updaterMSE(eta, lamda);
            double updateMse = rMSE2(lamda);
            System.out.println("K=" + K + " λ=" + lamda + "η=" + eta + " : " + (mse - updateMse));
            if (mse - updateMse > 0.0000001) mse = updateMse;
            else {
                count++;
                if (count == 2) {
                    break;
                }
                mse = updateMse;
            }
        }
    }

    /*
        確率勾配法によるU,Vの更新
     */
    public void SGD(double eta) {
        double mse = MSE();
        int count = 0;
        while (true) {
            update(eta);
            double updateMse = MSE();
            System.out.println("K=" + K + " η=" + eta + " : " + (mse - updateMse));
            if (mse - updateMse > 0) mse = updateMse;
            else {
                count++;
                if (count == 2) {
                    break;
                }
                mse = updateMse;
            }

        }
    }


    public Table<Integer> getTestTable() {
        return testTable;
    }

    public int getTestRating(int userId, int movieId) {
        return testTable.get(userId, movieId);
    }

}

