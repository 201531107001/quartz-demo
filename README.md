文章链接:https://www.cnblogs.com/drift-ice/p/3817269.html
Quartz可以用来做什么？
Quartz是一个任务调度框架。比如你遇到这样的问题

想每月25号，信用卡自动还款
想每年4月1日自己给当年暗恋女神发一封匿名贺卡
想每隔1小时，备份一下自己的爱情动作片 学习笔记到云盘
这些问题总结起来就是：在某一个有规律的时间点干某件事。并且时间的触发的条件可以非常复杂（比如每月最后一个工作日的17:50），复杂到需要一个专门的框架来干这个事。 Quartz就是来干这样的事，你给它一个触发条件的定义，它负责到了时间点，触发相应的Job起来干活。

Quartz最重要的3个基本要素：
1.Scheduler：调度器。所有的调度都是由它控制。
2.Trigger： 定义触发的条件。例子中，它的类型是SimpleTrigger，每隔1秒中执行一次（什么是SimpleTrigger下面会有详述）。
3.JobDetail & Job： JobDetail 定义的是任务数据，而真正的执行逻辑是在Job中，例子中是HelloQuartz。 为什么设计成JobDetail + Job，不直接使用Job？这是因为任务是有可能并发执行，如果Scheduler直接使用Job，就会存在对同一个Job实例并发访问的问题。而JobDetail & Job 方式，sheduler每次执行，都会根据JobDetail创建一个新的Job实例，这样就可以规避并发访问的问题。

//job相关的builder
import static org.quartz.JobBuilder.*;

//trigger相关的builder
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.DailyTimeIntervalScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;

//日期相关的builder
import static org.quartz.DateBuilder.*;

关于name和group
JobDetail和Trigger都有name和group。
name是它们在这个sheduler里面的唯一标识。如果我们要更新一个JobDetail定义，只需要设置一个name相同的JobDetail实例即可。
group是一个组织单元，sheduler会提供一些对整组操作的API，比如 scheduler.resumeJobs()。

Trigger
在开始详解每一种Trigger之前，需要先了解一下Trigger的一些共性。
StartTime & EndTime
startTime和endTime指定的Trigger会被触发的时间区间。在这个区间之外，Trigger是不会被触发的。
** 所有Trigger都会包含这两个属性 **

优先级（Priority）
当scheduler比较繁忙的时候，可能在同一个时刻，有多个Trigger被触发了，但资源不足（比如线程池不足）。那么这个时候比剪刀石头布更好的方式，就是设置优先级。优先级高的先执行。
需要注意的是，优先级只有在同一时刻执行的Trigger之间才会起作用，如果一个Trigger是9:00，另一个Trigger是9:30。那么无论后一个优先级多高，前一个都是先执行。优先级的值默认是5，当为负数时使用默认值。最大值似乎没有指定，但建议遵循Java的标准，使用1-10，不然鬼才知道看到【优先级为10】是时，上头还有没有更大的值。

Misfire(错失触发）策略
类似的Scheduler资源不足的时候，或者机器崩溃重启等，有可能某一些Trigger在应该触发的时间点没有被触发，也就是Miss Fire了。这个时候Trigger需要一个策略来处理这种情况。每种Trigger可选的策略各不相同。

Calendar
这里的Calendar不是jdk的java.util.Calendar，不是为了计算日期的。它的作用是在于补充Trigger的时间。可以排除或加入某一些特定的时间点
Quartz体贴地为我们提供以下几种Calendar，注意，所有的Calendar既可以是排除，也可以是包含，取决于：
HolidayCalendar。指定特定的日期，比如20140613。精度到天。
DailyCalendar。指定每天的时间段（rangeStartingTime, rangeEndingTime)，格式是HH:MM[:SS[:mmm]]。也就是最大精度可以到毫秒。
WeeklyCalendar。指定每星期的星期几，可选值比如为java.util.Calendar.SUNDAY。精度是天。
MonthlyCalendar。指定每月的几号。可选值为1-31。精度是天
AnnualCalendar。 指定每年的哪一天。使用方式如上例。精度是天。
CronCalendar。指定Cron表达式。精度取决于Cron表达式，也就是最大精度可以到秒。

