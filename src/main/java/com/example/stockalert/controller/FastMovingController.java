package com.example.stockalert.controller;

import com.example.stockalert.service.FastMovingService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fast-moving")
public class FastMovingController {

    private final FastMovingService fastMovingService;

    public FastMovingController(
            FastMovingService fastMovingService) {

        this.fastMovingService = fastMovingService;
    }

    @GetMapping
    public List<Map<String, Object>> getFastMovingProducts(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        LocalDateTime start =
                LocalDateTime.parse(startDate);

        LocalDateTime end =
                LocalDateTime.parse(endDate);

        return fastMovingService
                .getFastMovingProducts(start, end);
    }
}