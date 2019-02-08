package chapter04;


import java.io.*;

public class MatrixDecompositionTestEtaN {

    public static void main(String[] args) {
        double[] etas = new double[5];
        for (int i = 1; i <= 5; i++) {
            etas[i - 1] = (double) i * 0.002;
        }
        int[] ns = {100, 200};
        int[] ks = {16, 6};
        double[] lamdas = {0.18, 0.17};

        long[] times = new long[etas.length];

        for (int i = 0; i < ns.length; i++) {
            try {
                FileWriter fw = new FileWriter(new File(ns[i] + "MDηTimeResult.csv"));
                PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
                pw.println("N,η,time,TestMSE");

                MatrixDecomposition matrixDecomposition = new MatrixDecomposition();

                matrixDecomposition.importTrainData("exp_data/" + ns[i] + "User_train_Ratings.dat");
                matrixDecomposition.importTestData("exp_data/" + ns[i] + "User_test_Ratings.dat");

                for (int ei = 0; ei < etas.length; ei++) {

                    long start = System.currentTimeMillis();

                    matrixDecomposition.setK(ks[i]);
                    // 確率勾配法によるU, Vの更新
                    matrixDecomposition.rSGD(lamdas[i], etas[ei]);
                    double tMSE = matrixDecomposition.testMSE();

                    times[ei] = System.currentTimeMillis() - start;

                    System.out.println("N = " + ns[i] + " η = " + String.format("%.4f", etas[ei]) + " Time = " + times[ei] + " MSE = " + tMSE);
                    pw.println(ns[i] + "," + etas[ei] + "," + times[ei] + "," + tMSE);
                }
                pw.flush();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

