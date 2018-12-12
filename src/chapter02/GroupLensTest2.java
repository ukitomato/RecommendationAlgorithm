package chapter02;


import java.io.IOException;

public class GroupLensTest2 {


    public static void main(String[] args) {

        GroupLens groupLens = new GroupLens();
        try {
            groupLens.importTrainData("exp_data/100User_train_Ratings.dat");
            // 平均値の計算
            groupLens.calcSAverage();
            //Rを計算せずバイナリデータから読み込む
            groupLens.importR("100User similarities.dat");

            System.out.println(groupLens.s(1, 1029));
            System.out.println(groupLens.predictS(1, 1029));
            System.out.println(groupLens.s(11, 3197));
            System.out.println(groupLens.predictS(11, 3107));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
