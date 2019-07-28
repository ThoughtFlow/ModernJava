package chap01_2;

@FunctionalInterface
public interface Transformer {

    public String transform(String str);

    public default void printTransform(String str) {
        System.out.println("Transformed value is: " + transform(str));
    }

    public static String removeSpaces(String str) {
        return str.replaceAll(" ", "");
    }

    public static String capitalize(String str) {
        return str != null && str.length() > 0 ? str.substring(0, 1).toUpperCase() + str.substring(1) : str;
    }
}
