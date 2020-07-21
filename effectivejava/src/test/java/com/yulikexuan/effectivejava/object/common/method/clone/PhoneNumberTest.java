//: com.yulikexuan.effectivejava.object.common.method.clone.PhoneNumberTest.java


package com.yulikexuan.effectivejava.object.common.method.clone;


import com.google.common.collect.Maps;
import org.junit.jupiter.api.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Equals Methods of SubClasses - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PhoneNumberTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Cloning_A_PhoneNumber() {

        // Given
        PhoneNumber phoneNumber = PhoneNumber.at(
                707, 867, 5309);

        // When
        PhoneNumber clonedPhoneNumber = phoneNumber.clone();

        Map<PhoneNumber, String> pnm = Maps.newHashMap();
        pnm.put(phoneNumber, "Yul");
        pnm.put(clonedPhoneNumber, "Weil");

        System.out.println(phoneNumber.getClass());
        System.out.println(clonedPhoneNumber.getClass());

        System.out.println(phoneNumber.hashCode());
        System.out.println(clonedPhoneNumber.hashCode());

        // Then
        assertThat(phoneNumber).isEqualTo(clonedPhoneNumber);
        assertThat(phoneNumber).isNotSameAs(clonedPhoneNumber);
        assertThat(pnm).hasSize(1);
        assertThat(pnm.get(clonedPhoneNumber)).isEqualTo("Weil");
    }

}///:~