package com.example.stockalert.controller;

import com.example.stockalert.entity.StockMovement;
import com.example.stockalert.service.StockMovementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(
            StockMovementService stockMovementService) {

        this.stockMovementService = stockMovementService;
    }

    @PostMapping("/{productId}")
    public StockMovement addMovement(
            @PathVariable Long productId,
            @RequestBody StockMovement movement) {

        return stockMovementService.addMovement(productId, movement);
    }

    @GetMapping("/{productId}")
    public List<StockMovement> getMovements(
            @PathVariable Long productId) {

        return stockMovementService.getMovementsByProduct(productId);
    }

    @GetMapping("/{productId}/stock")
    public int getCurrentStock(
            @PathVariable Long productId) {

        return stockMovementService.getCurrentStock(productId);
    }
    @DeleteMapping("/{movementId}")
    public void deleteMovement(@PathVariable Long movementId) {
        stockMovementService.deleteMovement(movementId);
    }
}