//package com.ecommerce.inventory.servicetests;
//
//import com.ecommerce.inventory.entities.Product;
//import com.ecommerce.inventory.repositories.ProductRepository;
//import com.ecommerce.inventory.services.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class ProductServiceTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    private ProductService productService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetProducts() {
//        Product p1 = new Product();
//        Product p2 = new Product();
//        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));
//
//        var result = productService.getProducts();
//
//        assertEquals(2, result.size());
//        verify(productRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetProductByIdSuccess() {
//        Product product = new Product();
//        product.setId(1L);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        Product result = productService.getProductById(1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getId());
//    }
//
//    @Test
//    void testGetProductByIdNotFound() {
//        when(productRepository.findById(1L)).thenReturn(Optional.empty());
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> productService.getProductById(1L));
//        assertEquals("Product not found", ex.getMessage());
//    }
//
//    @Test
//    void testCreateProduct() {
//        Product product = new Product();
//        product.setName("Laptop");
//
//        when(productRepository.save(product)).thenAnswer(invocation -> invocation.getArgument(0));
//
//        Product result = productService.createProduct(product);
//
//        assertNotNull(result.getUpdatedAt());
//        assertEquals("Laptop", result.getName());
//        verify(productRepository, times(1)).save(product);
//    }
//
//    @Test
//    void testUpdateProductSuccess() {
//        Product existing = new Product();
//        existing.setId(1L);
//        existing.setName("Old Laptop");
//        existing.setDescription("Old Desc");
//        existing.setCategory("Old Cat");
//        existing.setPrice(BigDecimal.valueOf(1000));
//
//        Product update = new Product();
//        update.setId(1L);
//        update.setName("New Laptop");
//        update.setDescription("New Desc");
//        update.setCategory("New Cat");
//        update.setPrice(BigDecimal.valueOf(1200));
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
//        when(productRepository.save(existing)).thenAnswer(invocation -> invocation.getArgument(0));
//
//        Product result = productService.updateProduct(update);
//
//        assertEquals("New Laptop", result.getName());
//        assertEquals("New Desc", result.getDescription());
//        assertEquals("New Cat", result.getCategory());
//        assertEquals(BigDecimal.valueOf(1200), result.getPrice());
//    }
//
//    @Test
//    void testUpdateProductNotFound() {
//        Product update = new Product();
//        update.setId(1L);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.empty());
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> productService.updateProduct(update));
//        assertEquals("Product not found", ex.getMessage());
//    }
//
//    @Test
//    void testDeleteProductSuccess() {
//        Product product = new Product();
//        product.setId(1L);
//        product.setActive(true);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        productService.deleteProduct(1L);
//
//        assertFalse(product.getActive());
//        verify(productRepository, times(1)).save(product);
//    }
//
//    @Test
//    void testDeleteProductNotFound() {
//        when(productRepository.findById(1L)).thenReturn(Optional.empty());
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> productService.deleteProduct(1L));
//        assertEquals("Product not found", ex.getMessage());
//    }
//}
//
