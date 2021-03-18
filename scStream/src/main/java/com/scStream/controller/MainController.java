package com.scStream.controller;

import com.scStream.kafkaservice.provider.KafkaSender;
import com.scStream.sparkservice.SparkMain;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MainController {

  private final KafkaSender kafkaSender;
  private final SparkMain sparkMain;

  @RequestMapping("/kafkasend")
  public void kafkasend() {
    kafkaSender.send();
  }

  @RequestMapping("/startspark")
  public void startspark() {
    sparkMain.launch();
  }
}