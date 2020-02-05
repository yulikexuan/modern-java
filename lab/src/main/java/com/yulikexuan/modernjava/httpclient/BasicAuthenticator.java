//: com.yulikexuan.modernjava.httpclient.BasicAuthenticator.java


package com.yulikexuan.modernjava.httpclient;


import lombok.extern.slf4j.Slf4j;

import java.net.Authenticator;
import java.net.PasswordAuthentication;


@Slf4j
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
        log.info("{}>>>>>>> {} is asking for authentication: {}",
                System.lineSeparator(),
                this.getRequestingURL(),
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