//: com.yulikexuan.modernjava.fp.PersistentDataStructures.java


package com.yulikexuan.modernjava.fp;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder @AllArgsConstructor
class TrainJourney {
    private int price;
    private TrainJourney onward;
}

public class PersistentDataStructures {

    static TrainJourney destructiveAppend(TrainJourney tja, TrainJourney tjb) {

        if (tja == null) {
            return tjb;
        }

        TrainJourney tt = tja;

        while (tt.getOnward() != null) {
            tt = tt.getOnward();
        }

        tt.setOnward(tjb);

        return tja;
    }

    static TrainJourney append(TrainJourney tja, TrainJourney tjb) {

        return tja == null ? tjb : TrainJourney.builder()
                .price(tja.getPrice())
                .onward(append(tja.getOnward(), tjb))
                .build();
    }

    static int calculateFinalPrice(TrainJourney trainJourney) {

        if (trainJourney == null) {
            return 0;
        }

        return accumulate(trainJourney.getPrice(), trainJourney.getOnward());
    }

    private static int accumulate(int acc, TrainJourney trainJourney) {
        if (trainJourney == null) {
            return acc;
        }
        return accumulate(acc + trainJourney.getPrice(),
                trainJourney.getOnward());
    }

    static void printJourney(TrainJourney trainJourney) {

        //: TODO

    }

}///:~