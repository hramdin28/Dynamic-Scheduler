package com.scheduler.dynamic;

import org.joda.time.LocalDateTime;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import com.scheduler.dynamic.task.TaskA;
import com.scheduler.dynamic.task.TaskB;
import com.scheduler.dynamic.util.Freq;
import com.scheduler.dynamic.util.MakeCronExpression;
import com.scheduler.dynamic.util.ScheduleJob;

@SpringBootApplication
public class DynamicSchedulerApplication {

  
  private static ApplicationContext ctx;
  static Scheduler scheduler;


  public static void main(String[] args) {
    ctx = SpringApplication.run(DynamicSchedulerApplication.class, args);
    scheduler = ctx.getBean(Scheduler.class);
    
    
    scheduleTaskADaily();
    
    scheduleTaskBMonthly();
  }

  /**
   * Function to schedule task a for a DAILY frequency, to run between 13/04/2018 and 13/06/2018
   */
  private static void scheduleTaskADaily() {

    String taskName = "TaskA";
    
    //Param. For daily schedule, they should be empty
    int months[] = new int[0];
    int doM[] = new int[0];
    int doW[] = new int[0];

    //Time to run task
    int hour = 11;
    int minutes = 7; 

    //Start date of task to run
    LocalDateTime dateTorun =  new LocalDateTime(2018, 4,13, hour, minutes, 0);
    //End date of task to end
    LocalDateTime dateToEnd = new LocalDateTime(2018, 6, 13, hour, minutes, 0);

    //Every number of days to run task.
    //If numDays = 1 then every day task is run
    //IF numDays = 2 then task is run every 2 days
    int numDays = 1;

    //Generate cron expression
    String cronExpression = MakeCronExpression.buildQuartzCronExpressionUsingCronBuilder(Freq.DAILY, numDays,
        hour, minutes, months, doM, doW).asString();

    Job taskA = new TaskA();
    // Create Quartz Job detail
    JobDetail jobDetail = ScheduleJob.getJobDetail(taskA, "TaskA", "TaskA Job");

    // create Quartz trigger
    Trigger trigger = ScheduleJob.getTrigger(taskName, dateTorun.toDate(), dateToEnd.toDate(), cronExpression);

    // get job data map to use as parameters. we can pass any type of values to the task through the JobDataMap
    JobDataMap jdm = jobDetail.getJobDataMap();
    jdm.put("value", "My Value test for TaskA");

    // Schedule task
    try {
      ScheduleJob.scheduleJob(scheduler, jobDetail, trigger);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }

  } 

  /**
   * Function to schedule task a for a MONTHLY frequency, to run between 13/04/2018 and 13/06/2018
   */
  private static void scheduleTaskBMonthly() {

    String taskName = "TaskB";

    int months[] = new int[0];
    
    // Dates in months to run schedule
    int doM[] = new int[]{13,26};
    
    int doW[] = new int[0];

    int hour = 11;
    int minutes = 14; 

    //Start date of task to run
    LocalDateTime dateTorun =  new LocalDateTime(2018, 4,13, hour, minutes, 0);
    //End date of task to end
    LocalDateTime dateToEnd = new LocalDateTime(2018, 6, 13, hour, minutes, 0);

    //Generate cron expression
    String cronExpression = MakeCronExpression.buildQuartzCronExpressionUsingCronBuilder(Freq.MONTHLY, 0,
        hour, minutes, months, doM, doW).asString();

    Job taskB = new TaskB();
    // Create Quartz Job detail
    JobDetail jobDetail = ScheduleJob.getJobDetail(taskB, "TaskB", "TaskB Job");

    // create Quartz trigger
    Trigger trigger = ScheduleJob.getTrigger(taskName, dateTorun.toDate(), dateToEnd.toDate(), cronExpression);

    // get job data map to use as parameters. we can pass any type of values to the task through the JobDataMap
    JobDataMap jdm = jobDetail.getJobDataMap();
    jdm.put("value", "My Value test for TaskB");

    // Schedule task
    try {
      ScheduleJob.scheduleJob(scheduler, jobDetail, trigger);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }
}
