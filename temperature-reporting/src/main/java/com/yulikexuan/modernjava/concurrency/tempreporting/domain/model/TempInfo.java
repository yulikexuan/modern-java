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
        return this.town + " : " + temp;
    }

    public static ITempInfo fetch(final String town) {
        int temp = RANDOM.nextInt(10);
        if ((town == null) || (temp == 0)) {
            throw new RuntimeException("XXX Error! XXX");
        }
        return TempInfo.builder().town(town).temp(temp).build();
    }

}///:~