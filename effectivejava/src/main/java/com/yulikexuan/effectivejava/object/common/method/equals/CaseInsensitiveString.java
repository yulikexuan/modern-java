//: com.yulikexuan.effectivejava.object.common.method.equals.CaseInsensitiveString.java


package com.yulikexuan.effectivejava.object.common.method.equals;


import java.util.Objects;


public class CaseInsensitiveString {

    private final String s;

    private CaseInsensitiveString(String s) {
        this.s = s;
    }

    public static CaseInsensitiveString of(String string) {
        return new CaseInsensitiveString(Objects.requireNonNull(string));
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof CaseInsensitiveString) &&
                ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
    }

}///:~