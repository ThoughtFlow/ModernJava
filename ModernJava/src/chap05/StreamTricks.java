package chap05;

import lab.util.BiHolder;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamTricks {

    private static List<String> filterForOccurrences(List<String> stringList, String searchWord) {
        // Returns the lines where one or more occurrences of searchWord is found from the list of strings
        return stringList.stream().
                filter(s -> s.contains(searchWord)).
                collect(Collectors.toList());
    }

    private static long countLineMatches(List<String> stringList, String searchWord) {
        // Returns a count of lines where one or more occurrences of searchWord is found from the list of strings
        return stringList.stream().
                filter(s -> s.contains(searchWord)).
                count();
    }

    private static String getFirstLineWithMatchAndNoComma(List<String> stringList, String searchWord, char excludeChar) {
        // Returns the first line where one or more occurrences of searchWord is found from the list of strings.
        // Must not contain any occurrences of excludeChar
        return stringList.stream().
                filter(s -> s.contains(searchWord)).
                filter(s -> IntStream.range(0, s.length()).allMatch(i -> s.charAt(i) != excludeChar)).
                findFirst().
                orElse("None found");
    }

    private static String getLongestMatchingLine(List<String> stringList, String searchWord) {
        // Returns the longest line, excluding " ", ",", and "." where one or more occurrences of searchWord is found
        // from the list of strings.
        return stringList.stream().
                filter(s -> s.contains(searchWord)).
                map(s -> s.replace(" ", "")).
                map(s -> s.replace(",", "")).
                map(s -> s.replace(".", "")).
                reduce((l, r) -> l.length() > r.length() ? l : r).
                orElse("None found");
    }

    private static String getLongestMatchingLineKeepOriginal(List<String> stringList, String searchWord) {
        // Returns the longest line, excluding " ", ",", and "." where one or more occurrences of searchWord is found
        // from the list of strings but keeps original string intact.
        return stringList.stream().map(s -> new BiHolder<>(s, s)).
                filter(h -> h.getT().contains(searchWord)).
                peek(h -> h.setU(h.getU().replace(" ", ""))).
                peek(h -> h.setU(h.getU().replace(",", ""))).
                peek(h -> h.setU(h.getU().replace(".", ""))).
                reduce((l, r) -> l.getU().length() > r.getU().length() ? l : r).
                orElse(new BiHolder<>("None found")).getT();
    }

    private static String getLongestMatchingLineKeepOriginalOptimized(List<String> stringList, String searchWord) {
        // Returns the longest line, excluding " ", ",", and "." where one or more occurrences of searchWord is found
        // from the list of strings but keeps original string intact (using currying).
        BiFunction<BiHolder<String, String>, String, BiHolder<String, String>>
                replacer = (h, s) -> h.setU(h.getU().replace(s, ""));

        return stringList.stream().map(s -> new BiHolder<>(s, s)).
                filter(h -> h.getT().contains(searchWord)).
                map(h -> replacer.apply(h, " ")).
                map(h -> replacer.apply(h, ",")).
                map(h -> replacer.apply(h, "'")).
                reduce((l, r) -> l.getU().length() > r.getU().length() ? l : r).
                orElse(new BiHolder<>("None found")).getT();
    }

    public static void main(String... args) {
        List<String> list = Arrays.asList(
                "Java is a general-purpose programming language that is class-based, object-oriented, and designed to have as few implementation dependencies as possible.",
                "It is intended to let application developers \"write once, run anywhere\" (WORA), meaning that compiled code can run on all platforms that support",
                "Java without the need for recompilation.[17] Java applications are typically compiled to \"bytecode\" that can run on any Java virtual machine (JVM) regardless of the underlying computer architecture.",
                "The syntax of is similar to C and C++, but it has fewer low-level facilities than either of them.",
                "As of 2018, Java was one of the most popular programming languages in use according to GitHub, particularly for client-server web applications, with a reported 9 million developers.",
                "Java was originally developed by James Gosling at Sun Microsystems (which has since been acquired by Oracle) and released in 1995 as a core component of Sun Microsystems' Java platform. ",
                "The original and reference implementation Java compilers, virtual machines, and class libraries were originally released by Sun under proprietary licenses. ",
                "As of May 2007, in compliance with the specifications of the Java Community Process, Sun had relicensed most of its Java technologies under the GNU General Public License.",
                "Meanwhile, others have developed alternative implementations of these Sun technologies, such as the GNU Compiler for Java (bytecode compiler), GNU Classpath (standard libraries), and IcedTea-Web (browser plugin for applets).",
                "The latest version is 12, released in March 2019. Since version 9 is no longer supported, Oracle advises its users to \"immediately transition\" to version 12.",
                "Oracle released the last public update for the legacy Java 8 LTS, which is free for commercial use, in January 2019. ",
                "Version 8 will be supported with public updates for personal use up to at least December 2020.",
                "Oracle and others \"highly recommend that you uninstall older versions\" because of serious risks due to unresolved security issues.",
                "Oracle extended support for Java 6 ended in December 2018.");

        System.out.println("occurrences of the word \"Java \":");
        filterForOccurrences(list, "Java").forEach(l -> System.out.println("  - " + l));
        System.out.println("Lines with occurrences of word \"Java\": " + countLineMatches(list, "Java"));
        System.out.println("First matching line: " + getFirstLineWithMatchAndNoComma(list, "Java", '-'));
        System.out.println("Longest matching line: " + getLongestMatchingLine(list, "Java"));
        System.out.println("Longest matching line: " + getLongestMatchingLineKeepOriginal(list, "Java"));
        System.out.println("Longest matching line: " + getLongestMatchingLineKeepOriginalOptimized(list, "Java"));
    }
}
