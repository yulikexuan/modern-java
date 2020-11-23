//: com.yulikexuan.effectivejava.stream.AnagramsService.java

package com.yulikexuan.effectivejava.stream;


import com.google.common.collect.ImmutableList;
import lombok.NonNull;

import javax.annotation.Nonnegative;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;


public class AnagramsService {

    List<List<String>> groups(
            @NonNull Path filePath, @Nonnegative int minSize)
            throws IOException {

        List<List<String>> groups = List.of();

        try (Stream<String> words = Files.lines(filePath)) {
            groups = words.collect(groupingBy(word -> alphabetize(word)))
                    .values().stream()
                    .filter(group -> group.size() >= minSize)
                    .collect(ImmutableList.toImmutableList());
        }

        return groups;
    }

    /*
     * Just REFRAIN from using streams to process char type values !!!
     */
    private static String alphabetize(@NonNull String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

}///:~