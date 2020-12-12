//: com.yulikexuan.effectivejava.methods.Period.java

package com.yulikexuan.effectivejava.methods;


import java.util.Date;


// Broken "immutable" time period class (Pages 231-3)
public final class Period {

    private final Date start;
    private final Date end;

    /**
     * This constructor make Period be mutable
     *
     * @param  start the beginning of the period
     * @param  end the end of the period; must not precede start
     * @throws IllegalArgumentException if start is after end
     * @throws NullPointerException if start or end is null
     */
//    public Period(Date start, Date end) {
//        if (start.compareTo(end) > 0) {
//            throw new IllegalArgumentException(start + " after " + end);
//        }
//        this.start = start;
//        this.end = end;
//    }

    /*
     * Repaired constructor - makes defensive copies of parameters (Page 232)
     *
     * Note that defensive copies are made before checking the validity of the
     * parameters (Item 49), and the validity check is performed on the copies
     * rather than on the originals. This protects the class against changes to
     * the parameters from another thread during the window of vulnerability
     * between the time the parameters are checked and the time they are copied
     *
     * In the computer security community, this is known as a
     * time-of-check/time-of-use or TOCTOU attack [Viega01].
     */
    public Period(Date start, Date end) {

        this.start = new Date(start.getTime());
        this.end   = new Date(end.getTime());

        if (this.start.compareTo(this.end) > 0)
            throw new IllegalArgumentException(
                    this.start + " after " + this.end);
    }

//    This method make Period be mutable
//    public Date start() {
//        return start;
//    }

    // Repaired accessors - make defensive copies of internal fields (Page 233)
    public Date getStart() {
        return new Date(start.getTime());
    }

//    This method make Period be mutable
//    public Date end() {
//        return end;
//    }

    public Date getEnd() {
        return new Date(end.getTime());
    }

    public String toString() {
        return start + " - " + end;
    }

}///:~