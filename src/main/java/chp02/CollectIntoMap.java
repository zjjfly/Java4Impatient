package chp02;

import common.Person;
import common.StreamFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectIntoMap {
    public static void main(String[] args) {
        Stream<Person> people = StreamFactory.objectStream();
        Map<Integer, String> idToName = people.collect(Collectors.toMap(Person::getId, Person::getName));
        people = StreamFactory.objectStream();
        Map<Integer, Person> idToPerson = people.collect(Collectors.toMap(Person::getId, Function.identity()));
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        Map<String, String> languageNames = locales.collect(Collectors.toMap(
                Locale::getDisplayLanguage,
                l -> l.getDisplayLanguage(l),
                (existingValue, newValue) -> existingValue));
        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> countryLanguageSets = locales
                .collect(Collectors.toMap(Locale::getDisplayCountry, l -> Collections.singleton(l.getDisplayLanguage()),
                                          (a, b) -> {
                                              HashSet<String> set = new HashSet<>(a);
                                              set.addAll(b);
                                              return set;
                                          }));
        people = StreamFactory.objectStream();
        TreeMap<Integer, Person> id2Person = people
                .collect(Collectors.toMap(Person::getId, Function.identity(), (o, o2) -> {
                    throw new IllegalStateException();
                }, TreeMap::new));
    }
}
