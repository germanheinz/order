//package com.order.system.stock.service.messaging.publisher.kafka;
//
//import com.order.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
//import com.order.system.kafka.producer.KafkaMessageHelper;
//import com.order.system.kafka.producer.service.KafkaProducer;
//import com.order.system.stock.service.domain.config.RestaurantServiceConfigData;
//import com.order.system.stock.service.domain.event.OrderRejectedEvent;
//import com.order.system.stock.service.messaging.mapper.RestaurantMessagingDataMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class OrderRejectedKafkaMessagePublisher implements OrderRejectedMessagePublisher {
//
//    private final RestaurantMessagingDataMapper restaurantMessagingDataMapper;
//    private final KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer;
//    private final RestaurantServiceConfigData restaurantServiceConfigData;
//    private final KafkaMessageHelper kafkaMessageHelper;
//
//    public OrderRejectedKafkaMessagePublisher(RestaurantMessagingDataMapper restaurantMessagingDataMapper,
//                                              KafkaProducer<String, RestaurantApprovalResponseAvroModel> kafkaProducer,
//                                              RestaurantServiceConfigData restaurantServiceConfigData,
//                                              KafkaMessageHelper kafkaMessageHelper) {
//        this.restaurantMessagingDataMapper = restaurantMessagingDataMapper;
//        this.kafkaProducer = kafkaProducer;
//        this.restaurantServiceConfigData = restaurantServiceConfigData;
//        this.kafkaMessageHelper = kafkaMessageHelper;
//    }
//
//    @Override
//    public void publish(OrderRejectedEvent orderRejectedEvent) {
//        String orderId = orderRejectedEvent.getOrderApproval().getOrderId().getValue().toString();
//
//        log.info("Received OrderRejectedEvent for order id: {}", orderId);
//
//        try {
//            RestaurantApprovalResponseAvroModel restaurantApprovalResponseAvroModel =
//                    restaurantMessagingDataMapper
//                            .orderRejectedEventToRestaurantApprovalResponseAvroModel(orderRejectedEvent);
//
//            kafkaProducer.send(restaurantServiceConfigData.getRestaurantApprovalResponseTopicName(),
//                    orderId,
//                    restaurantApprovalResponseAvroModel,
//                    kafkaMessageHelper.getKafkaCallback(restaurantServiceConfigData
//                                    .getRestaurantApprovalResponseTopicName(),
//                            restaurantApprovalResponseAvroModel,
//                            orderId,
//                            "RestaurantApprovalResponseAvroModel"));
//
//            log.info("RestaurantApprovalResponseAvroModel sent to kafka at: {}", System.nanoTime());
//        } catch (Exception e) {
//            log.error("Error while sending RestaurantApprovalResponseAvroModel message" +
//                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
//        }
//    }
//
//}
