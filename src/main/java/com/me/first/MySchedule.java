package com.me.first;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Properties;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 每秒执行一次Job
 * @author 清明
 *
 */
public class MySchedule {
	
	public static void main(String[] args) {
        try {
            //创建scheduler，使用默认的配置quartz.properties
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            
            //通过编码自定义属性创建scheduler
//            Properties prop = new Properties();
//            StdSchedulerFactory schedulerFactory = new StdSchedulerFactory(prop);
//            Scheduler scheduler = schedulerFactory.getScheduler();
            
            //定义一个Trigger
            Trigger trigger = newTrigger().withIdentity("trigger1", "group1") //定义name/group
                .startNow()//一旦加入scheduler，立即生效
                .withSchedule(simpleSchedule() //使用SimpleTrigger
                	.withIntervalInSeconds(1) //每隔一秒执行一次
                	.repeatForever()) //一直执行，奔腾到老不停歇
                .build();
            
            //定义一个JobDetail
            JobDetail job = newJob(MyJob.class) //定义Job类，这是真正的执行逻辑所在
                .withIdentity("job1", "group1") //定义name/group
                .usingJobData("name", "quartz") //定义属性
                .build();

            //加入这个调度
            scheduler.scheduleJob(job, trigger);

            //启动之
            scheduler.start();

            //运行一段时间后关闭
            Thread.sleep(10000);
            scheduler.shutdown(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
