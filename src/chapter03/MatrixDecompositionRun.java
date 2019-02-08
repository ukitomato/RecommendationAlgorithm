package chapter03;


import chapter04.MatrixDecomposition;

import java.io.IOException;

public class MatrixDecompositionRun {
    public static void main(String[] args) {
        double lamda = 0.31;
        int k = 2;
        double eta = 0.00019;


        try {
            MatrixDecomposition matrixDecomposition = new MatrixDecomposition();

            matrixDecomposition.importTrainData("exp_data/train.dat");
            matrixDecomposition.importTestData("exp_data/test.dat");

            // Kのセット
            matrixDecomposition.setK(k);
            // 確率勾配法によるU, Vの更新
            matrixDecomposition.rSGD(lamda, eta);

            double tMSE = matrixDecomposition.testMSE();
            System.out.println("K = " + String.format("%2d", k) + " λ = " + String.format("%.2f", lamda) + " TestMSE = " + tMSE);

            matrixDecomposition.resultToCSV("Chapter03MatrixEtaResult.csv");


        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }
}

