package com.example.stockalert.service;

import com.example.stockalert.entity.MovementType;
import com.example.stockalert.entity.Product;
import com.example.stockalert.entity.StockMovement;
import com.example.stockalert.repository.ProductRepository;
import com.example.stockalert.repository.StockMovementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class FastMovingService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;

    public FastMovingService(
            StockMovementRepository stockMovementRepository,
            ProductRepository productRepository) {

        this.stockMovementRepository = stockMovementRepository;
        this.productRepository = productRepository;
    }

    public List<Map<String, Object>> getFastMovingProducts(
            LocalDateTime startDate,
            LocalDateTime endDate) {

        List<StockMovement> sales =
                stockMovementRepository
                        .findByMovementTypeAndMovementDateBetween(
                                MovementType.OUT,
                                startDate,
                                endDate);

        Map<Long, Integer> salesMap = new HashMap<>();

        for (StockMovement movement : sales) {

            Long productId =
                    movement.getProduct().getId();

            salesMap.put(
                    productId,
                    salesMap.getOrDefault(productId, 0)
                            + movement.getQuantity());
        }

        List<Map<String, Object>> result =
                new ArrayList<>();

        for (Map.Entry<Long, Integer> entry :
                salesMap.entrySet()) {

            Product product =
                    productRepository
                            .findById(entry.getKey())
                            .orElse(null);

            if (product != null) {

                Map<String, Object> data =
                        new LinkedHashMap<>();

                data.put("productId", product.getId());
                data.put("productName", product.getName());
                data.put("salesVolume", entry.getValue());

                result.add(data);
            }
        }

        result.sort((a, b) ->
                Integer.compare(
                        (Integer) b.get("salesVolume"),
                        (Integer) a.get("salesVolume")));

        return result;
    }
}