package com.BackendChallenge.TechTrendEmporium.service;
import com.BackendChallenge.TechTrendEmporium.Response.PurchaseResponse;
import com.BackendChallenge.TechTrendEmporium.entity.Cart;
import com.BackendChallenge.TechTrendEmporium.entity.Sale;
import com.BackendChallenge.TechTrendEmporium.entity.SaleStatus;
import com.BackendChallenge.TechTrendEmporium.entity.User;
import com.BackendChallenge.TechTrendEmporium.repository.SaleRepository;
import com.BackendChallenge.TechTrendEmporium.service.PurchasesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PurchasesServiceTest {

    @InjectMocks
    private PurchasesService purchasesService;

    @Mock
    private SaleRepository saleRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void getPurchasesByUserIdTest() {
        // Prepare the data
        Sale sale1 = new Sale();
        Sale sale2 = new Sale();

        Cart cart1 = mock(Cart.class);
        Cart cart2 = mock(Cart.class);
        User user1 = mock(User.class);
        User user2 = mock(User.class);

        when(user1.getId()).thenReturn(1L);
        when(user2.getId()).thenReturn(1L);

        when(cart1.getUser()).thenReturn(user1);
        when(cart2.getUser()).thenReturn(user2);

        sale1.setCart(cart1);
        sale1.setStatus(SaleStatus.SENT); // Set status for sale1

        sale2.setCart(cart2);
        sale2.setStatus(SaleStatus.SENT); // Set status for sale2

        // Mock the repository call
        when(saleRepository.findByCartUserId(anyLong())).thenReturn(Arrays.asList(sale1, sale2));

        // Call the method and check the result
        List<PurchaseResponse> result = purchasesService.getPurchasesByUserId(1L);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(1L, result.get(1).getUserId());
    }

    @Test
    public void getAllPurchasesTest() {
        // Prepare the data
        Sale sale1 = new Sale();
        Sale sale2 = new Sale();

        Cart cart1 = mock(Cart.class);
        Cart cart2 = mock(Cart.class);
        User user1 = mock(User.class);
        User user2 = mock(User.class);

        when(user1.getId()).thenReturn(1L);
        when(user2.getId()).thenReturn(1L);

        when(cart1.getUser()).thenReturn(user1);
        when(cart2.getUser()).thenReturn(user2);

        sale1.setCart(cart1);
        sale1.setStatus(SaleStatus.SENT); // Set status for sale1

        sale2.setCart(cart2);
        sale2.setStatus(SaleStatus.SENT); // Set status for sale2

        // Mock the repository call
        when(saleRepository.findAll()).thenReturn(Arrays.asList(sale1, sale2));

        // Call the method and check the result
        List<PurchaseResponse> result = purchasesService.getAllPurchases();
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(1L, result.get(1).getUserId());
    }

    @Test
    public void updatePurchaseStatusTest_Sent() {
        Sale sale = new Sale();
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        assertTrue(purchasesService.updatePurchaseStatus(1L, "SENT"));
        assertEquals(SaleStatus.SENT, sale.getStatus());
    }

    @Test
    public void updatePurchaseStatusTest_ToSend() {
        Sale sale = new Sale();
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        assertTrue(purchasesService.updatePurchaseStatus(1L, "TO_SEND"));
        assertEquals(SaleStatus.TO_SEND, sale.getStatus());
    }

    @Test
    public void updatePurchaseStatusTest_Closed() {
        Sale sale = new Sale();
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        assertTrue(purchasesService.updatePurchaseStatus(1L, "CLOSED"));
        assertEquals(SaleStatus.CLOSED, sale.getStatus());
    }

    @Test
    public void updatePurchaseStatusTest_InvalidStatus() {
        Sale sale = new Sale();
        when(saleRepository.findById(anyLong())).thenReturn(Optional.of(sale));
        assertFalse(purchasesService.updatePurchaseStatus(1L, "INVALID"));
    }

    @Test
    public void updatePurchaseStatusTest_SaleDoesNotExist() {
        when(saleRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertFalse(purchasesService.updatePurchaseStatus(1L, "SENT"));
    }
}