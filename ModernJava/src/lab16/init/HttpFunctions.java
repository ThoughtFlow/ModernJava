package lab16.init;

import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class HttpFunctions {

    private static void getBodyAsString(String urlString) {
        // Implement a function that takes a URL as a string and performs an HTTP GET
        // Should have a connection timeout of 5 seconds and NOT follow any redirects
        // Body must be returned as one big string.
        Function<String, HttpResponse<String>> getAsStringFunction = null;

        // Implement a function that takes the body from the previous response and prints if status == 200
        // Otherwise, just print the status.
        Consumer<HttpResponse<String>> responseConsumer = null;

        responseConsumer.accept(getAsStringFunction.apply(urlString));
    }

    private static void getBodyAsStream(String urlString) {

        // Implement a function that takes a URL as a string and performs an HTTP GET
        // Should have a connection timeout of 5 seconds and should follow method redirects only
        // Body must be returned as a stream of strings.
        Function<String, HttpResponse<Stream<String>>> getAsStreamFunction = null;

        // Implement a consumer that takes the body from the previous response and prints if status == 200
        // Otherwise, just print the status.
        // Consumer must also print the headers received.
        Consumer<HttpResponse<Stream<String>>> responseConsumer = null;

        responseConsumer.accept(getAsStreamFunction.apply(urlString));
    }

    private static void postResponseToFile(String urlString) {

        // Implement a function that takes a URL and performs an HTTP POST.
        // Should have a connection timeout of 5 seconds and should follow method redirects only.
        // POST must pass a form parameter named "Param1" with value "Value1".
        // Body must be returned as a future that writes to a file.
        Function<String, HttpResponse<Path>> postResponseToFileFunction = null;

        // Implement a function that takes the body from the previous response and prints if status == 200
        // Otherwise, just print the status.
        Consumer<HttpResponse<Path>> responseConsumer = null;

        responseConsumer.accept(postResponseToFileFunction.apply(urlString));
    }

    private static void printSeperator() {
        System.out.println("================================");
    }

    public static void main(String... args) {
        getBodyAsString("http://www.oracle.com/technetwork/java/index.html");

        printSeperator();
        getBodyAsString("https://www.oracle.com/technetwork/java/index.html");

        printSeperator();
        getBodyAsStream("https://www.oracle.com/technetwork/java/index.html");

        printSeperator();
        postResponseToFile("http://httpbin.org/post");
    }
}