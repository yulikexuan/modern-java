//: com.yulikexuan.modernjava.lambdas.observer.FeedTest.java


package com.yulikexuan.modernjava.lambdas.observer;


import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class FeedTest {

    static final String TWEET =
            "The queen said her favourite gift is a Java book other than " +
                    "wine and money!";

    private Feed feed;

    @BeforeEach
    void setUp() {
        this.feed = new Feed();
        this.feed.registerObserver(new NYTimes());
        this.feed.registerObserver(new Guardian());
        this.feed.registerObserver(new LeMonde());
    }

    @Test
    void test_Given_A_Tweet_When_Notify_Then_Get_Retweets_Back() {

        // Given

        // When
        List<String> retweets = this.feed.notifyObservers(TWEET);

        // Then
        assertThat(retweets.size()).isEqualTo(3);
        retweets.stream().forEach(retweet ->
                assertThat(retweet).contains("queen"));
    }

    @Test
    void test_Given_Lambdas_As_Observers_Then_Notify_Them() {

        // Given
        this.feed.clearObservers();

        this.feed.registerObserver(tweet -> {
            if (tweet != null && StringUtils.containsIgnoreCase(tweet,
                    NYTimes.KEY_WORD)) {
                return Optional.of(String.format(
                        ">>>>>>> Breaking news in NY! -> %s", tweet));
            }
            return Optional.empty();
        });

        this.feed.registerObserver(tweet -> {
            if (tweet != null && StringUtils.containsIgnoreCase(tweet,
                    Guardian.KEY_WORD)) {
                return Optional.of(String.format(
                        ">>>>>>> Yet more news from London... -> %s", tweet));
            }
            return Optional.empty();
        });

        this.feed.registerObserver(tweet -> {
            if (tweet != null && StringUtils.containsIgnoreCase(tweet,
                    LeMonde.KEY_WORD)) {
                return Optional.of(String.format(
                        ">>>>>>> Today cheese, wine and news! -> %s", tweet));
            }
            return Optional.empty();
        });

        // When
        List<String> retweets = this.feed.notifyObservers(TWEET);

    // Then
        assertThat(retweets.size()).isEqualTo(3);
        retweets.stream().forEach(retweet ->
                assertThat(retweet).contains("queen"));
    }

}///:~