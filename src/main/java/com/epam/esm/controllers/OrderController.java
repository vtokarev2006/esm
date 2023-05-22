package com.epam.esm.controllers;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.dto.CreateOrderDto;
import com.epam.esm.exceptions.ErrorCode;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.hateoas.OrderModel;
import com.epam.esm.hateoas.OrderModelAssembler;
import com.epam.esm.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/v2/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderModelAssembler orderModelAssembler;
    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;

    @GetMapping
    public ResponseEntity<PagedModel<OrderModel>> fetchAllPageable(@PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {
        Page<Order> orders = orderService.fetchAll(pageable);
        PagedModel<OrderModel> orderModels = pagedResourcesAssembler.toModel(orders, orderModelAssembler);
        return new ResponseEntity<>(orderModels, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderModel> fetchById(@PathVariable long id) {
        try {
            Order order = orderService.fetchById(id);
            OrderModel orderModel = modelFromOrder(order);
            return new ResponseEntity<>(orderModel, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceDoesNotExistException("Order not found, orderId = " + id, ErrorCode.OrderNotExist);
        }
    }

    @PostMapping
    public ResponseEntity<OrderModel> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        Order order = orderService.createOrder(createOrderDto);
        OrderModel orderModel = orderModelAssembler.toModel(order);
        Link selfLink = linkTo(OrderController.class).slash(order.getId()).withSelfRel();
        orderModel.add(selfLink);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, String.valueOf(location)).body(orderModel);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<PagedModel<OrderModel>> fetchByUserPageable(@PathVariable long userId, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {
        Page<Order> orderPage = orderService.fetchByUserId(userId, pageable);
        PagedModel<OrderModel> orderModels = pagedResourcesAssembler.toModel(orderPage, orderModelAssembler);
        return new ResponseEntity<>(orderModels, HttpStatus.OK);
    }

    private OrderModel modelFromOrder(Order order) {
        OrderModel orderModel = orderModelAssembler.toModel(order);
        Link selfLink = linkTo(CertificateController.class).slash(order.getId()).withSelfRel();
        orderModel.add(selfLink);
        return orderModel;
    }
}