# Spring Boot 结合 Kafka 的Demo

环境配置参考文档：[编程环境和软件工具安装手册.pdf](./files/编程环境和软件工具安装手册.pdf)

KafkaDemo参考博客：http://www.54tianzhisheng.cn/2018/01/05/SpringBoot-Kafka/

Spark安装：[厦大数据库实验室Spark安装](http://dblab.xmu.edu.cn/blog/1307-2/)

Scala安装：[厦大数据库实验室Scala安装](http://dblab.xmu.edu.cn/blog/929-2/)

## 相应软件的安装

> 环境：
> jdk 1.8.0_161	
>
> scala 2.12.12	
>
> spark 2.4.7	
>
> maven 3.6.3	
>
> zookeeper 3.6.1
>
> kafka_2.12-2.5.0
>
> apache-tomcat-8.5.61
>
> nginx-1.17.10.tar.gz

### jdk的安装

1. 在 /usr/local/ 下创建 java ⽂件夹并进⼊。

> cd /usr/local/ 
>
> mkdir java 
>
> cd java

2. 将上⾯准备好的 JDK 安装包解压到 /usr/local/java 中即可

> tar -zxvf /root/jdk-8u161-linux-x64.tar.gz -C ./

解压完之后， /usr/local/java ⽬录中会出现⼀个 jdk1.8.0_161 的⽬录

3. 编辑 /etc/profile ⽂件，在⽂件尾部加⼊如下 JDK 环境配置即可

```sh
JAVA_HOME=/usr/local/java/jdk1.8.0_161  
CLASSPATH=$JAVA_HOME/lib/  
PATH=$PATH:$JAVA_HOME/bin  
export PATH JAVA_HOME CLASSPATH
```

然后执⾏如下命令让环境变量⽣效

> source /etc/profile

4. 输⼊如下命令即可检查安装结果：

```sh
java -version  
javac
```



### Maven安装

1. 这⾥下载的是 `apache-maven-3.6.3-bin.tar.gz` 安装包，并将其放置于提前创建好的 `/opt/maven`
   ⽬录下并解压。

```sh
tar zxvf apache-maven-3.6.3-bin.tar.gz
```

2. 配置Maven阿里加速镜像源

编辑修改 `/opt/maven/apache-maven-3.6.3/conf/settings.xml`
⽂件，在 `<mirrors></mirrors>` 标签对⾥添加如下内容即可：  

```sh
<mirror>
    <id>alimaven</id>
    <name>aliyun maven</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    <mirrorOf>central</mirrorOf>
</mirror>
```

3. 配置环境变量

   编辑修改 `/etc/profile` ⽂件，在⽂件尾部添加如下内容，配置 maven 的安装路径  

   ```sh
   export MAVEN_HOME=/opt/maven/apache-maven-3.6.3
   export PATH=$MAVEN_HOME/bin:$PATH
   ```

   接下来执行` source /etc/profile` 来刷新环境变量，让 maven 环境的路径配置生效 

4. 执⾏ `mvn –v` ，能打印出 maven 版本信息说明安装、配置成功 

### zookeeper的安装

ZooKeeper是一个分布式的，开放源码的分布式应用程序协调服务

1. 在 /usr/local/ 下创建zookeeper ⽂件夹并进⼊.

> cd /usr/local/

2. 将 ZooKeeper 安装包解压到 /usr/local/zookeeper 中即可

>  tar -zxvf /root/apache-zookeeper-3.6.1-bin.tar.gz -C ./  
>  解压完之后， /usr/local/zookeerper ⽬录中会出现⼀个 apache-zookeeper-3.6.1-bin 的⽬录

3. 创建Data目录，这⾥直接在 /usr/local/zookeeper/apache-zookeeper-3.6.1-bin ⽬录中创建⼀个 data ⽬录,等下该 data ⽬录地址要配到 ZooKeeper 的配置⽂件中
4. 创建配置⽂件并修改，进⼊到 zookeeper 的 conf ⽬录，复制 zoo_sample.cfg 得到 zoo.cfg ：

> cd conf  
> cp zoo_sample.cfg zoo.cfg

修改配置⽂件 zoo.cfg ，将其中的 dataDir 修改为上⾯刚创建的 data ⽬录，其他选项可以按需配置

> ticiktime = 2000  
> initiLimit = 10  
> synLimit = 5
> datadir = /home/zcb/zookeeper/data  
> clientPort = 2181

4. 启动zooKeeper

>  ./bin/zkServer.sh start  

启动后可以通过如下命令来检查启动后的状态：

>  ./bin/zkServer.sh status

5. 编辑配置⽂件

>  vim /etc/profile

尾部加⼊ ZooKeeper 的 bin 路径配置即可

```sh
export ZOOKEEPER_HOME=/usr/local/zookeeper/apache-zookeeper-3.6.1-bin  
export PATH=$PATH:$ZOOKEEPER_HOME/bin
```

最后执⾏ source /etc/profile 使环境变量⽣效即可

⾸先进⼊ /etc/rc.d/init.d ⽬录，创建⼀个名为 zookeeper 的⽂件，并赋予执⾏权限

>  cd /etc/rc.d/init.d/  
>  touch zookeeper  
>  chmod +x zookeeper  

接下来编辑 zookeeper ⽂件，并在其中加⼊如下内容：

```shell
    #!/bin/bash  
    #chkconfig:- 20 90
    #description:zookeeper
    #processname:zookeeper
    ZOOKEEPER_HOME=/usr/local/zookeeper/apache-zookeeper-3.6.1-bin
    export JAVA_HOME=/usr/local/java/jdk1.8.0_161 # 此处根据你的实际情况更换对应
    case $1 in
        start) su root $ZOOKEEPER_HOME/bin/zkServer.sh start;;
        stop) su root $ZOOKEEPER_HOME/bin/zkServer.sh stop;;
        status) su root $ZOOKEEPER_HOME/bin/zkServer.sh status;;
        restart) su root $ZOOKEEPER_HOME/bin/zkServer.sh restart;;
        *) echo "require start|stop|status|restart" ;;
    esac
```

### Kafka安装部署

Kafka是最初由Linkedin公司开发，是一个分布式、分区的、多副本的、多订阅者，基于zookeeper协调的分布式日志系统（也可以当做MQ系统），常见可以用于web/nginx日志、访问日志，消息服务等等，Linkedin于2010年贡献给了Apache基金会并成为顶级开源项目。因为 Kafka 的运⾏环境依赖于 ZooKeeper ，所以⾸先得安装并运⾏ ZooKeeper 。

1.  准备KAFKA安装包,这⾥下载的是 2.5.0 版： kafka_2.12-2.5.0.tgz ，将下载后的安装包放在了 /root ⽬录下
2.  解压并安装，在 /usr/local/ 下创建 kafka ⽂件夹并进⼊

> cd /usr/local/  
> mkdir kafka  
> cd kafka  

3. 将Kafka安装包解压到 /usr/local/kafka 中即可

>  tar -zxvf /root/kafka_2.12-2.5.0.tgz -C ./  

解压完之后， /usr/local/kafka ⽬录中会出现⼀个 kafka_2.12-2.5.0 的⽬录

4. 创建Logs目录，这⾥直接在 /usr/local/kafka/kafka_2.12-2.5.0 ⽬录中创建⼀个 logs ⽬录等下该 logs ⽬录地址要配到Kafka的配置⽂件中。
5. 修改配置⽂件，进⼊到 Kafka 的 config ⽬录，编辑配置⽂件 server.properties

> cd config/  
> vim server.properties  

修改配置⽂件，⼀是将其中的 log.dirs 修改为上⾯刚创建的 logs ⽬录，其他选项可以按需配置

```shell
############################# Log Basics #############################

# A comma separated list of directories under which to store log files
log.dirs=/home/zcb/kafka_2.12-2.5.0/logs
```

另外关注⼀下连接 ZooKeeper 的相关配置，根据实际情况进⾏配置：

> zooKeeper.connect = localhost:2181  
>
> zooKeeper.connection = 18000

5. 启动KAFKA，执行如下命令即可：

> ./bin/kafka-server-start.sh ./config/server.properties  
>
> 如果需要后台启动，则在./bin/kafka-server-start.sh后加上 -daemon 参数即可

6. 实验验证，这里要注意要先关闭防火墙

然后⾸先创建⼀个名为 demo 的 topic(对应一个消息队列) ：

> ./bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic demo

创建完成以后，可以使⽤命令来列出⽬前已有的 topic 列表

> ./bin/kafka-topics.sh --list --bootstrap-server localhost:9092

接下来创建⼀个⽣产者，⽤于在demo 这个 topic 上⽣产消息：

> ./bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic demo

⽽后接着创建⼀个消费者，⽤于在 demo 这个 topic 上获取消息：

> ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic demo

此时在生产者中发送消息,可以观察能否在消费者中接受到,若成功接受到,则代表安装完成

## tomcat安装

安装tomcat参考[博客](https://www.cnblogs.com/weosuper/p/10591877.html)

版本为  apache-tomcat-8.5.61

简化tomcat启动参考[博客](https://yq.aliyun.com/articles/762676)

只看其中的第四部分：创建SystemD单元文件

配置文件如下：

```bash
[Unit]
Description=Tomcat Service
After=network.target

[Service]
Type=forking

Environment="JAVA_HOME=/usr/local/java/jdk1.8.0_161"
Environment="JAVA_OPTS=-Djava.security.egd=file:///dev/urandom -Djava.awt.headless=true"

Environment="CATALINA_BASE=/usr/local/apache-tomcat-8.5.61"
Environment="CATALINA_HOME=/usr/local/apache-tomcat-8.5.61"
Environment="CATALINA_PID=/usr/local/apache-tomcat-8.5.61/temp/tomcat.pid"
Environment="CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC"

ExecStart=/usr/local/apache-tomcat-8.5.61/bin/startup.sh
ExecStop=/usr/local/apache-tomcat-8.5.61/bin/shutdown.sh

[Install]
WantedBy=multi-user.target
```

将设置开机启动tomcat

```sh
sudo systemctl enable tomcat
```

启动tomcat

```sh
sudo systemctl start tomcat
```

在浏览器中访问localhost:8080

出现tomcat页面表示tomcat启动成功



检查的服务状态

```sh
sudo systemctl status tomcat
```

关闭tomcat

```sh
sudo systemctl stop tomcat
```



## nginx安装

1. 首先下载nginx-1.17.10.tar.gz安装包

2. 解压缩安装包

```sh
cd /usr/local
mkdir nginx
cd nginx
tar zxvf /home/hsb/LinuxSoftware/nginx-1.17.10.tar.gz -C ./
```

之后将在/usr/local/nginx目录下出现一个nginx-1.17.10的文件夹

3. 安装环境依赖

```sh
apt-get install gcc
apt-get install libpcre3 libpcre3-dev
apt-get install zlib1g zlib1g-dev
# Ubuntu的仓库中没有发现openssl-dev，由下面openssl和libssl-dev替代
#apt-get install openssl openssl-dev
sudo apt-get install openssl 
sudo apt-get install libssl-dev
```

4. 编译安装nginx

```sh
cd nginx-1.17.10
./configure
make && make install
```

5. 启动nginx

`/usr/local/nginx/sbin/nginx`

停止nginx服务

`/usr/local/nginx/sbin/nginx -s stop`

修改了配置文件后想重新加载Nginx

`/usr/local/nginx/sbin/nginx -s reload`

配置文件位于`/usr/local/nginx/conf/nginx.conf`

6. 验证

在浏览器中输入本地IP，出现`Welcome to nginx!`表示安装成功。



---

## 尝试Spring Boot结合Kafka的Demo

通过之前的配置，相应的软件在如下位置：

```sh
Kafka	/usr/local/kafka/kafka_2.12-2.5.0
zookeeper	/usr/local/zookeeper/apache-zookeeper-3.6.1-bin
jdk		/usr/local/java/jdk1.8.0_161
```

### 项目配置

为了便于开发，我在Ubuntu虚拟机中安装了IDEA，可以方便地创建springboot项目

项目配置文件 application.properties

对kafka和服务器端口进行配置

spring.kafka.bootstrap-servers后是本机IP地址的9092端口

为了保证正确运行，使用**sudo ufw disable命令**来关闭防火墙

```properties
#============== kafka ===================
# 指定kafka 代理地址，可以多个
spring.kafka.bootstrap-servers=192.168.133.130:9092
#=============== provider  =======================
spring.kafka.producer.retries=0
# 每次批量发送消息的数量
spring.kafka.producer.batch-size=16384
spring.kafka.producer.buffer-memory=33554432
# 指定消息key和消息体的编解码方式
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
#=============== consumer  =======================
# 指定默认消费者group id
spring.kafka.consumer.group-id=test-consumer-group
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.consumer.enable-auto-commit=true
spring.kafka.consumer.auto-commit-interval=100
# 指定消息key和消息体的编解码方式
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer

# 设置服务器端口号
server.port=8085
```

### Maven依赖

pom文件主要引入了spring-web、kafka、lombok、gson的依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.7.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.graduation</groupId>
    <artifactId>kafkademo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>kafkademo</name>
    <description>KafakaDemo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
        </dependency>
<!--        Lombok是一个可以通过简单的注解形式来帮助我们简化消除一些必须有但显得很臃肿的Java代码的工具，通过使用对应的注解，可以在编译源码的时候生成对应的方法。-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
<!--        GSON是Google提供的用来在Java对象和JSON数据之间进行映射的Java类库。可以将一个Json字符转成一个Java对象，或者将一个Java转化为Json字符串。-->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.2</version>
        </dependency>
        <!--spark相关依赖-->
        <dependency>
            <groupId>org.apache.kafka</groupId>
            <artifactId>kafka_2.12</artifactId>
            <version>2.5.0</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-kafka -->
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-core_2.12</artifactId>
            <version>2.4.7</version>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-streaming_2.12</artifactId>
            <version>2.4.7</version>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-streaming-kafka-0-10_2.12</artifactId>
            <version>2.4.7</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-configuration-processor</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

```

gson可以将一个Json字符转成一个Java对象，或者将一个Java转化为Json字符串，在Demo中用来对要发送的消息进行处理。

### 代码

项目结构如下：

./src/
├── main
     ├── java
     │   └── com
     │       └── graduation
     │           └── kafkademo
     │               ├── **beans**
     │               │   └── Message.java
     │               ├── **consumer**
     │                │   └── KafkaConsumer.java
     │                ├── KafkademoApplication.java
     │                └── **producer**
     │                   └── KafkaProducer.java
     └── resources

创建消息类Message.java，消息生产者类KafkaProducer.java，消息消费者类KafkaConsumer.java，用来尝试kafka的Producer API和Consumer API。

Message.java定义了要发送消息的格式

```java
package com.graduation.kafkademo.beans;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private Long id;    //id

    private String msg; //消息

    private Date sendTime;  //时间戳
}
```

KafkaProducer.java 

```java
package com.graduation.kafkademo.producer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.graduation.kafkademo.beans.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class KafkaProducer{

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Gson gson = new GsonBuilder().create();

    //发送消息方法
    public void send() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        log.info("+++++++++++++++++++++  message = {}", gson.toJson(message));
        kafkaTemplate.send("kafkademo", gson.toJson(message));
    }
}
```

Demo发送的消息是转换为json字符串的Message对象。

kafka发送消息使用kafkaTemplate.send("kafkademo", gson.toJson(message))

其中kafkademo是topic，在发送消息时会自动创建该topic，gson.toJson(message)即是要发送的消息。

KafkaConsumer.java

```java
package com.graduation.kafkademo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = {"kafkademo"})
    public void listen(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {

            Object message = kafkaMessage.get();

            log.info("----------------- record =" + record);
            log.info("------------------ message =" + message);
        }

    }
}
```

使用注解@KafkaListener(topics = {"kafkademo"})即可让consumer接收消息，其中的参数是要接受消息的topic，要和KafkaProducer.java 中的topic相同。

启动类 KafkademoApplication.java

```java
package com.graduation.kafkademo;

