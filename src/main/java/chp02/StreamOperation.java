package chp02;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zjjfly
 */
public class StreamOperation {
    public static void main(String[] args) {
        try {
            String content = new String(Files.readAllBytes(
                    Paths.get("gradlew")), StandardCharsets.UTF_8);
            List<String> words = Arrays.asList(content.split("[\\P{L}]+"));
            //使用传统的迭代
            int count = 0;
            for (String word : words) {
                if (word.length() > 12) {
                    count++;
                }
            }
            //使用Stream
            long cnt = words.stream().filter(w -> w.length() > 12).count();
            System.out.println(cnt);
            //使用并行Stream
            ArrayList<String> collect = words.parallelStream().filter(w -> w.length() > 12)
                                             .collect(ArrayList<String>::new, ArrayList::add,
                                                      ArrayList::addAll);
            System.out.println(collect);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
