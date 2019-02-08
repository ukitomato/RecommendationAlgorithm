package chapter03;


import chapter04.MatrixDecomposition;

import java.io.*;

public class MatrixDecompositionTestEta {

    public static void main(String[] args) {
        double[] etas = new double[50];
        for (int i = 1; i <= 50; i++) {
            etas[i - 1] = (double) i * 0.00001;
        }
        int K = 2;
        double lambda = 0.31;

        try {
            FileWriter fw = new FileWriter(new File("Chapter03MatrixResultEta.csv"));
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.println("K,λ,η,TestMSE");

            MatrixDecomposition matrixDecomposition = new MatrixDecomposition();

            matrixDecomposition.importTrainData("exp_data/train.dat");
            matrixDecomposition.importTestData("exp_data/test.dat");

            for (double eta : etas) {

                matrixDecomposition.setK(K);
                // 確率勾配法によるU, Vの更新
                matrixDecomposition.rSGD(lambda, eta);

                double tMSE = matrixDecomposition.testMSE();

                System.out.println("K = " + String.format("%2d", K) + " λ = " + String.format("%.2f", lambda) + " η = " + String.format("%.4f", eta) + " " + tMSE);
                pw.println(K + "," + lambda + "," + eta + "," + tMSE);
            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

