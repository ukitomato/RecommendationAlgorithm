package chapter02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;


public class GroupLensItem extends GroupLens {

    @Override
    public void importTrainData(String filePath) throws IOException {
        clear();
        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        String line;
        while ((line = br.readLine()) != null) {
            String[] words = line.split("::", 0);
            int userId = Integer.parseInt(words[1]);
            int movieId = Integer.parseInt(words[0]);
            int rating = Integer.parseInt(words[2]);
            put(userId, movieId, rating);
        }
        br.close();

        setRatingArray();
    }

    @Override
    public void importTestData(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        String line;
        while ((line = br.readLine()) != null) {
            String[] words = line.split("::", 0);
            int userId = Integer.parseInt(words[1]);
            int movieId = Integer.parseInt(words[0]);
            int rating = Integer.parseInt(words[2]);
            getTestTable().put(userId, movieId, rating);
        }
        br.close();
    }

    public void prune() {
        HashSet<Integer> removeSet = new HashSet<>();
        for (int ti : getTestTable().getFirstKeySet()) {
            if (!getUserIdSet().contains(ti)) removeSet.add(ti);
        }
        for (int ri : removeSet) {
            getTestTable().getTable().remove(ri);
        }

    }

}
