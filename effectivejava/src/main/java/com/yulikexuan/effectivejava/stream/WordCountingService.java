//: com.yulikexuan.effectivejava.stream.WordCountingService.java

package com.yulikexuan.effectivejava.stream;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import lombok.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;


public class WordCountingService {

    Map<String, Long> countWords(@NonNull Path input) throws FileNotFoundException {

        final File file = input.toFile();

        Map<String, Long> freq = Maps.newHashMap();

        try (Stream<String> wordStream = new Scanner(file).tokens()) {
            freq = wordStream.collect(groupingBy(String::toLowerCase, counting()));
        }

        return freq;
    }

    List<String> getTopTokens(@NonNull Map<String, Long> tokens, int size) {
        return tokens.keySet().stream()
                .sorted(Comparator.comparing(tokens::get).reversed())
                .limit(size)
                .collect(ImmutableList.toImmutableList());
    }

}///:~