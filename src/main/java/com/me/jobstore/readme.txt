目前，Quartz 提供了两种类型的持久性 JobStore，每一种类型都有其独特的持久化机制。

持久性  JobStore = JDBC +  关 系型 数 据 库
尽管有几种不同的持久化机制可被  Quartz  用于持久化  Scheduler  信息， Quartz  依赖于一
个关系型数据库管理系统 ( RDMS)  来持久化存储。假如你想用某种别的而不是数据库来持久化
存储，那么你必须通过实现  JobStore  接口自己构建它。假定你想用文件系统来持久化存
储。你可以创建一个类，这个类要实现  JobStore 接口，在本章中，当我们说  " 持久化 " ，我
们隐式的是说用  JDBC  来持久化  Scheduler  状态到数据库中

Quartz 所带的所有的持久化的 JobStore 都扩展自 org.quartz.impl.jdbcjobstore.JobStoreSupport 类。
JobStoreSupport 是个抽象类，并实现了 JobStore 接口，在此章前面就讨论过的。它为所有基于  JDBC  的 JobStore 提供了基本的功能。

因为 JobStoreSupport 类是抽象的，因此 Quartz 提供了两种不同类型的具体化的 JobStore，每一个设计为针对特定的数据库
环境和配置：
·org.quartz.impl.jdbcjobstore.JobStoreTX
·org.quartz.impl.jdbcjobstore.JobStoreCMT

· 独 立 环 境中的持久性存 储
JobStoreTX 类设计为用于独立环境中。这里的  " 独立 " ，我们是指这样一个环境，在其中不存在与应用容器的事物集成。这里并不
意味着你不能在一个容器中使用 JobStoreTX，只不过，它不是设计来让它的事特受容器管理。区别就在于 Quartz 的事物是否要
参与到容器的事物中去。
· 程序容器中的持久性存 储
JobStoreCMT 类设计为当你想要程序容器来为你的 JobStore 管理事物时，并且那些事物要参与到容器管理的事物边界时使用。
它的名字明显是来源于容器管理的事物(Container Managed Transactions (CMT))。

 创 建  Quartz 数 据 库结 构
JobStore 是基于  JDBC  的，它需要一个数据用于 Scheduler 信息的持久化。Quartz 需要创建  12  张数据库表。
表名 描述
QRTZ_CALENDARS 以  Blob  类型存储  Quartz  的  Calendar  信息
QRTZ_CRON_TRIGGERS 存储  Cron Trigger ，包括  Cron  表达式和时区信息
QRTZ_FIRED_TRIGGERS 存储与已触发的  Trigger  相关的状态信息，以及相联  Job  的执行信息
QRTZ_PAUSED_TRIGGER_GRPS 存储已暂停的  Trigger  组的信息
QRTZ_SCHEDULER_STATE 存储少量的有关  Scheduler  的状态信息，和别的  Scheduler  实例 ( 假如是用于一个集群中 )
QRTZ_LOCKS 存储程序的非观锁的信息 ( 假如使用了悲观锁 )
QRTZ_JOB_DETAILS 存储每一个已配置的  Job  的详细信息
QRTZ_JOB_LISTENERS 存储有关已配置的  JobListener 的信息
QRTZ_SIMPLE_TRIGGERS 存储简单的  Trigger ，包括重复次数，间隔，以及已触的次数
QRTZ_BLOG_TRIGGERS Trigger  作为  Blob  类型存储 ( 用于  Quartz  用户用  JDBC  创建他们自己定制的  Trigger  类型， JobStore 并不知道如何存储实例的时候 )
QRTZ_TRIGGER_LISTENERS 存储已配置的  TriggerListener 的信息
QRTZ_TRIGGERS 存储已配置的  Trigger  的信息

 使用  JobStoreTX
配置  JobStoreTX
要告诉 Quartz 运行环境你想使用一个别的 JobStore 而不是默认的 RAMJobStore，你必须配置几个属性。配置它们的顺序无
关紧要，只要保证在第一次运行程序之前都做了设置。
1.设 置  JobStore  属 性
	欲告知 Scheduler 应该使用 JobStoreTX，你必须加上下面一行到 quartz.properties 文件中：
	org.quartz.jobStore.class = org.quartz.ompl.jdbcjobstore.JobStoreTX
