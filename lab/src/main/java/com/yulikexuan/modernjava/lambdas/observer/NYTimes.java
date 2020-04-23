//: com.yulikexuan.modernjava.lambdas.observer.NYTimes.java


package com.yulikexuan.modernjava.lambdas.observer;


import java.util.Optional;

import org.apache.commons.lang3.StringUtils;


public class NYTimes implements IObserver {

    static final String KEY_WORD = "money";

    @Override
    public Optional<String> notify(final String tweet) {

        if (tweet != null && StringUtils.containsIgnoreCase(tweet, KEY_WORD)) {
            return Optional.of(String.format(
                    ">>>>>>> Breaking news in NY! -> %s", tweet));
        }

        return Optional.empty();
    }

}///:~