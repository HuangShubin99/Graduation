// package com.scStream.kafkaservice.consumer;

// import lombok.extern.slf4j.Slf4j;
// import org.apache.kafka.clients.consumer.ConsumerRecord;
// import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Service;

// import java.util.Optional;

// @Service
// @Slf4j
// public class KafkaReceiver {

//     @KafkaListener(topics = {"scFastq"})
//     public void listen(ConsumerRecord<?, ?> record) {

//         Optional<?> kafkaMessage = Optional.ofNullable(record.value());

//         if (kafkaMessage.isPresent()) {

//             Object message = kafkaMessage.get();

//             log.info("----------------- record = " + record);
//             log.info("----------------- message = " + message);
//         }

//     }
// }