//: com.yulikexuan.modernjava.quartz.HelloJob.java


package com.yulikexuan.modernjava.quartz;


import lombok.Setter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


@Setter
public class HelloJob implements Job {

    static final String HELLO_TASK_KEY = "helloRunnable";
    static final String IDENTITY = "HELLO_JOB";
    static final String GROUP_ID = "HELLO_GROUP_1";
    static final String TRIGGER_KEY = "HELLO_JOB_TRIGGER_KEY";
    static final String TRIGGER_GROUP_ID = "TRIGGER_GROUP_ID";

    private Runnable helloRunnable;

    @Override
    public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
        System.out.println(">>>>>> Executing hello task ... ... ");
        System.out.println(">>>>>> Is the job recovering?  " +
                jobExecutionContext.isRecovering());
        this.helloRunnable.run();
    }

}///:~