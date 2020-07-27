//: com.yulikexuan.effectivejava.object.common.method.comparable.CaseInsensitiveString.java


package com.yulikexuan.effectivejava.object.common.method.comparable;


import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;


public class CaseInsensitiveString implements Comparable<CaseInsensitiveString> {

    private final String s;

    public CaseInsensitiveString(String s) {
        this.s = Objects.requireNonNull(s);
    }

    // Fixed equals method (Page 40)
    @Override public boolean equals(Object o) {
        return o instanceof CaseInsensitiveString &&
                ((CaseInsensitiveString) o).s.equalsIgnoreCase(s);
    }

    @Override public int hashCode() {
        return 31 * s.hashCode();
    }

    @Override public String toString() {
        return s;
    }

    // Using an existing comparator to make a class comparable
    public int compareTo(CaseInsensitiveString cis) {
        return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s);
    }

    public static void main(String[] args) {
        Set<CaseInsensitiveString> s = new TreeSet<>();
        for (String arg : args)
            s.add(new CaseInsensitiveString(arg));
        System.out.println(s);
    }

}///:~