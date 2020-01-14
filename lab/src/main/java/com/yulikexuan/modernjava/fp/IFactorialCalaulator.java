//: com.yulikexuan.modernjava.fp.IFactorialCalaulator.java


package com.yulikexuan.modernjava.fp;


/*
 * Recursion in computer science is a method where the solution to a problem
 * depends on solutions to smaller instances of the same problem (as opposed to
 * iteration)
 *
 * Recursions are really cool and highly expressive
 *
 * Recursions don’t scale very well for large input sizes
 *
 * And in practical way, we want to use recursions when the problem size is
 * complex and big
 *
 * However, we are not able to use it when we need it the most
 *
 */
public interface IFactorialCalaulator {

    long calculateFactorial(long n);

}///:~