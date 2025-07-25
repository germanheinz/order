package com.order.system.data.restaurant.mapper;


import com.order.system.common.data.restaurant.entity.RestaurantEntity;
import com.order.system.common.data.restaurant.exception.RestaurantDataAccessException;
import com.order.system.domain.core.entity.Product;
import com.order.system.domain.core.entity.Restaurant;
import com.order.system.domain.valueobject.Money;
import com.order.system.domain.valueobject.ProductId;
import com.order.system.domain.valueobject.RestaurantId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(product -> UUID.fromString(product.getId().toString()))
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity =
                restaurantEntities.stream().findFirst().orElseThrow(() ->
                        new RestaurantDataAccessException("Restaurant could not be found!"));

        List<Product> restaurantProducts = restaurantEntities.stream().map(entity ->
                new Product(new ProductId(entity.getProductId()), entity.getProductName(),
                        new Money(entity.getProductPrice()))).toList();

        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}
