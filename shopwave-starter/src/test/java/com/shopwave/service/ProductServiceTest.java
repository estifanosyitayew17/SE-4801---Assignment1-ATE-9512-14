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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Category testCategory;
    private Product testProduct;
    private ProductDTO testProductDTO;
    private CreateProductRequest testRequest;

    @BeforeEach
    void setUp() {
        // Setup test data
        testCategory = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic devices")
                .build();

        testProduct = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming laptop")
                .price(new BigDecimal("1499.99"))
                .stock(10)
                .category(testCategory)
                .build();

        testProductDTO = ProductDTO.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming laptop")
                .price(new BigDecimal("1499.99"))
                .stock(10)
                .categoryId(1L)
                .categoryName("Electronics")
                .build();

        testRequest = new CreateProductRequest();
        testRequest.setName("Laptop");
        testRequest.setDescription("Gaming laptop");
        testRequest.setPrice(new BigDecimal("1499.99"));
        testRequest.setStock(10);
        testRequest.setCategoryId(1L);
    }

    @Test
    void testCreateProduct_Success() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productMapper.toEntity(testRequest, testCategory)).thenReturn(testProduct);
        when(productRepository.save(testProduct)).thenReturn(testProduct);
        when(productMapper.toDTO(testProduct)).thenReturn(testProductDTO);

        // When
        ProductDTO result = productService.createProduct(testRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Laptop");
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void testCreateProduct_CategoryNotFound() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.createProduct(testRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Category not found with id: 1");
    }

    @Test
    void testGetProductById_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productMapper.toDTO(testProduct)).thenReturn(testProductDTO);

        // When
        ProductDTO result = productService.getProductById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Laptop");
    }

    @Test
    void testGetProductById_NotFound() {
        // Given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.getProductById(999L))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found with id: 999");
    }

    @Test
    void testUpdateStock_Success() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(testProduct)).thenReturn(testProduct);
        when(productMapper.toDTO(testProduct)).thenReturn(testProductDTO);

        // When - Sell 2 units (delta = -2)
        ProductDTO result = productService.updateStock(1L, -2);

        // Then
        assertThat(result).isNotNull();
        assertThat(testProduct.getStock()).isEqualTo(8); // Stock decreased from 10 to 8
    }

    @Test
    void testUpdateStock_NegativeStock_ThrowsException() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        // Current stock is 10, try to sell 20 units (delta = -20)

        // When & Then
        assertThatThrownBy(() -> productService.updateStock(1L, -20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Stock cannot be negative");
    }

    @Test
    void testGetAllProducts_Paginated() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(testProduct));
        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toDTO(testProduct)).thenReturn(testProductDTO);

        // When
        Page<ProductDTO> result = productService.getAllProducts(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Laptop");
    }
}