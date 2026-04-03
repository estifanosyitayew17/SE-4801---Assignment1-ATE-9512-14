// Esrifanos Yitayew - ATE/9512/14
package com.shopwave.controller;

import com.shopwave.dto.CreateProductRequest;
import com.shopwave.dto.ProductDTO;
import com.shopwave.exception.ProductNotFoundException;
import com.shopwave.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void testGetAllProducts_Returns200() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming laptop")
                .price(new BigDecimal("1499.99"))
                .stock(10)
                .categoryId(1L)
                .categoryName("Electronics")
                .build();
        
        Page<ProductDTO> productPage = new PageImpl<>(List.of(productDTO), pageable, 1);
        
        when(productService.getAllProducts(any(Pageable.class))).thenReturn(productPage);

        // When & Then
        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Laptop")))
                .andExpect(jsonPath("$.content[0].price", is(1499.99)));
    }

    @Test
    void testGetProductById_Returns200() throws Exception {
        // Given
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming laptop")
                .price(new BigDecimal("1499.99"))
                .stock(10)
                .categoryId(1L)
                .categoryName("Electronics")
                .build();
        
        when(productService.getProductById(1L)).thenReturn(productDTO);

        // When & Then
        mockMvc.perform(get("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.price", is(1499.99)));
    }

    @Test
    void testGetProductById_Returns404() throws Exception {
        // Given
        when(productService.getProductById(999L)).thenThrow(new ProductNotFoundException(999L));

        // When & Then
        mockMvc.perform(get("/api/products/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", containsString("Product not found with id: 999")));
    }

    @Test
    void testCreateProduct_Returns201() throws Exception {
        // Given
        String requestJson = """
                {
                    "name": "New Product",
                    "description": "Brand new product",
                    "price": 299.99,
                    "stock": 50,
                    "categoryId": 1
                }
                """;
        
        ProductDTO createdProduct = ProductDTO.builder()
                .id(10L)
                .name("New Product")
                .description("Brand new product")
                .price(new BigDecimal("299.99"))
                .stock(50)
                .categoryId(1L)
                .categoryName("Electronics")
                .build();
        
        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(createdProduct);

        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.name", is("New Product")))
                .andExpect(jsonPath("$.price", is(299.99)));
    }

    @Test
    void testCreateProduct_ValidationError_Returns400() throws Exception {
        // Given - Missing name and negative price
        String invalidRequestJson = """
                {
                    "name": "",
                    "price": -100,
                    "stock": -5,
                    "categoryId": null
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation Failed")));
    }

    @Test
    void testSearchProducts_Returns200() throws Exception {
        // Given
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .name("Gaming Laptop")
                .description("High performance laptop")
                .price(new BigDecimal("1499.99"))
                .stock(10)
                .categoryId(1L)
                .categoryName("Electronics")
                .build();
        
        when(productService.searchProducts(any(), any())).thenReturn(List.of(productDTO));

        // When & Then
        mockMvc.perform(get("/api/products/search")
                .param("keyword", "laptop")
                .param("maxPrice", "2000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Gaming Laptop")));
    }

    @Test
    void testUpdateStock_Returns200() throws Exception {
        // Given
        String requestJson = """
                {
                    "delta": -2
                }
                """;
        
        ProductDTO updatedProduct = ProductDTO.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming laptop")
                .price(new BigDecimal("1499.99"))
                .stock(8)  // Stock decreased from 10 to 8
                .categoryId(1L)
                .categoryName("Electronics")
                .build();
        
        when(productService.updateStock(eq(1L), eq(-2))).thenReturn(updatedProduct);

        // When & Then
        mockMvc.perform(patch("/api/products/1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock", is(8)));
    }

    @Test
    void testUpdateStock_InvalidDelta_Returns400() throws Exception {
        // Given
        String requestJson = """
                {
                    "delta": -100
                }
                """;
        
        when(productService.updateStock(eq(1L), eq(-100)))
                .thenThrow(new IllegalArgumentException("Stock cannot be negative. Current stock: 10, Delta: -100"));

        // When & Then
        mockMvc.perform(patch("/api/products/1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", containsString("Stock cannot be negative")));
    }
}