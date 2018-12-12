package chapter04;


import java.io.*;

public class MatrixDecompositionN {
    private static double ETA = 0.01;

    public static void main(String[] args) {
        int[] ks = {2, 3, 4, 5, 6, 7, 8,  9,10, 11, 12, 13, 14, 15, 20, 30, 40, 50};
        double[] lamdas = new double[34];
        for (int i = 1; i <= 30; i++) {
            lamdas[i - 1] = (double) i * 0.01;
        }
        for (int i = 30; i < 34; i++) {
            lamdas[i] = lamdas[i - 1] + 0.05;
        }

        int n = 10;

        try {
            FileWriter fw = new FileWriter(new File("Result/" + n + "/N_" + n + "MDResult.csv"));
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.println("N,K,λ,TestMSE");

            MatrixDecomposition matrixDecomposition = new MatrixDecomposition();

            matrixDecomposition.importTrainData("exp_data/" + n + "User_train_Ratings.dat");
            matrixDecomposition.importTestData("exp_data/" + n + "User_test_Ratings.dat");
            for (int k : ks) {
                for (double lamda : lamdas) {
                    System.out.print("N = " + String.format("%4d", n) + " K = " + String.format("%2d", k) + " λ = " + String.format("%.2f", lamda));

                    // Kのセット
                    matrixDecomposition.setK(k);
                    // 確率勾配法によるU, Vの更新
                    matrixDecomposition.rSGD(lamda, ETA);

                    double tMSE = matrixDecomposition.testMSE();

                    System.out.println(" TestMSE = " + tMSE);
                    pw.println(n + "," + k + "," + lamda + "," + tMSE);
                }
                pw.flush();
            }

            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}



