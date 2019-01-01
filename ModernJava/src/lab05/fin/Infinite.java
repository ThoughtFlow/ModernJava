package lab05.fin;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Infinite {
    public static void main(String[] args) {
        List<String> list =
                IntStream.iterate(1, i -> ++i).
                        mapToObj(Integer::toString).
                        map(s -> s + s).
                        collect(Collectors.toList());
        System.out.println(list.size());
    }
}