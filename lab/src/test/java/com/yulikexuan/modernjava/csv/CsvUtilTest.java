//: com.yulikexuan.modernjava.csv.CsvUtilTest.java


package com.yulikexuan.modernjava.csv;


import com.yulikexuan.modernjava.io.FileIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Disabled
class CsvUtilTest {

    private FileIO fileIO;

    @BeforeEach
    void setUp() {
        this.fileIO = FileIO.newInstance();
    }

    @Test
    void test_Read_Csv() throws Exception {

        // Given & When
        List<String> keys = CsvUtil.getAllKeys(CsvUtil.APP_PROPERTIES_FILE_NAME);

        keys.stream().forEach(key -> System.out.println(key));
    }

    @Test
    void getChangedBootstrapPropertyValueTypes() throws Exception {

        // When
        List<String> allChangedTypes = CsvUtil.getChangedPropertyValueTypes(
                CsvUtil.EFFECTIVE_PROPERTIES_FILE_NAME,
                CsvUtil.BOOTSTRAP_PROPERTIES_FILE_NAME,
                CsvUtil.CSV_BOOTSTRAP_HEADERS, 2);

        System.out.printf(">>>>>>> How many types changed? %1$d%n",
                allChangedTypes.size());

        // Then
        allChangedTypes.stream()
                .forEach(changeType -> System.out.println(changeType));

        Path changedValueTypesFilePath = Paths.get(
                "C:\\res\\docs\\Tecsys\\Rollout Properties Guide\\changed_bootstrap_value_types.txt");

        Files.write(changedValueTypesFilePath, allChangedTypes,
                StandardCharsets.UTF_8);
    }

    @Test
    void getChangedSystemPropertyValueTypes() throws Exception {

        // When
        List<String> allChangedTypes = CsvUtil.getChangedPropertyValueTypes(
                CsvUtil.EFFECTIVE_PROPERTIES_FILE_NAME,
                CsvUtil.SYSTEM_PROPERTIES_FILE_NAME,
                CsvUtil.CSV_SYS_HEADERS, 2);

        System.out.printf(">>>>>>> How many types changed? %1$d%n",
                allChangedTypes.size());

        // Then
        allChangedTypes.stream()
                .forEach(changeType -> System.out.println(changeType));

        Path changedValueTypesFilePath = Paths.get(
                "C:\\res\\docs\\Tecsys\\Rollout Properties Guide\\changed_system_value_types.txt");

        Files.write(changedValueTypesFilePath, allChangedTypes,
                StandardCharsets.UTF_8);
    }

}///:~