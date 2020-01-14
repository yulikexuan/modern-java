//: com.yulikexuan.modernjava.generics.GenericWildcardsTest.java


package com.yulikexuan.modernjava.generics;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class GenericWildcardsTest {

    private List<? extends IC2> listExtendsIC2;
    private List<? super IC2> listSuperIC2;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test_Producer_Extends() {

        List<IC2> list_1 = List.of(new C2());
        listExtendsIC2 = list_1; // List<IC2> ===> List<? extends IC2>
        assertThat(listExtendsIC2.get(0)).isInstanceOf(C2.class);
        // Error: assertThatThrownBy(() -> listC2.add(new C2()));

        List<D1> list_2 = List.of(new D1());
        listExtendsIC2 = list_2; // List<D1> ===> List<? extends IC2>
        assertThat(listExtendsIC2.get(0)).isInstanceOf(D1.class);
        // Error: assertThatThrownBy(() -> listC2.add(new D1()));

        List<D2> list_3 = List.of(new D2());
        listExtendsIC2 = list_3; // List<D2> ===> List<? extends IC2>
        assertThat(listExtendsIC2.get(0)).isInstanceOf(D2.class);
        // Error: assertThatThrownBy(() -> listC2.add(new D2()));

        /*
         * In this case, the inferred type is the upper bound of the type
         * parameter "? extends IC2", which is IC2
         * The compiler will replace the variable declaration with the following:
         * List<IC2> listExtendsIC2 = new ArrayList<>():
         */
        this.listExtendsIC2 = new ArrayList<>();
        // this.listExtendsIC2.add(new IC2() {});
        IC2 ic2 = this.listExtendsIC2.get(0);
    }

    @Test
    void test_Consumer_Super() throws NoSuchFieldException {

        List<IA1> listIA1 = new ArrayList<>();
        this.listSuperIC2 = listIA1;
        listIA1.add(new IC2() {});
        // Error: IC2 ic2 = this.listSuperIC2.get(0);
        assertThat(this.listSuperIC2.get(0)).isInstanceOf(IA1.class);

        List<IA2> listIA2 = List.of(new IA2() {});
        this.listSuperIC2 = listIA2;
        // Error: IC2 ic2 = this.listSuperIC2.get(0);
        assertThat(this.listSuperIC2.get(0)).isInstanceOf(IA2.class);

        List<D1> listD1 = List.of(new D1());
        // this.listSuperIC2 = listD1;

        /*
         * In this case, the inferred type is the lower bound of the type
         * parameter "? super IC2", which is IC2
         * The compiler will replace the variable declaration with the following:
         * List<IC2> listSuperIC2 = new ArrayList<>():
         */
        this.listSuperIC2 = new ArrayList<>();
        listSuperIC2.add(new IC2() {});
        listSuperIC2.add(new C2());
        listSuperIC2.add(new D1());
        // listSuperIC2.add(new B2());
        // listSuperIC2.add(new A1());

        Field listField = GenericWildcardsTest.class.getDeclaredField(
                "listSuperIC2");
        ParameterizedType listType =
                (ParameterizedType) listField.getGenericType();
        Type listClass = listType.getActualTypeArguments()[0];
        System.out.println(listClass);
    }

}///:~