2.配置 驱动 代理
	JDBC API  依赖于专属于某个数据库平台的  JDBC  驱动，同样的，Quartz 依赖于某个  DriverDelegate 来与给定数据库进行通
	信。顾名思义，从 Scheduler 通过 JobStore 对数据库的调用是委托给一个预配置的  DriverDelegate 实例。这个代理承担起所
	有与  JDBC driver  也就是数据库的通信。
	所有的  DriverDelegate 类都继承自  org.quartz.impl.jdbcjobstore.StdDriverDelegate 类。 StdDriverDelegte 只有所有代
	理可用的，平台无关性的基本功能。然而，在不同的数据库平台间还是存在太多的差异，因此需要为某个平台创建特定的代理。表
  6.2 列出特定的代理。
   表  6.2.  你 必 须为 你 的平台配置其中一 个 DriverDelegate 数 据 库 平台 Quartz  代理 类
 Cloudscape/Derby org.quartz.impl.jdbcjobstore.CloudscapeDelegate
 DB2 (version 6.x) org.quartz.impl.jdbcjobstore.DB2v6Delegate
 DB2 (version 7.x) org.quartz.impl.jdbcjobstore.DB2v7Delegate
 DB2 (version 8.x) org.quartz.impl.jdbcjobstore.DB2v8Delegate
 HSQLDB org.quartz.impl.jdbcjobstore.PostgreSQLDelegate
 MS SQL Server org.quartz.impl.jdbcjobstore.MSSQLDelegate
 Pointbase org.quartz.impl.jdbcjobstore.PointbaseDelegate
 PostgreSQL org.quartz.impl.jdbcjobstore.PostgreSQLDelegate
 (WebLogic JDBC Driver) org.quartz.impl.jdbcjobstore.WebLogicDelegate
 (WebLogic 8.1 with Oracle) org.quartz.impl.jdbcjobstore.oracle.weblogic.WebLogicOracleDelegate
 Oracle org.quartz.impl.jdbcjobstore.oracle.OracleDelegate

  如果你的 RDBMS没在上面列出，那么最好的选择就是，直接使用标准的 JDBC代理org.quartz.impl.jdbcjobstore.StdDriverDelegate 就能正常的工作。
  在你决定好了基于你的数据库平台使用哪个代理，你就需要加入下面的行到 quartz.properties 文件中：
 org.quartz.jobStore.driverDelegateClass = <FQN of driver delegate class>
3.配置 数 据 库 表的前 缀
  配置在 quartz.properties 文件中，使用属性 org.quartz.jobStore.tablePrefix。要改变这一前缀，只要设置这个属性为不同的值：
 org.quartz.jobStore.tablePrefix = SCHEDULER2_
 
4.为 JobStroreTX 创 建 数 据源
 默认的，Quartz能使用另一开源的框架，叫做  Commons DBCP ，或者可以通过  JNDI  查找应用服务器中定义的 DataSource。
 表  6.4.  配置  Quartz Datasource  的可用 属 性
属 性 必 须
org.quartz.dataSource.NAME.driver 是		 描述： JDBC  驱动类的全限名
org.quartz.dataSource.NAME.URL 是	描述：连接到你的数据库的  URL( 主机，端口等 )
org.quartz.dataSource.NAME.user 否	描述：用于连接你的数据库的用户名
org.quartz.dataSource.NAME.password 否	描述：用于连接你的数据库的密码
org.quartz.dataSource.NAME.maxConnections 否	描述： DataSource  在连接接中创建的最大连接数
org.quartz.dataSource.NAME.validationQuary 否
描述：一个可选的  SQL查询字串， DataSource 用它来侦测并替换失败 / 断开的连接。例如， Oracle  用户可选用  select table_name from user_tables ，这个查询应当永远不会失败，除非直的就是连接不上了。
 
使用 数 据 库 存 储 Scheduler  信息
· 加 载 Job  到 数 据 库 
存在以下几个方法把 Job 信息存入到数据库：
   · 在你的程序中加入 Job 信息
   · 使用  JobInitializationPlugin
   · 使用 Quartz Web 应用程序
 
 
使用  JobStoreCMT
JobStoreCMT 被设计成参与到容器的事物边界内。这意味着容器创建一个 JTA 事物并使之对于 JobStore 可用。Quartz 与
JobStore 的交互保持在这个事物中。假如出现任何问题，Quartz 能给容器一个信号，它希望通过调用事物的  setRollbackOnly
() 使事物回滚。

1.配置  JobStoreCMT
同之前的 JobStoreTX 和 RAMJobStore 一样，要使用 JobStoreCMT 的第一步是告知 Scheduler 你打算用
JobStoreCMT。和以前类似，也是通过在 quartz.properties 文件中设置 JobStore 类属性来做到这一点的：
org.quartz.jobStore.class = org.quartz.impl.jdbcjobstore.JobStoreCMT
如果属性文件中存在 RAMJobStore 行，要确保移除了它。

