//: com.yulikexuan.effectivejava.optionals.ParentPid.java

package com.yulikexuan.effectivejava.optionals;


import java.util.Objects;
import java.util.Optional;


class ParentPid {

    static Optional<String> getParentProcessId(
            ProcessHandle currentProcessHandle) {

        if (Objects.isNull(currentProcessHandle)) {
            return Optional.empty();
        }

        return currentProcessHandle.parent()
                .map(ProcessHandle::pid)
                .map(pid -> Long.toString(pid));
    }

}///:~