//: com.yulikexuan.modernjava.io.ApacheCommonsIOTest.java


package com.yulikexuan.modernjava.io;


import com.google.common.collect.ImmutableList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class ApacheCommonsIOTest {

    static final String TEST_FILE_NAME = "fileTest.txt";

    static final List<String> FILE_CONTENT = ImmutableList.of(
            RandomStringUtils.randomAlphabetic(10),
            RandomStringUtils.randomAlphabetic(10),
            RandomStringUtils.randomAlphabetic(10));

    private FileCopy fileCopy;
    private File targetFile;

    @BeforeEach
    void setUp() {
        this.fileCopy = new FileCopy();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (this.targetFile != null) {
            this.targetFile.delete();
        }
    }

    @DisplayName("FileUtils::copyFileToDirectory Test - ")
    @Test
    void able_To_Copy_Files_With_FileUtils() throws Exception {

        // Given
        File srcFile = this.fileCopy.getFile(TEST_FILE_NAME);
        FileUtils.writeLines(srcFile, FILE_CONTENT);
        File targetDir = FileUtils.getTempDirectory();

        // When
        FileUtils.copyFileToDirectory(srcFile, targetDir);
        this.targetFile = FileUtils.getFile(targetDir, TEST_FILE_NAME);
        String targetContent = this.fileCopy.getFileContent(targetFile).get();

        // Then
        assertThat(targetContent).containsSubsequence(
                FILE_CONTENT.get(0),
                FILE_CONTENT.get(1),
                FILE_CONTENT.get(2));
    }



}///:~