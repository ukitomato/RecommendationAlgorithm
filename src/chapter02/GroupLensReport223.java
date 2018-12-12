package chapter02;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class GroupLensReport223 {

    public static void main(String[] args) {

        int[] ns = {100,200,400,800,1600};
        //平均時間計測

        long time;
        long start;
        double task1;
        double task2;
        //入力変化用
        Random random = new Random(0);
        try {
            FileWriter fw = new FileWriter(new File("2-2-3.csv"));
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            pw.println("N,Task1,Task2");
            for (int ni = 0; ni < 5; ni++) {
                pw.print(ns[ni] + ",");
                GroupLens groupLens = new GroupLens();
                // 訓練データの読み込み
                groupLens.importTrainData("exp_data/" + ns[ni] + "User_train_Ratings.dat");
                // 平均値とRの計算
                groupLens.calcSAverage();

                int N = 10;// 繰り返し回数
                int R = 1;//反復回数
                time = 0;
                for (int j = 0; j < N; j++) {
                    for (int i = 0; i < R; i++) {
                        start = System.currentTimeMillis();
                        groupLens.calcR();
                        time += System.currentTimeMillis() - start;
                    }
                }
                task1 = (double) time / (double) N;
                pw.print(task1 + ",");
                // テストデータの読み込み
                groupLens.importTestData("exp_data/" + ns[ni] + "User_test_Ratings.dat");

                int userNum = groupLens.getUserNum();
                N = 10;
                R = 10000;
                time = 0;
                for (int j = 0; j < N; j++) {
                    for (int i = 0; i < R; i++) {

                        start = System.currentTimeMillis();
                        groupLens.predictS(random.nextInt(userNum - 1) + 1, random.nextInt(userNum - 1) + 1);
                        time += System.currentTimeMillis() - start;
                    }
                }
                task2 = (double) time / (double) N;
                pw.println(task2);
                pw.flush();
                System.out.println("N = " + ns[ni] + " Task1 : " + task1 + " Task2 : " + task2);
            }

            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
