//: com.yulikexuan.modernjava.concurrency.tempreporting.app.TemperatureReportingApp.java


package com.yulikexuan.modernjava.concurrency.tempreporting.app;


import com.yulikexuan.modernjava.concurrency.tempreporting.domain.model.CelsiusTemperatureReport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan("com.yulikexuan.modernjava.concurrency.tempreporting")
public class TemperatureReportingApp {

    public static void main(String... args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(
                TemperatureReportingApp.class);

        String town = "Montréal";

        System.out.printf("%n>>>>>>> Starts to report temperature for %s%n", town);

        context.getBean(CelsiusTemperatureReport.class)
                .reportTemperatureForTown(town);
    }

}///:~