//: com.yulikexuan.modernjava.props.PropertiesUtil.java


package com.yulikexuan.modernjava.props;


import com.google.common.collect.ImmutableList;
import com.yulikexuan.modernjava.csv.CsvUtil;
import com.yulikexuan.modernjava.io.FileIO;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;


public class PropertiesUtil {

    public static final String BOOTSTRAP_PROPERTIES_FILE_NAME =
            "bootstrap.properties";

    public static final String SYSTEM_PROPERTIES_FILE_NAME =
            "md_environment.properties";

    private static FileIO fileIO = FileIO.newInstance();

    public static List<String> getAllKeys(String fileName) throws Exception {

        Properties props = new Properties();
        props.load(new FileReader(fileIO.getFile(fileName)));

        return props.keySet().stream()
                .map(o -> o.toString())
                .collect(Collectors.toList());
    }

    public static Map<String, String> getAllKeyValuePairs(String fileName)
            throws Exception {

        Properties props = new Properties();
        props.load(new FileReader(fileIO.getFile(fileName)));

        Map<String, String> pairs = new HashMap<>();

        props.forEach((o1, o2) -> pairs.put(o1.toString(), o2.toString()));

        return pairs;
    }

    public static Collection<String> getNewPropertyKeys(
            String propsFileName, String csvFileName) throws Exception {

        List<String> keysFromPropertiesFile = getAllKeys(propsFileName);
        List<String> keysFromCsv = CsvUtil.getAllKeys(csvFileName);

        keysFromPropertiesFile.removeAll(keysFromCsv);

        Collections.sort(keysFromPropertiesFile);

        return ImmutableList.copyOf(keysFromPropertiesFile);
    }

    public static Collection<String> getDispearedPropertyKeys(
            String propsFileName, String csvFileName) throws Exception {

        List<String> keysFromPropertiesFile = getAllKeys(propsFileName);
        List<String> keysFromCsv = CsvUtil.getAllKeys(csvFileName);

        keysFromCsv.removeAll(keysFromPropertiesFile);

        Collections.sort(keysFromCsv);

        return ImmutableList.copyOf(keysFromCsv);
    }

    public static List<String> getChangedSysPropValues(
            String propsFileName, String csvFileName) throws Exception {

        Map<String, String> allKeyValuePairs = getAllKeyValuePairs(
                propsFileName);

        Map<String, String> allSysKeyValuePairs = CsvUtil.getAllSysKeyValuePairs(
                csvFileName);

        List<String> newKeyValues = new ArrayList<>();

        allKeyValuePairs.forEach((k, v) -> {
            if (allSysKeyValuePairs.containsKey(k) &&
                    !allSysKeyValuePairs.get(k).equals(v)) {

                newKeyValues.add(k + "=" + v);
            }
        });

        Collections.sort(newKeyValues);

        return ImmutableList.copyOf(newKeyValues);
    }

}///:~