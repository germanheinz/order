package com.order.system.application.service;


import com.order.system.application.service.dto.message.RestaurantApprovalResponse;
import com.order.system.application.service.ports.output.message.publisher.payment.OrderCancelPaymentRequestMessagePublisher;
import com.order.system.config.kafka.SagaStep;
import com.order.system.domain.core.OrderDomainService;
import com.order.system.domain.core.entity.Order;
import com.order.system.domain.core.event.OrderCancelledEvent;
import com.order.system.domain.event.EmptyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse, EmptyEvent, OrderCancelledEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final OrderCancelPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher;


    public OrderApprovalSaga(OrderDomainService orderDomainService, OrderSagaHelper orderSagaHelper, OrderCancelPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher) {
        this.orderDomainService = orderDomainService;
        this.orderSagaHelper = orderSagaHelper;
        this.orderCancelledPaymentRequestMessagePublisher = orderCancelledPaymentRequestMessagePublisher;
    }

    @Override
    @Transactional
    public EmptyEvent process(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Approving order with id: {}", restaurantApprovalResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        orderDomainService.approveOrder(order);
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} is approved", order.getId().getValue());
        return EmptyEvent.INSTANCE;
    }

    @Override
    @Transactional
    public OrderCancelledEvent rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
        log.info("Cancelling order with id: {}", restaurantApprovalResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(restaurantApprovalResponse.getOrderId());
        OrderCancelledEvent domainEvent = orderDomainService.cancelOrderPayment(order,
                restaurantApprovalResponse.getFailureMessages(), orderCancelledPaymentRequestMessagePublisher);
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} is cancelling", order.getId().getValue());
        return domainEvent;
    }
}
