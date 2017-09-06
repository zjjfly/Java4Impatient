package chp02;

import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@BenchmarkMode({Mode.SingleShotTime})
public class BenchMarkTest {
    @State(Scope.Benchmark)
    public static class BenchmarkState {
        static List<String> words;

        static {
            ClassLoader classLoader = new BenchmarkState().getClass().getClassLoader();
            URL resource = classLoader.getResource("War and Peace.txt");
            assert resource != null;
            try {
                URI uri = resource.toURI();
                words = Files.lines(Paths.get(uri)).flatMap(Pattern.compile("[\\P{L}]+")::splitAsStream).collect(
                        Collectors.toList());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Benchmark
    public void exer3Steam(BenchmarkState state) throws Exception {
        state.words.stream().filter(s -> s.length() >= 12).count();
    }

    @Benchmark
    public void exer3ParallelSteam(BenchmarkState state) throws Exception {
        state.words.parallelStream().filter(s -> s.length() > 12).count();
    }
}
