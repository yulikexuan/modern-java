//: com.yulikexuan.modernjava.nio2.PathMatchers.java


package com.yulikexuan.modernjava.nio2;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;


public class PathMatchers {

    public static boolean matches(Path path, String glob) {
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(glob);
        return pathMatcher.matches(path);
    }

}///:~