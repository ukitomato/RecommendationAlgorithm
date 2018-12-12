package chapter02;

import chapter01.RatingDualTable;
import chapter01.RatingTable;
import chapter01.Table;

import java.io.*;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class GroupLens extends RatingDualTable {

    private HashMap<Integer, Double> averageS;
    private Table<Double> r;
    private Table<Integer> testTable;

    public GroupLens() {
        super();
        averageS = new HashMap<>();
        r = new Table<>();
        testTable = new Table<>();
    }

    /*
        double型二次元配列（R等）をバイナリファイルに出力
     */
    public static void exportToBinaryFile(double[][] array, String fileName) throws IOException {

        BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(fileName));
        DataOutputStream dos = new DataOutputStream(bo);
        for (int i = 1; i < array.length; i++) {
            for (int j = 1; j < array.length; j++) {
                //double型書き込み
                dos.writeDouble(array[i][j]);
            }
        }
        //ストリームをフラッシュ
        dos.flush();
        dos.close();
    }

    /*
          double型二次元配列（R等）をバイナリファイルから読み取り
     */
    public static double[][] importFromBinaryFile(int userNum, String fileName) throws IOException {

        BufferedInputStream bi = new BufferedInputStream(new FileInputStream(fileName));
        DataInputStream dis = new DataInputStream(bi);
        double[][] array = new double[userNum + 1][userNum + 1];
        for (int i = 1; i <= userNum; i++) {
            for (int j = 1; j <= userNum; j++) {
                //double型書き込み
                array[i][j] = dis.readDouble();
            }
        }

        dis.close();

        return array;
    }

    public int s(int i, int k) {
        return get(i, k);
    }

    public double avg_s(int i) {
        return averageS.get(i);
    }

    public int N() {
        return this.getUserNum();
    }

    public int M() {
        return this.getMovieNum();
    }

    public Set<Integer> I(int i) {
        return getWatchedMoviesSet(i);
    }

    public Set<Integer> U(int k) {
        return getWatchersSet(k);
    }

    public double r(int i, int j) {
        return r.get(i, j);
    }

    /*
        rのTableを返す
     */
    public Table<Double> r() {
        return r;
    }

    /*
        rを配列で返す
     */
    public double[][] rArray() {
        double[][] array = new double[getUserNum() + 1][getUserNum() + 1];
        for (int i = 1; i <= getUserNum(); i++) {
            for (int j = 1; j <= getUserNum(); j++) {
                array[i][j] = r(i, j);
            }
        }
        return array;
    }

    /*
        評価値の推定
     */
    public double predictS(int i, int k) {
        double denominator = 0.0;
        double numerator = 0.0;

        Set<Integer> U = U(k);
        U.remove(i);

        if (U.size() == 0) return avg_s(i);

        for (int j : U) {
            numerator += r(i, j) * (s(j, k) - avg_s(j));
            denominator += Math.abs(r(i, j));
        }
        if (denominator == 0) return avg_s(i);

        return avg_s(i) + numerator / denominator;
    }

    @Override
    public void importTrainData(String filePath) throws IOException {
        super.importTrainData(filePath);
        // 値の初期化
        averageS.clear();
        r.clear();
        testTable.clear();
    }

    public void importTestData(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        String line;
        while ((line = br.readLine()) != null) {
            String[] words = line.split("::", 0);
            int userId = Integer.parseInt(words[0]);
            int movieId = Integer.parseInt(words[1]);
            int rating = Integer.parseInt(words[2]);
            testTable.put(userId, movieId, rating);
        }
        br.close();
    }

    /*
        評価値平均の計算
     */
    public void calcSAverage() {
        for (int i : getUserIdSet()) {
            double s_avg_i = 0;
            for (int k : I(i)) {
                s_avg_i += s(i, k);
            }
            averageS.put(i, s_avg_i / (double) I(i).size());
        }
    }

    /*
        ユーザー間類似度の計算
     */
    public void calcR() {
        for (int i : getUserIdSet()) {
            for (int j : getUserIdSet()) {

                Set<Integer> Iij = I(i);
                Iij.retainAll(I(j));
                if (Iij.size() <= 1) {
                    r.put(i, j, 0.0);
                    continue;
                }

                double denominator_sik = 0.0;
                double denominator_sjk = 0.0;
                double numerator = 0.0;


                for (int k : Iij) {
                    numerator += (s(i, k) - avg_s(i)) * (s(j, k) - avg_s(j));
                    denominator_sik += (s(i, k) - avg_s(i)) * (s(i, k) - avg_s(i));
                    denominator_sjk += (s(j, k) - avg_s(j)) * (s(j, k) - avg_s(j));
                }

                if (denominator_sik == 0 || denominator_sjk == 0) {
                    r.put(i, j, 0.0);
                    continue;
                }
                r.put(i, j, numerator / (Math.sqrt(denominator_sik) * Math.sqrt(denominator_sjk)));
            }
        }

    }

    /*
        バイナリファイルからrを読み込む
     */
    public void importR(String similarityFilePath) throws IOException {
        double[][] array = importFromBinaryFile(getUserNum(), similarityFilePath);
        for (int i = 1; i < array.length; i++) {
            for (int j = 1; j < array[i].length; j++) {
                r.put(i, j, array[i][j]);
            }
        }
    }

    /*
        バイナリファイルへrを出力
     */
    public void exportR(String similarityFilePath) throws IOException {
        exportToBinaryFile(rArray(), similarityFilePath);
    }

    public Table<Integer> getTestTable() {
        return testTable;
    }

    public int getTestRating(int userId, int movieId) {
        return testTable.get(userId, movieId);
    }


    public double MSE() {
        double mse = 0;
        double num = 0;
        for (int i : getTestTable().getFirstKeySet()) {
            for (int j : getTestTable().getMapKeySet(i)) {
                mse += Math.pow((getTestRating(i, j) - predictS(i, j)), 2);
                num++;
            }
        }
        return Math.sqrt(mse / num);
    }

    public void init() {
        calcSAverage();
        calcR();
    }

    public static void exportShowingArray(double[][] array,String fileName) throws IOException {
        FileWriter fw = new FileWriter(new File(fileName));
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        System.out.print("    ");
        pw.print("    ");
        for(int i = 1; i < array.length; i++){
            System.out.print(String.format("%6d", i)+" ");
            pw.print(String.format("%6d", i) + " ");
        }
        System.out.println();
        pw.println();
        for (int i = 1; i < array.length; i++) {
            System.out.print( String.format("%4d", i) + " ");
            pw.print(String.format("%4d", i) + " ");
            for (int j = 1; j < array[i].length; j++) {
                System.out.print(String.format("%6s",String.format("%2.3f", array[i][j])) + " ");
                pw.print(String.format("%6s",String.format("%2.3f", array[i][j])) + " ");
            }
            System.out.println();
            pw.println();
        }
        pw.close();
    }
}
