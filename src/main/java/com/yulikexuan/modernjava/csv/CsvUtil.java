//: com.yulikexuan.modernjava.csv.CsvUtil.java


package com.yulikexuan.modernjava.csv;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.yulikexuan.modernjava.io.FileIO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CsvUtil {

    public static final String APP_PROPERTIES_FILE_NAME =
            "app_properties.csv";

    public static final String BOOTSTRAP_PROPERTIES_FILE_NAME =
            "bootstrap_properties.csv";

    public static final String SYSTEM_PROPERTIES_FILE_NAME =
            "system_properties.csv";

    public static final String EFFECTIVE_PROPERTIES_FILE_NAME =
            "effective_properties.csv";

    static final String[] CSV_BOOTSTRAP_HEADERS = {
            "Key",
            "Template Value",
            "Value Type",
            "Dynamic?",
            "Description"};

    static final String[] CSV_SYS_HEADERS = {
            "Key",
            "Default Value",
            "Value Type",
            "Description"};

    static final String[] CSV_EFFECTIVE_HEADERS = {
            "Key",
            "Attribute Value"};

    private static FileIO fileIO = FileIO.newInstance();

    public static List<String> getAllKeys(String fileName) throws Exception {

        Reader in = new FileReader(fileIO.getFile(fileName));

        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(CSV_BOOTSTRAP_HEADERS)
                .parse(in);

        return StreamSupport.stream(records.spliterator(), false)
                .map(r -> r.get(CSV_BOOTSTRAP_HEADERS[0]))
                .filter(key -> key.contains("."))
                .collect(Collectors.toList());
    }

    public static Map<String, String> getAllSysKeyValuePairs(String fileName)
            throws Exception {

        Reader in = new FileReader(fileIO.getFile(fileName));

        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(CSV_SYS_HEADERS)
                .parse(in);

        Map<String, String> props = new HashMap<>();

        StreamSupport.stream(records.spliterator(), false)
                .filter(r -> r.get(CSV_SYS_HEADERS[0]).contains(".") &&
                        !r.get(CSV_SYS_HEADERS[0]).startsWith("#"))
                .forEach(r -> props.put(r.get(CSV_SYS_HEADERS[0]),
                        r.get(CSV_SYS_HEADERS[1])));

        return ImmutableMap.copyOf(props);
    }

    public static Map<String, String> getAllKeyValueTypePairs(
            String fileName, final String[] header, int typeHeaderIndex)
            throws Exception {

        Reader in = new FileReader(fileIO.getFile(fileName));

        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(header)
                .parse(in);

        Map<String, String> props = new HashMap<>();

        StreamSupport.stream(records.spliterator(), false)
                .filter(r -> r.get(header[0]).contains(".") &&
                        !r.get(header[0]).startsWith("#"))
                .forEach(r -> props.put(r.get(header[0]),
                        r.get(header[typeHeaderIndex])));

        return ImmutableMap.copyOf(props);
    }

    public static Map<String, String> getAllEffectiveKeyValuePairs(
            String fileName) throws Exception {

        Reader in = new FileReader(fileIO.getFile(fileName));

        Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withHeader(CSV_EFFECTIVE_HEADERS)
                .parse(in);

        Map<String, String> props = new HashMap<>();

        StreamSupport.stream(records.spliterator(), false)
                .forEach(r -> props.put(r.get(CSV_EFFECTIVE_HEADERS[0]),
                        r.get(CSV_EFFECTIVE_HEADERS[1])));

        return props;
    }

    public static List<String> getChangedPropertyValueTypes(
            String effectivePropertyFile, String bootstrapCsvFileName,
            final String[] header, int typeHeaderIndex) throws Exception {

        Map<String, String> allEffectiveKeyValuePairs =
                getAllEffectiveKeyValuePairs(effectivePropertyFile);

        Map<String, String> allKeyValueTypePairs = getAllKeyValueTypePairs(
                bootstrapCsvFileName, header, typeHeaderIndex);

        List<String> keyValueTypes = new ArrayList<>();

        allEffectiveKeyValuePairs.entrySet().stream()
                .forEach(entry -> {
                    final String key = entry.getKey();
                    if (allKeyValueTypePairs.containsKey(key)) {
                        final String value = entry.getValue();
                        final String vtype = allKeyValueTypePairs.get(key);
                        if (value.equals("0") || value.equals("1")) {
                            if (!vtype.contains("boolean") &&
                                    !vtype.contains("integer") &&
                                    !vtype.contains("number") &&
                                    !vtype.contains("numeric")) {
                                keyValueTypes.add(key + " : " + vtype);
                            }
                        } else if (isNumeric(value)) {
                            if (!vtype.equals("float") &&
                                    !vtype.contains("integer") &&
                                    !vtype.contains("long") &&
                                    !vtype.contains("number") &&
                                    !vtype.contains("numeric")) {
                                keyValueTypes.add(key + " : " + vtype);
                            }
                        } else {
                            if (!vtype.contains("string")) {
                                keyValueTypes.add(key + " : " + vtype);
                            }
                        }
                    }
                });

        Collections.sort(keyValueTypes);

        return ImmutableList.copyOf(keyValueTypes);
    }

    public static boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }

}///:~