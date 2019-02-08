package chapter03;


import chapter04.MatrixDecomposition;

import java.io.*;

public class MatrixDecompositionTest {
    public static double ETA = 0.01;

    public static void main(String[] args) {
        int[] ks = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 25, 30};

        double[] lamdas = new double[75];
        for (int i = 1; i <= 75; i++) {
            lamdas[i - 1] = (double) i * 0.01;
        }

        try {
            FileWriter fw = new FileWriter(new File("Chapter03MatrixResult.csv"));
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            pw.println("K,λ,TestMSE");

            MatrixDecomposition matrixDecomposition = new MatrixDecomposition();

            matrixDecomposition.importTrainData("exp_data/train.dat");
            matrixDecomposition.importTestData("exp_data/test.dat");

            for (int k : ks) {
                for (double lamda : lamdas) {
                    // Kのセット
                    matrixDecomposition.setK(k);
                    // 確率勾配法によるU, Vの更新
                    matrixDecomposition.rSGD(lamda, ETA);

                    double tMSE = matrixDecomposition.testMSE();
                    System.out.println("K = " + String.format("%2d", k) + " λ = " + String.format("%.2f", lamda) + " TestMSE = " + tMSE);
                    pw.println(k + "," + lamda + "," + tMSE);
                }

            }
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}

