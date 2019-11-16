//: com.yulikexuan.modernjava.props.PropertiesUtilTest.java


package com.yulikexuan.modernjava.props;


import com.yulikexuan.modernjava.csv.CsvUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class PropertiesUtilTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Get_All_Keys() throws Exception {

        // Given
        List<String> keys = PropertiesUtil.getAllKeys(
                PropertiesUtil.BOOTSTRAP_PROPERTIES_FILE_NAME);

        // When
        int size = keys.size();
        System.out.printf(">>>>>>> How many bootstrap keys? %1$d%n", keys.size());

        // Then
        keys.stream().forEach(k -> System.out.println(k));
    }

    @Test
    void test_Get_New_Bootstrap_PropertyKeys() throws Exception {

        // When
        Collection<String> newKeys = PropertiesUtil.getNewPropertyKeys(
                PropertiesUtil.BOOTSTRAP_PROPERTIES_FILE_NAME,
                CsvUtil.BOOTSTRAP_PROPERTIES_FILE_NAME);
        int count = newKeys.size();

        // Then
        System.out.printf(">>>>>>> How many new keys? %1$d%n%n", count);
        newKeys.stream().forEach(k -> System.out.println(k));
    }

    @Test
    void test_Get_Dispeared_Bootstrap_PropertyKeys() throws Exception {

        // When
        Collection<String> dispearedKeys =
                PropertiesUtil.getDispearedPropertyKeys(
                    PropertiesUtil.BOOTSTRAP_PROPERTIES_FILE_NAME,
                    CsvUtil.BOOTSTRAP_PROPERTIES_FILE_NAME);
        int count = dispearedKeys.size();

        // Then
        System.out.printf(">>>>>>> How many dispared bootstrap keys? %1$d%n%n",
                count);
        dispearedKeys.stream().forEach(k -> System.out.println(k));
    }

    @Test
    void test_Get_New_System_PropertyKeys() throws Exception {

        // When
        Collection<String> newKeys = PropertiesUtil.getNewPropertyKeys(
                PropertiesUtil.SYSTEM_PROPERTIES_FILE_NAME,
                CsvUtil.SYSTEM_PROPERTIES_FILE_NAME);
        int count = newKeys.size();

        // Then
        System.out.printf(">>>>>>> How many new keys? %1$d%n%n", count);
        newKeys.stream().forEach(k -> System.out.println(k));
    }

    @Test
    void test_Get_Dispeared_System_PropertyKeys() throws Exception {

        // When
        Collection<String> dispearedKeys =
                PropertiesUtil.getDispearedPropertyKeys(
                        PropertiesUtil.SYSTEM_PROPERTIES_FILE_NAME,
                        CsvUtil.SYSTEM_PROPERTIES_FILE_NAME);
        int count = dispearedKeys.size();

        // Then
        System.out.printf(">>>>>>> How keys were dispeared? %1$d%n%n", count);
        dispearedKeys.stream().forEach(k -> System.out.println(k));
    }

    @Test
    void getChangedSysPropertyValues() throws Exception {

        // When
        List<String> newValues = PropertiesUtil.getChangedSysPropValues(
                PropertiesUtil.SYSTEM_PROPERTIES_FILE_NAME,
                CsvUtil.SYSTEM_PROPERTIES_FILE_NAME);

        System.out.printf(">>>>>>> How many new default values? %1$d%n",
                newValues.size());

        // Then
        newValues.forEach(kv -> System.out.println(kv));
    }

}///:~