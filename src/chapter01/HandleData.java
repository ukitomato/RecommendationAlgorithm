package chapter01;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HandleData {

    /*
        配列のcsv書き出し
     */
    public static void exportFromArrayToCSV(int[][] array, String fileName) throws IOException {

        FileWriter fw = new FileWriter(new File(fileName));
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
        // 列の属性書き込み（movieID等）
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length - 1; j++) {
                pw.print(array[i][j] + ",");
            }
            pw.println(array[i][array[i].length - 1]);
        }
        // ファイル出力
        pw.close();

        System.out.println("Exported CSV file.");
    }
    public static void exportFromArrayToCSV(double[][] array, String fileName) throws IOException {

        FileWriter fw = new FileWriter(new File(fileName));
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
        // 列の属性書き込み（movieID等）
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length - 1; j++) {
                pw.print(array[i][j] + ",");
            }
            pw.println(array[i][array[i].length - 1]);
        }
        // ファイル出力
        pw.close();

        System.out.println("Exported CSV file.");
    }

    /*
        csvの配列読み出し
     */
    public static int[][] importFromCSVToArray(String filePath) throws IOException {
        List<int[]> arrayList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        String line;
        while ((line = br.readLine()) != null) {
            String[] words = line.split(",", 0);
            int[] nums = Stream.of(words).mapToInt(Integer::parseInt).toArray();
            arrayList.add(nums);
        }
        int[][] array = new int[arrayList.size()][arrayList.get(0).length];
        for (int i = 0; i < arrayList.size(); i++) {
            array[0] = arrayList.get(0);
        }
        br.close();
        return array;
    }

    /*
        実行時間結果の出力
     */
    public static void exportResultTimesFile(int[] ns, double[] times, String fileName) throws IOException {

        FileWriter fw = new FileWriter(new File(fileName));
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
        //計測時間結果書き出し
        pw.println("----------Result----------");
        for (int ni = 0; ni < ns.length; ni++) {
            pw.println("N = " + String.format("%4d", ns[ni]) + " average : " + times[ni] + "ms");

        }

        // ファイル出力
        pw.close();

    }
    public static void exportResultTimesCSV(int[] ns, double[] times, String fileName) throws IOException {

        FileWriter fw = new FileWriter(new File(fileName));
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
        //計測時間結果書き出し
        pw.println("N,time");
        for (int ni = 0; ni < ns.length; ni++) {
            pw.println( ns[ni] + "," + times[ni]);
        }
        // ファイル出力
        pw.close();

    }

    /*
        実行時間結果のCSV出力
     */
    public static void exportResultTimesCSV(int[] ns, double[] times, int[] itemNum, String fileName) throws IOException {

        FileWriter fw = new FileWriter(new File(fileName));
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
        //計測時間結果書き出し
        for (int ni = 0; ni < ns.length; ni++) {
            pw.println(ns[ni] + "," + times[ni] + "," + itemNum[ni]);
        }

        // ファイル出力
        pw.close();

    }

    /*
       int型二次元配列をバイナリファイルに出力
    */
    public static void exportFromArrayToBinary(int[][] array, String fileName) throws IOException {

        BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(fileName));
        DataOutputStream dos = new DataOutputStream(bo);

        dos.writeInt(array.length);
        dos.writeInt(array[0].length);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                //double型書き込み
                dos.writeInt(array[i][j]);
            }
        }
        //ストリームをフラッシュ
        dos.flush();

        dos.close();
    }

    /*
          int型二次元配列をバイナリファイルから読み取り
     */
    public static int[][] importFromBinaryToArray(String fileName) throws IOException {

        BufferedInputStream bi = new BufferedInputStream(new FileInputStream(fileName));
        DataInputStream dis = new DataInputStream(bi);
        int row = dis.readInt();
        int column = dis.readInt();
        int[][] array = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                //double型書き込み
                array[i][j] = dis.readInt();
            }
        }

        dis.close();

        return array;
    }


}
