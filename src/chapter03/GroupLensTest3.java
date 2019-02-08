package chapter03;

import chapter02.GroupLens;

import java.io.IOException;

public class GroupLensTest3 {

    public static void main(String[] args) {


        try {
            GroupLens groupLens = new GroupLens();
            // 訓練データの読み込み
            groupLens.importTrainData("exp_data/" + "train.dat");
            // テストデータの読み込み
            groupLens.importTestData("exp_data/" + "test.dat");

            groupLens.exportToCSV("Chapter03RatingTable.csv");

            // 平均値とRの計算
            groupLens.init();

            groupLens.exportR("Chapter03Similarity.csv");

            System.out.println("MSE : " + groupLens.MSE());
            groupLens.resultToCSV("Chapter03GroupLens.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
