package com.romit.ecommerce.repository;

import com.romit.ecommerce.model.OrderItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderItemRepository extends MongoRepository<OrderItem, String> {

    List<OrderItem> findByOrderId(String orderId);
}
