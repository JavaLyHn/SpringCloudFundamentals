**Q1:注册中心宕机，远程调用还能成功吗？**
答：如果是第一次调用，不能成功；如果调用过，那么远程调用将不再依赖注册中心，可以通过。

**Q2:Nacos中的数据集合application.properties中有相同的配置项，哪个生效**
答：以Nacos配置中心为准。遵循先导入优先①，外部优先②。
①：spring.config.import=nacos:service-order.properties,nacos:common.properties
②：Nacos中的数据集合application.properties中有相同的配置项