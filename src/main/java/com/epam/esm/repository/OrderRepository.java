package com.epam.esm.repository;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.dto.CreateOrderDto;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends GenericRepository<Order> {
    List<Order> getByUserId(long userId);
}
