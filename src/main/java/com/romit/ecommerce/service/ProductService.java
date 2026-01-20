package com.romit.ecommerce.service;

import com.romit.ecommerce.model.Product;
import com.romit.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public List<Product> searchProducts(String query) {
        return productRepository.findByNameIgnoreCase(query);
    }

}
