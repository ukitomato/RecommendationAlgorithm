package chapter01;

import java.io.*;
import java.util.Random;
import java.util.TreeSet;

import static chapter01.HandleData.exportResultTimesCSV;
import static chapter01.HandleData.exportResultTimesFile;


public class HandleDataTest3 {
    public static void main(String[] args) {
        try {

            /*
                課題1-3
             */
            // nを格納
            int[] ns = {100, 200, 400, 800, 1600};
            // nの種類分ratingTableを作成
            RatingTable[] ratingTables = new RatingTable[ns.length];

            for (int i = 0; i < ns.length; i++) {
                ratingTables[i] = new RatingTable();
                ratingTables[i].importTrainData("exp_data/" + ns[i] + "User_train_Ratings.dat");
            }

            // Movieデータの読み込み
            MovieTable movieTable = new MovieTable();
            movieTable.importFromFile("exp_data/movies.dat");

            // 課題1-3-2
            System.out.println("課題1-3-2");
            for (int i = 0; i < ns.length; i++) {
                System.out.println("\n" + ns[i] + "User_train_Ratings.dat\n");
                // 表示
                movieTable.showMovieList(ratingTables[i].getCommonWatchedMoviesSet(1, 5));
            }

            // 課題1-3-3
            System.out.println("課題1-3-3");
            for (int i = 0; i < ns.length; i++) {
                System.out.println("\n" + ns[i] + "User_train_Ratings.dat\n");
                // 表示
                movieTable.showUserList(ratingTables[i].getCommonWatchersSet(1, 5), 1, 5);
            }

            // 課題1-3-4
            System.out.println("課題1-3-4");

            //平均時間計測
            int N = 100;// 繰り返し回数
            int R = 100;//反復回数
            long time = 0;
            long start;

            //入力変化用
            Random random = new Random(0);
            Object[] movieIdArray = movieTable.getMovieIdArray();

            FileWriter fw = new FileWriter(new File("1-3-4.csv"));
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

            //計測時間表示用
            double[] times = new double[ns.length];
            int[] itemNum = new int[ns.length];
            /*
                1-3-4-1
             */
            for (int ni = 0; ni < ns.length; ni++) {
                time = 0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < R; j++) {
                        //入力の変化
                        int movieId = (int) movieIdArray[random.nextInt(movieIdArray.length)];

                        start = System.currentTimeMillis();
                        // 処理
                        for (int userId : new TreeSet<>(ratingTables[ni].getWatchersSet(movieId))) {
                            ratingTables[ni].get(userId, movieId);
                        }
                        time += System.currentTimeMillis() - start;
                        itemNum[ni] += ratingTables[ni].getWatchersSet(movieId).size();
                    }
                }
                itemNum[ni] = itemNum[ni] / N;
                times[ni] = ((double) time / (double) N);
            }
            //計測時間結果表示
            System.out.println("----------Result----------");
            for (int ni = 0; ni < ns.length; ni++) {
                System.out.println("N = " + ns[ni] + " average : " + times[ni] + "ms : " + itemNum[ni]);
            }
            //計測時間結果書き出し
            exportResultTimesFile(ns, times, "1-3-4-1.txt");
            exportResultTimesCSV(ns, times, itemNum, "1-3-4-1.csv");

            //計測時間結果書き出し
            pw.println("N,time");
            for (int ni = 0; ni < ns.length; ni++) {
                pw.println( ns[ni] + "," + times[ni]);
            }
            /*
                1-3-4-2
             */
            itemNum = new int[ns.length];
            for (int ni = 0; ni < ns.length; ni++) {
                time = 0;
                Object[] userIdArray = ratingTables[ni].getUserIdArray();
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < R; j++) {
                        //入力の変化
                        int userId = (int) userIdArray[random.nextInt(userIdArray.length)];

                        start = System.currentTimeMillis();
                        // 処理
                        for (int movieId : new TreeSet<>(ratingTables[ni].getWatchedMoviesSet(userId))) {
                            ratingTables[ni].get(userId, movieId);
                        }
                        time += System.currentTimeMillis() - start;
                        itemNum[ni] += ratingTables[ni].getWatchedMoviesSet(userId).size();
                    }
                }
                itemNum[ni] = itemNum[ni] / N;
                times[ni] = ((double) time / (double) N);
            }
            //計測時間結果表示
            System.out.println("----------Result----------");
            for (int ni = 0; ni < ns.length; ni++) {
                System.out.println("N = " + ns[ni] + " average : " + times[ni] + "ms : " + itemNum[ni]);
            }
            //計測時間結果書き出し
            exportResultTimesFile(ns, times, "1-3-4-2.txt");
            exportResultTimesCSV(ns, times, itemNum, "1-3-4-2.csv");
