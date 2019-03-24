package chap12;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Flow;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.net.http.HttpClient.Redirect;
import static java.net.http.HttpClient.Version.HTTP_2;

public class HttpExamples {

    private static List<String> JSON_BODY = Arrays.asList("{",
            "   \"Param1\": \"Value1\",",
            "   \"Param2\": \"Value2\",",
            "}");


    private static HttpClient getClient() {
        return HttpClient.newBuilder().version(HTTP_2).connectTimeout(Duration.ofSeconds(5)).followRedirects(Redirect.NORMAL).build();
    }

    private static void getBodyAsString() throws URISyntaxException, InterruptedException, IOException {
        // Get body as string
        System.out.println("GetBodyAsString=============");

        URI uri = new URI("http://en.wikipedia.org/wiki/Java_(programming_language)");
        HttpClient client =
                HttpClient.newBuilder().version(HTTP_2).connectTimeout(Duration.ofSeconds(5)).followRedirects(Redirect.ALWAYS).build();

        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private static void getBodyAsStream(HttpClient client, URI uri) throws InterruptedException, IOException {
        // Get body as stream
        System.out.println("GetBodyAsStream=============");

        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<Stream<String>> response = client.send(request, HttpResponse.BodyHandlers.ofLines());
        response.body().forEach(System.out::println);
    }

    private static void getBodyAsFile(HttpClient client, URI uri) throws InterruptedException, IOException {
        // Get body as path
        System.out.println("GetBodyAsFile=============");

        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(Path.of("/tmp/downloaded.txt")));
        response.body().forEach(System.out::println);
    }

    private static void getBodyAsSubscriber(HttpClient client, URI uri) throws InterruptedException, IOException {
        // Get body as subscriber
        System.out.println("getBodyAsSubscriber=============");

        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        client.send(request,
                HttpResponse.BodyHandlers.fromLineSubscriber(new Flow.Subscriber<>() {

                    private Flow.Subscription subscription;

                    @Override
                    public void onSubscribe(Flow.Subscription subscription) {
                        this.subscription = subscription;
                        subscription.request(1);
                    }

                    @Override
                    public void onNext(String item) {
                        subscription.request(1);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    private static void getHeaders(HttpClient client, URI uri) throws InterruptedException, IOException {
        // get headers
        System.out.println("getHeaders=============");

        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        response.headers().map().entrySet().stream().forEach(next -> {
            System.out.println(next.getKey());
            next.getValue().stream().forEach(value -> System.out.println("  " + value));
        });
    }

    private static void getBodyWithCookies() throws InterruptedException, URISyntaxException, IOException {
        // Get cookies
        System.out.println("getBodyWithCookies=============");

        CookieManager cookieManager = new CookieManager();
        URI uri = new URI("https://postman-echo.com/cookies/set?cookie1=value1&cookie2=value2");

        HttpClient client = HttpClient.newBuilder().version(HTTP_2).connectTimeout(Duration.ofSeconds(5)).
                followRedirects(Redirect.NORMAL).cookieHandler(cookieManager).build();
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Map<String, List<String>> map = cookieManager.get(uri, response.headers().map());
        map.keySet().stream().forEach(k -> map.get(k).stream().forEach(System.out::println));
    }

    private static void getBodyAsPromise(HttpClient client, URI uri) throws InterruptedException, ExecutionException {
        // Get body as promise
        System.out.println("getBodyAsPromise=============");

        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        CompletableFuture<HttpResponse<Stream<String>>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofLines());
        Stream<String> stream = response.get().body();
        System.out.println("Line count: " + stream.count());
    }

    private static void postAsString(HttpClient client, URI uri) throws InterruptedException, IOException {
        // POST a JSON construct
        System.out.println("postAsString=============");

        String stringifiedBody = JSON_BODY.stream().reduce("", String::concat);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(stringifiedBody))
                .uri(uri)
                .header("Accept-Language", "en-US,en")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        System.out.println(response.statusCode());
    }

    private static void getAsWebsocket(HttpClient client, URI uri) throws InterruptedException, ExecutionException {

        System.out.println("getAsWebsocket=============");

        WebSocket.Builder webSocketBuilder = client.newWebSocketBuilder();
        CompletableFuture<WebSocket> promise = webSocketBuilder.buildAsync(uri, new WebSocket.Listener() {

            @Override
            public void onOpen(WebSocket webSocket) {
                System.out.println("Connected");
                WebSocket.Listener.super.onOpen(webSocket);
            }

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                System.out.println("onText received with data " + data);
                return WebSocket.Listener.super.onText(webSocket, data, last);
            }

            @Override
            public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                System.out.println("Closed with status " + statusCode + ", reason: " + reason);
                return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
            }
        });

        WebSocket webSocket = promise.get();
        IntStream.rangeClosed(1, 100).forEach(i -> webSocket.sendText("This is message: " + i, true));
        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "ok").thenRun(() -> System.out.println("Sent close"));
        Thread.sleep(5000);
    }

    public static void main(String... args) {
        try {
            URI uri = new URI("http://en.wikipedia.org/wiki/Java_(programming_language)");
            HttpClient client = getClient();
            getBodyAsString();
            getBodyAsFile(client, uri);
            getBodyAsStream(client, uri);
            getBodyAsSubscriber(client, uri);
            getHeaders(client, uri);
            getBodyAsPromise(client, uri);
            postAsString(client, URI.create("http://httpbin.org/post"));
            getBodyWithCookies();
            getAsWebsocket(client, new URI("wss://echo.websocket.org"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}