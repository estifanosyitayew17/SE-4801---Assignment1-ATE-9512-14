// Estifanos Yitayew - ATE/9512/14
package com.shopwave.service;

import com.shopwave.dto.CreateProductRequest;
import com.shopwave.dto.ProductDTO;
import com.shopwave.exception.ProductNotFoundException;
import com.shopwave.mapper.ProductMapper;
import com.shopwave.model.Category;
import com.shopwave.model.Product;
import com.shopwave.repository.CategoryRepository;
import com.shopwave.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    // Create a new product
    public ProductDTO createProduct(CreateProductRequest request) {
        // Find the category or throw exception
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
        
        // Convert request to entity
        Product product = productMapper.toEntity(request, category);
        
        // Save to database
        Product savedProduct = productRepository.save(product);
        
        // Return DTO
        return productMapper.toDTO(savedProduct);
    }

    // Get all products with pagination
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDTO);
    }

    // Get product by ID
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toDTO(product);
    }

    // Search products by keyword and/or max price
    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String keyword, BigDecimal maxPrice) {
        List<Product> products;
        
        // Case 1: Both keyword and maxPrice provided
        if (keyword != null && !keyword.isEmpty() && maxPrice != null) {
            products = productRepository.findByNameContainingIgnoreCase(keyword).stream()
                    .filter(p -> p.getPrice().compareTo(maxPrice) <= 0)
                    .collect(Collectors.toList());
        } 
        // Case 2: Only keyword provided
        else if (keyword != null && !keyword.isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(keyword);
        } 
        // Case 3: Only maxPrice provided
        else if (maxPrice != null) {
            products = productRepository.findByPriceLessThanEqual(maxPrice);
        } 
        // Case 4: No filters - return all
        else {
            products = productRepository.findAll();
        }
        
        return products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Update product stock (positive or negative delta)
    public ProductDTO updateStock(Long id, int delta) {
        // Find product or throw exception
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        
        // Calculate new stock
        int newStock = product.getStock() + delta;
        
        // Validate stock cannot be negative
        if (newStock < 0) {
            throw new IllegalArgumentException(
                "Stock cannot be negative. Current stock: " + 
                product.getStock() + ", Delta: " + delta
            );
        }
        
        // Update stock
        product.setStock(newStock);
        
        // Save and return
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDTO(updatedProduct);
    }
}