//: com.yulikexuan.modernjava.lambdas.chainofresp.SpellCheckerProcessing.java


package com.yulikexuan.modernjava.lambdas.chainofresp;


import org.apache.commons.lang3.StringUtils;


public class SpellCheckerProcessing extends ProcessingObject<String> {

    @Override
    String handleWork(String input) {
        return StringUtils.replaceIgnoreCase(input,
                "labda", "Lambda");
    }

}///:~