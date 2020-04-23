//: com.yulikexuan.modernjava.lambdas.observer.Guardian.java


package com.yulikexuan.modernjava.lambdas.observer;


import org.apache.commons.lang3.StringUtils;

import java.util.Optional;


public class Guardian implements IObserver {

    static final String KEY_WORD = "queen";

    @Override
    public Optional<String> notify(String tweet) {

        if (tweet != null && StringUtils.containsIgnoreCase(tweet, KEY_WORD)) {
            return Optional.of(String.format(
                    ">>>>>>> Yet more news from London... -> %s", tweet));
        }

        return Optional.empty();
    }

}///:~