//: com.yulikexuan.modernjava.lambdas.observer.LeMonde.java


package com.yulikexuan.modernjava.lambdas.observer;


import org.apache.commons.lang3.StringUtils;

import java.util.Optional;


public class LeMonde implements IObserver {

    static final String KEY_WORD = "wine";

    @Override
    public Optional<String> notify(String tweet) {

        if (tweet != null && StringUtils.containsIgnoreCase(tweet, KEY_WORD)) {
            return Optional.of(String.format(
                    ">>>>>>> Today cheese, wine and news! -> %s", tweet));
        }

        return Optional.empty();
    }

}///:~