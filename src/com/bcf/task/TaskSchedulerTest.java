package com.bcf.task;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.bcf.properties.PropertySingleton;

public class TaskSchedulerTest {
	
	public static void init() throws Exception {
		String time = PropertySingleton.getInstance().getProperty("timerSchedule");
		// Detalhes da tarefa
        JobDetail job = JobBuilder.newJob(WriteOracleTest.class).withIdentity(
                "Writter", "group1").build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(
                "trigger Writter", "group1").withSchedule(
                CronScheduleBuilder.cronSchedule(time)).build();
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
	}

}
