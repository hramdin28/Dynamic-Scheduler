package com.scheduler.dynamic.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskA implements Job{

  
  private static Logger log = LoggerFactory.getLogger(TaskA.class);
  
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    log.info("Task A is running: "+ context.getJobDetail().getJobDataMap().get("value"));
    
    System.out.println("Task A is running: "+ context.getJobDetail().getJobDataMap().get("value"));
  }

}
