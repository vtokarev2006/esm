package com.epam.esm.services;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.domain.dto.CreateOrderDto;
import com.epam.esm.exceptions.BadRequestException;
import com.epam.esm.exceptions.ErrorCode;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;

    @Transactional
    public Order createOrder(CreateOrderDto createOrderDto) {
        Optional<User> user = userRepository.findById(createOrderDto.getUserId());
        if (user.isEmpty()) {
            throw new BadRequestException("User doesnt exist, userId = " + createOrderDto.getUserId(), ErrorCode.UserNotExistInDtoObject);
        }
        Optional<Certificate> certificate = certificateRepository.findById(createOrderDto.getCertificateId());
        if (certificate.isEmpty()) {
            throw new BadRequestException("Certificate doesnt exist, certificateId = " + createOrderDto.getCertificateId(), ErrorCode.CertificateNotExistInDtoObject);
        }
        Order order = Order.builder()
                .certificate(certificate.get())
                .user(user.get())
                .price(certificate.get().getPrice())
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
        return orderRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }
}