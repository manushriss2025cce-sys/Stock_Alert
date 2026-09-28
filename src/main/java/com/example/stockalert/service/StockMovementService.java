package com.example.stockalert.service;

import com.example.stockalert.entity.MovementType;
import com.example.stockalert.entity.Product;
import com.example.stockalert.entity.StockMovement;
import com.example.stockalert.repository.ProductRepository;
import com.example.stockalert.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;
    private final ReorderAlertService reorderAlertService;

    public StockMovementService(
            StockMovementRepository stockMovementRepository,
            ProductRepository productRepository,
            ReorderAlertService reorderAlertService) {

        this.stockMovementRepository = stockMovementRepository;
        this.productRepository = productRepository;
        this.reorderAlertService = reorderAlertService;
    }

    public StockMovement addMovement(
            Long productId,
            StockMovement movement) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException("Product not found"));

        movement.setProduct(product);

        if (movement.getMovementDate() == null) {
            movement.setMovementDate(LocalDateTime.now());
        }

        StockMovement savedMovement =
                stockMovementRepository.save(movement);

        int currentStock =
                getCurrentStock(productId);

        if (movement.getMovementType() == MovementType.IN) {
            reorderAlertService.fulfillOpenAlert(productId);
        }

        reorderAlertService.checkAndCreateAlert(
                productId,
                currentStock);

        return savedMovement;
    }

    public List<StockMovement> getMovementsByProduct(
            Long productId) {

        return stockMovementRepository
                .findByProductId(productId);
    }

    public int getCurrentStock(Long productId) {

        List<StockMovement> movements =
                stockMovementRepository
                        .findByProductId(productId);

        int stock = 0;

        for (StockMovement movement : movements) {

            if (movement.getMovementType() == MovementType.IN) {
                stock += movement.getQuantity();
            }

            if (movement.getMovementType() == MovementType.OUT) {
                stock -= movement.getQuantity();
            }
        }

        return stock;
    }

    public void deleteMovement(Long movementId) {
        stockMovementRepository.deleteById(movementId);
    }
}