Trigger实现类
Quartz有以下几种Trigger实现:
1.SimpleTrigger
指定从某一个时间开始，以一定的时间间隔（单位是毫秒）执行的任务。
它适合的任务类似于：9:00 开始，每隔1小时，执行一次。
它的属性有：
repeatInterval 重复间隔
repeatCount 重复次数。实际执行次数是 repeatCount+1。因为在startTime的时候一定会执行一次。** 下面有关repeatCount 属性的都是同理。

2.CalendarIntervalTrigger
类似于SimpleTrigger，指定从某一个时间开始，以一定的时间间隔执行的任务。 但是不同的是SimpleTrigger指定的时间间隔为毫秒，没办法指定每隔一个月执行一次（每月的时间间隔不是固定值），而CalendarIntervalTrigger支持的间隔单位有秒，分钟，小时，天，月，年，星期。
相较于SimpleTrigger有两个优势：1、更方便，比如每隔1小时执行，你不用自己去计算1小时等于多少毫秒。 2、支持不是固定长度的间隔，比如间隔为月和年。但劣势是精度只能到秒。
它适合的任务类似于：9:00 开始执行，并且以后每周 9:00 执行一次
它的属性有:
interval 执行间隔
intervalUnit 执行间隔的单位（秒，分钟，小时，天，月，年，星期）
calendarIntervalSchedule()
    .withIntervalInDays(1) //每天执行一次
    .build();

calendarIntervalSchedule()
    .withIntervalInWeeks(1) //每周执行一次
    .build();

3.DailyTimeIntervalTrigger
指定每天的某个时间段内，以一定的时间间隔执行任务。并且它可以支持指定星期。
它适合的任务类似于：
指定每天9:00 至 18:00 ，每隔70秒执行一次，并且只要周一至周五执行。
它的属性有:
startTimeOfDay 每天开始时间
endTimeOfDay 每天结束时间
daysOfWeek 需要执行的星期
interval 执行间隔
intervalUnit 执行间隔的单位（秒，分钟，小时，天，月，年，星期）
repeatCount 重复次数
dailyTimeIntervalSchedule()
    .startingDailyAt(TimeOfDay.hourAndMinuteOfDay(9, 0)) //第天9：00开始
    .endingDailyAt(TimeOfDay.hourAndMinuteOfDay(16, 0)) //16：00 结束 
    .onDaysOfTheWeek(MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY) //周一至周五执行
    .withIntervalInHours(1) //每间隔1小时执行一次
    .withRepeatCount(100) //最多重复100次（实际执行100+1次）
    .build();

dailyTimeIntervalSchedule()
    .startingDailyAt(TimeOfDay.hourAndMinuteOfDay(9, 0)) //第天9：00开始
    .endingDailyAfterCount(10) //每天执行10次，这个方法实际上根据 startTimeOfDay+interval*count 算出 endTimeOfDay
    .onDaysOfTheWeek(MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY) //周一至周五执行
    .withIntervalInHours(1) //每间隔1小时执行一次
    .build();

4.CronTrigger
适合于更复杂的任务，它支持类型于Linux Cron的语法（并且更强大）。基本上它覆盖了以上三个Trigger的绝大部分能力（但不是全部）—— 当然，也更难理解。
它适合的任务类似于：每天0:00,9:00,18:00各执行一次。
它的属性只有:
Cron表达式。但这个表示式本身就够复杂了。下面会有说明。


Quartz调度一次任务，会干如下的事：
JobClass jobClass=JobDetail.getJobClass()
Job jobInstance=jobClass.newInstance()。所以Job实现类，必须有一个public的无参构建方法。
jobInstance.execute(JobExecutionContext context)。JobExecutionContext是Job运行的上下文，可以获得Trigger、Scheduler、JobDetail的信息。
也就是说，每次调度都会创建一个新的Job实例，这样的好处是有些任务并发执行的时候，不存在对临界资源的访问问题——当然，如果需要共享JobDataMap的时候，还是存在临界资源的并发访问的问题。

JobDataMap
Job都次都是newInstance的实例，那我怎么传值给它？ 比如我现在有两个发送邮件的任务，一个是发给"liLei",一个发给"hanmeimei",不能说我要写两个Job实现类LiLeiSendEmailJob和HanMeiMeiSendEmailJob。实现的办法是通过JobDataMap。
每一个JobDetail都会有一个JobDataMap。JobDataMap本质就是一个Map的扩展类，只是提供了一些更便捷的方法，比如getString()之类的。
newJob().usingJobData("age", 18) //加入属性到ageJobDataMap or 
job.getJobDataMap().put("name", "quertz"); //加入属性name到JobDataMap
然后在Job中可以获取这个JobDataMap的值，方式同样有二：

