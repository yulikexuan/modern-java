//: com.yulikexuan.effectivejava.enums.FragilePayrollDay.java

package com.yulikexuan.effectivejava.enums;


enum FragilePayrollDay implements IPayrollDay {

    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY,
    VACATION_DAY;

    private static final int MINS_PER_SHIFT = 8 * 60;

    @Override
    public int pay(int minutesWorked, int payRate) {

        int basePay = minutesWorked * payRate;

        int overtimePay =
                switch (this) {
                    case SATURDAY, SUNDAY -> basePay / 2;
                    default -> minutesWorked <= MINS_PER_SHIFT ?
                            0 : (minutesWorked - MINS_PER_SHIFT) * payRate / 2;
                };

        return basePay + overtimePay;
    }

}///:~
