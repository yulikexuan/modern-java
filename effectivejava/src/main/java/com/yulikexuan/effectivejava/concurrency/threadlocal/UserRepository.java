//: com.yulikexuan.effectivejava.concurrency.threadlocal.UserRepository.java

package com.yulikexuan.effectivejava.concurrency.threadlocal;


import com.google.common.collect.Maps;
import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentMap;


public enum UserRepository {

    INSTANCE;

    private final ConcurrentMap<UUID, String> idNameMap;

    private UserRepository() {

        UUID[] userIds = {
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        };

        String[] usernames = {
                RandomStringUtils.randomAlphanumeric(17),
                RandomStringUtils.randomAlphanumeric(17),
                RandomStringUtils.randomAlphanumeric(17),
                RandomStringUtils.randomAlphanumeric(17),
                RandomStringUtils.randomAlphanumeric(17),
                RandomStringUtils.randomAlphanumeric(17),
                RandomStringUtils.randomAlphanumeric(17)
        };

        idNameMap = Maps.newConcurrentMap();

        for (int i = 0; i < userIds.length; i++) {
            idNameMap.put(userIds[i], usernames[i]);
        }
    }

    public Optional<String> getUsernameById(@NonNull UUID userId) {
        return Optional.ofNullable(this.idNameMap.get(userId));
    }

    public Collection<UUID> getAllUserIds() {
        return this.idNameMap.keySet();
    }

    public Collection<String> getAllUsernames() {
        return this.idNameMap.values();
    }

}///:~