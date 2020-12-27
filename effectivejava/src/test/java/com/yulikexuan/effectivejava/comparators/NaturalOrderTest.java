//: com.yulikexuan.effectivejava.comparators.NaturalOrderTest.java

package com.yulikexuan.effectivejava.comparators;


import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Test Natural Order from Comparator - ")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class NaturalOrderTest {

    private List<Employee> employees;
    private ThreadLocalRandom random;

    @BeforeEach
    void setUp() {
        random = ThreadLocalRandom.current();
        employees = Lists.newArrayList(
                Employee.of(RandomStringUtils.randomAlphabetic(10),
                    random.nextInt(50000, 1000000)),
                Employee.of(RandomStringUtils.randomAlphabetic(10),
                        random.nextInt(50000, 1000000)),
                Employee.of(RandomStringUtils.randomAlphabetic(10),
                        random.nextInt(50000, 1000000)),
                Employee.of(RandomStringUtils.randomAlphabetic(10),
                        random.nextInt(50000, 1000000)),
                Employee.of(RandomStringUtils.randomAlphabetic(10),
                        random.nextInt(50000, 1000000)));
    }

    @Test
    void given_Natural_Order_From_Comparator_Then_Sorted_By_Salary() {

        // Given
        Comparator<Employee> employeeComparator = Comparator.naturalOrder();

        // When
        employees.sort(employeeComparator);

        // Then
        assertThat(employees).isSorted();
    }

}///:~