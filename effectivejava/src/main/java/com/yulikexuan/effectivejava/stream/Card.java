//: com.yulikexuan.effectivejava.stream.Card.java

package com.yulikexuan.effectivejava.stream;


import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


@Getter
@AllArgsConstructor(staticName = "of")
public class Card {

    private static final List<Card> NEW_DECK = newDeck();

    private final Suit suit;
    private final Rank rank;

    @Override
    public String toString() {
        return rank + " of " + suit + "S";
    }

    public enum Suit {
        SPADE,
        HEART,
        DIAMOND,
        CLUB
    }

    public enum Rank {
        ACE,
        DEUCE,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING
    }

    // Iterative Cartesian product computation
    private static List<Card> newDeck() {

        List<Card> result = Lists.newArrayList();

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                result.add(Card.of(suit, rank));
            }
        }

        return result;
    }

    private static List<Card> newDeckByStream() {

        return Stream.of(Suit.values())
                .flatMap(suit -> Stream.of(Rank.values()).map(
                        rank -> new Card(suit, rank)))
                .collect(toList());
    }

}///:~