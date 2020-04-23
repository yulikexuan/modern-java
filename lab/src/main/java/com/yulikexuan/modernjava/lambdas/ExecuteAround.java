//: com.yulikexuan.modernjava.lambdas.ExecuteAround.java


package com.yulikexuan.modernjava.lambdas;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * An example shows how lambdas, together with behavior parameterization,
 * can be used in practice to make my code more flexible and concise
 *
 * What is Execute-Around Pattern
 *
 *   - A recurrent pattern in resource processing (for example, dealing with files
 *     or databases) is to
 *       - Open a resource
 *       - Do some processing on it
 *       - Close the resource
 *
 *   - The setup and cleanup phases are always similar and surround the
 *     important code doing the processing
 *
 */
class ExecuteAround {

    @FunctionalInterface
    static interface IBufferedReaderProcessor {
        String process(BufferedReader bufferedReader) throws IOException;
    }

    static final String FILE_NAME = "test.json";

    /*
     * How to add new features:
     *   1. Return the first two lines
     *   2. Return those words used most frequently
     *
     * The ideal solution:
     *   1. Reuse the code doing the setup and cleanup
     *   2. Make the processFile method to perform different actions on the file
     *
     * The goal is to parameterize the behaviour of method processFile
     *   - Then, I need a way to pass behaviour to method processFile so it can
     *     execute different behaviours using a BufferedReader
     *       - Passing behavior is exactly what lambdas are for
     *       - Lambdas can be used only in the context of a functional interface
     */
    String processFile() throws IOException {

        try (BufferedReader br = new BufferedReader( new FileReader(FILE_NAME))) {
            return br.readLine();
        }
    }

    /*
     * Step 1 Remember Behavior Parameterization
     * Step 2 Use a functional interface to pass behavior
     *     - java.util.function.Function<T, R> interface is not very suitable
     *       here because it's method apply(T t) throws no checked exception
     *       I have to handle the IOException inside apply method
     * Step 3 Execute a behavor
     *     - Any lambdas of the form BufferedReader -> String can be passed as
     *       arguments, because they match the signature of the process method
     *       defined in the Function interface
     *     - Lambda expressions let me provide the implementation of the
     *       abstract method of a functional interface directly inline,
     *     - Lambda expressions treat the whole expression as an instance of a
     *       functional interface
     */
    String processFileBetter(
            IBufferedReaderProcessor bufferedReaderProcessor)
            throws IOException {

        try (BufferedReader br = new BufferedReader( new FileReader(FILE_NAME))) {
            return bufferedReaderProcessor.process(br);
        }
    }

    /*
     * Step 4 Pass Lambdas
     */
    public static void main(String[] args) throws Exception {

        ExecuteAround executeAround = new ExecuteAround();

        String twoLines = executeAround.processFileBetter(
                br -> br.readLine() + br.readLine());
    }

}///:~