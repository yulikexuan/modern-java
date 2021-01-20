//: com.yulikexuan.concurrency.thread.confinement.ConnectionDispenser.java

package com.yulikexuan.concurrency.thread.confinement;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * ConnectionDispenser
 * <p/>
 * Using ThreadLocal to ensure thread confinement
 *
 * @author Brian Goetz and Tim Peierls
 */
public class ConnectionDispenser implements Runnable {

    static String DB_URL = "jdbc:mysql://localhost/mydatabase";

    private static ThreadLocal<Connection> connectionHolder =
            new ThreadLocal<Connection>() {
                public Connection initialValue() {
                    try {
                        return DriverManager.getConnection(DB_URL);
                    } catch (SQLException e) {
                        throw new RuntimeException(
                                "Unable to acquire Connection, e");
                    }
                };
            };

    private static Connection getConnection() {
        return connectionHolder.get();
    }

    @Override
    public void run() {
        Connection connection = connectionHolder.get();
    }

}///:~