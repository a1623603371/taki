# 1.Eureka源码分析
    spring cloud 默认使用的是netflix 开源的 Eureka 做为注册中心,Eureka 做为一个AP架构的分布式注册中心，
    我们通过阅读源码去掌握eureka架构和当前架构存在问题,掌握好源码和架构就能在开发过程如何避免一些些，遇到bug能快速定位问题，
    同时也能
    自己做2次的开发，也能学到一下优秀的架构思想。
    eurka集群没有主从概念每个节点都对等的，同时 eureka server 也 是 eurka client ，具备client 的所有功能，
    在集群环境下eurka server 节点 都是要相互向对方节点进行注册的。


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

##2.Eureka Client 初始化源码分析 DiscoveryClient 类

### 2.1 DiscoveryClient 初始化
    @Inject
    DiscoveryClient(ApplicationInfoManager applicationInfoManager, EurekaClientConfig config, AbstractDiscoveryClientOptionalArgs args, Provider<BackupRegistry> backupRegistryProvider, EndpointRandomizer endpointRandomizer) {
        // 创建哈希码不匹配 监听器
        this.RECONCILE_HASH_CODES_MISMATCH = Monitors.newCounter("DiscoveryClient_ReconcileHashCodeMismatch");
        // 创建 获取注册表定时器
        this.FETCH_REGISTRY_TIMER = Monitors.newTimer("DiscoveryClient_FetchRegistry");
        // 创建注册表 计数器
        this.REREGISTER_COUNTER = Monitors.newCounter("DiscoveryClient_Reregister");
        // 创建 本地注册信息  
        this.localRegionApps = new AtomicReference();
        // 创建更新注册表独占锁
        this.fetchRegistryUpdateLock = new ReentrantLock();
        // 健康检查处理程序
        this.healthCheckHandlerRef = new AtomicReference();
        // 
        this.remoteRegionVsApps = new ConcurrentHashMap();
        this.lastRemoteInstanceStatus = InstanceStatus.UNKNOWN;
        this.eventListeners = new CopyOnWriteArraySet();
        this.registrySize = 0;
        this.lastSuccessfulRegistryFetchTimestamp = -1L;
        this.lastSuccessfulHeartbeatTimestamp = -1L;
        this.isShutdown = new AtomicBoolean(false);
        this.stats = new DiscoveryClient.Stats();
        if (args != null) {
            this.healthCheckHandlerProvider = args.healthCheckHandlerProvider;
            this.healthCheckCallbackProvider = args.healthCheckCallbackProvider;
            this.eventListeners.addAll(args.getEventListeners());
            this.preRegistrationHandler = args.preRegistrationHandler;
        } else {
            this.healthCheckCallbackProvider = null;
            this.healthCheckHandlerProvider = null;
            this.preRegistrationHandler = null;
        }

        this.applicationInfoManager = applicationInfoManager;
        InstanceInfo myInfo = applicationInfoManager.getInfo();
        this.clientConfig = config;
        staticClientConfig = this.clientConfig;
        this.transportConfig = config.getTransportConfig();
        this.instanceInfo = myInfo;
        if (myInfo != null) {
            this.appPathIdentifier = this.instanceInfo.getAppName() + "/" + this.instanceInfo.getId();
        } else {
            logger.warn("Setting instanceInfo to a passed in null value");
        }

        this.backupRegistryProvider = backupRegistryProvider;
        this.endpointRandomizer = endpointRandomizer;
        this.urlRandomizer = new InstanceInfoBasedUrlRandomizer(this.instanceInfo);
        this.localRegionApps.set(new Applications());
        this.fetchRegistryGeneration = new AtomicLong(0L);
        this.remoteRegionsToFetch = new AtomicReference(this.clientConfig.fetchRegistryForRemoteRegions());
        this.remoteRegionsRef = new AtomicReference(this.remoteRegionsToFetch.get() == null ? null : ((String)this.remoteRegionsToFetch.get()).split(","));
        if (config.shouldFetchRegistry()) {
            this.registryStalenessMonitor = new ThresholdLevelsMetric(this, "eurekaClient.registry.lastUpdateSec_", new long[]{15L, 30L, 60L, 120L, 240L, 480L});
        } else {
            this.registryStalenessMonitor = ThresholdLevelsMetric.NO_OP_METRIC;
        }

        if (config.shouldRegisterWithEureka()) {
            this.heartbeatStalenessMonitor = new ThresholdLevelsMetric(this, "eurekaClient.registration.lastHeartbeatSec_", new long[]{15L, 30L, 60L, 120L, 240L, 480L});
        } else {
            this.heartbeatStalenessMonitor = ThresholdLevelsMetric.NO_OP_METRIC;
        }

        logger.info("Initializing Eureka in region {}", this.clientConfig.getRegion());
        if (!config.shouldRegisterWithEureka() && !config.shouldFetchRegistry()) {
            logger.info("Client configured to neither register nor query for data.");
            this.scheduler = null;
            this.heartbeatExecutor = null;
            this.cacheRefreshExecutor = null;
            this.eurekaTransport = null;
            this.instanceRegionChecker = new InstanceRegionChecker(new PropertyBasedAzToRegionMapper(config), this.clientConfig.getRegion());
            DiscoveryManager.getInstance().setDiscoveryClient(this);
            DiscoveryManager.getInstance().setEurekaClientConfig(config);
            this.initTimestampMs = System.currentTimeMillis();
            this.initRegistrySize = this.getApplications().size();
            this.registrySize = this.initRegistrySize;
            logger.info("Discovery Client initialized at timestamp {} with initial instances count: {}", this.initTimestampMs, this.initRegistrySize);
        } else {
            try {
                this.scheduler = Executors.newScheduledThreadPool(2, (new ThreadFactoryBuilder()).setNameFormat("DiscoveryClient-%d").setDaemon(true).build());
                this.heartbeatExecutor = new ThreadPoolExecutor(1, this.clientConfig.getHeartbeatExecutorThreadPoolSize(), 0L, TimeUnit.SECONDS, new SynchronousQueue(), (new ThreadFactoryBuilder()).setNameFormat("DiscoveryClient-HeartbeatExecutor-%d").setDaemon(true).build());
                this.cacheRefreshExecutor = new ThreadPoolExecutor(1, this.clientConfig.getCacheRefreshExecutorThreadPoolSize(), 0L, TimeUnit.SECONDS, new SynchronousQueue(), (new ThreadFactoryBuilder()).setNameFormat("DiscoveryClient-CacheRefreshExecutor-%d").setDaemon(true).build());
                this.eurekaTransport = new DiscoveryClient.EurekaTransport();
                this.scheduleServerEndpointTask(this.eurekaTransport, args);
                Object azToRegionMapper;
                if (this.clientConfig.shouldUseDnsForFetchingServiceUrls()) {
                    azToRegionMapper = new DNSBasedAzToRegionMapper(this.clientConfig);
                } else {
                    azToRegionMapper = new PropertyBasedAzToRegionMapper(this.clientConfig);
                }

                if (null != this.remoteRegionsToFetch.get()) {
                    ((AzToRegionMapper)azToRegionMapper).setRegionsToFetch(((String)this.remoteRegionsToFetch.get()).split(","));
                }

                this.instanceRegionChecker = new InstanceRegionChecker((AzToRegionMapper)azToRegionMapper, this.clientConfig.getRegion());
            } catch (Throwable var12) {
                throw new RuntimeException("Failed to initialize DiscoveryClient!", var12);
            }

            if (this.clientConfig.shouldFetchRegistry()) {
                try {
                    boolean primaryFetchRegistryResult = this.fetchRegistry(false);
                    if (!primaryFetchRegistryResult) {
                        logger.info("Initial registry fetch from primary servers failed");
                    }

                    boolean backupFetchRegistryResult = true;
                    if (!primaryFetchRegistryResult && !this.fetchRegistryFromBackup()) {
                        backupFetchRegistryResult = false;
                        logger.info("Initial registry fetch from backup servers failed");
                    }

                    if (!primaryFetchRegistryResult && !backupFetchRegistryResult && this.clientConfig.shouldEnforceFetchRegistryAtInit()) {
                        throw new IllegalStateException("Fetch registry error at startup. Initial fetch failed.");
                    }
                } catch (Throwable var11) {
                    logger.error("Fetch registry error at startup: {}", var11.getMessage());
                    throw new IllegalStateException(var11);
                }
            }

            if (this.preRegistrationHandler != null) {
                this.preRegistrationHandler.beforeRegistration();
            }

            if (this.clientConfig.shouldRegisterWithEureka() && this.clientConfig.shouldEnforceRegistrationAtInit()) {
                try {
                    if (!this.register()) {
                        throw new IllegalStateException("Registration error at startup. Invalid server response.");
                    }
                } catch (Throwable var10) {
                    logger.error("Registration error at startup: {}", var10.getMessage());
                    throw new IllegalStateException(var10);
                }
            }

            this.initScheduledTasks();

            try {
                Monitors.registerObject(this);
            } catch (Throwable var9) {
                logger.warn("Cannot register timers", var9);
            }

            DiscoveryManager.getInstance().setDiscoveryClient(this);
            DiscoveryManager.getInstance().setEurekaClientConfig(config);
            this.initTimestampMs = System.currentTimeMillis();
            this.initRegistrySize = this.getApplications().size();
            this.registrySize = this.initRegistrySize;
            logger.info("Discovery Client initialized at timestamp {} with initial instances count: {}", this.initTimestampMs, this.initRegistrySize);
        }
    }