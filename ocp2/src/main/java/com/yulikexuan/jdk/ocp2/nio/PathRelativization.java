//: com.yulikexuan.jdk.ocp2.nio.PathRelativization.java

package com.yulikexuan.jdk.ocp2.nio;


import java.nio.file.Path;


class PathRelativization {

    String relativize(Path path1, Path path2) {
        return path1.relativize(path2).toString();
    }

}///:~