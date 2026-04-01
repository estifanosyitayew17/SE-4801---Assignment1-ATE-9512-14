// Estifanos Yitayew - ATE/9512/14
package com.shopwave.repository;

import com.shopwave.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find products by category ID
    List<Product> findByCategoryId(Long categoryId);
    
    // Find products with price less than or equal to maxPrice
    List<Product> findByPriceLessThanEqual(BigDecimal maxPrice);
    
    // Find products containing keyword in name (case insensitive)
    List<Product> findByNameContainingIgnoreCase(String keyword);
    
    // Find most expensive product
    Optional<Product> findTopByOrderByPriceDesc();
}