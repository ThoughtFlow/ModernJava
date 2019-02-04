package chap01_2;

public interface ChildOneInterface extends ParentInterface {

    default String convert(String str) {
        return str.toUpperCase();
    }
}
