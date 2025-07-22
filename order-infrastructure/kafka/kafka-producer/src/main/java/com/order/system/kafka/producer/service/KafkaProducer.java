package com.order.system.kafka.producer.service;

import com.google.common.util.concurrent.ListenableFuture;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase> {
//    void send(String topicName, K key, V message, CompletableFuture<SendResult<K, V>> callback);

    void send(String topicName, K key, V message, CompletableFuture<SendResult<K, V>> callback);
}
