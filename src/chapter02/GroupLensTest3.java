package chapter02;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.IOException;
import java.util.Arrays;

public class GroupLensTest3 {

    public static void main(String[] args) {

        Arrays.asList(10, 100, 200, 400, 800, 1600).parallelStream().forEach(n -> {
            try {
                GroupLens groupLens = new GroupLens();
                // 訓練データの読み込み
                groupLens.importTrainData("exp_data/" + n + "User_train_Ratings.dat");
                // テストデータの読み込み
                groupLens.importTestData("exp_data/" + n + "User_test_Ratings.dat");


                // 平均値とRの計算
                groupLens.init();

                System.out.println(" MSE_" + n + " : " + groupLens.MSE());


            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
}
