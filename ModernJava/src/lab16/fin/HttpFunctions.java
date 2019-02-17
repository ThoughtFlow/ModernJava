package lab16.fin;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.net.http.HttpClient.Version.HTTP_2;

public class HttpFunctions {

    private static void getBodyAsString(String urlString) {
        // Implement a function that takes a URL as a string and performs an HTTP GET
        // Should have a connection timeout of 5 seconds and NOT follow any redirects
        // Body must be returned as one big string.
        Function<String, HttpResponse<String>> getAsStringFunction = url -> {
            try {
                URI uri = new URI(url);
                HttpClient client =
                        HttpClient.newBuilder().
                                version(HTTP_2).
                                connectTimeout(Duration.ofSeconds(5)).
                                followRedirects(HttpClient.Redirect.NEVER).build();

                HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
                return client.send(request, HttpResponse.BodyHandlers.ofString());

            } catch (URISyntaxException | InterruptedException | IOException exception) {
                throw new RuntimeException(exception);
            }
        };

        // Implement a function that takes the body from the previous response and prints if status == 200
        // Otherwise, just print the status.
        Consumer<HttpResponse<String>> responseConsumer = response -> {
            if (response.statusCode() == 200) {
                System.out.println(response.body());
            } else {
                System.out.println("Unexpected response: " + response.statusCode());
            }
        };

        responseConsumer.accept(getAsStringFunction.apply(urlString));
    }

    private static void getBodyAsStream(String urlString) {

        // Implement a function that takes a URL as a string and performs an HTTP GET
        // Should have a connection timeout of 5 seconds and should follow method redirects only
        // Body must be returned as a stream of strings.
        Function<String, HttpResponse<Stream<String>>> getAsStreamFunction = url -> {
            try {
                URI uri = new URI(url);
                HttpClient client =
                        HttpClient.newBuilder().
                                version(HTTP_2).
                                connectTimeout(Duration.ofSeconds(5)).
                                followRedirects(HttpClient.Redirect.NORMAL).build();

                HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
                return client.send(request, HttpResponse.BodyHandlers.ofLines());

            } catch (URISyntaxException | InterruptedException | IOException exception) {
                throw new RuntimeException(exception);
            }
        };

        // Implement a consumer that takes the body from the previous response and prints if status == 200
        // Otherwise, just print the status.
        // Consumer must also print the headers received.
        Consumer<HttpResponse<Stream<String>>> responseConsumer = response -> {
            if (response.statusCode() == 200) {
                response.body().forEach(System.out::println);
                response.headers().map().forEach((k, v) -> System.out.println("Header key: " + k + " Value: " + v));
            } else {
                System.out.println("Unexpected response: " + response.statusCode());
            }
        };

        responseConsumer.accept(getAsStreamFunction.apply(urlString));
    }

    private static void postResponseToFile(String urlString, String outputFile) {

        // Implement a function that takes a URL and performs an HTTP POST.
        // Should have a connection timeout of 5 seconds and should follow method redirects only.
        // POST must pass a form parameter named "Param1" with value "Value1".
        // Body must be returned as a future that writes to a file.
        Function<String, HttpResponse<Path>> postResponseToFileFunction = url -> {
            try {
                HttpClient client = HttpClient.newBuilder().
                        version(HTTP_2).
                        connectTimeout(Duration.ofSeconds(5)).
                        followRedirects(HttpClient.Redirect.NORMAL).
                        build();
                URI uri = new URI(url);
                String stringifiedBody = "\"Param1:\" \" value1\"";
                HttpRequest request = HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(stringifiedBody))
                        .uri(uri)
                        .header("Accept-Language", "en-US,en")
                        .header("caching", "nocache")
                        .header("time", new Date().toString())
                        .build();

                CompletableFuture<HttpResponse<Path>> future =
                        client.sendAsync(request, HttpResponse.BodyHandlers.ofFile(Path.of(outputFile)));
                return future.get();
            } catch (URISyntaxException | InterruptedException | ExecutionException exception) {
                throw new RuntimeException(exception);
            }
        };

        // Implement a function that takes the body from the previous response and prints if status == 200
        // Otherwise, just print the status.
        Consumer<HttpResponse<Path>> responseConsumer = response -> {
            if (response.statusCode() == 200) {
                System.out.println(response.body());
            } else {
                System.out.println("Unexpected response: " + response.statusCode());
            }
        };

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
        postResponseToFile("http://httpbin.org/post", "/tmp/download.txt"); // use a different path for Windows
    }
}
