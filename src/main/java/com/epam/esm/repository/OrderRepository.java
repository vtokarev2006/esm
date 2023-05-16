package com.epam.esm.repository;

import com.epam.esm.domain.Order;
import org.springframework.context.annotation.Profile;

import java.util.List;
@Deprecated
public interface OrderRepository extends GenericRepository<Order> {
    List<Order> getByUserId(long userId);
}
