package chap01_2;

public interface GrandChildInterface extends ChildOneInterface, ChildTwoInterface {

    default String convert(String str) {
        return ChildOneInterface.super.convert(str) + ChildTwoInterface.super.convert(str);
    }
}
