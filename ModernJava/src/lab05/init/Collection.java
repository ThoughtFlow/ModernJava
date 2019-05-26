package lab05.init;


import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Collection {

    private static Double getAverageWithFilter(List<String> list, String searchWord) {
        // Implement this
        return 0d;
    }

    private static Map<Boolean, List<String>> getLinesWithWord(List<String> list, String searchWord) {
        // Implement this
        return new HashMap<>();
    }

    private static Map<String, Integer> getWordDictionary(List<String> list) {
        // Implement this
        return new HashMap<>();
    }

    public static void main(String... args) {

        List<String> topCountrySongs =  Arrays.asList(
                "Good Hearted Woman",
                "Colder weather",
                "Kiss An Angel Good Morning",
                "Wagon Wheel",
                "All My Ex’s Live in Texas",
                "Don’t Blink",
                "A Boy Named Sue",
                "It’s 5 O’clock Somewhere",
                "The Gambler",
                "Big Green Tractor",
                "Neon Moon",
                "Don’t Take the Girl",
                "Okie From Muskogee",
                "Forever and Ever Amen",
                "Take This Job and Shove it",
                "It’s a great day to be alive",
                "Coal Miner’s Daughter",
                "Lookin for Love",
                "Cruise",
                "Hello Darlin’",
                "God Bless the USA",
                "Play it Again",
                "Luckenbach Texas",
                "Drink in My Hand",
                "When You Say Nothing at All",
                "Your Cheatin Heart",
                "Live Like You Were Dying",
                "Ring of Fire",
                "Living on Love",
                "Stand By Your Man",
                "How Forever Feels",
                "Mountain Music",
                "On the Road Again",
                "The Dance",
                "I Fall to Pieces",
                "El Paso",
                "Dirt Road Anthem",
                "King of the Road",
                "The Devil Went Down to Georgia",
                "He Stopped Loving Her Today",
                "Springsteen",
                "I will Always Love You",
                "Folsom Prison Blues",
                "Crazy",
                "Friends In Low Places");

        System.out.println("Average title length for songs that contain the word \"Love\": " + getAverageWithFilter(topCountrySongs, "Love"));
        Map<Boolean, List<String>> found = getLinesWithWord(topCountrySongs, "Love");

        System.out.println("Songs containing the word \"Love\":");
        found.getOrDefault(true, Collections.emptyList()).forEach(s -> System.out.println(" - " + s));

        System.out.println("Dictionary of words and occurences in songs: ");
        getWordDictionary(topCountrySongs).entrySet().forEach((e -> System.out.println(" - " + e.getKey() + ": " + e.getValue())));
    }
}