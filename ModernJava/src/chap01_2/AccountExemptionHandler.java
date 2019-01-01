package chap01_2;

@FunctionalInterface
public interface AccountExemptionHandler {
    public void onAccountExempted(Account account);
}
