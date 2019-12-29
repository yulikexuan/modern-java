//: com.yulikexuan.modernjava.fp.Currings.java


package com.yulikexuan.modernjava.fp;


import java.util.function.BiFunction;
import java.util.function.DoubleUnaryOperator;

public class Currings {

    static final BiFunction<Double, Double, DoubleUnaryOperator> CURRIED_FUNC =
            (f, b) -> (double x) -> x * f + b;

    static final DoubleUnaryOperator CELSIUS_2_FAHRENHEIT_FUNC =
            CURRIED_FUNC.apply(9/5d, 32d);

    static final DoubleUnaryOperator USD_2_CAD_FUNC =
            CURRIED_FUNC.apply(1.31, 0d);

    static final DoubleUnaryOperator KM_2_MILES_FUNC =
            CURRIED_FUNC.apply(0.621371, 0d);

    public static void main(String[] args) {

        System.out.printf(">>>>>>> 100C is %.2fF%n",
                CELSIUS_2_FAHRENHEIT_FUNC.applyAsDouble(100));
        System.out.printf(">>>>>>> $100USD is $%.2fCAD%n",
                USD_2_CAD_FUNC.applyAsDouble(100));
        System.out.printf(">>>>>>> 1Km is $%.2fM %n",
                KM_2_MILES_FUNC.applyAsDouble(1));

    }


}///:~