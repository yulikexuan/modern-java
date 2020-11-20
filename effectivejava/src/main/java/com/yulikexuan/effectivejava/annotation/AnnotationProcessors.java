//: com.yulikexuan.effectivejava.annotation.AnnotationProcessors.java

package com.yulikexuan.effectivejava.annotation;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;


@Slf4j
@Getter
@ToString
@AllArgsConstructor(staticName = "of")
public class AnnotationProcessors {

    static ImmutableTriple<String, Boolean, Boolean> processEffctiveTest(
            @NonNull Method method) {

        return EFFEC_TEST_ANNOTATION_PROCESSOR.apply(method);
    }

    static ImmutableTriple<String, Boolean, Boolean> processExceptionTest(
            @NonNull Method method) {

        return EXCEPTION_TEST_ANNOTATION_PROCESSOR.apply(method);
    }

    static ImmutableTriple<String, Boolean, Boolean> processRepeatableExceptionTest(
            @NonNull Method method) {

        if (AN_EXCEPTION_TEST_CONTAINER_PREDICATE.test(method)) {
            return REPEATABLE_EXCEPTION_TEST_ANNOTATION_PROCESSOR.apply(method);
        }

        return ImmutableTriple.of(method.getName(), false, true);
    }

    static final Predicate<Method> AN_EXCEPTION_TEST_CONTAINER_PREDICATE =
            m -> m.isAnnotationPresent(AnExceptionTest.class) ||
                    m.isAnnotationPresent(AnExceptionTestContainer.class);

    static final Function<Method, ImmutableTriple<String, Boolean, Boolean>>
            EFFEC_TEST_ANNOTATION_PROCESSOR = method -> {

        boolean passed = false;
        boolean invalid= false;
        try {
            method.invoke(null);
            passed = true;
        } catch (InvocationTargetException wrappedExc) {
        } catch (Exception exc) {
            invalid = true;
        }

        return ImmutableTriple.of(method.getName(), passed, invalid);
    };

    static final Function<Method, ImmutableTriple<String, Boolean, Boolean>>
            EXCEPTION_TEST_ANNOTATION_PROCESSOR = method -> {

        boolean passed = false;
        boolean invalid= false;
        String methodName = method.getName();

        try {
            method.invoke(null);
            log.debug(">>>>>>> [Processor] - Test {} failed: no exception",
                    methodName);
        } catch (InvocationTargetException wrappedExc) {
            Throwable exc = wrappedExc.getCause();
            Class<? extends Exception>[] excType = method.getAnnotation(
                    AnyExceptionTest.class).value();
            if (Arrays.stream(excType).anyMatch(et -> et.isInstance(exc))) {
                passed = true;
            } else {
                log.debug(">>>>>>> [Processor] - Test {} failed: " +
                        "got unexpected exception {}", methodName, exc.toString());
            }
        } catch (Exception exc) {
            invalid = true;
            log.debug(">>>>>>> [Processor] - Test {} failed: " +
                    "invalid annotation used", methodName);
        }

        return ImmutableTriple.of(methodName, passed, invalid);
    };

    static final Function<Method, ImmutableTriple<String, Boolean, Boolean>>
            REPEATABLE_EXCEPTION_TEST_ANNOTATION_PROCESSOR = method -> {

        boolean passed = false;
        boolean invalid= false;

        String methodName = method.getName();

        try {
            method.invoke(null);
            log.debug(">>>>>>> [Processor] - Test {} failed: no exception",
                    methodName);
        } catch (InvocationTargetException wrappedExc) {
            Throwable exc = wrappedExc.getCause();

            passed = Arrays.stream(method.getAnnotationsByType(
                    AnExceptionTest.class)).anyMatch(
                            a -> a.value().isInstance(exc));

            if (!passed) {
                log.debug(">>>>>>> [Processor] - Test {} failed: " +
                        "got unexpected exception {}", methodName, exc.toString());
            }

        } catch (Exception exc) {
            invalid = true;
            log.debug(">>>>>>> [Processor] - Test {} failed: " +
                    "invalid annotation used", methodName);
        }

        return ImmutableTriple.of(methodName, passed, invalid);
    };

}///:~