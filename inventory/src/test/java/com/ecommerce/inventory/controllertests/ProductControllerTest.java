//package com.ecommerce.inventory.controllertests;
//
//import com.ecommerce.inventory.controllers.ProductController;
//import com.ecommerce.inventory.entities.Product;
//import com.ecommerce.inventory.services.ProductService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ProductController.class)
//@Import(ProductControllerTest.TestConfig.class)
//class ProductControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Product product1;
//    private Product product2;
//
//    @BeforeEach
//    void setUp() {
//        Mockito.reset(productService);
//
//        product1 = new Product();
//        product1.setId(1L);
//        product1.setName("Laptop");
//        product1.setDescription("High-end laptop");
//        product1.setCategory("Electronics");
//        product1.setPrice(BigDecimal.valueOf(1500));
//        product1.setUpdatedAt(LocalDateTime.now());
//
//        product2 = new Product();
//        product2.setId(2L);
//        product2.setName("Smartphone");
//        product2.setDescription("Latest model");
//        product2.setCategory("Electronics");
//        product2.setPrice(BigDecimal.valueOf(800));
//        product2.setUpdatedAt(LocalDateTime.now());
//    }
//
//    @Test
//    void testGetProducts() throws Exception {
//        when(productService.getProducts()).thenReturn(Arrays.asList(product1, product2));
//
//        mockMvc.perform(get("/products"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].name").value("Laptop"))
//                .andExpect(jsonPath("$[1].name").value("Smartphone"));
//    }
//
//    @Test
//    void testGetProductById() throws Exception {
//        when(productService.getProductById(1L)).thenReturn(product1);
//
//        mockMvc.perform(get("/products/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Laptop"));
//    }
//
//    @Test
//    void testCreateProduct() throws Exception {
//        when(productService.createProduct(any(Product.class))).thenReturn(product1);
//
//        mockMvc.perform(post("/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(product1)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Laptop"));
//    }
//
//    @Test
//    void testUpdateProduct() throws Exception {
//        Product updatedProduct = new Product();
//        updatedProduct.setId(1L);
//        updatedProduct.setName("Updated Laptop");
//        updatedProduct.setDescription("Updated description");
//        updatedProduct.setCategory("Electronics");
//        updatedProduct.setPrice(BigDecimal.valueOf(1600));
//
//        when(productService.updateProduct(any(Product.class))).thenReturn(updatedProduct);
//
//        mockMvc.perform(patch("/products/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedProduct)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Laptop"))
//                .andExpect(jsonPath("$.price").value(1600));
//    }
//
//    @Test
//    void testDeleteProduct() throws Exception {
//        doNothing().when(productService).deleteProduct(1L);
//
//        mockMvc.perform(delete("/products/1"))
//                .andExpect(status().isOk());
//
//        verify(productService, times(1)).deleteProduct(1L);
//    }
//
//    /**
//     * Test configuration to provide a mocked ProductService
//     * without using the deprecated @MockBean.
//     */
//    @TestConfiguration
//    static class TestConfig {
//        @Bean
//        public ProductService productService() {
//            return mock(ProductService.class);
//        }
//    }
//}
//
