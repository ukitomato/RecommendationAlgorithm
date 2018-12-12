package chapter04;


import java.io.*;
import java.util.Arrays;
import java.util.List;

public class MatrixDecompositionTest {
    public static double ETA = 0.01;

    public static void main(String[] args) {
        int[] ks = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 30, 40, 50};

        double[] lamdas = new double[30];
        for (int i = 1; i <= 30; i++) {
            lamdas[i - 1] = (double) i * 0.01;
        }

        List<Integer> nslist = Arrays.asList(100, 200, 400, 800, 1600);

        nslist.parallelStream().forEach(n -> {
            try {
                FileWriter fw = new FileWriter(new File("Result/" + n + "MDResult.csv"));
                PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

                pw.println("N,K,λ,TestMSE");

                MatrixDecomposition matrixDecomposition = new MatrixDecomposition();

                matrixDecomposition.importTrainData("expdata/" + n + "User_train_ratings.dat");
                matrixDecomposition.importTestData("expdata/" + n + "User_test_ratings.dat");

                for (int k : ks) {

                    for (double lamda : lamdas) {
                        // Kのセット
                        matrixDecomposition.setK(k);
                        // 確率勾配法によるU, Vの更新
                        matrixDecomposition.rSGD(lamda, ETA);

                        double tMSE = matrixDecomposition.testMSE();
                        System.out.println("N = " + String.format("%4d", n) + " K = " + String.format("%2d", k) + " λ = " + String.format("%.2f", lamda) + " TestMSE = " + tMSE);
                        pw.println(n + "," + k + "," + lamda + "," + tMSE);
                    }

                }
                pw.flush();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }


}

