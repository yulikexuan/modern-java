//: com.yulikexuan.modernjava.spliterators.WordCounterSpliterator.java


package com.yulikexuan.modernjava.spliterators;


import lombok.Getter;

import java.util.Spliterator;
import java.util.function.Consumer;


@Getter
public class WordCounterSpliterator implements Spliterator<Character> {

    private final String string;
    private final int lowLimit;

    private int currCharIndex = 0;

    private WordCounterSpliterator(String string, int lowLimit) {
        this.string = string;
        this.lowLimit = lowLimit;
    }

    public static WordCounterSpliterator of(String string, int lowLimit) {
        int half = string.length() / 2;
        if (lowLimit > half) {
            lowLimit = half;
        }
        return new WordCounterSpliterator(string, lowLimit);
    }

    @Override
    public boolean tryAdvance(Consumer<? super Character> charConsumer) {
        charConsumer.accept(string.charAt(this.currCharIndex++));
        return currCharIndex < string.length();
    }

    @Override
    public Spliterator<Character> trySplit() {

        int currentSize = string.length() - currCharIndex;

        if (currentSize < 10) {
            return null;
        }

        for (int splitPos = currentSize / 2 + currCharIndex;
             splitPos < string.length();
             splitPos++) {

            if (Character.isWhitespace(string.charAt(splitPos))) {

                Spliterator<Character> spliterator = WordCounterSpliterator.of(
                        string.substring(currCharIndex, splitPos), this.lowLimit);

                currCharIndex = splitPos;

                return spliterator;
            }
        }

        return null;
    }

    @Override
    public long estimateSize() {
        return string.length() - currCharIndex;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }

}///:~