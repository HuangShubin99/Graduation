package com.scStream;

import lombok.RequiredArgsConstructor;
import com.scStream.servlet.Combine;
import com.scStream.servlet.Upload;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/** 
 实际上相当于:
 @Configuration:JavaConfig配置类
   @Bean:任何一个标注@Bean的方法，返回值将作为一个bean定义注册到Sprin的IoC容器，方法名默认成
   该bean定义的id
 @EnableAutoConfiguration:自动扫描并加载符合条件的组件或者bean定义，最终将bean定义加载到IoC
   容器中
 @ComponentScan:将所有符合自动配置条件的bean定义加载到IoC容器中
 */
@SpringBootApplication
public class Main {
  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(
      Main.class, args
    );
  }
    //定义servlet，注册
    @Bean
    public ServletRegistrationBean Upload(){
        //指定访问的url
        return new ServletRegistrationBean(new Upload(),"/Upload");
    }

    @Bean
    public ServletRegistrationBean Combine(){
        return new ServletRegistrationBean(new Combine(),"/Combine");
    }
}