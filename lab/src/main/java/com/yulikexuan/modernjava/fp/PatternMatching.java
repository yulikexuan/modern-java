//: com.yulikexuan.modernjava.fp.PatternMatching.java


package com.yulikexuan.modernjava.fp;


import lombok.AllArgsConstructor;

import java.util.function.Function;
import java.util.function.Supplier;


interface Expr {
    int getValue();
}

@AllArgsConstructor
class Number implements Expr {
    int val;

    @Override
    public int getValue() {
        return this.val;
    }
}

@AllArgsConstructor
class BinOp implements Expr {

    String opname;
    Expr left;
    Expr right;

    @Override
    public int getValue() {
        switch (opname) {
            case "+": return left.getValue() + right.getValue();
            case "-": return left.getValue() - right.getValue();
            case "*": return left.getValue() * right.getValue();
            case "/": return left.getValue() / right.getValue();
        }
        throw new IllegalArgumentException();
    }
}


@FunctionalInterface
interface TriFunction<S, T, U, R> {
    R apply(S s, T t, U u);
}

public class PatternMatching {


    public static <T> T eif(boolean condition, Supplier<T> trueCase,
                            Supplier<T> falseCase) {

        return condition ? trueCase.get() : falseCase.get();
    }

    public static Expr simplify(Expr e) {

        TriFunction<String, Expr, Expr, Expr> binOpCase =
                (opname, left, right) -> {
                        if ("+".equals(opname)) {
                            if (left instanceof Number &&
                                    ((Number)left).val == 0) {
                                return right;
                            }
                            if (right instanceof Number &&
                                    ((Number)right).val == 0) {
                                return left;
                            }
                        }
                        if ("*".equals(opname)) {
                            if (left instanceof Number &&
                                    ((Number)left).val == 1) {
                                return right;
                            }
                            if (right instanceof Number &&
                                    ((Number)right).val == 1) {
                                return left;
                            }
                        }
                        return new BinOp(opname, left, right);
                };

        Function<Integer, Expr> numCase = val -> new Number(val);
        Supplier<Expr> defaultCase = () -> new Number(0);

        return patternMatchExpr(e, binOpCase, numCase, defaultCase);
    }

    public static <T> T patternMatchExpr(
            Expr e , TriFunction<String, Expr, Expr, T> binOpCase,
            Function<Integer, T> numCase, Supplier<T> defaultCase) {

        return (e instanceof BinOp) ?
                binOpCase.apply(((BinOp)e).opname, ((BinOp)e).left,
                        ((BinOp)e).right) :
                (e instanceof  Number) ? numCase.apply(((Number)e).val) :
                        defaultCase.get();
    }

}///:~