//: com.yulikexuan.modernjava.httpclient.HttpClientsIT.java


package com.yulikexuan.modernjava.httpclient;


import org.junit.jupiter.api.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.net.http.HttpClient.Version.HTTP_2;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


@DisplayName("Java 12 Http Client Test - ")
class HttpClientsIT {

    static final String MY_IP = "207.96.192.66";

    static final int TIMEOUT_SECONDS = 5;

    public static final String HTTPBIN_URI_ADDRESS = "http://httpbin.org";

    public static final URI HTTPBIN_URI = URI.create(HTTPBIN_URI_ADDRESS);
    public static final URI HTTPBIN_IP_URI = URI.create(HTTPBIN_URI_ADDRESS + "/ip");

    static boolean isHttpBinOrgAvailable = false;

    /*
     * HttpClient holds:
     *   - an authenticator
     *   - a connection timeout
     *   - a cookie manager
     *   - an executor
     *   - a redirect policy
     *   - a request priority
     *   - a proxy selector
     *   - an SSL context
     *   - SSL parameters
     *   - an HTTP version
     *
     * HttpClient class stores these request-specific configurations
     * HttpClient instances are immutable
     * The HttpClient instance can be reused for multiple requests
     */
    static HttpClient httpClient;

    @BeforeAll
    static void beforeAll() {

        httpClient = HttpClient.newBuilder()
                .version(HTTP_2) // Just to show off, HTTP/2 is the default
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();

        isHttpBinOrgAvailable = httpClient
                .sendAsync(HttpRequest.newBuilder().uri(HTTPBIN_URI)
                                .method("HEAD",
                                        HttpRequest.BodyPublishers.noBody())
                                .build(),
                        HttpResponse.BodyHandlers.discarding())
                .thenApply(HttpResponse::statusCode)
                .join() == 200;
    }

    @BeforeEach
    void setUp() {
    }

    @Nested
    @DisplayName("Http Request Tests - ")
    class HttpRequestTest {
        @Test
        @DisplayName("Test body string of my ip -")
        void test_Getting_IP() {

            // Given
            assumeTrue(isHttpBinOrgAvailable);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(HTTPBIN_IP_URI)
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .build();

            // When
            String ipAddress = httpClient.sendAsync(request,
                    HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();

            // Then
            assertThat(ipAddress).contains(MY_IP);
        }

        @Test
        @DisplayName("Test post method and body publisher for HttpClient - ")
        void test_Post_Method() {

            // Given
            assumeTrue(isHttpBinOrgAvailable);

            HttpRequest request = HttpRequest.newBuilder(
                    URI.create(HTTPBIN_URI_ADDRESS + "/post"))
                    .header("Content-Type",
                            "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "first_name=Yu&last_name=Li"))
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .build();

            // When
            String verifiedName = httpClient.sendAsync(request,
                    HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();

            // Then
            assertThat(verifiedName).contains("\"first_name\": \"Yu\"")
                    .contains("\"last_name\": \"Li\"");
        }

        @Test
        @DisplayName("Test OPTIONS request - ")
        void test_OPTIONS_Request() {

            // Given
            assumeTrue(isHttpBinOrgAvailable);

            HttpRequest request = HttpRequest.newBuilder(
                    URI.create(HTTPBIN_URI_ADDRESS + "/post"))
                    .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .build();

            // When
            HttpHeaders httpHeaders = httpClient.sendAsync(request,
                    HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::headers)
                    .join();

            // Then
            assertThat(httpHeaders.allValues("Allow").get(0))
                    .contains("OPTIONS", "POST");
        }

        @Test
        @DisplayName("Test sending Expect 100-Continue request - ")
        void test_Header_Expect() {

            // Given
            URI uri = URI.create(HTTPBIN_URI_ADDRESS + "/post");
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .build();

        }

    }//: End of class HttpRequestTest

}///:~