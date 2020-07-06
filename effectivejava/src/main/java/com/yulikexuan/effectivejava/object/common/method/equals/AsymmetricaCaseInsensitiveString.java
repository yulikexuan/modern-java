//: com.yulikexuan.effectivejava.object.common.method.equals.AsymmetricaCaseInsensitiveString.java


package com.yulikexuan.effectivejava.object.common.method.equals;


import java.util.Objects;


public class AsymmetricaCaseInsensitiveString {

    private final String s;

    private AsymmetricaCaseInsensitiveString(String s) {
        this.s = s;
    }

    public static AsymmetricaCaseInsensitiveString of(String string) {
        return new AsymmetricaCaseInsensitiveString(Objects.requireNonNull(string));
    }

    // Broken - violates Symmetry!
    @Override
    public boolean equals(Object o) {

        if (o instanceof AsymmetricaCaseInsensitiveString) {
            return s.equalsIgnoreCase(
                    ((AsymmetricaCaseInsensitiveString) o).s);
        }

        /*
         * String class's equals method has no idea about
         * AsymmetricaCaseInsensitiveString class's equals method
         */
        if (o instanceof String)  { // One-way interoperability!
            return s.equalsIgnoreCase((String) o);
        }

        return false;
    }

}///:~