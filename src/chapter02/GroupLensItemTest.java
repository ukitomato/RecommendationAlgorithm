package chapter02;

import java.io.IOException;

public class GroupLensItemTest {

    public static void main(String[] args) {
        int[] ns = {100, 200, 400, 800, 1600};
        long[] times = new long[ns.length];
        double[] mses = new double[ns.length];
        for (int i = 0; i < 3; i++) {
            for (int ni = 0; ni < ns.length; ni++) {
                try {

                    GroupLens groupLens = new GroupLens();
                    // 訓練データの読み込み
                    groupLens.importTrainData("exp_data/" + ns[ni] + "User_train_Ratings.dat");
                    // テストデータの読み込み
                    groupLens.importTestData("exp_data/" + ns[ni] + "User_test_Ratings.dat");


                    long start = System.currentTimeMillis();
                    // 平均値とRの計算
                    groupLens.init();

                    double mse = groupLens.MSE();
                    mses[ni] = mse;
                    times[ni] += System.currentTimeMillis() - start;
                    System.out.println("MSE_" + ns[ni] + " : " + mse);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for (long time : times) {
            System.out.println((double) time / 3.0);
        }
        for (double mse : mses) {
            System.out.println(mse);
        }

    }
}
