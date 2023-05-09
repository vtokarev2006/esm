package com.epam.esm.controllers.v2;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.dto.CreateOrderDto;
import com.epam.esm.hateoas.OrderModel;
import com.epam.esm.hateoas.OrderModelAssembler;
import com.epam.esm.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController("OrderControllerV2")
@RequestMapping("/api/v2/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderModelAssembler orderModelAssembler;
    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;


    @Autowired
    public OrderController(OrderService orderService, OrderModelAssembler orderModelAssembler, PagedResourcesAssembler<Order> pagedResourcesAssembler) {
        this.orderService = orderService;
        this.orderModelAssembler = orderModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<OrderModel>> getAll(@RequestParam Optional<Long> userId,
                                                         @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {
        Page<Order> orders = orderService.getByUserId(userId, pageable);
        PagedModel<OrderModel> orderModels = pagedResourcesAssembler.toModel(orders, orderModelAssembler);
        return new ResponseEntity<>(orderModels, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderModel> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        Order order = orderService.createOrder(createOrderDto);
        OrderModel orderModel = orderModelAssembler.toModel(order);
        Link selfLink = linkTo(TagController.class).slash(order.getId()).withSelfRel();
        orderModel.add(selfLink);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.getId())
                .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, String.valueOf(location))
                .body(orderModel);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<PagedModel<OrderModel>> getUserOrders(@PathVariable Optional<Long> userId,
                                                                @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, value = 30) Pageable pageable) {
        Page<Order> orderPage = orderService.getByUserId(userId, pageable);
        PagedModel<OrderModel> orderModels = pagedResourcesAssembler.toModel(orderPage, orderModelAssembler);
        return new ResponseEntity<>(orderModels, HttpStatus.OK);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<OrderModel> getUserOrders(@PathVariable long userId, @PathVariable long orderId) {
        OrderModel orderModel = orderModelAssembler.toModel(orderService.getByUserOrderId(userId, orderId));
        return new ResponseEntity<>(orderModel, HttpStatus.OK);
    }
}