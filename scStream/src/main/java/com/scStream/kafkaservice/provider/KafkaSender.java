package com.scStream.kafkaservice.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.scStream.kafkaservice.beans.Message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.UUID;

import java.io.*;

@Service
@Slf4j
public class KafkaSender {
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Value("${like.topic}")
  private String topic;

  private Gson gson = new GsonBuilder().create();

  public void send() {
    Message message = new Message();
    // 这部分你们传完文件后，改写下
    try {
      File file = new File("/mnt/spark/10k_pbmc/100m/pbmc_100m_v3_1.fastq");
      if (file.isFile() && file.exists()) {
        InputStreamReader read = new InputStreamReader(new FileInputStream(file));
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        long readNums = 0;
        StringBuilder sb = new StringBuilder();
        while ((lineTxt = bufferedReader.readLine()) != null) {
          // 10000为暂时的每块大小
          message.setId(readNums);
          for (int tmp = 0; tmp < 10000 && lineTxt != null; tmp++, readNums++) {
            sb.append(lineTxt+"\n");
            lineTxt =bufferedReader.readLine();
          }
          message.setMsg(sb.toString());
          sb.delete(0, sb.length());
          message.setSendTime(new Date());
          kafkaTemplate.send(topic, gson.toJson(message));
        }
        
        bufferedReader.close();
        read.close();
      } else {
        System.out.println("can't find file");
      }
    } catch (Exception e) {
      System.out.println("have error");
      e.printStackTrace();
    }
  }
}