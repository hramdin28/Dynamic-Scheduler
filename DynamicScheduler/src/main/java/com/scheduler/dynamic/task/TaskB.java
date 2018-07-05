package com.scheduler.dynamic.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskB implements Job{

  private static Logger log = LoggerFactory.getLogger(TaskB.class);
  
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("Task B is running: "+ context.getJobDetail().getJobDataMap().get("value"));
    
    System.out.println("Task B is running: "+ context.getJobDetail().getJobDataMap().get("value"));
  }

}
