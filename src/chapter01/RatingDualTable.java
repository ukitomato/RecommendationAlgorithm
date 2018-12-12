package chapter01;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class RatingDualTable extends RatingTable {
    // 新たにMovieにUserを格納するtableを定義
    private Table<Integer> tableOfMovie;


    public RatingDualTable() {
        tableOfMovie = new Table<>();
    }

    /*
        両方のtableに要素を格納
    */
    @Override
    public void put(int userId, int movieId, Integer rating) {
        super.put(userId, movieId, rating);
        tableOfMovie.put(movieId, userId, rating);
    }

    /*
        MovieIDのSetを返す
     */
    public Set<Integer> getMovieIdSet() {
        return tableOfMovie.getFirstKeySet();
    }


    public Object[] getMovieIdArray() {
        return getMovieIdSet().toArray();
    }

    /*
        映画数
     */
    public int getMovieNum() {
        return tableOfMovie.getFirstKeyNum();
    }

    /*
        MovieIDの最大値
     */
    public int getMaxMoiveID() {
        return Collections.max(getMovieIdSet());
    }

    /*
        MovieIDの映画を見たUserを返す
     */
    public Set<Integer> getWatchersSet(int movieId) {
        if (tableOfMovie.getTable().containsKey(movieId))
            return tableOfMovie.getTable().get(movieId).keySet();
        else return new TreeSet<>();
    }


}
