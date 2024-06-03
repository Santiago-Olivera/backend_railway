package com.BackendChallenge.TechTrendEmporium.controller;
import com.BackendChallenge.TechTrendEmporium.Requests.PurchaseRequest;
import com.BackendChallenge.TechTrendEmporium.service.PurchasesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseControllerTest {

    @Mock
    private PurchasesService purchasesService;

    @InjectMocks
    private PurchaseController purchaseController;

    @Test
    public void getAllPurchasesTest_Success() {
        when(purchasesService.getAllPurchases()).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = purchaseController.getAllPurchases();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getPurchasesByUserIdTest_Success() {
        PurchaseRequest request = new PurchaseRequest();
        request.setUser_id(1L);
        when(purchasesService.getPurchasesByUserId(any())).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = purchaseController.getPurchasesByUserId(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updatePurchaseStatusTest_Success() {
        PurchaseRequest request = new PurchaseRequest();
        request.setSale_id(1L);
        request.setStatus("Completed");
        when(purchasesService.updatePurchaseStatus(any(), any())).thenReturn(true);
        ResponseEntity<?> response = purchaseController.updatePurchaseStatus(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updatePurchaseStatusTest_Failure() {
        PurchaseRequest request = new PurchaseRequest();
        request.setSale_id(1L);
        request.setStatus("Completed");
        when(purchasesService.updatePurchaseStatus(any(), any())).thenReturn(false);
        ResponseEntity<?> response = purchaseController.updatePurchaseStatus(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}