//計測時間結果書き出し
            pw.println("N,time");
            for (int ni = 0; ni < ns.length; ni++) {
                pw.println( ns[ni] + "," + times[ni]);
            }
           /*
                1-3-4-3
             */
            itemNum = new int[ns.length];
            for (int ni = 0; ni < ns.length; ni++) {
                time = 0;
                Object[] userIdArray = ratingTables[ni].getUserIdArray();
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < R; j++) {
                        //入力の変化
                        int userId1 = (int) userIdArray[random.nextInt(userIdArray.length)];
                        int userId2 = (int) userIdArray[random.nextInt(userIdArray.length)];

                        start = System.currentTimeMillis();
                        // 処理
                        for (int movieId : new TreeSet<>(ratingTables[ni].getCommonWatchedMoviesSet(userId1, userId2))) {
                            ratingTables[ni].get(userId1, movieId);
                            ratingTables[ni].get(userId2, movieId);
                        }
                        time += System.currentTimeMillis() - start;
                        itemNum[ni] += ratingTables[ni].getCommonWatchedMoviesSet(userId1, userId2).size();
                    }
                }
                itemNum[ni] = itemNum[ni] / N;
                times[ni] = ((double) time / (double) N);
            }
            //計測時間結果表示
            System.out.println("----------Result----------");
            for (int ni = 0; ni < ns.length; ni++) {
                System.out.println("N = " + ns[ni] + " average : " + times[ni] + "ms : " + itemNum[ni]);
            }
            //計測時間結果書き出し
            exportResultTimesFile(ns, times, "1-3-4-3.txt");
            exportResultTimesCSV(ns, times, itemNum, "1-3-4-3.csv");
//計測時間結果書き出し
            pw.println("N,time");
            for (int ni = 0; ni < ns.length; ni++) {
                pw.println( ns[ni] + "," + times[ni]);
            }
                       /*
                1-3-4-4
             */
            itemNum = new int[ns.length];
            for (int ni = 0; ni < ns.length; ni++) {
                time = 0;
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < R; j++) {
                        //入力の変化
                        int movieId1 = (int) movieIdArray[random.nextInt(movieIdArray.length)];
                        int movieId2 = (int) movieIdArray[random.nextInt(movieIdArray.length)];

                        start = System.currentTimeMillis();
                        // 処理
                        for (int userId : new TreeSet<>(ratingTables[ni].getCommonWatchersSet(movieId1, movieId2))) {
                            ratingTables[ni].get(userId, movieId1);
                            ratingTables[ni].get(userId, movieId2);
                        }
                        time += System.currentTimeMillis() - start;
                        itemNum[ni] += ratingTables[ni].getCommonWatchersSet(movieId1, movieId2).size();
                    }
                }
                itemNum[ni] = itemNum[ni] / N;
                times[ni] = ((double) time / (double) N);
            }
            //計測時間結果表示
            System.out.println("----------Result----------");
            for (int ni = 0; ni < ns.length; ni++) {
                System.out.println("N = " + ns[ni] + " average : " + times[ni] + "ms : " + itemNum[ni]);
            }
            //計測時間結果書き出し
            exportResultTimesFile(ns, times, "1-3-4-4.txt");
            exportResultTimesCSV(ns, times, itemNum, "1-3-4-4.csv");

            //計測時間結果書き出し
            pw.println("N,time");
            for (int ni = 0; ni < ns.length; ni++) {
                pw.println( ns[ni] + "," + times[ni]);
            }
            // ファイル出力
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
