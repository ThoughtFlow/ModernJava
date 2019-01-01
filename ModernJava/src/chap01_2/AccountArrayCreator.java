package chap01_2;

@FunctionalInterface
public interface AccountArrayCreator {
    Account[] create(int value);
}
