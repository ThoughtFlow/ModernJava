package chap01_2;

public class ExtensionDemo {

    public static void main(String... args) {
        GrandChildInterface grandChildInterface = x -> x;

        System.out.println( grandChildInterface.convert("Hello"));
    }
}
