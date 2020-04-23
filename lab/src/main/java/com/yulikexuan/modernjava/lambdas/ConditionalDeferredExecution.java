//: com.yulikexuan.modernjava.lambdas.ConditionalDeferredExecution.java


package com.yulikexuan.modernjava.lambdas;


import org.apache.commons.lang3.RandomStringUtils;

import java.util.logging.Level;
import java.util.logging.Logger;


public class ConditionalDeferredExecution {

    private final static Logger LOGGER = Logger.getLogger(
            ConditionalDeferredExecution.class.getName());

    /*
     * What's wrong is it?
     *
     *   - The state of the logger (what level it supports) is exposed in the
     *     client code through the method isLoggable
     *
     *   - Why should I have to query the state of the logger object every
     *     time before I log a message? It clutters my code
     */
    void logProblem() {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer(String.format("[Problem] %s", this.generateDiagnostic()));
        }
    }

    /*
     * This approach is better because my code isn’t cluttered with if checks,
     * and the state of the logger is no longer exposed
     *
     * Unfortunately, I still have a issue here,
     * the logging message is always evaluated, even if the logger isn’t
     * enabled for the message level passed as an argument
     */
    void logProblemBetter() {
        LOGGER.log(Level.FINER, String.format("[Problem] %s", this.generateDiagnostic()));
    }

    /*
     * Lambda expressions can help
     * What I need is a way to defer the construction of the message
     *   - So that it can be generated only under a given condition: Level.FINER
     *
     * The Java 8 API designers knew about this problem and introduced an
     * overloaded alternative to log:
     *
     * public void log(Level level, Supplier<String> msgSupplier)
     *
     *
     */
    void logProblemMuchBetter() {
        // LOGGER.log(Level.FINER, String.format("[Problem] %s", this.generateDiagnostic()));
        // Let's debug and see something underneath
        LOGGER.log(Level.FINER, () -> String.format("[Problem] %s", this.generateDiagnostic()));
    }

    private String generateDiagnostic() {
        System.out.println(">>>>>>> Preparing Diagnostic in logProblemBetter() ... ...");
        return RandomStringUtils.randomAlphanumeric(64);
    }

    /*
     * What’s the takeaway from the story?
     *
     * If I see myself:
     *
     *   - Querying the state of an object many times in client
     *     code
     *   - Then, only to call some method on this object with arguments
     *
     * Consider introducing a new method
     *   - Calls that method, passed as a lambda or method reference
     *   - Evaluating the passed lambda expression only after internally
     *     checking the state of the object
     *
     * My code will be
     *   - More readable (less cluttered)
     *   - Better encapsulated, without exposing the state of the object in
     *     client code
     *   - Better performance
     */
    public static void main(String[] args) {

        LOGGER.setLevel(Level.INFO);

        ConditionalDeferredExecution loggerTest = new ConditionalDeferredExecution();

        loggerTest.logProblemMuchBetter();
    }

}///:~