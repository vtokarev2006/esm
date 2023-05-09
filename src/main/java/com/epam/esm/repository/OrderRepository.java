package com.epam.esm.repository;

import com.epam.esm.domain.Order;

import java.util.List;

public interface OrderRepository extends GenericRepository<Order> {
    List<Order> getByUserId(long userId);
}
