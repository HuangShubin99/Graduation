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
