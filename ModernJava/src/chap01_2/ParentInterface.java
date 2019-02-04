package chap01_2;

public interface ParentInterface extends GrandParentInterface {

    default String convert(String str) {
        return str.toLowerCase();
    }
}
