package com.order.system.data.order.mapper;


import com.order.system.domain.core.entity.Order;
import com.order.system.domain.core.entity.OrderItem;
import com.order.system.domain.core.entity.Product;
import com.order.system.domain.core.valueobject.OrderItemId;
import com.order.system.domain.core.valueobject.StreetAddress;
import com.order.system.domain.core.valueobject.TrackingId;
import com.order.system.domain.valueobject.*;
import com.order.system.data.order.entity.OrderAddressEntity;
import com.order.system.data.order.entity.OrderItemEntity;
import com.order.system.data.order.entity.OrderEntity;



import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.order.system.domain.core.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Component
public class OrderDataAccessMapper {

        public OrderEntity orderToOrderEntity(Order order) {
            OrderEntity orderEntity = OrderEntity.builder()
                    .id(order.getId().getValue())
                    .customerId(order.getCustomerId().getValue())
                    .stockId(order.getStockId().getValue())
                    .trackingId(order.getTrackingId().getValue())
                    .address(deliveryAddressToAddressEntity(order.getDeliveryAddress()))
                    .price(order.getPrice().getAmount())
                    .items(orderItemsToOrderItemEntities(order.getItems()))
                    .orderStatus(order.getOrderStatus())
                    .failureMessages(order.getFailureMessages() != null ?
                            String.join(FAILURE_MESSAGE_DELIMITER, order.getFailureMessages()) : "")
                    .build();
            orderEntity.getAddress().setOrder(orderEntity);
            orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));

            return orderEntity;
        }

        public Order orderEntityToOrder(OrderEntity orderEntity) {
            return Order.builder()
                    .orderId(new OrderId(orderEntity.getId()))
                    .customerId(new CustomerId(orderEntity.getCustomerId()))
                    .stockId(new StockId(orderEntity.getStockId()))
                    .deliveryAddress(addressEntityToDeliveryAddress(orderEntity.getAddress()))
                    .price(new Money(orderEntity.getPrice()))
                    .items(orderItemEntitiesToOrderItems(orderEntity.getItems()))
                    .trackingId(new TrackingId(orderEntity.getTrackingId()))
                    .orderStatus(orderEntity.getOrderStatus())
                    .failureMessages(orderEntity.getFailureMessages().isEmpty() ? new ArrayList<>() :
                            new ArrayList<>(Arrays.asList(orderEntity.getFailureMessages()
                                    .split(FAILURE_MESSAGE_DELIMITER))))
                    .build();
        }

        private List<OrderItem> orderItemEntitiesToOrderItems(List<OrderItemEntity> items) {
            return items.stream()
                    .map(orderItemEntity -> OrderItem.builder()
                            .orderItemId(new OrderItemId(orderItemEntity.getId()))
                            .product(new Product(new ProductId(orderItemEntity.getProductId())))
                            .price(new Money(orderItemEntity.getPrice()))
                            .quantity(orderItemEntity.getQuantity())
                            .subTotal(new Money(orderItemEntity.getSubTotal()))
                            .build())
                    .collect(Collectors.toList());
        }

        private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {
            return new StreetAddress(address.getId(),
                    address.getStreet(),
                    address.getPostalCode(),
                    address.getCity());
        }

        private List<OrderItemEntity> orderItemsToOrderItemEntities(List<OrderItem> items) {
            return items.stream()
                    .map(orderItem -> OrderItemEntity.builder()
                            .id(orderItem.getId().getValue())
                            .productId(orderItem.getProduct().getId().getValue())
                            .price(orderItem.getPrice().getAmount())
                            .quantity(orderItem.getQuantity())
                            .subTotal(orderItem.getSubTotal().getAmount())
                            .build())
                    .collect(Collectors.toList());
        }

        private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
            return OrderAddressEntity.builder()
                    .id(deliveryAddress.getId())
                    .street(deliveryAddress.getStreet())
                    .postalCode(deliveryAddress.getPostalCode())
                    .city(deliveryAddress.getCity())
                    .build();
        }
    }
