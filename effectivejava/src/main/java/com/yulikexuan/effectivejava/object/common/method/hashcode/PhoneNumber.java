//: com.yulikexuan.effectivejava.object.common.method.hashcode.PhoneNumber.java


package com.yulikexuan.effectivejava.object.common.method.hashcode;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


/*
 * The hashCode() Recipe:
 * 1. Declare an int variable named result, and initialize it to the hash code c for
the first significant field in your object, as computed in step 2.a. (Recall from
Item 10 that a significant field is a field that affects equals comparisons.)
2. For every remaining significant field f in your object, do the following:
a. Compute an int hash code c for the field:
i. If the field is of a primitive type, compute Type.hashCode(f), where
Type is the boxed primitive class corresponding to f’s type.
ii. If the field is an object reference and this class’s equals method
compares the field by recursively invoking equals, recursively
invoke hashCode on the field. If a more complex comparison is
required, compute a “canonical representation” for this field and
invoke hashCode on the canonical representation. If the value of the
field is null, use 0 (or some other constant, but 0 is traditional).
iii. If the field is an array, treat it as if each significant element were a
separate field. That is, compute a hash code for each significant
element by applying these rules recursively, and combine the values
per step 2.b. If the array has no significant elements, use a constant,
preferably not 0. If all elements are significant, use Arrays.hashCode.
b. Combine the hash code c computed in step 2.a into result as follows:
result = 31 * result + c;
 */
@Getter
@Builder @AllArgsConstructor
public class PhoneNumber {

    private final short areaCode, prefix, lineNum;

    private static short rangeCheck(int val, int max, String arg) {

        if (val < 0 || val > max) {
            throw new IllegalArgumentException(arg + ": " + val);
        }

        return (short) val;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof PhoneNumber)) {
            return false;
        }

        PhoneNumber pn = (PhoneNumber)o;

        return pn.lineNum == lineNum
                && pn.prefix == prefix
                && pn.areaCode == areaCode;
    }

    // Broken with no hashCode; works with any of the three below

//    // Typical hashCode method (Page 52)
//    @Override public int hashCode() {
//        int result = Short.hashCode(areaCode);
//        result = 31 * result + Short.hashCode(prefix);
//        result = 31 * result + Short.hashCode(lineNum);
//        return result;
//    }

//    // One-line hashCode method - mediocre performance  (page 53)
//    @Override public int hashCode() {
//        return Objects.hash(lineNum, prefix, areaCode);
//    }

    // hashCode method with lazily initialized cached hash code  (page 53)
    private int hashCode; // Automatically initialized to 0

    @Override public int hashCode() {

        int result = hashCode;

        if (result == 0) {
            result = Short.hashCode(areaCode);
            result = 31 * result + Short.hashCode(prefix);
            result = 31 * result + Short.hashCode(lineNum);
            hashCode = result;
        }
        return result;
    }

//    public static void main(String[] args) {
//        Map<PhoneNumber, String> m = new HashMap<>();
//        m.put(new PhoneNumber(707, 867, 5309), "Jenny");
//        System.out.println(m.get(new PhoneNumber(707, 867, 5309)));
//    }

}///:~