//package com.ecommerce.inventory.servicetests;
//
//import com.ecommerce.inventory.entities.Inventory;
//import com.ecommerce.inventory.entities.Product;
//import com.ecommerce.inventory.repositories.InventoryRepository;
//import com.ecommerce.inventory.repositories.ProductRepository;
//import com.ecommerce.inventory.services.InventoryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class InventoryServiceTest {
//
//    @Mock
//    private InventoryRepository inventoryRepository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    private InventoryService inventoryService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetInventory() {
//        Inventory inv1 = new Inventory();
//        Inventory inv2 = new Inventory();
//        when(inventoryRepository.findAll()).thenReturn(Arrays.asList(inv1, inv2));
//
//        List<Inventory> result = inventoryService.getInventory();
//
//        assertEquals(2, result.size());
//        verify(inventoryRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testCreateInventorySuccess() {
//        Product product = new Product();
//        product.setId(1L);
//        Inventory inventory = new Inventory();
//        inventory.setProduct(product);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(inventoryRepository.save(inventory)).thenReturn(inventory);
//
//        Inventory result = inventoryService.createInventory(inventory);
//
//        assertNotNull(result);
//        verify(inventoryRepository, times(1)).save(inventory);
//    }
//
//    @Test
//    void testCreateInventoryProductNotFound() {
//        Product product = new Product();
//        product.setId(1L);
//        Inventory inventory = new Inventory();
//        inventory.setProduct(product);
//
//        when(productRepository.findById(1L)).thenReturn(Optional.empty());
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> inventoryService.createInventory(inventory));
//        assertEquals("Product not found", ex.getMessage());
//    }
//
//    @Test
//    void testEditQuantitySuccess() {
//        Inventory inv = new Inventory();
//        inv.setInventoryId(1L);
//        inv.setQuantityAvailable(10);
//        inv.setActive(true);
//
//        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inv));
//        when(inventoryRepository.save(inv)).thenReturn(inv);
//
//        Inventory result = inventoryService.editQuantity(1L, 5);
//
//        assertEquals(15, result.getQuantityAvailable());
//        assertTrue(result.getActive());
//    }
//
//    @Test
//    void testEditQuantitySetInactive() {
//        Inventory inv = new Inventory();
//        inv.setInventoryId(1L);
//        inv.setQuantityAvailable(5);
//        inv.setActive(true);
//
//        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inv));
//        when(inventoryRepository.save(inv)).thenReturn(inv);
//
//        Inventory result = inventoryService.editQuantity(1L, -5);
//
//        assertEquals(0, result.getQuantityAvailable());
//        assertFalse(result.getActive());
//    }
//
//    @Test
//    void testDeleteInventorySuccess() {
//        Inventory inv = new Inventory();
//        inv.setInventoryId(1L);
//        inv.setActive(true);
//
//        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inv));
//
//        inventoryService.deleteInventory(1L);
//
//        assertFalse(inv.getActive());
//        verify(inventoryRepository, times(1)).save(inv);
//    }
//
//    @Test
//    void testTransferQuantitySuccess() {
//        Inventory from = new Inventory();
//        from.setInventoryId(1L);
//        from.setQuantityAvailable(10);
//        from.setActive(true);
//
//        Inventory to = new Inventory();
//        to.setInventoryId(2L);
//        to.setQuantityAvailable(5);
//        to.setActive(true);
//
//        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(from));
//        when(inventoryRepository.findById(2L)).thenReturn(Optional.of(to));
//        when(inventoryRepository.save(from)).thenReturn(from);
//        when(inventoryRepository.save(to)).thenReturn(to);
//
//        Inventory result = inventoryService.transferQuantity(1L, 5, 2L);
//
//        assertEquals(5, from.getQuantityAvailable());
//        assertEquals(10, result.getQuantityAvailable());
//        verify(inventoryRepository, times(1)).save(from);
//        verify(inventoryRepository, times(1)).save(to);
//    }
//
//}
