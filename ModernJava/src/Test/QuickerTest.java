package Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class QuickerTest {

    public static void main(String... args) {
        try {
            forwardToPrivateHttpServer(new URI("http://google.com?hello=yes&goodbye=no"), "bing.com", 80);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processCallback(URI uri) {

        try {
            System.out.println(uri.getHost());
            System.out.println(uri.getScheme());
            System.out.println(uri.getAuthority());
            System.out.println(uri.getPath());
            System.out.println(uri.getQuery());
            System.out.println();
//            URI fullPath = new URI("http://paypi.firstone.ca/payment/checkout?completionurl=123&order=135");
            URI fullPath = new URI(uri.getScheme(), "paypi.fairstone.ca:8080", uri.getPath(), uri.getQuery(), null);
            System.out.println(fullPath.getHost());
            System.out.println(fullPath.getPort());
            System.out.println(fullPath.getPath());
            System.out.println(fullPath.getQuery());
        } catch (URISyntaxException e) {

        }
    }

    /**
     *
     * @param publicUrl
     * @param privateHttpServerHost
     * @param port
     * @throws URISyntaxException
     * @throws InterruptedException
     * @throws IOException
     */
    private static void forwardToPrivateHttpServer(URI publicUrl, String privateHttpServerHost,int port)
            throws URISyntaxException, InterruptedException, IOException {

        HttpClient client = HttpClient.newBuilder().
                version(HttpClient.Version.HTTP_2).
                followRedirects(HttpClient.Redirect.NORMAL).
                connectTimeout(Duration.ofSeconds(60)).build();

        URI privateServerUrl = new URI(publicUrl.getScheme(), privateHttpServerHost + ":" + port,
                publicUrl.getPath(), publicUrl.getQuery(), null);

        System.out.println(privateServerUrl);
        HttpRequest request = HttpRequest.newBuilder().
                POST(HttpRequest.BodyPublishers.noBody()).
                uri(privateServerUrl).
                build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

    }
}