import com.graduation.kafkademo.producer.KafkaProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/*
参考博客：http://www.54tianzhisheng.cn/2018/01/05/SpringBoot-Kafka/
 */

@SpringBootApplication
public class KafkademoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(KafkademoApplication.class, args);

        KafkaProducer sender = context.getBean(KafkaProducer.class);

        for (int i = 0; i < 3; i++) {
            //调用消息发送类中的消息发送方法
            sender.send();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
```

在启动类中调用KafkaProducer.java中的方法进行测试，Demo中发送了三次消息。

发送消息和消费消息的内容将在日志中展现。

### 运行结果

启动Demo

1. 启动zookeeper

   在kafka安装目录下（/usr/local/kafka/kafka_2.12-2.5.0）

   使用

   ```sh
   bin/zookeeper-server-start.sh -daemon config/zookeeper.properties
   ```

   启动zookeeper服务

2. 启动kafka

   在kafka安装目录下（/usr/local/kafka/kafka_2.12-2.5.0）

   使用

   ```sh
   bin/kafka-server-start.sh  config/server.properties
   ```

   启动kafka服务

在IDEA中点击运行项目，可在Console中的日志查看Producer和Consumer发送、接收消息的过程。

```txt
2021-01-13 19:08:15.249  INFO 8461 --- [  restartedMain] c.g.kafkademo.producer.KafkaProducer     : +++++++++++++++++++++  message = {"id":1610536095248,"msg":"82e876d8-7b87-43c6-903c-4413b12f0f04","sendTime":"Jan 13, 2021 7:08:15 PM"}
2021-01-13 19:08:15.257  INFO 8461 --- [ntainer#0-0-C-1] c.g.kafkademo.consumer.KafkaConsumer     : ----------------- record =ConsumerRecord(topic = kafkademo, partition = 0, leaderEpoch = 0, offset = 5, CreateTime = 1610536095250, serialized key size = -1, serialized value size = 102, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = {"id":1610536095248,"msg":"82e876d8-7b87-43c6-903c-4413b12f0f04","sendTime":"Jan 13, 2021 7:08:15 PM"})
2021-01-13 19:08:15.258  INFO 8461 --- [ntainer#0-0-C-1] c.g.kafkademo.consumer.KafkaConsumer     : ------------------ message ={"id":1610536095248,"msg":"82e876d8-7b87-43c6-903c-4413b12f0f04","sendTime":"Jan 13, 2021 7:08:15 PM"}

```

其中一条消息的产生和发送如上。

在kafka安装目录下运行命令

```sh
bin/kafka-topics.sh --list --zookeeper localhost:2181
```

可发现topic kafkademo已经被自动创建。
