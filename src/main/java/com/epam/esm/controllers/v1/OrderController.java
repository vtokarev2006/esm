package com.epam.esm.controllers.v1;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.dto.CreateOrderDto;
import com.epam.esm.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAll(@RequestParam Optional<Long> userId) {
        if (userId.isPresent())
            return orderService.getByUserId(userId.get());
        else
            return orderService.getAll();
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        Order order = orderService.createOrder(createOrderDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.getId())
                .toUri();
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, String.valueOf(location)).build();
    }
}