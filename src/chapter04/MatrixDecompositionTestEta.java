package chapter04;


import java.io.*;
import java.util.stream.IntStream;

public class MatrixDecompositionTestEta {

    public static void main(String[] args) {
        double[] defaultEtas = new double[20];
        for (int i = 1; i <= 20; i++) {
            defaultEtas[i - 1] = (double) i * 0.0005;
        }

        double[][] etas = new double[4][10];
        for (int i = 1; i <= 10; i++) {
            etas[0][i - 1] = (double) i * 0.0001 + 0.009;
        }

        for (int i = 1; i <= 10; i++) {
            etas[1][i - 1] = (double) i * 0.001 + 0.009;
        }


        int[] ns = {100, 200, 400, 800, 1600};
        int[] ks = {16, 6, 11, 14};
        double[] lamdas = {0.18, 0.17, 0.18, 0.11, 0.10};

        IntStream.rangeClosed(0, ns.length).parallel().forEach(i -> {
            try {
                FileWriter fw = new FileWriter(new File(ns[i] + "MDηResult.csv"));
                PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
                pw.println("N,K,λ,η,TestMSE");

                MatrixDecomposition matrixDecomposition = new MatrixDecomposition();

                matrixDecomposition.importTrainData("exp_data/" + ns[i] + "User_train_Ratings.dat");
                matrixDecomposition.importTestData("exp_data/" + ns[i] + "User_test_Ratings.dat");

                for (double eta : etas[i]) {

                    matrixDecomposition.setK(ks[i]);
                    // 確率勾配法によるU, Vの更新
                    matrixDecomposition.rSGD(lamdas[i], eta);

                    double tMSE = matrixDecomposition.testMSE();

                    System.out.println("N = " + String.format("%4d", ns[i]) + " K = " + String.format("%2d", ks[i]) + " λ = " + String.format("%.2f", lamdas[i]) + " η = " + String.format("%.4f", eta) + " " + tMSE);
                    pw.println(ns[i] + "," + ks[i] + "," + lamdas[i] + "," + eta + "," + tMSE);
                }
                pw.flush();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}

