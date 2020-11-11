//: com.yulikexuan.effectivejava.enums.VersatilePayrollDay.java

package com.yulikexuan.effectivejava.enums;


import static com.yulikexuan.effectivejava.enums.VersatilePayrollDay.PayType.*;


/*
 * What really needs is to be forced to choose an overtime pay strategy each
 * time an new enum constant should be added
 *
 * There is a nice way to achieve this
 *
 * The idea is to move the overtime pay computation into a private nested enum,
 * and to pass an instance of this strategy enum to the constructor for the
 * VersatilePayrollDay enum
 *
 * The VersatilePayrollDay enum then delegates the overtime pay calculation to
 * the strategy enum, eliminating the need for a switch statement or
 * constant-specific method implementation in PayrollDay
 *
 * While this pattern is  safer and more flexible :)
 */
enum VersatilePayrollDay implements IPayrollDay {

    MONDAY(WEEKDAY),
    TUESDAY(WEEKDAY),
    WEDNESDAY(WEEKDAY),
    THURSDAY(WEEKDAY),
    FRIDAY(WEEKDAY),
    SATURDAY(WEEKEND),
    SUNDAY(WEEKEND);

    private final IPayrollDay payType;

    private VersatilePayrollDay(IPayrollDay payType) {
        this.payType = payType;
    }

    @Override
    public int pay(int minutesWorked, int payRate) {
        return this.payType.pay(minutesWorked, payRate);
    }

    enum PayType implements IPayrollDay {

        WEEKDAY {
            @Override
            int overtimePay(int minsWorked, int payRate) {
                return 0;
            }
        },
        WEEKEND {
            @Override
            int overtimePay(int minsWorked, int payRate) {
                return 0;
            }
        };

        abstract int overtimePay(int minsWorked, int payRate);

        private static final int MINS_PER_SHIFT = 8 * 60;

        @Override
        public int pay(int minsWorked, int payRate) {
            int basePay = minsWorked * payRate;
            return basePay + overtimePay(minsWorked, payRate);
        }

    }

}///:~