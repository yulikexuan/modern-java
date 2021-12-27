//: com.yulikexuan.jdk.ocp2.nio.SerializationInheritanceRules.java

package com.yulikexuan.jdk.ocp2.nio;


import lombok.extern.slf4j.Slf4j;

import java.io.*;


@Slf4j
class SerializationInheritanceRules {

    static class Player {
        Player() {
            log.info(">>>>>>> P ");
        }
    }

    static class CardPlayer extends Player implements Serializable {
        CardPlayer() {
            log.info(">>>>>>> C ");
        }
    }

    public static void main(String[] args) {

        CardPlayer c1 = new CardPlayer();
        try {
            FileOutputStream fos = new FileOutputStream("play.txt");
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(c1);

            os.close();

            FileInputStream fis = new FileInputStream("play.txt");
            ObjectInputStream is = new ObjectInputStream(fis);
            CardPlayer c2 = (CardPlayer) is.readObject();

            is.close();

        } catch (Exception x ) {
            log.info(">>>>>>> {} ", x.getMessage());
        }
    }

}///:~