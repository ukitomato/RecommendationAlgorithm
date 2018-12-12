package chapter02;

import java.io.*;
import java.util.Arrays;

public class GroupLensReport222 {

    public static void main(String[] args) {

        Arrays.asList(10, 100, 200, 400, 800, 1600).parallelStream().forEach(n -> {
            try {
                GroupLens groupLens = new GroupLens();
                // 訓練データの読み込み
                groupLens.importTrainData("exp_data/" + n + "User_train_Ratings.dat");
                // 平均値とRの計算
                groupLens.init();
                // テストデータの読み込み
                groupLens.importTestData("exp_data/" + n + "User_test_Ratings.dat");

                FileWriter fw = new FileWriter(new File("2-2-2-" + n + ".txt"));
                PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

                for (int userId : groupLens.getTestTable().getFirstKeySet()) {
                    if (userId > 10) break;
                    for (int movieId : groupLens.getTestTable().getMapKeySet(userId)) {
                        System.out.println("N = " + n + " s(" + String.format("%2d", userId) + "," + String.format("%4d", movieId) + ") = " + String.format("%.16f", groupLens.predictS(userId, movieId)) + " (Collect s = " + groupLens.getTestRating(userId, movieId) + ")");
                        pw.println("s(" + String.format("%2d", userId) + "," + String.format("%4d", movieId) + ") = " + String.format("%.8f", groupLens.predictS(userId, movieId)) + " (Collect s = " + groupLens.getTestRating(userId, movieId) + ", Diff = " + String.format("%.8f", Math.abs(groupLens.getTestRating(userId, movieId) - groupLens.predictS(userId, movieId))) + ")");
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
