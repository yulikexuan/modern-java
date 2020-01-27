//: com.yulikexuan.modernjava.httpclient.HttpClientsIT.java


package com.yulikexuan.modernjava.httpclient;


import org.apache.commons.io.Charsets;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static java.net.http.HttpClient.Version.HTTP_2;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


@DisplayName("Java 12 Http Client Test - ")
class HttpClientsIT {

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
            assertThat(ipAddress).isNotNull();
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
                    .header("Content-Type",
                            "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString("name=yul"))
                    // Must use the expectContinue(true) method to enable
                    // Expect Continue Header
                    .expectContinue(true)
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .build();

            // When
            String verifiedName = httpClient.sendAsync(request,
                            HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();

            // Then
            assertThat(verifiedName).contains("\"name\": \"yul\"");
        }

    }//: End of class HttpRequestTest

    @Nested
    @DisplayName("Response Handling Test - ")
    class ResponseHandlingTest {

        static final String REDIRECT_TO_URI = HTTPBIN_URI_ADDRESS + "/redirect-to";

        @Test
        @DisplayName("Test headers and status code - ")
        void test_Response_Header_And_Status_Code() throws IOException, InterruptedException {

            // Given
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(HTTPBIN_URI)
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<Void> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.discarding());

            // When
            int statusCode = response.statusCode();
            Map<String, List<String>> headers = response.headers().map();
            String contentType = headers.get("content-type").get(0);

            // Then
            assertThat(statusCode).isEqualTo(200);
            assertThat(contentType).isEqualTo("text/html; charset=utf-8");
        }

        @Test
        @DisplayName("Test saving response body to a file - ")
        void test_Saving_Response_Body_To_A_File() throws IOException,
                InterruptedException {

            // Given
            Path httpbinImagePath = Paths.get("src",
                    "main", "resources", "httpbinImage.jpeg");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(HTTPBIN_URI_ADDRESS + "/image/jpeg"))
                    .build();

            HttpResponse<Path> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofFile(httpbinImagePath));

            int statusCode = response.statusCode();
            Path savedFilePath = response.body();

            // When
            boolean imageSaved = Files.exists(savedFilePath);
            String savedFilePathStr = savedFilePath.toAbsolutePath().toString();

            // Then
            assertThat(statusCode).isEqualTo(200);
            assertThat(imageSaved).isTrue();
            // System.out.println(savedFilePathStr);
        }

        @Test
        @DisplayName("Test Downloading File as an Attachment - ")
        void test_Downloading_Body_As_An_Attachment() throws IOException,
                InterruptedException {

            // Given
            String paramName = "Content-Disposition";
            String paramValue = "attachment; filename=test.json";
            String encodedParamValue = URLEncoder.encode(paramValue,
                    StandardCharsets.UTF_8);

            String urlString = String.format("%s%s?%s=%s",
                    HTTPBIN_URI_ADDRESS,
                    "/response-headers",
                    paramName,
                    encodedParamValue);

            URI uri = URI.create(urlString);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .build();

            Path downloadDirPath = Paths.get("src","main",
                    "resources");

            HttpResponse<Path> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofFileDownload(downloadDirPath,
                            StandardOpenOption.CREATE, StandardOpenOption.WRITE));

            // When
            int statusCode = response.statusCode();
            Path filePath = response.body();

            // Then
            assertThat(statusCode).isEqualTo(200);
            assertThat(filePath).exists();
            System.out.println(filePath.toAbsolutePath().normalize());
        }

        @Test
        @DisplayName("Test Default Redirection Policy - ")
        void test_Following_Redirection_With_Default_Redirection_Policy()
                throws IOException, InterruptedException {

            // Given
            HttpClient.Redirect defaultRedirect = httpClient.followRedirects();

            String redirectToUrl = HTTPBIN_URI_ADDRESS + "/ip";

            String requestUrl = String.format(
                    "%s?url=%s&status_code=301",
                    REDIRECT_TO_URI,
                    URLEncoder.encode(redirectToUrl, StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            // When
            String body = response.body();
            int statusCode = response.statusCode();
            String locationHeader = response.headers()
                    .firstValue("Location")
                    .orElse("N/A");

            // Then
            assertThat(defaultRedirect)
                    .as("Redirect To policy should be NEVER")
                    .isEqualTo(HttpClient.Redirect.NEVER);
            assertThat(statusCode)
                    .as("Status code sould be 301")
                    .isEqualTo(301);
            assertThat(body).as("Redirect To Body should be empty")
                    .isEmpty();
            assertThat(locationHeader)
                    .isEqualTo("http://httpbin.org/ip");
        }

        @Test
        @DisplayName("Test Normal Redirection Policy - ")
        void test_Following_Redirection_With_Normal_Redirection_Policy()
                throws IOException, InterruptedException {

            // Given
            HttpClient httpClient = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

            HttpClient.Redirect redirectPolicy = httpClient.followRedirects();

            String redirectToUrl = HTTPBIN_URI_ADDRESS + "/ip";

            String requestUrl = String.format(
                    "%s?url=%s&status_code=301",
                    REDIRECT_TO_URI,
                    URLEncoder.encode(redirectToUrl, StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            // When
            String body = response.body();
            int statusCode = response.statusCode();
            String locationHeader = response.headers()
                    .firstValue("Location")
                    .orElse(null);

            // Then
            assertThat(redirectPolicy)
                    .as("Redirect To policy should be NORMAL")
                    .isEqualTo(HttpClient.Redirect.NORMAL);
            assertThat(statusCode)
                    .as("Status code sould be 200")
                    .isEqualTo(200);
            assertThat(body).as("Redirect To Body should not be empty")
                    .isNotEmpty();
            assertThat(locationHeader)
                    .as("Location header should be null")
                    .isNull();
        }

    }//: End of class ResponseHandlingTest

}///:~