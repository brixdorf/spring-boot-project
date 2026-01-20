package com.romit.ecommerce.repository;

import com.romit.ecommerce.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartRepository extends MongoRepository<CartItem, String> {

    List<CartItem> findByUserId(String userId);

    void deleteByUserId(String userId);
}
