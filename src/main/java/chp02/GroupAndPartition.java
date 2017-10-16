package chp02;

import common.City;
import common.StreamFactory;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * @author zjjfly
 */
public class GroupAndPartition {
    public static void main(String[] args) {
        Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
        Map<String, List<Locale>> country2Locales = locales.collect(groupingBy(Locale::getCountry));
        List<Locale> swissLocales = country2Locales.get("CH");
        locales = Stream.of(Locale.getAvailableLocales());
        Map<Boolean, List<Locale>> englishAndOtherLocales = locales.collect(partitioningBy(l -> "en"
                .equals(l.getLanguage())));
        List<Locale> englishLocales = englishAndOtherLocales.get(true);
        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Set<Locale>> countryToLocaleSet = locales.collect(groupingBy(Locale::getCountry, toSet()));
        locales = Stream.of(Locale.getAvailableLocales());
        Map<String, Long> countryToLocaleCounts = locales.collect(groupingBy(Locale::getCountry, counting()));
        Stream<City> cities = StreamFactory.cityStream();
        Map<String, Integer> stateToPopulation = cities.collect(groupingBy(City::getState, summingInt(City::getPopulation)));
        cities = StreamFactory.cityStream();
        Map<String, Optional<City>> stateToLargestCity = cities
                .collect(groupingBy(City::getState, maxBy(Comparator.comparingInt(City::getPopulation))));
        cities = StreamFactory.cityStream();
        Map<String, Optional<String>> stateToLongestCityName = cities.collect(
                groupingBy(City::getState, mapping(City::getName, maxBy(Comparator.comparingInt(String::length)))));
        locales=Stream.of(Locale.getAvailableLocales());
        Map<String, Set<String>> countryToLanguages = locales
                .collect(groupingBy(Locale::getDisplayCountry, mapping(Locale::getDisplayLanguage, toSet())));
        cities = StreamFactory.cityStream();
        Map<String, IntSummaryStatistics> stateToCityPopulationSummary = cities
                .collect(groupingBy(City::getState, summarizingInt(City::getPopulation)));
        cities = StreamFactory.cityStream();
        Map<String, String> stateToCityNames = cities.collect(groupingBy(City::getState, reducing("", City::getName, (s, s2) -> s
                .length() == 0 ? s2 : s + "," + s2)));
        //reducing和reduce一样，用的不多，因为常常可以使用其他更简单的代码替代
        cities = StreamFactory.cityStream();
        stateToCityNames = cities.collect(groupingBy(City::getState, mapping(City::getName, joining(","))));
    }
}
