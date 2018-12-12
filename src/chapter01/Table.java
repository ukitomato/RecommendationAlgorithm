package chapter01;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class Table<T> {
    private HashMap<Integer, HashMap<Integer, T>> table;
    private TreeSet<Integer> secondKeySet;
    public Table() {
        table = new HashMap<>();
        secondKeySet = new TreeSet<>();
    }

    /*
        要素取得メソッド
     */
    public T get(int i, int j) {
        return table.get(i).getOrDefault(j, null);
    }

    /*
        要素格納メソッド
     */
    public void put(int i, int j, T v) {
        if (table.containsKey(i)) {
            table.get(i).put(j, v);
        } else {
            table.put(i, new HashMap<>());
            table.get(i).put(j, v);
        }
        secondKeySet.add(j);
    }

    /*
        要素が存在するかの判定
    */
    public boolean contains(int i, int j) {
        return table.containsKey(i) && table.get(i).containsKey(j);
    }

    /*
        全要素のリセット
     */
    public void clear() {
        table.clear();
        secondKeySet.clear();
    }

    /*
        最初のキーのSetを返す
     */
    public Set<Integer> getFirstKeySet() {
        return table.keySet();
    }

    /*
        最初のキーの数を返す
     */
    public int getFirstKeyNum() {
        return table.keySet().size();
    }

    /*
        ２番目のキーのSetを返す
     */
    public TreeSet<Integer> getSecondAllKeySet() {
        return secondKeySet;
    }

    /*
        ２番目ののキーの数を返す
     */
    public int getSecondKeyNum() {
        return secondKeySet.size();
    }

    /*
        tableを返す
    */
    public HashMap<Integer, HashMap<Integer, T>> getTable() {
        return table;
    }

    /*
        内側のHashMapを取得
     */
    public HashMap<Integer, T> getMap(int i) {
        return table.get(i);
    }

    /*
        内側のHashMapのKeySet()を取得
     */
    public Set<Integer> getMapKeySet(int i) {
        return table.get(i).keySet();
    }


}
