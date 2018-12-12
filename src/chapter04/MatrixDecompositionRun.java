package chapter04;


import java.io.*;

public class MatrixDecompositionRun {
    public static void main(String[] args) {
        int n = 1600;
        double lamda = 0.1;
        int k = 14;
        double eta = 0.002;

        if (args.length != 0) {
            n = Integer.parseInt(args[0]);
            lamda = Double.parseDouble(args[1]);
            k = Integer.parseInt(args[2]);
            eta = Double.parseDouble(args[3]);
        }
        try {
            MatrixDecomposition matrixDecomposition = new MatrixDecomposition();

            matrixDecomposition.importTrainData("exp_data/" + n + "User_train_Ratings.dat");
            matrixDecomposition.importTestData("exp_data/" + n + "User_test_Ratings.dat");

            long start = System.currentTimeMillis();
            // Kのセット
            matrixDecomposition.setK(k);
            // 確率勾配法によるU, Vの更新
            matrixDecomposition.SGD(eta);

            double tMSE = matrixDecomposition.testMSE();
            System.out.println("N = " + String.format("%4d", n) + " K = " + String.format("%2d", k) + " λ = " + String.format("%.2f", lamda) + " TestMSE = " + tMSE);
            System.out.println("Time : " + (System.currentTimeMillis() - start));
        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }
}

