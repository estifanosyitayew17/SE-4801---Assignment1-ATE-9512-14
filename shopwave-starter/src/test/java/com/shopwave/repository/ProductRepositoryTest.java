// Estifanos Yitayew - ATE/9512/14
package com.shopwave.repository;

import com.shopwave.model.Category;
import com.shopwave.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Create a test category
        testCategory = Category.builder()
                .name("Test Electronics")
                .description("Test category description")
                .build();
        categoryRepository.save(testCategory);

        // Create a test product
        testProduct = Product.builder()
                .name("Test Laptop")
                .description("Test laptop description")
                .price(new BigDecimal("999.99"))
                .stock(10)
                .category(testCategory)
                .build();
        productRepository.save(testProduct);
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        // When
        List<Product> products = productRepository.findByNameContainingIgnoreCase("laptop");

        // Then
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getName()).containsIgnoringCase("laptop");
    }

    @Test
    void testFindByPriceLessThanEqual() {
        // When
        List<Product> products = productRepository.findByPriceLessThanEqual(new BigDecimal("500.00"));

        // Then
        assertThat(products).isEmpty(); // Our test product is $999.99, so shouldn't be found
    }

    @Test
    void testFindTopByOrderByPriceDesc() {
        // When
        Product mostExpensive = productRepository.findTopByOrderByPriceDesc().orElse(null);

        // Then
        assertThat(mostExpensive).isNotNull();
        assertThat(mostExpensive.getPrice()).isEqualByComparingTo(new BigDecimal("999.99"));
    }
}