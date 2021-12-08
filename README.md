## 1、 概念介绍：quarkus是什么？
+ Quarkus是红帽面向云原生推出的java技术体系，它已经不能被称为框架了，体系这个词也许更适合它，它有不少非常好的新思路。
+ 它侧重于对GraalVM的支持，倾向于使用GraalVM来打包为Native原生应用。Java曾经的优势是Write once run anywhere，但是现在这个优势已经被docker取代，有了docker，只要制作好镜像，其它语言也可以做到Write once run anywhere。而Java庞大的JVM运行时反而就成了它的劣势之一，所以Oracle发布了GraalVM，将Java程序打包成原生程序，去掉了JVM运行时，在很大程度上弥补了这一劣势。而拥有庞大的开源Java库的红帽，为了积极应对云环境的挑战，开发了Quarkus体系，旨在弥补Java库的劣势，在云原生时代依然能够保持其产品的竞争力。
+ 它与Spring的一些功能和性能的比较，[参考网页](https://simply-how.com/quarkus-vs-spring)。
+ 它支持Java和Kotlin。
+ 红帽的很多Java库都提供了接入Quarkus的扩展，并且提供了Quarkiverse作为一个平台，广泛接纳社区开发的扩展。
+ [官网地址](https://quarkus.io/)

## 2、简单的DEMO程序介绍
+ Quarkus+Kotlin
+ 使用扩展

|扩展名|作用|
|--|--|
|quarkus-config-yaml|默认的配置文件是properties格式，我个人喜欢yaml格式，所以增加支持yaml的扩展|
|quarkus-resteasy-reactive|reactive的Web服务扩展|
|quarkus-resteasy-reactive-jackson|reactive的JSON序列化扩展|
|quarkus-hibernate-reactive-panache|reactive的数据库操作扩展|
|quarkus-reactive-mysql-client|reactive的mysql驱动，其实也可以直接使用它，但是易用性略差|
|quarkus-redis-client|reactive的redis操作扩展|

## 3、为什么使用Kotlin
+ 很简单，Kotlin的Coroutine可以把原本的异步代码写在同步，大大减少了程序员的心智负担，举个例子


```
        // java代码
        return this.getA(id).chain(aInfo -> {
            return BInfo.findById(aInfo.bid);
        }).chain(bInfo -> {
            AssertUtil.DEFAULT.notNull(bInfo, new NoDataException());
            return CInfo.findByName(bInfo.cName);
        }).map(cInfo -> {
            ResDto resDto = new ResDto();
            resDto.setExpireAt(AppUtil.formatDateTime(cInfo.expireTime));
            return resDto;
        });
```

```
        // kotlin代码
        val aInfo: AInfo = this.getA(id).awaitSuspending()
        val bInfo: BInfo = BInfo.findById(aInfo.bid).awaitSuspending() ?: throw NoDataException()
        val cInfo: CInfo = CInfo.findByName(bInfo.cName).awaitSuspending()
        val ResDto resDto = new ResDto()
        resDto.setExpireAt(AppUtil.formatDateTime(cInfo.expireTime))
        return resDto
```

## 4、使用Kotlin遇到的坑
+ quarkus-hibernate-panache有一个支持Kotlin的版本，但是不支持reactive，所以性能会差一些，弃用。
+ quarkus-hibernate-panache的java的entity直接转成kotlin不能直接使用，默认的panache entity采用如下的static方法来操作数据库，而Kotlin是没有static方法的，而是采用companion object来对应Java的static方法，而companion object其实是不属于entity类的，所以在执行的时候会报错，因为关联不到entity类


```
    // java方法
    public static Uni<AInfo> findByCode(String aCode) {
        return find("aCode = ?1 and deleteType = ?2", aCode, 0).firstResult();
    }
```

```
    // kotlin方法
    companion object {
        fun findByCode(MySQLPool, aCode: String): Uni<AInfo?> {
            return find("aCode = ?1 and deleteType = ?2", aCode, 0).firstResult();
        }
    }
```
+ 通过不使用companion object，而定义Repository的方式来解决

```
  @ApplicationScoped
  class AInfoRepository: PanacheRepository<AInfo> {

      fun findByCode(aCode: String): Uni<AInfo?> {
          return find("aCode = ?1 and deleteType = ?2", aCode, 0).firstResult()
      }
  }
```
+ 响应为application/json的数据类，全部要加注解，类注解加上@RegisterForReflection，属性注解加上@field:JsonProperty("xxx")，否则无法转换为json
+ 请求为application/json的数据类，官方文档上也说，属性注解加上@field:JsonProperty("xxx")，但是实际上没加似乎也能运行，另外data class的属性要加上初始值，否则可能会导致报错cannot deserialize from Object value (no delegate- or property-based Creator)

## 4、打包遇到的坑
+ kotlin的所有类会默认是final类，打包时会报警告hibernate无法给entity生成代理类，会影响性能，所以最好是把所有entity类及其字段全部加上open修饰
+ application.properties或者application.yml里的quarkus.native.native-image-xmx属性，严重影响打包速度，我最开始设置的3000M，甚至会经常outofmemory，后来我设置成了10000M，打包时间从以前的20分钟提高到了3分半

### 5、demo其它说明
+ [github地址](https://github.com/aaavieri/quarkus-try.git)
+ 组件版本：
  * GraalVM：20.3.0
  * quarkus：2.4.2.final
  * Kotlin：1.5.31
+ 打包成fat-jar：mvn clean package -Dquarkus.package.type=uber-jar
+ 运行fat-jar：java -jar ./build/quarkus-1.0.0-SNAPSHOT-runner.jar -Dsmallrye.config.locations=./external.yml
+ 打包成原生应用：mvn clean package -Dquarkus.package.type=native
+ 运行原生应用：./target/quarkus-try-1.0.0-SNAPSHOT-runner -Dsmallrye.config.locations=./external.yml
+ 注意，打包成原生应用windows可能不行，因为除了GraalVM之外，还需要额外安装C++编译器，GraalVM官方说安装VS 2017 C++ builder即可，但是我试过不行，
  幸好我还有一台Macbook
