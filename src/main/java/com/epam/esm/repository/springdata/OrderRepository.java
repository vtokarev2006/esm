package com.epam.esm.repository.springdata;

import com.epam.esm.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
