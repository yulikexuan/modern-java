//: com.yulikexuan.effectivejava.serialization.Util.java

package com.yulikexuan.effectivejava.serialization;


import java.io.*;


public class Util {

    public static byte[] serialize(Object o) {
        try (ByteArrayOutputStream ba = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(ba);)  {
            oos.writeObject(o);
            return ba.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Object deserialize(byte[] bytes) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(bytes));) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

}///:~