//: com.yulikexuan.modernjava.lambdas.template.Customer.java


package com.yulikexuan.modernjava.lambdas.template;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public class Customer {

    private long id;
    private String name;
    private String accountNumber;
    private int bonus;
    private boolean sendingPaperStatement;

    public int addBonus(int bonus) {
        this.bonus = this.bonus + bonus;
        return this.bonus;
    }

    public boolean setSendingPaperStatement(boolean isSendingPaperStatement) {
        this.sendingPaperStatement = isSendingPaperStatement;
        return this.sendingPaperStatement;
    }

}///:~