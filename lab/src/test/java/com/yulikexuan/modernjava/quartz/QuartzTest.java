//: com.yulikexuan.modernjava.quartz.QuartzTest.java


package com.yulikexuan.modernjava.quartz;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


@DisplayName("Test the API of Quartz - ")
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class QuartzTest {

    private static SchedulerFactory schedulerFactory;

    @Mock
    private Runnable helloRunnable;

    private JobDetail jobDetail;

    private JobDataMap jobDataMap;

    private Scheduler scheduler;

    @BeforeAll
    static void beforeAll() {
        schedulerFactory = new StdSchedulerFactory();
    }

    @BeforeEach
    void setUp() throws SchedulerException {
        this.scheduler = schedulerFactory.getScheduler();
        this.scheduler.start();
        this.jobDataMap = new JobDataMap();
        this.jobDataMap.put(HelloJob.HELLO_TASK_KEY, this.helloRunnable);
        this.jobDetail = newJob(HelloJob.class)
                .usingJobData(jobDataMap)
                .withIdentity(HelloJob.IDENTITY, HelloJob.GROUP_ID)
                .build();
    }

    @AfterEach
    void tearDown() throws SchedulerException {
        this.scheduler.shutdown();
    }

    @Nested
    @DisplayName("Test the ‘nature’ of Jobs and the life-cycle of job instances")
    class JobTest {

        @Test
        void test_Given_JobDetail_And_Trigger_Then_Schedule_It() throws Exception {

            // Given
            boolean durable = jobDetail.isDurable();

            // Trigger the job to run now, and then every 40 seconds
            Trigger trigger = newTrigger()
                    .withIdentity(HelloJob.TRIGGER_KEY, HelloJob.TRIGGER_GROUP_ID)
                    .startNow()
                    .withSchedule(simpleSchedule()
                            //.withIntervalInMilliseconds(100).repeatForever())
                    ).build();

            // When
            /*
             * Tell quartz to schedule the job using our trigger
             */
            scheduler.scheduleJob(jobDetail, trigger);

            // Then
            Thread.sleep(10);
            then(helloRunnable).should(times(1)).run();
            assertThat(durable).isFalse();
        }

    }//: End of class JobTest

    @Nested
    @DisplayName("Test Different Triggers - ")
    class TriggerTest {

        @Test
        void test_Given_Job_And_Simple_Trigger_Then_Verify_Common_Attributes_Of_The_Trigger()
                throws Exception {

            // Given
            Date endTime = java.sql.Timestamp.valueOf(
                    LocalDateTime.now()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime()
                            .plus(500L, ChronoUnit.MILLIS));

            Trigger trigger = newTrigger()
                    .withIdentity(HelloJob.TRIGGER_KEY, HelloJob.TRIGGER_GROUP_ID)
                    .startNow()
                    .endAt(endTime)
                    .withSchedule(simpleSchedule())
                    .build();

            // When
            scheduler.scheduleJob(jobDetail, trigger);
            Thread.sleep(10);

            JobKey jobKey = trigger.getJobKey();
            String jobKeyValue = jobKey.getName();
            String jobGroup = jobKey.getGroup();
            LocalDateTime startTime = trigger.getStartTime()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime actualEndTime = trigger.getEndTime()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            // Then
            assertThat(jobKeyValue).isEqualTo(HelloJob.IDENTITY);
        }

    }//: End of TriggerTest



}///:~