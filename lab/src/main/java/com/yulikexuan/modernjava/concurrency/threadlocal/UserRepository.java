//: com.yulikexuan.modernjava.concurrency.threadlocal.UserRepository.java


package com.yulikexuan.modernjava.concurrency.threadlocal;


import lombok.NonNull;

import java.util.Map;


final class UserRepository {

    private final Map<Integer, String> repository = Map.of(
            1, "Mike",
            2, "Trump",
            3, "Biden",
            4, "Pence",
            5, "Obama");

    String getUsernameForUserId(@NonNull Integer id) {
        return this.repository.get(id);
    }

}///:~