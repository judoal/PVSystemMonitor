/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */
 

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * This Example will demonstrate all of the basics of scheduling capabilities of Quartz using Cron Triggers.
 * 
 * @author Bill Kratzer
 * 
 * CRON:
 * 1 Seconds 2 Minutes 3 Hours 4 Day-of-Month 5 Month 6 Day-of-Week 7 Year (optional field)  
 *   fire at midnight --> 0 0 0 * * ?
 *   fire on 1st day at 00:00    or 0 0 0 1 * ?
 * 
 * 
 */
public class SchedTrigger {

  public void run() throws Exception {
    Logger log = LoggerFactory.getLogger(SchedTrigger.class);

    log.info("<SchedTrigger>------- Initializing -------------------");

    // First we must get a reference to a scheduler
    SchedulerFactory sf = new StdSchedulerFactory();
    Scheduler sched = sf.getScheduler();

    log.info("------- Initialization Complete --------");

    log.info("------- Scheduling Jobs ----------------");

    // jobs can be scheduled before sched.start() has been called

    // job 1 will run at midnight every night - new day
    JobDetail job1 = newJob(NewDay.class).withIdentity("SchedTrigger", "group1").build();
    CronTrigger trigger = newTrigger().withIdentity("SchedTrigger", "group").withSchedule(cronSchedule("0 0 0 * * ?"))
            .build();
    Date ft = sched.scheduleJob(job1, trigger);
    log.info(job1.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
             + trigger.getCronExpression());

    //job2 will run on new month
    
    //job3 will run on new year
    
    
    // Set up the listener
/*    JobListener listener = new NewFilePathStreamListener();
    Matcher<JobKey> matcher = KeyMatcher.keyEquals(job.getKey());
    sched.getListenerManager().addJobListener(listener, matcher);
*/
    // schedule the job to run

    log.info("------- Starting Scheduler ----------------");

    // All of the jobs have been added to the scheduler, but none of the
    // jobs
    // will run until the scheduler has been started
    sched.start();

    log.info("------- Started Scheduler -----------------");
  }
}
