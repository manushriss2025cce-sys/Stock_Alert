package com.example.stockalert.controller;

import com.example.stockalert.entity.ReorderAlert;
import com.example.stockalert.service.ReorderAlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reorder-alerts")
public class ReorderAlertController {

    private final ReorderAlertService reorderAlertService;

    public ReorderAlertController(
            ReorderAlertService reorderAlertService) {

        this.reorderAlertService = reorderAlertService;
    }

    @GetMapping("/{productId}")
    public List<ReorderAlert> getAlerts(
            @PathVariable Long productId) {

        return reorderAlertService
                .getAlertsByProduct(productId);
    }
}