package chap01_2;

@FunctionalInterface
public interface AccountCreator {
    Account create(String accountId);
}
