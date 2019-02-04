package chap01_2;

public interface GrandParentInterface {

    public String convertMethod(String str);

    default public String convert(String str) {
        return str;
    }
}
