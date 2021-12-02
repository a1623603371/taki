# 1.Eureka源码分析
    spring cloud 默认使用的是netflix 开源的 Eureka 做为注册中心,Eureka 做为一个AP架构的分布式注册中心，
    我们通过阅读源码去掌握eureka架构和当前架构存在问题,掌握好源码和架构就能在开发过程如何避免一些些，遇到bug能
    自己做2次的开发，也能学到一下优秀的架构思想。


## 1.1EurekaServerBootstrap类分析

    public class EurekaServerBootstrap {

	private static final Log log = LogFactory.getLog(EurekaServerBootstrap.class);
    
	private static final String TEST = "test";
    // 部署环境
	private static final String ARCHAIUS_DEPLOYMENT_ENVIRONMENT = "archaius.deployment.environment";
    // eureka 环境
	private static final String EUREKA_ENVIRONMENT = "eureka.environment";
    
	private static final String DEFAULT = "default";
    // 部署数据中心
	private static final String ARCHAIUS_DEPLOYMENT_DATACENTER = "archaius.deployment.datacenter";
    // eureka 数据中心
	private static final String EUREKA_DATACENTER = "eureka.datacenter";
    // eureka 服务端配置
	protected EurekaServerConfig eurekaServerConfig;
    // 应用上下文管理器 保存eureka各种配置
	protected ApplicationInfoManager applicationInfoManager;
    // eureka 客户端配置
	protected EurekaClientConfig eurekaClientConfig;
    //注册中心管理组件
	protected PeerAwareInstanceRegistry registry;
    // eureka服务端上下文 负责启动，关闭 eureka ，获取注册信息等
	protected volatile EurekaServerContext serverContext;
    aws 云服务相关可忽略
	protected volatile AwsBinder awsBinder;
    

	public EurekaServerBootstrap(ApplicationInfoManager applicationInfoManager,
			EurekaClientConfig eurekaClientConfig, EurekaServerConfig eurekaServerConfig,
			PeerAwareInstanceRegistry registry, EurekaServerContext serverContext) {
		this.applicationInfoManager = applicationInfoManager;
		this.eurekaClientConfig = eurekaClientConfig;
		this.eurekaServerConfig = eurekaServerConfig;
		this.registry = registry;
		this.serverContext = serverContext;
	}
    // 初始化上下文
	public void contextInitialized(ServletContext context) {
		try {
            初始化eureka环境配置
			initEurekaEnvironment();
			initEurekaServerContext();

			context.setAttribute(EurekaServerContext.class.getName(), this.serverContext);
		}
		catch (Throwable e) {
			log.error("Cannot bootstrap eureka server :", e);
			throw new RuntimeException("Cannot bootstrap eureka server :", e);
		}
	}

	public void contextDestroyed(ServletContext context) {
		try {
			log.info("Shutting down Eureka Server..");
			context.removeAttribute(EurekaServerContext.class.getName());

			destroyEurekaServerContext();
			destroyEurekaEnvironment();

		}
		catch (Throwable e) {
			log.error("Error shutting down eureka", e);
		}
		log.info("Eureka Service is now shutdown...");
	}
    // 初始化 eureka环境配置
	protected void initEurekaEnvironment() throws Exception {
		log.info("Setting the eureka configuration..");
        //通过 archaius 包 里的  ConfigurationManager 获取配置实例获取数据中心
		String dataCenter = ConfigurationManager.getConfigInstance()
				.getString(EUREKA_DATACENTER);
        // 判断数据中心是否为空
		if (dataCenter == null) {
			log.info(
					"Eureka data center value eureka.datacenter is not set, defaulting to default");
            // 如果数据中心为空，设置数据中心为 deflet 
			ConfigurationManager.getConfigInstance()
					.setProperty(ARCHAIUS_DEPLOYMENT_DATACENTER, DEFAULT);
		}
		else {
            // 不为空设置数据中心
			ConfigurationManager.getConfigInstance()
					.setProperty(ARCHAIUS_DEPLOYMENT_DATACENTER, dataCenter);
		}
        // 获取 运行环境
		String environment = ConfigurationManager.getConfigInstance()
				.getString(EUREKA_ENVIRONMENT);
        // 运行环境为空
		if (environment == null) {
            // 设置默认环境为TEST 
			ConfigurationManager.getConfigInstance()
					.setProperty(ARCHAIUS_DEPLOYMENT_ENVIRONMENT, TEST);
			log.info(
					"Eureka environment value eureka.environment is not set, defaulting to test");
		}
		else {
			ConfigurationManager.getConfigInstance()
					.setProperty(ARCHAIUS_DEPLOYMENT_ENVIRONMENT, environment);
		}
	}
    // 初始化 eureka 服务端上下文
	protected void initEurekaServerContext() throws Exception {
		// For backward compatibility
        // 注册 JSON XML处理 装换器
		JsonXStream.getInstance().registerConverter(new V1AwareInstanceInfoConverter(),
				XStream.PRIORITY_VERY_HIGH);
		XmlXStream.getInstance().registerConverter(new V1AwareInstanceInfoConverter(),
				XStream.PRIORITY_VERY_HIGH);
        // 判断是否 Amazon 服务器
		if (isAws(this.applicationInfoManager.getInfo())) {
			this.awsBinder = new AwsBinderDelegate(this.eurekaServerConfig,
					this.eurekaClientConfig, this.registry, this.applicationInfoManager);
			this.awsBinder.start();
		}
        // 将 eureka 服务端 上下 加载到 holder 中
		EurekaServerContextHolder.initialize(this.serverContext);
        // 初始化完毕 服务端上下文
		log.info("Initialized server context");

		// Copy registry from neighboring eureka node

        //这里 获取其他 eureka server 上的 服务注册表的注册数量
		int registryCount = this.registry.syncUp();
        //这里会启动心跳检测线程，统计线程，计算每分钟需要接受到多少心跳，设置 本身实例的 的状态为UP
		this.registry.openForTraffic(this.applicationInfoManager, registryCount);
		// Register all monitoring statistics.
        // 注册所有的监听统计信息
		EurekaMonitors.registerAllStats();
	}

	/**
	 * Server context shutdown hook. Override for custom logic
	 * @throws Exception - calling {@link AwsBinder#shutdown()} or
	 * {@link EurekaServerContext#shutdown()} may result in an exception
	 */
	protected void destroyEurekaServerContext() throws Exception {
		EurekaMonitors.shutdown();
		if (this.awsBinder != null) {
			this.awsBinder.shutdown();
		}
		if (this.serverContext != null) {
			this.serverContext.shutdown();
		}
	}

	/**
	 * Users can override to clean up the environment themselves.
	 * @throws Exception - shutting down Eureka servers may result in an exception
	 */
	protected void destroyEurekaEnvironment() throws Exception {
	}

	protected boolean isAws(InstanceInfo selfInstanceInfo) {
		boolean result = DataCenterInfo.Name.Amazon == selfInstanceInfo
				.getDataCenterInfo().getName();
		log.info("isAws returned " + result);
		return result;
	}

    }    
    通过 EurekaServerInitializerConfiguration 类中start方法 启动一个 线程 去执行 上面 EurekaServerBootstrap
    初始化 eureka server 启动
    @Override
	public void start() {
		new Thread(() -> {
			try {
				// TODO: is this class even needed now?
				eurekaServerBootstrap.contextInitialized(
						EurekaServerInitializerConfiguration.this.servletContext);
				log.info("Started Eureka Server");
                //发布 eureka注册的可用事件
				publish(new EurekaRegistryAvailableEvent(getEurekaServerConfig()));
			    // 设置eureka 运行 状态
				EurekaServerInitializerConfiguration.this.running = true;
                //发布 eureka 启动事件
				publish(new EurekaServerStartedEvent(getEurekaServerConfig()));
			}
			catch (Exception ex) {
				// Help!
				log.error("Could not initialize Eureka servlet context", ex);
			}
		}).start();
	}

##Eureka Server端  