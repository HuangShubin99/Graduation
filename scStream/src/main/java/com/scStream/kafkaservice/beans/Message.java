package com.scStream.kafkaservice.beans;

import lombok.Data;

import java.util.Date;

/**
 * Lombok注解，注解在类上，提供类所有属性的get和set方法，同时提供equals, canEqual, hashCode, toString
 */
@Data
public class Message {
  private Long id;

  private String msg;

  private Date sendTime;
}