2.配置  DriverDelegate 类
你也是需要像为 JobStoreTX 所做的那样选择  DriverDelegate 。Quartz 依靠一个  DriverDelegate 与给定的数据库通信。代理
负责了与  JDBC Driver ，也就是数据库的所有通信。
回到表 6.2 中的  DriverDelegate 列表，并基于你的数据库平台和环境选择一个。要加 MS SQLServer 代理到
quart.properties 文件，那就加入下一行：
org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.MSSQLDelegate
 
3. 为 JobStoreCMT 配置 数据源
跟 JobStoreTX 一样，我们需要配置一个 Datasource 才能使用 JobStoreCMT。然而，JobStoreCMT 需要两个
Datasource 而不是像 JobStoreTX 只要一个。其中一个 Datasource 和我们为 JobStoreTX 设置的类同：作为不受管理的数
据源。同时呢，我们还需配置第二个数据源，是作为受管理的数据源，它由应用服务器来进行管理。
 
 配置不受管理的 数 据源
我们在设置不受管理的数据源的多数操作与为 JobStoreTX 所做是相同的，只是我们还要加上一行来指定这是
nonManagedTXDataSource:
# Add the property for the nonManagedTXDataSource
org.quartz.jobStore.nonManagedTXDataSource = myDS
org.quartz.dataSource.myDS.driver = net.sourceforge.jtds.jdbc.Driver
org.quartz.dataSource.myDS.URL = jdbc:jtds:sqlserver://localhost:1433/quartz
org.quartz.dataSource.myDS.user = admin
org.quartz.dataSource.myDS.password = myPassword
org.quartz.dataSource.myDS.maxConnections = 10
这是配置不受管理的数据源，并让 JobStore 知道这个 nonManagedTXDataSource 叫做  " myDS" 。
 
 配置受管理的 Datasource
 第二个数据源需配置为一个受管理的 Datasource。这意味着 Quartz 在执行 Scheduler 操作时使用一个容器已创建好的
Datasorce 与数据库交互。当 Quartz 从 Datasource 上取得了连接后，在 Quartz 部署 Job 和  Trigger  时应有一个 JTA 事
物。例如，代码要求 Quartz 在  SessionBean 的一个方法上的事物描述符设置为  REQUIRED 。另一个应用是客户端程序要通过
使用 javax.transaction.UserTransaction 直接启动一个事物。
和不受管理的 Datasoure 一样，也是要在 quartz.properties 文件中配置容器管理的 Datasource。下面的例子描述了如何设
置受管理的 Datasource:
org.quartz.dataSource.NAME.jndiURL=jdbc/quartzDS
org.quartz.dataSource.NAME.java.naming.factory.initial=weblogic.jndi.WLInitialContextFactory
org.quartz.dataSource.NAME.java.naming.provider.url=t3://localhost:7001
org.quartz.dataSource.NAME.java.naming.security.principal=weblogic
org.quartz.dataSource.NAME.java.naming.security.credentials=weblogic
 
 属 性 必 须 
org.quartz.dataSource.NAME.jndiURL 是	描述：受你的应用服务器管理的   DataSource 的  JNDI URL
org.quartz.dataSource.NAME.java.naming.factory.initial 否	描述：可选项，你想用的  JNDI InitialContextFactory 的类名称
org.quartz.dataSource.NAME.java.naming.provider.url 否	描述：可选项，连接到  JNDI  上下文的  URL
org.quartz.dataSource.NAME.java.naming.security.principal 否	描述：可选项，连接到  JNDI上下文的用户主体 ( Unmi注：用户名 )
org.quartz.dataSource.NAME.java.naming.security.credential 否	描述：可选项，连接到  JNDI上下文的用户凭证 (Unmi 注：密码 
 
 使用到表 6.6 中的属性，这儿有一个在 quartz.properties 中配置受管理的 Datasource 的例子。
org.quartz.dataSource.WL.jndiURL = OraDataSource
org.quartz.dataSource.WL.jndiAlwaysLookup = DB_JNDI_ALWAYS_LOOKUP
org.quartz.dataSource.WL.java.naming.factory.initial = weblogic.jndi.WLInitialContextFactory
org.quartz.dataSource.WL.java.naming.provider.url = t3://localhost:7001
org.quartz.dataSource.WL.java.naming.security.principal = weblogic
org.quartz.dataSource.WL.java.naming.security.credentials = weblogic

 改善持久性  JobStore 的性能
 
 
 
 
 
