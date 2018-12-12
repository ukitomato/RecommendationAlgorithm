package chapter02;

import java.io.IOException;

import static chapter02.GroupLens.exportShowingArray;

public class GroupLensReport221 {

    public static void main(String[] args) {
        int n = 10;

        try {
            GroupLens groupLens = new GroupLens();
            // 訓練データの読み込み
            groupLens.importTrainData("exp_data/" + n + "User_train_Ratings.dat");
            // 平均値とRの計算
            groupLens.init();
            //exportFromArrayToCSV(groupLens.rArray(), n + "User similarities.csv");
            exportShowingArray(groupLens.rArray(),"2-2-1.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