public class HelloQuartz implements Job {
    private String name;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail detail = context.getJobDetail();
        JobDataMap map = detail.getJobDataMap(); //方法一：获得JobDataMap
        System.out.println("say hello to " + name + "[" + map.getInt("age") + "]" + " at "
                           + new Date());
    }

    //方法二：属性的setter方法，会将JobDataMap的属性自动注入
    public void setName(String name) { 
        this.name = name;
    }
}
对于同一个JobDetail实例，执行的多个Job实例，是共享同样的JobDataMap，也就是说，如果你在任务里修改了里面的值，会对其他Job实例（并发的或者后续的）造成影响。
除了JobDetail，Trigger同样有一个JobDataMap，共享范围是所有使用这个Trigger的Job实例。

Job并发
Job是有可能并发执行的，比如一个任务要执行10秒中，而调度算法是每秒中触发1次，那么就有可能多个任务被并发执行。
有时候我们并不想任务并发执行，比如这个任务要去”获得数据库中所有未发送邮件的名单“，如果是并发执行，就需要一个数据库锁去避免一个数据被多次处理。这个时候一个@DisallowConcurrentExecution解决这个问题。
就是这样
public class DoNothingJob implements Job {
    @DisallowConcurrentExecution
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("do nothing");
    }
}
注意，@DisallowConcurrentExecution是对JobDetail实例生效，也就是如果你定义两个JobDetail，引用同一个Job类，是可以并发执行的。
Job的声明和并发
关于Job的声明和并发需要说明一下，以下一对注解使用在你的Job类中，可以影响Quartz的行为：
@DisallowConcurrentExecution : 可以添加到你的任务类中，它会告诉Quartz不要执行多个任务实例。
注意措辞，在上面的”SalesReportJob”类添加该注解，将会只有一个”SalesReportForJoe”实例在给定的时间执行，但是”SalesReportForMike”是可以执行的。这个约束是基于JobDetail的，而不是基于任务类的。
@PersistJobDataAfterExecution : 告诉Quartz在任务执行成功完毕之后（没有抛出异常），修改JobDetail的JobDataMap备份，以供下一个任务使用。
如果你使用了@PersistJobDataAfterExecution 注解的话，强烈建议同时使用@DisallowConcurrentExecution注解，以避免当两个同样的job并发执行的时候产生的存储数据迷惑。

Scheduler
Scheduler就是Quartz的大脑，所有任务都是由它来设施。
Job和 Trigger可在任何时候在  Scheduler添加或删除 ( 除非是调用了它的  shutdown() 方法 ) 。
Schduelr包含一个两个重要组件: JobStore和ThreadPool。

JobStore是会来存储运行时信息的，包括Trigger,Schduler,JobDetail，业务锁等。它有多种实现RAMJob(内存实现)，JobStoreTX(JDBC，事务由Quartz管理），JobStoreCMT(JDBC，使用容器事务)，ClusteredJobStore(集群实现)、TerracottaJobStore(什么是Terractta)。

ThreadPool就是线程池，Quartz有自己的线程池实现。所有任务的都会由线程池执行。

SchedulerFactory
SchdulerFactory，顾名思义就是来用创建Schduler了，有两个实现：DirectSchedulerFactory和 StdSchdulerFactory。前者可以用来在代码里定制你自己的Schduler参数。后者是直接读取classpath下的quartz.properties（不存在就都使用默认值）配置来实例化Schduler。通常来讲，我们使用StdSchdulerFactory也就足够了。
SchdulerFactory本身是支持创建RMI stub的，可以用来管理远程的Scheduler，功能与本地一样，可以远程提交个Job什么的。

StdSchdulerFactory的配置例子， 更多配置，参考Quartz配置指南：
org.quartz.scheduler.instanceName = DefaultQuartzScheduler
org.quartz.threadPool.class = org.quartz.simpl.SimpleThreadPool
org.quartz.threadPool.threadCount = 10 
org.quartz.threadPool.threadPriority = 5
org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread = true
org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore