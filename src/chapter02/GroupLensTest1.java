package chapter02;


import java.io.IOException;
import java.util.Arrays;

import static chapter01.HandleData.exportFromArrayToCSV;
import static chapter02.GroupLens.exportToBinaryFile;
import static chapter02.GroupLens.importFromBinaryFile;

public class GroupLensTest1 {


    public static void main(String[] args) {

        GroupLens groupLens = new GroupLens();
        try {
            int n = 100;
            groupLens.importTrainData("exp_data/" + n + "User_train_Ratings.dat");
            // sの平均値とrの計算
            groupLens.init();
            System.out.println(groupLens.predictS(1, 234));

            // バイナリファイルへ出力
            exportToBinaryFile(groupLens.rArray(), n + "User similarities.dat");
            exportFromArrayToCSV(groupLens.rArray(), n + "User similarities.csv");
            // バイナリファイルから読み込み
            double[][] array = importFromBinaryFile(n, n + "User similarities.dat");
            // 確認
            System.out.println(Arrays.deepToString(groupLens.rArray()));
            System.out.println(Arrays.deepToString(array));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
