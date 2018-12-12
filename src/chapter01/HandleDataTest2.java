package chapter01;

import java.io.IOException;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.IntStream;

import static chapter01.HandleData.exportResultTimesCSV;
import static chapter01.HandleData.exportResultTimesFile;


public class HandleDataTest2 {
    public static void main(String[] args) {
        try {

            /*
                課題1-2
             */
            // 要素格納MovieTableの作成
            MovieTable movieTable = new MovieTable();
            movieTable.importFromFile("exp_data/movies.dat");

            // 取り出したIDをSetに格納
            TreeSet<Integer> movieIds = new TreeSet<>();
            // movieID=1~10
            for (int movieId = 1; movieId <= 10; movieId++) {
                movieIds.add(movieId);
            }

            // JTableによる表の表示
            //movieTable.showMovieJTable(movieIds);
            // 標準出力による表示
            movieTable.showMovieList(movieIds);

            //平均時間計測
            int[] ns = {1,10,100,1000,2000,3000};  // データ数
            int N = 100000;                         // 繰り返し回数
            int R = 10;
            long time;
            long start;

            //入力変化用
            Random random = new Random(0);
            Object[] movieIdArray = movieTable.getMovieIdArray();

            double[] times = new double[ns.length];

            for (int ni = 0; ni < ns.length; ni++) {
                movieTable = new MovieTable();
                movieTable.importFromFile("exp_data/movies.dat", ns[ni]);
                time = 0;
                for (int j = 0; j < 10; j++) {
                    for (int i = 0; i < N; i++) {
                        //入力の変化
                        int movieId = (int) movieIdArray[random.nextInt(movieTable.getMaxMovieId())];

                        start = System.currentTimeMillis();
                        // 処理
                        movieTable.get(movieId);

                        time += System.currentTimeMillis() - start;

                    }
                }
                times[ni] = ((double) time / (double) (R));
                System.out.println("N = " +

                        String.format("%4d", ns[ni]) + " average : " + time  + "ms");
            }

            exportResultTimesCSV(ns, times, "1-2-5.csv");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
