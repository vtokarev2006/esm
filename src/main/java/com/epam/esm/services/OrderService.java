package com.epam.esm.services;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.domain.dto.CreateOrderDto;
import com.epam.esm.exceptions.BadRequestException;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CertificateRepository certificateRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
    }

    public List<Order> getAll() {
        return orderRepository.getAll();
    }

    public List<Order> getByUserId(long userId) {
        return orderRepository.getByUserId(userId);
    }

    @Transactional
    public Order createOrder(CreateOrderDto createOrderDto) {
        Optional<User> user = userRepository.get(createOrderDto.getUserId());
        if (user.isEmpty())
            throw new BadRequestException("User doesnt exist, userId = " + createOrderDto.getUserId());

        Optional<Certificate> certificate = certificateRepository.get(createOrderDto.getCertificateId());
        if (certificate.isEmpty())
            throw new BadRequestException("Certificate doesnt exist, certificateId = " + createOrderDto.getCertificateId());

        Order order = Order.builder()
                .certificate(certificate.get())
                .user(user.get())
                .price(certificate.get().getPrice())
                .description(createOrderDto.getDescription())
                .createDate(Instant.now())
                .build();
        return orderRepository.create(order);
    }

    public Order getByUserOrderId(long userId, long orderId) {
        Optional<Order> order = orderRepository.get(orderId);

        if (order.isEmpty())
            throw new ResourceDoesNotExistException("Order doesnt exist, orderId = " + orderId);

        if (order.get().getUser().getId() != userId)
            throw new ResourceDoesNotExistException("User with userId = " + userId + " doesn't have Order with orderId = " + orderId);

        return order.get();
    }
}
