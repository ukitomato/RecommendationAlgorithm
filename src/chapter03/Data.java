package chapter03;

import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Data {
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(new File("a.html")));
        FileWriter fw = new FileWriter(new File("shops.csv"));
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
        pw.println("ShopID,ShopName");
        String line;
        int index = 1;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            String regex = ".html\">.*?</a>";
            Pattern pattern;
            Matcher matcher;
            pattern = Pattern.compile(regex);

            matcher = pattern.matcher(line);
            while (matcher.find()) {
                System.out.println(matcher.group().substring(7, matcher.group().length() - 4));
                pw.println(index++ + "," + matcher.group().substring(7, matcher.group().length() - 4));
            }
        }
        br.close();
        pw.close();
    }
}
