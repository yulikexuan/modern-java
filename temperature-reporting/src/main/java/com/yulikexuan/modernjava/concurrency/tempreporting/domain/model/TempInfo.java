//: com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.TempInfo.java


package com.yulikexuan.modernjava.concurrency.tempreporting.domain.model;


import lombok.*;


@Getter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class TempInfo implements ITempInfo {

    private final int temp;
    private final String town;

    @Override
    public String toString() {
        return String.format(REPORT_TEMPLATE, this.town, temp);
    }

}///:~