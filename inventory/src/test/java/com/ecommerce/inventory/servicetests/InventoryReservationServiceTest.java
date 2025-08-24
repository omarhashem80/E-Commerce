//package com.ecommerce.inventory.servicetests;
//
//import com.ecommerce.inventory.entities.Inventory;
//import com.ecommerce.inventory.entities.InventoryReservation;
//import com.ecommerce.inventory.repositories.InventoryRepository;
//import com.ecommerce.inventory.repositories.InventoryReservationRepository;
//import com.ecommerce.inventory.services.InventoryReservationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class InventoryReservationServiceTest {
//
//    @Mock
//    private InventoryRepository inventoryRepository;
//
//    @Mock
//    private InventoryReservationRepository inventoryReservationRepository;
//
//    @InjectMocks
//    private InventoryReservationService inventoryReservationService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testReserveQuantitySuccess() {
//        Inventory inventory = new Inventory();
//        inventory.setInventoryId(1L);
//        inventory.setQuantityAvailable(10);
//        inventory.setActive(true);
//
//        InventoryReservation reservation = new InventoryReservation();
//        reservation.setInventory(inventory);
//        reservation.setQuantity(5);
//        reservation.setActive(true);
//
//        // Mock repository calls
//        when(inventoryReservationRepository.findById(1L))
//                .thenReturn(Optional.of(reservation));
//        when(inventoryReservationRepository.save(any(InventoryReservation.class)))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        InventoryReservation result = inventoryReservationService.reserveQuantity(1L, 5);
//
//        assertEquals(5, result.getQuantity());
//        assertTrue(result.isActive());
//        assertEquals(5, inventory.getQuantityAvailable()); // 10 - 5
//        verify(inventoryReservationRepository, times(1)).save(any(InventoryReservation.class));
//    }
//
//    @Test
//    void testReserveQuantityInsufficientInventory() {
//        Inventory inventory = new Inventory();
//        inventory.setInventoryId(1L);
//        inventory.setQuantityAvailable(2);
//        inventory.setActive(true);
//
//        InventoryReservation reservation = new InventoryReservation();
//        reservation.setInventory(inventory);
//
//        when(inventoryReservationRepository.findById(1L))
//                .thenReturn(Optional.of(reservation));
//
//        RuntimeException ex = assertThrows(RuntimeException.class,
//                () -> inventoryReservationService.reserveQuantity(1L, 5));
//        assertEquals("Insufficient inventory", ex.getMessage());
//    }
//
//    @Test
//    void testReleaseQuantitySuccess() {
//        Inventory inventory = new Inventory();
//        inventory.setInventoryId(1L);
//        inventory.setQuantityAvailable(5);
//        inventory.setActive(true);
//
//        InventoryReservation reservation = new InventoryReservation();
//        reservation.setId(1L);
//        reservation.setInventory(inventory);
//        reservation.setQuantity(3);
//        reservation.setActive(true);
//
//        when(inventoryReservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
//
//        inventoryReservationService.releaseQuantity(1L);
//
//        assertFalse(reservation.isActive());
//        assertEquals(8, inventory.getQuantityAvailable()); // 5 + 3
//        verify(inventoryReservationRepository, times(1)).save(reservation);
//    }
//
//    @Test
//    void testSellQuantitySuccess() {
//        Inventory inventory = new Inventory();
//        inventory.setInventoryId(1L);
//        inventory.setQuantityAvailable(10);
//        inventory.setQuantitySold(5);
//        inventory.setActive(true);
//
//        InventoryReservation reservation = new InventoryReservation();
//        reservation.setId(1L);
//        reservation.setInventory(inventory);
//        reservation.setQuantity(3);
//        reservation.setActive(true);
//
//        when(inventoryReservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
//        when(inventoryReservationRepository.save(reservation)).thenReturn(reservation);
//
//        Inventory result = inventoryReservationService.sellQuantity(1L);
//
//        assertFalse(reservation.isActive());
//        assertEquals(8, result.getQuantitySold()); // 5 + 3
//        verify(inventoryReservationRepository, times(1)).save(reservation);
//    }
//
//    @Test
//    void testGetReservationsByInventoryId() {
//        Inventory inventory = new Inventory();
//        inventory.setInventoryId(1L);
//        inventory.setActive(true);
//        List<InventoryReservation> reservations = new ArrayList<>();
//        reservations.add(new InventoryReservation());
//        reservations.add(new InventoryReservation());
//        inventory.setReservations(reservations);
//
//        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
//
//        List<InventoryReservation> result = inventoryReservationService.getReservationsByInventoryId(1L);
//
//        assertEquals(2, result.size());
//    }
//}
//
