package chapter01;

import java.io.IOException;

import static chapter01.HandleData.*;


public class HandleDataTest4 {
    public static void main(String[] args) {
        try {
            RatingTable ratingTable = new RatingTable();
            ratingTable.importTrainData("exp_data/1600User_train_Ratings.dat");

            //課題1-4
            int[][] exportArray = ratingTable.toArray();
            exportFromArrayToCSV(exportArray, "array.csv");
            int[][] importCSVArray = importFromCSVToArray("array.csv");

            exportFromArrayToBinary(exportArray, "array.dat");
            int[][] importDatArray = importFromBinaryToArray("array.dat");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
