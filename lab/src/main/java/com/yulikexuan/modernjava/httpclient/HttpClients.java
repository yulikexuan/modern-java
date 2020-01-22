//: com.yulikexuan.modernjava.httpclient.HttpClients.java


package com.yulikexuan.modernjava.httpclient;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


/*
 * Steps in an HTTP communication are as follows:
 *   - Create an HttpClient to store HTTP configuration information
 *   - Create an HttpRequest and pupulate it with information to be sent to the
 *     server such as URI, header, HTTP method, request body, and so on
 *   - Send the request to the server using the HttpClient
 *   - Receive a response from the server as an HttpResponse<T>
 *   - Process the HTTP response such as print the status code and body
 *
 */
public class HttpClients {

    public static void main(String[] args) {

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://httpbin.org/ip"))
                .GET() // default setting
                .build();

        HttpClient.newHttpClient().sendAsync(httpRequest,
                        HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();


    }

}///:~