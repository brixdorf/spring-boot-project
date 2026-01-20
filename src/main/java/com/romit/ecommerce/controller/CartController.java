package com.romit.ecommerce.controller;

import com.romit.ecommerce.dto.AddToCartRequest;
import com.romit.ecommerce.model.CartItem;
import com.romit.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    @PostMapping("/add")
    public CartItem addToCart(@RequestBody AddToCartRequest request) {
        return cartService.addToCart(request.getUserId(), request.getProductId(), request.getQuantity());
    }

    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable String userId) {
        return cartService.getCartByUserId(userId);
    }

    @DeleteMapping("/{userId}/clear")
    public Map<String, String> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cart cleared successfully");
        return response;
    }
}
