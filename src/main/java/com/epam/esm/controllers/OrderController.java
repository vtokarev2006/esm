package com.epam.esm.controllers;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.dto.CreateOrderDto;
import com.epam.esm.hateoas.OrderModel;
import com.epam.esm.hateoas.OrderModelAssembler;
import com.epam.esm.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderModelAssembler orderModelAssembler;
    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<OrderModel> fetchAllPageable(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {
        return pagedResourcesAssembler.toModel(orderService.findAllPageable(pageable), orderModelAssembler);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderModel fetchById(@PathVariable long id) {
        return orderService.modelFromOrder(orderService.fetchById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderModel createOrder(@RequestBody CreateOrderDto createOrderDto) {
        return orderService.modelFromOrder(orderService.createOrder(createOrderDto));
    }

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<OrderModel> fetchByUserPageable(@PathVariable long userId, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {
        return pagedResourcesAssembler.toModel(orderService.findByUserId(userId, pageable), orderModelAssembler);
    }
}