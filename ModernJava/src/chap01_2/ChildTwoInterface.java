package chap01_2;

public interface ChildTwoInterface extends ParentInterface {

    default String convert(String str) {
        return str.concat(str);
    }
}
