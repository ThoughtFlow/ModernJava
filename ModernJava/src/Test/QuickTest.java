package Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.stream.Stream;

public class QuickTest {

    private static String bytesToHex(byte[] hash) {

        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String... args) throws Exception {
        String x = """
        POST
        dev-retail-xapi.fairstonecanada.net
        2020-03-10T20:22:44.000Z
        cliendId=D9D4071C22C7509&transEpoch=1583871764
        {"storeNumber":910001922,"authorizationCode":"60001","accountNumber":"0006030491058002215","additionalRequestField1":"NO MEMO-POST","planNumber":12120,"salesperson":"1811","transactionAmount":10,"ticketNumber":"123456","actionCode":"SFSC","sKU":0,"effectiveDate":"2020-02-01"}""";

        Stream.of(x.toCharArray()).forEach(System.out::print);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update("mysecret".getBytes(StandardCharsets.UTF_8));
        byte[] array = digest.digest(x.getBytes(StandardCharsets.UTF_8));

        System.out.println();
        System.out.println(bytesToHex(array));
    }
}
