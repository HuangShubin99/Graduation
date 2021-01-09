package com.graduation.demo.control;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.graduation.demo.spark.SparkService;

@RestController
public class DemoControl {
    @Autowired
    private SparkService sparkService;

    @RequestMapping("/demo/spark")
    public Map<String, Object> sparkDemo() {
        return sparkService.sparkDemo();
    }
}
