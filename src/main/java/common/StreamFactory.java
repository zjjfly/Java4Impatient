package common;

import java.util.stream.Stream;

/**
 * @author zjjfly
 */
public class StreamFactory {
    public static Stream<Integer> intStream(){
        return Stream.of(1,2,4,7,6,2,9);
    }

    public static Stream<String> stringStream(){
        return Stream.of("what", "which", "where","when");
    }

    public static Stream<Person> objectStream(){
        return Stream.of(new Person(1, "jjzi"), new Person(2, "zjjfly"), new Person(3, "zcc"));
    }

    public static Stream<City> cityStream(){
        return Stream.of(new City("Montgomery", "AL", 100000), new City("Juneau", "AK", 50000), new City("Sacramento", "CA", 800000),new City("Los Angeles", "CA", 4000000));
    }

}
