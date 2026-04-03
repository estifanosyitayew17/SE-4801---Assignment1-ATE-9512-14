// Estifanos Yitayew - ATE/9512/14
package com.shopwave.config;

import com.shopwave.model.Category;
import com.shopwave.model.Product;
import com.shopwave.repository.CategoryRepository;
import com.shopwave.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        
        // Create Categories
        Category electronics = Category.builder()
                .name("Electronics")
                .description("Electronic devices and gadgets")
                .build();
        
        Category books = Category.builder()
                .name("Books")
                .description("All kinds of books and publications")
                .build();
        
        Category clothing = Category.builder()
                .name("Clothing")
                .description("Fashion and apparel")
                .build();
        
        categoryRepository.save(electronics);
        categoryRepository.save(books);
        categoryRepository.save(clothing);
        
        // Create Products - Electronics
        Product laptop = Product.builder()
                .name("Gaming Laptop")
                .description("High performance laptop with RTX graphics")
                .price(new BigDecimal("1499.99"))
                .stock(10)
                .category(electronics)
                .build();
        
        Product smartphone = Product.builder()
                .name("Smartphone Pro")
                .description("Latest model with amazing camera")
                .price(new BigDecimal("999.99"))
                .stock(25)
                .category(electronics)
                .build();
        
        Product headphones = Product.builder()
                .name("Wireless Headphones")
                .description("Noise cancelling bluetooth headphones")
                .price(new BigDecimal("199.99"))
                .stock(50)
                .category(electronics)
                .build();
        
        // Create Products - Books
        Product javaBook = Product.builder()
                .name("Java Programming")
                .description("Complete guide to Java 21")
                .price(new BigDecimal("59.99"))
                .stock(100)
                .category(books)
                .build();
        
        Product springBook = Product.builder()
                .name("Spring Boot Mastery")
                .description("Build enterprise applications")
                .price(new BigDecimal("49.99"))
                .stock(75)
                .category(books)
                .build();
        
        // Create Products - Clothing
        Product tshirt = Product.builder()
                .name("Cotton T-Shirt")
                .description("100% cotton, available in multiple colors")
                .price(new BigDecimal("19.99"))
                .stock(200)
                .category(clothing)
                .build();
        
        Product jeans = Product.builder()
                .name("Denim Jeans")
                .description("Classic fit blue jeans")
                .price(new BigDecimal("49.99"))
                .stock(150)
                .category(clothing)
                .build();
        
        // Save all products
        productRepository.save(laptop);
        productRepository.save(smartphone);
        productRepository.save(headphones);
        productRepository.save(javaBook);
        productRepository.save(springBook);
        productRepository.save(tshirt);
        productRepository.save(jeans);
        
        System.out.println("========================================");
        System.out.println("Sample Data Loaded Successfully!");
        System.out.println("========================================");
        System.out.println("Categories: Electronics, Books, Clothing");
        System.out.println("Products: " + productRepository.count() + " products");
        System.out.println("========================================");
    }
}