package chapter01;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class MovieTable {
    private HashMap<Integer, String> movieMap;

    public MovieTable() {
        movieMap = new HashMap<>();
    }

    public String get(int movieId) {
        return movieMap.get(movieId);
    }

    public boolean contains(int movieId) {
        return movieMap.containsKey(movieId);
    }

    public int getMaxMovieId() {
        return new TreeSet<>(movieMap.keySet()).last();
    }

    public int getMovieNum() {
        return movieMap.keySet().size();
    }

    public Object[] getMovieIdArray() {
        return movieMap.keySet().toArray();
    }

    public HashMap<Integer, String> getMovieMap() {
        return movieMap;
    }


    public void importFromFile(String dataFilePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(dataFilePath)));
        String line;
        while ((line = br.readLine()) != null) {
            String[] words = line.split("::", 0);
            int movieId = Integer.parseInt(words[0]);
            String movieName = words[1];
            movieMap.put(movieId, movieName);
        }
        br.close();

    }

    public void importFromFile(String dataFilePath, int limit) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(dataFilePath)));
        String line;
        int count = 0;
        while ((line = br.readLine()) != null) {
            if (count > limit) break;
            String[] words = line.split("::", 0);
            int movieId = Integer.parseInt(words[0]);
            String movieName = words[1];
            movieMap.put(movieId, movieName);
            count++;
        }
        br.close();

    }

    /*
       Tableのcsv書き出し
    */
    public void exportToCSV(String csvFileName) throws IOException {

        FileWriter fw = new FileWriter(new File(csvFileName));
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));

        //ファイル書き込み
        pw.println("MovieID,MovieName");
        for (int movieId : movieMap.keySet()) {
            pw.println(movieId + "," + movieMap.get(movieId));
        }
        // ファイル出力
        pw.close();

        System.out.println("Exported CSV file.");

    }


    /*
       Table内容の表示
    */
    public void showMovieList(Set<Integer> movieIdSet) {
        int movieIdDigit = String.valueOf(Collections.max(movieMap.keySet())).length();
        System.out.println(String.format("%" + movieIdDigit + "s", "MID") + " : Title");
        //ファイル書き込み
        for (int movieId : new TreeSet<>(movieIdSet)) {
            System.out.println(String.format("%" + movieIdDigit + "d", movieId) + " : " + movieMap.get(movieId));
        }
    }

    /*
        UserIDとTitleの表示
     */
    public void showUserList(Set<Integer> userIdSet, int movieId1, int movieId2) {
        int movieIdDigit = String.valueOf(Collections.max(movieMap.keySet())).length();
        System.out.println("Watcher list of [" + get(movieId1) + "] & [" + get(movieId2) + "]");
        System.out.println(String.format("%" + movieIdDigit + "s", "UID"));
        //ファイル書き込み
        for (int userId : new TreeSet<>(userIdSet)) {
            System.out.println(String.format("%" + movieIdDigit + "d", userId));
        }
    }

    /*
       JTableを用いた表で表示
    */
    public void showMovieJTable(Set<Integer> movieIdSet) {

        JTable jTable;
        JScrollPane sp;
        String[] columnNames = {"MovieID", "MovieName"};

        DefaultTableModel tableModel
                = new DefaultTableModel(columnNames, 0);

        for (int movieId : movieIdSet) {
            tableModel.addRow(new String[]{String.valueOf(movieId), movieMap.get(movieId)});
        }

        jTable = new JTable(tableModel); //10行*2列のJTableの生成
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable.getColumn("MovieID").setPreferredWidth(50);
        jTable.getColumn("MovieName").setPreferredWidth(350);

        sp = new JScrollPane(jTable);    //スクロールペインに付加

        JFrame frame = new JFrame("Movie ID x Movie Name");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(sp, "Center");
        frame.setSize(new Dimension(420, 220));
        frame.setVisible(true);


    }

}
