package com.epam.esm.services;

import com.epam.esm.controllers.OrderController;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.domain.dto.CreateOrderDto;
import com.epam.esm.exceptions.BadRequestException;
import com.epam.esm.exceptions.ErrorCode;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.hateoas.OrderModel;
import com.epam.esm.hateoas.OrderModelAssembler;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;
    private final OrderModelAssembler orderModelAssembler;

    @Transactional
    public Order createOrder(CreateOrderDto createOrderDto) {
        User user = userRepository.findById(createOrderDto.getUserId()).orElseThrow(() -> new BadRequestException("User doesnt exist, userId = " + createOrderDto.getUserId(), ErrorCode.UserNotExistInDtoObject));
        Certificate certificate = certificateRepository.findById(createOrderDto.getCertificateId()).orElseThrow(() -> new BadRequestException("Certificate doesnt exist, certificateId = " + createOrderDto.getCertificateId(), ErrorCode.CertificateNotExistInDtoObject));
        Order order = Order.builder()
                .certificate(certificate)
                .user(user)
                .price(certificate.getPrice())
                .description(createOrderDto.getDescription())
                .build();
        return orderRepository.save(order);
    }

    public Page<Order> findByUserId(long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    public Page<Order> findAllPageable(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order fetchById(long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceDoesNotExistException("Order not found, orderId = " + id, ErrorCode.OrderNotExist));
    }

    public OrderModel modelFromOrder(Order order) {
        OrderModel orderModel = orderModelAssembler.toModel(order);
        Link selfLink = linkTo(OrderController.class).slash(order.getId()).withSelfRel();
        orderModel.add(selfLink);
        return orderModel;
    }
}