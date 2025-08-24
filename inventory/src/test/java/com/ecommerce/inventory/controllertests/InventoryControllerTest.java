//package com.ecommerce.inventory.controllertests;
//
//import com.ecommerce.inventory.controllers.InventoryController;
//import com.ecommerce.inventory.dtos.QuantityRequest;
//import com.ecommerce.inventory.entities.Inventory;
//import com.ecommerce.inventory.entities.InventoryReservation;
//import com.ecommerce.inventory.services.InventoryReservationService;
//import com.ecommerce.inventory.services.InventoryService;
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
//import java.util.Arrays;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(InventoryController.class)
//@Import(InventoryControllerTest.TestConfig.class)
//class InventoryControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private InventoryService inventoryService;
//
////    @Autowired
////    private InventoryReservationService inventoryReservationService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private Inventory inventory1;
//    private Inventory inventory2;
////    private InventoryReservation reservation1;
//
//    @BeforeEach
//    void setUp() {
//        Mockito.reset(inventoryService, inventoryReservationService);
//
//        inventory1 = new Inventory();
//        inventory1.setInventoryId(1L);
//        inventory1.setQuantityAvailable(100);
//
//        inventory2 = new Inventory();
//        inventory2.setInventoryId(2L);
//        inventory2.setQuantityAvailable(50);
//
//        reservation1 = new InventoryReservation();
//        reservation1.setId(1L);
//        reservation1.setQuantity(10);
//        reservation1.setInventory(inventory1);
//    }
//
//    @Test
//    void testGetInventory() throws Exception {
//        when(inventoryService.getInventory()).thenReturn(Arrays.asList(inventory1, inventory2));
//
//        mockMvc.perform(get("/inventory"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//    }
//
//    @Test
//    void testCreateInventory() throws Exception {
//        when(inventoryService.createInventory(any(Inventory.class))).thenReturn(inventory1);
//
//        mockMvc.perform(post("/inventory")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(inventory1)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.inventoryId").value(1));
//    }
//
//    @Test
//    void testEditQuantity() throws Exception {
//        when(inventoryService.editQuantity(eq(1L), eq(20))).thenReturn(inventory1);
//
//        mockMvc.perform(patch("/inventory/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("20"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.inventoryId").value(1));
//    }
//
//    @Test
//    void testTransferQuantity() throws Exception {
//        when(inventoryService.transferQuantity(eq(1L), eq(10), eq(2L))).thenReturn(inventory2);
//
//        mockMvc.perform(patch("/inventory/1/transfer/2")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.inventoryId").value(2));
//    }
//
//    @Test
//    void testDeleteInventory() throws Exception {
//        doNothing().when(inventoryService).deleteInventory(1L);
//
//        mockMvc.perform(delete("/inventory/1"))
//                .andExpect(status().isOk());
//
//        verify(inventoryService, times(1)).deleteInventory(1L);
//    }
//
//    @Test
//    void testGetReservations() throws Exception {
//        when(inventoryReservationService.getReservationsByInventoryId(1L))
//                .thenReturn(Arrays.asList(reservation1));
//
//        mockMvc.perform(get("/inventory/1/reservations"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(1));
//    }
//
//    @Test
//    void testReserveQuantity() throws Exception {
//        QuantityRequest request = new QuantityRequest();
//        request.setQuantity(10);
//
//        when(inventoryReservationService.reserveQuantity(eq(1L), eq(10)))
//                .thenReturn(reservation1);
//
//        mockMvc.perform(post("/inventory/1/reservations")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.inventory.inventoryId").value(1))
//                .andExpect(jsonPath("$.quantity").value(10));
//    }
//
//    @Test
//    void testReleaseQuantity() throws Exception {
//        doNothing().when(inventoryReservationService).releaseQuantity(1L);
//
//        mockMvc.perform(delete("/inventory/1/reservations"))
//                .andExpect(status().isOk());
//
//        verify(inventoryReservationService, times(1)).releaseQuantity(1L);
//    }
//
//    @Test
//    void testSellQuantity() throws Exception {
//        when(inventoryReservationService.sellQuantity(1L)).thenReturn(inventory1);
//
//        mockMvc.perform(patch("/inventory/1/reservations"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.inventoryId").value(1));
//    }
//
//    /**
//     * Test configuration to provide mocked services without using deprecated @MockBean
//     */
//    @TestConfiguration
//    static class TestConfig {
//        @Bean
//        public InventoryService inventoryService() {
//            return mock(InventoryService.class);
//        }
//
//        @Bean
//        public InventoryReservationService inventoryReservationService() {
//            return mock(InventoryReservationService.class);
//        }
//    }
//}
