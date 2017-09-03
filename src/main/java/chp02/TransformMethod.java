package chp02;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TransformMethod {
    public static void main(String[] args) {
        //filter
        List<String> wordsList = new ArrayList<>();
        Stream<String> words = wordsList.stream();
        Stream<String> longWords = words.filter(s -> s.length() > 12);
        //map
        Stream<String> lowcaseWords = words.map(String::toLowerCase);
        Stream<Character> firstChars = words.map(s -> s.charAt(0));
        Stream<Stream<Character>> result = words.map(TransformMethod::characterStream);
        //flatMap
        Stream<Character> characterStream = words.flatMap(TransformMethod::characterStream);
    }

    public static Stream<Character> characterStream(String s){
        List<Character> result = new ArrayList<>();
        for (char c : s.toCharArray()) {
            result.add(c);
        }
        return result.stream();
    }
}
