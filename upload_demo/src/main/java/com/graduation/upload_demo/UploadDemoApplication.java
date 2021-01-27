package com.graduation.upload_demo;

import com.graduation.upload_demo.servlet.Combine;
import com.graduation.upload_demo.servlet.Upload;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UploadDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadDemoApplication.class, args);
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
