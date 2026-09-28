package com.example.stockalert.service;

import com.example.stockalert.entity.AlertStatus;
import com.example.stockalert.entity.Product;
import com.example.stockalert.entity.ReorderAlert;
import com.example.stockalert.repository.ProductRepository;
import com.example.stockalert.repository.ReorderAlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReorderAlertService {

    private final ReorderAlertRepository reorderAlertRepository;
    private final ProductRepository productRepository;

    public ReorderAlertService(
            ReorderAlertRepository reorderAlertRepository,
            ProductRepository productRepository) {

        this.reorderAlertRepository = reorderAlertRepository;
        this.productRepository = productRepository;
    }

    public void checkAndCreateAlert(
            Long productId,
            int currentStock) {

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new RuntimeException("Product not found"));

        if (currentStock <= product.getReorderThreshold()) {

            boolean alertExists =
                    reorderAlertRepository
                            .findByProductIdAndStatus(
                                    productId,
                                    AlertStatus.OPEN)
                            .isPresent();

            if (!alertExists) {

                ReorderAlert alert = new ReorderAlert();

                alert.setProduct(product);
                alert.setCurrentStock(currentStock);
                alert.setThreshold(
                        product.getReorderThreshold());
                alert.setStatus(AlertStatus.OPEN);
                alert.setCreatedAt(LocalDateTime.now());

                reorderAlertRepository.save(alert);
            }
        }
    }

    public void fulfillOpenAlert(Long productId) {

        reorderAlertRepository
                .findByProductIdAndStatus(
                        productId,
                        AlertStatus.OPEN)
                .ifPresent(alert -> {

                    alert.setStatus(AlertStatus.FULFILLED);
                    alert.setFulfilledAt(LocalDateTime.now());

                    reorderAlertRepository.save(alert);
                });
    }

    public List<ReorderAlert> getAlertsByProduct(
            Long productId) {

        return reorderAlertRepository
                .findByProductId(productId);
    }
}