//: com.yulikexuan.effectivejava.enums.enumset.Text.java

package com.yulikexuan.effectivejava.enums.enumset;


import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Set;


/**
 * EnumSet - a modern replacement for bit fields (Page 170)
 */
@Slf4j
class Text {

    static enum Style {
        BOLD,
        ITALIC,
        UNDERLINE,
        STRIKETHROUGH
    }

    // Any Set could be passed in, but EnumSet is clearly best
    public int applyStyles(Set<Style> styles) {

        log.debug(">>>>>>> [EnumSet] Appling text styles {} to text.",
                Objects.requireNonNull(styles).toString());

        styles.stream().forEach(s ->
                log.debug(">>>>>>> The Ordinal of {} is {}", s, s.ordinal()));

        return styles.size();
    }

}///:~