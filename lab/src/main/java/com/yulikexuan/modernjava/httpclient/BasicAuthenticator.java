//: com.yulikexuan.modernjava.httpclient.BasicAuthenticator.java


package com.yulikexuan.modernjava.httpclient;


import java.net.Authenticator;
import java.net.PasswordAuthentication;


public class BasicAuthenticator extends Authenticator {

    private final String username;
    private final char[] password;

    public static BasicAuthenticator of(String username, String password) {
        return new BasicAuthenticator(username, password.toCharArray());
    }

    private BasicAuthenticator(String username, char[] password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        System.out.println(this.getRequestingURL() +
                " is asking for authentication: " +
                this.getRequestingPrompt());
        return new PasswordAuthentication(this.username, this.password);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return String.copyValueOf(this.password);
    }

}///:~