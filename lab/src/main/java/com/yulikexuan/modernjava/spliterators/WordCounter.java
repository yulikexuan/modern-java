//: com.yulikexuan.modernjava.spliterators.WordCounter.java


package com.yulikexuan.modernjava.spliterators;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@ToString
@AllArgsConstructor(staticName = "of")
public class WordCounter {

    private final int counter;
    private final boolean lastSpace;

    public WordCounter accumulate(Character c) {
        if (Character.isWhitespace(c)) {
            return lastSpace ? this : WordCounter.of(counter, true);
        } else {
            return lastSpace ? WordCounter.of(counter + 1, false)
                    : this;
        }
    }
    public WordCounter combine(WordCounter wordCounter) {
        return new WordCounter(counter + wordCounter.counter,
                wordCounter.lastSpace);
    }

}///:~