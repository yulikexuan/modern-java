//: com.yulikexuan.modernjava.httpclient.IPPrinter.java


package com.yulikexuan.modernjava.httpclient;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class IPPrinter {

    public static void main(String[] args) {

        // Create an HttpClient with default cnfig
        HttpClient httpClient = HttpClient.newHttpClient();

        // Create an HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://httpbin.org/ip"))
                .build();

    }

}///:~