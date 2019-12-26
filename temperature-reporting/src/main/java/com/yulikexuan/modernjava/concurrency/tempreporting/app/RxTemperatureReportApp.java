//: com.yulikexuan.modernjava.concurrency.tempreporting.app.RxTemperatureReportApp.java


package com.yulikexuan.modernjava.concurrency.tempreporting.app;


import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.rx.ITemperaturePublisher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
@ComponentScan("com.yulikexuan.modernjava.concurrency.tempreporting")
public class RxTemperatureReportApp {

    public static void main(String... towns) {

        if ((towns != null) && (towns.length > 0)) {
            ApplicationContext context = new AnnotationConfigApplicationContext(
                    RxTemperatureReportApp.class);

            System.out.printf("%n>>>>>>> Starts to report Ice Temperature " +
                    "[Celsius] for %s%n", Arrays.asList(towns));

            context.getBean(ITemperaturePublisher.class)
                    .reportTemperatureForTown(towns);
        } else {
            System.out.println("XXX Needs cities! XXX");
        }
    }

}///:~