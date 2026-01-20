package com.romit.ecommerce.repository;

import com.romit.ecommerce.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByNameIgnoreCase(String name);
}
