package chp01;

import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class Exercise {
    @Test
    public void exer1() {
        Integer[] ints = {1, 2, 5, 6, 3, 141, 12};
        long i = Thread.currentThread().getId();
        Comparator<Integer> comp = (o1, o2) -> {
            assertTrue(i == Thread.currentThread().getId());
            return Integer.compare(o1, o2);
        };
        Arrays.sort(ints, comp);
    }

    @Test
    public void exer2() {
        File test = new File("/Users/zjjfly/idea works/Java4Impatient/src/test");
        File[] files = test.listFiles(pathname -> pathname.isDirectory());
        File java = new File("/Users/zjjfly/idea works/Java4Impatient/src/test/java");
        File resources = new File("/Users/zjjfly/idea works/Java4Impatient/src/test/resources");
        assertArrayEquals(new File[]{java, resources}, files);
        File[] dir = test.listFiles(File::isDirectory);
        assertArrayEquals(new File[]{java, resources}, dir);
    }

    public File[] selectFileBySuffix(String path, String suffix) {
        return Paths.get(path).toFile().listFiles(file -> file.isFile() && file.getName().endsWith("." + suffix));
    }

    @Test
    public void exer3() throws Exception {
        File gradleConfig = new File("/Users/zjjfly/idea works/Java4Impatient/gradlew.bat");
        File[] files = new Exercise().selectFileBySuffix("/Users/zjjfly/idea works/Java4Impatient", "bat");
        assertArrayEquals(new File[]{gradleConfig}, files);
    }

    @Test
    public void exer4() throws Exception {
        File gradleScript = new File("/Users/zjjfly/idea works/Java4Impatient/build.gradle");
        File java = new File("/Users/zjjfly/idea works/Java4Impatient/src/test/java");
        File gradleSetting = new File("/Users/zjjfly/idea works/Java4Impatient/setting.gradle");
        File[] files = {gradleScript, java, gradleSetting};
        Arrays.sort(files, (o1, o2) -> o1.getParent().compareTo(o2.getParent()));
        System.out.println(Arrays.toString(files));
        Arrays.sort(files, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        System.out.println(Arrays.toString(files));
    }

    public static Runnable uncheck(RunnableEx runnableEx) {
        Runnable runnable = () -> {
            try {
                runnableEx.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        return runnable;
    }

    @Test
    public void exer6() throws Exception {
        new Thread(uncheck(
                () -> {
                    System.out.println("Zzz");
                    Thread.sleep(1000);
                })).start();
    }

    public static Runnable andThen(Runnable r1, Runnable r2) {
        return () -> {
            r1.run();
            r2.run();
        };
    }

    @Test
    public void exer7() throws Exception {
        Runnable runnable = andThen(() -> System.out.println("1"), () -> System.out.println("2"));
        new Thread(runnable).start();
    }

    @Test
    public void exer8() throws Exception {
        String[] names = {"Peter", "Paul", "Mary"};
        List<Runnable> runnables = new ArrayList<>();
        for (String name : names) {
            runnables.add(() -> System.out.println(name));
        }
        for (Runnable runnable : runnables) {
            new Thread(runnable).start();
        }
//        i不是事实上的final，所以下面的代码会报错
//        runnables.clear();、
//        for (int i = 0; i < names.length; i++) {
//            runnables.add(() -> System.out.println(names[i].var));
//        }
    }
}
