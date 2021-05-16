//: com.yulikexuan.modernjava.algorithms.InterviewMeeting.java

package com.yulikexuan.modernjava.algorithms;

import org.apache.commons.lang3.StringUtils;


final class InterviewMeeting {

    static final char LEFT = '(';
    static final char RIGHT = ')';

    /**
     *
     * @param input
     * @return
     */
    boolean isBalanced ( String input ) {

        int count = 0;

        if ((StringUtils.isBlank(input)) || (input.startsWith(")"))) {
            return false;
        }

        char[] chars = input.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == LEFT) {
                count++;
            } else {
                count--;
            }
            if ((count == 0) && (chars[i] == RIGHT)) {
                return false;
            }
        }

        return count == 0;
    }

}///:~