package com.epam.esm.legacy.repository;

import com.epam.esm.domain.Order;

import java.util.List;
@Deprecated
public interface OrderRepository extends GenericRepository<Order> {
    List<Order> fetchByUserId(long userId);
}
