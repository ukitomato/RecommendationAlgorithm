package chapter01;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class RatingTable extends Table<Integer> {

    private int[][] ratingArray;

    /*
        コンストラクタ
     */
    public RatingTable() {
        super();
    }

    /*
        要素取得メソッド
     */
    @Override
    public Integer get(int i, int j) {
        return ratingArray[i][j];
    }

    /*
             要素が存在するかの判定
         */
    @Override
    public boolean contains(int i, int j) {
        return super.contains(i, j);
    }

    /*
        datファイルからRatingデータをインポート
    */
    public void importTrainData(String filePath) throws IOException {
        clear();
        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        String line;
        while ((line = br.readLine()) != null) {
            String[] words = line.split("::", 0);
            int userId = Integer.parseInt(words[0]);
            int movieId = Integer.parseInt(words[1]);
            int rating = Integer.parseInt(words[2]);
            put(userId, movieId, rating);
        }
        br.close();

        setRatingArray();
    }


    /*
        全UserIdのSetを返す
     */
    public Set<Integer> getUserIdSet() {
        return getFirstKeySet();
    }

    /*
        ユーザー数を返す
     */
    public int getUserNum() {
        return getFirstKeyNum();
    }

    /*
        全UserIdの配列を返す
     */
    public Object[] getUserIdArray() {
        return getUserIdSet().toArray();
    }


    /*
        tableに存在するMovieIdのSetを返す
    */
    public Set<Integer> getMovieIdSet() {
        return this.getSecondAllKeySet();
    }

    /*
        映画数を返す
     */
    public int getMovieNum() {
        return getSecondKeyNum();
    }

    /*
        MoiveIDの最大値を取得
     */
    public int getMaxMoiveID() {
        return Collections.max(getMovieIdSet());
    }


    /*
        movieIDの映画を見た人のIDを返す（ソート済・非破壊的）
     */
    public Set<Integer> getWatchersSet(int movieId) {
        HashSet<Integer> watchers = new HashSet<>();
        for (int userId : getUserIdSet()) {
            if (getTable().get(userId).containsKey(movieId)) {
                watchers.add(userId);
            }
        }
        return watchers;
    }

    /*
        movieIDの映画を見た人のIDとRatingを表示
     */
    public void showWatchers(int movieId) {
        System.out.println("Watchers list of Movie[" + movieId + "]. (UserID:Rating)");
        int userIdDigit = String.valueOf(Collections.max(getTable().keySet())).length();
        //ファイル書き込み
        for (int userId : new TreeSet<>(getWatchersSet(movieId))) {
            System.out.println(String.format("%" + userIdDigit + "d", userId) + " : " + get(userId, movieId));
        }

    }

    /*
        UserIDの人が見た映画IDを返す
     */
    public Set<Integer> getWatchedMoviesSet(int userId) {
        return getMapKeySet(userId);
    }

    /*
        userIDが見た映画IDとRatingを表示
     */
    public void showWatchedMovies(int userId) {
        System.out.println("Watched movie list of User[" + userId + "]. (MovieID:Rating)");
        int movieIdDigit = String.valueOf(Collections.max(getTable().get(userId).keySet())).length();
        //ファイル書き込み
        for (int movieId : new TreeSet<>(getWatchedMoviesSet(userId))) {
            System.out.println(String.format("%" + movieIdDigit + "d", movieId) + " : " + get(userId, movieId));
        }


    }

    /*
        UserID1とUserID2の共通で見たMovieIDSetを返す
     */
    public HashSet<Integer> getCommonWatchedMoviesSet(int userId1, int userId2) {
        // userID1が見た映画を取得
        HashSet<Integer> commonMoviesSet = new HashSet<>(getWatchedMoviesSet(userId1));
        // userId2が見た映画との共通要素を取得
        commonMoviesSet.retainAll(getWatchedMoviesSet(userId2));
        return commonMoviesSet;
    }

    /*
        UserID1とUserID2の共通で見たMovieIDとRatingを表示
    */
    public void showCommonWatchedMovies(int userId1, int userId2) {
        System.out.println("Watched movie list of User[" + userId1 + "] & User[" + userId2 + "]. (MovieID:Rating1:Rating2)");
        int movieIdDigit = String.valueOf(Integer.max(Collections.max(getTable().get(userId1).keySet()), Collections.max(getTable().get(userId1).keySet()))).length();
        //ファイル書き込み
        System.out.println(String.format("%" + movieIdDigit + "s", "MID") + " : 1 : 2");
        for (int movieId : new TreeSet<>(getCommonWatchedMoviesSet(userId1, userId2))) {
            System.out.println(String.format("%" + movieIdDigit + "d", movieId) + " : " + get(userId1, movieId) + " : " + get(userId2, movieId));
        }
    }

    /*
        MovieID1とMovieID2の映画を二つとも見たUserIDSetを返す
     */
    public HashSet<Integer> getCommonWatchersSet(int movieId1, int movieId2) {
        // movieId1を見たユーザーを取得
        HashSet<Integer> commonUsersSet = new HashSet<>(getWatchersSet(movieId1));
        // movieId2を見たユーザーとの共通要素を取得
        commonUsersSet.retainAll(getWatchersSet(movieId2));
        return commonUsersSet;
    }

    /*
    　MovieID1とMovieID2の映画を二つとも見た見たUserIDとRatingを表示
    */
    public void showCommonWatchers(int movieId1, int movieId2) {
        System.out.println("Watchers list of Movie[" + movieId1 + "] & Movie[" + movieId2 + "]. (UserID:Rating1:Rating2)");
        int userIdDigit = 4;
        //ファイル書き込み
        System.out.println(String.format("%" + userIdDigit + "s", "UID") + " : 1 : 2");
        for (int userId : new TreeSet<>(getCommonWatchersSet(movieId1, movieId2))) {
            System.out.println(String.format("%" + userIdDigit + "d", userId) + " : " + get(userId, movieId1) + " : " + get(userId, movieId2));
        }
    }


    /*
        Tableを配列に変換
     */
    public int[][] toArray() {
        int[][] array = new int[getUserNum() + 1][getMaxMoiveID() + 1];
        for (int userId : getUserIdSet()) {
            for (int movieId : getWatchedMoviesSet(userId)) {
                array[userId][movieId] = getTable().get(userId).get(movieId);
            }
        }
        return array;
    }

    /*
       Table内容の表示
    */
    void showTable() {
        int userIdDigit = String.valueOf(getUserNum()).length();
        int movieIdDigit = String.valueOf(Collections.max(getMovieIdSet())).length();

        // movieIDのインデックス表示
        System.out.print("     ");
        for (int movieId : getMovieIdSet()) {
            System.out.print(String.format("%" + movieIdDigit + "d", movieId) + "  ");
        }
        System.out.println();

        // Rating表示
        for (int userId = 1; userId <= getUserNum(); userId++) {
            System.out.print(String.format("%" + userIdDigit + "d", userId) + " : ");

            Iterator iterator = getMovieIdSet().iterator();
            while (true) {
                int movieId = (int) iterator.next();
                int rating = get(userId, movieId);
                System.out.print(rating != 0 ? String.format("%" + userIdDigit + "d", get(userId, movieId)) : String.format("%" + userIdDigit + "s", "_"));
                if (iterator.hasNext()) {
                    System.out.print(", ");
                } else {
                    System.out.println();
                    break;
                }

            }

        }
    }

    /*
        TableをObject配列に変換
     */
    private Object[][] toObjectArray() {
        Object[][] array = new Object[getUserNum()][getMovieNum() + 1];
        Object[] movieIdArray = getMovieIdSet().toArray();
        for (int userId = 1; userId <= getUserNum(); userId++) {
            array[userId - 1][0] = userId;
            for (int movieId = 1; movieId <= getMovieNum(); movieId++) {
                array[userId - 1][movieId] = contains(userId, (int) movieIdArray[movieId - 1]) ? get(userId, (int) movieIdArray[movieId - 1]) : "_";
            }
        }
        return array;
    }

    /*
       JTableを用いた表で表示
    */
    public void showJTable() {

        JTable jTable;
        JScrollPane sp;
        List<String> columnNames = new ArrayList<>();
        columnNames.add("UID/MID");
        for (int movieId : getMovieIdSet()) {
            columnNames.add(String.valueOf(movieId));
        }
        jTable = new JTable(toObjectArray(), columnNames.toArray());
        jTable.setShowGrid(true);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable.getColumn("UID/MID").setPreferredWidth(55);
        Object[] movieIdArray = getMovieIdSet().toArray();
        for (int i = 0; i < getMovieNum(); i++) {
            jTable.getColumn(String.valueOf(movieIdArray[i])).setPreferredWidth(40);
        }
        sp = new JScrollPane(jTable);    //スクロールペインに付加

        JFrame frame = new JFrame("Rating");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(sp, "Center");
        frame.setSize(new Dimension(800, 500));
        frame.setVisible(true);


    }

    /*
        Tableのcsv書き出し
     */
    public void exportToCSV(String csvFileName) throws IOException {

        FileWriter fw = new FileWriter(new File(csvFileName));
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
        // 列の属性書き込み（movieID等）
        pw.print("UserID,");
        Iterator iter1 = getMovieIdSet().iterator();
        while (true) {
            pw.print(iter1.next());
            if (iter1.hasNext()) {
                pw.print(",");
            } else {
                pw.println();
                break;
            }
        }
        // Rating書き込み
        for (int userId = 1; userId <= getUserNum(); userId++) {
            pw.print(userId + ",");
            Iterator iter2 = getMovieIdSet().iterator();
            while (true) {
                int movieId = (int) iter2.next();
                int rating = get(userId, movieId);
                pw.print(rating != 0 ? rating : "");
                if (iter2.hasNext()) {
                    pw.print(",");
                } else {
                    pw.println();
                    break;
                }
            }
        }
        // ファイル出力
        pw.close();

        System.out.println("Exported CSV file.");
    }

    public void setRatingArray() {
        ratingArray = toArray();
    }


}
