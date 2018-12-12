package chapter01;

import java.io.IOException;


public class HandleDataTest1 {
    public static void main(String[] args) {
        try {
            /*
                課題1-1
             */
            // 要素格納HashMapTableの作成
            RatingTable ratingTable = new RatingTable();
            ratingTable.importTrainData("exp_data/10User_train_Ratings.dat");
            // csv書き出し
            ratingTable.exportToCSV("ratingTable.csv");
            // Table表示
            ratingTable.showTable();
            ratingTable.showJTable();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
