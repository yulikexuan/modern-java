//: com.yulikexuan.modernjava.generics.GenericWildcards.java


package com.yulikexuan.modernjava.generics;

/******************************************************************************/

interface IA1 {}

class A1 implements IA1 {}

interface IA2 {}

interface IA3 {}

interface IA4 {}

/******************************************************************************/

class B1 implements IA1 {}

interface IB2 extends IA1, IA2 {}
class B2 implements IB2 {}

interface IB3 extends IA3, IA4 {}

class B4 implements IA4 {}

/******************************************************************************/

interface IC1 extends IB2 {}

interface IC2 extends IB2, IB3 {}
class C2 implements IC2 {}

class C3 implements IB3 {}

/******************************************************************************/

class D1 implements IC1, IC2 {}

class D2 implements IC2 {}

/******************************************************************************/

class E1 extends D1 {}

class E2 extends D1 {}

class E3 extends D2 {}

class E4 extends D2 {}

/******************************************************************************/

/*
 * PECS: Producer Extends / Consumer Super
 *
 *   - If needs a List to produce T values: read Ts from the List
 *     List<? extends T>
       CANNOT ADD ANYTHING TO THIS LIST ANYMORE
 *   - If needs a List to consume T values: write Ts into the List
 *     List<? super T>
 *     NO GUARANTEES WHAT TYPE OF OBJECT BEING READ FROM THE LIST
 *
 */
public class GenericWildcards {

}///:~