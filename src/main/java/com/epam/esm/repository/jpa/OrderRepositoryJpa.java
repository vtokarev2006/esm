package com.epam.esm.repository.jpa;

import com.epam.esm.domain.Order;
import com.epam.esm.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("OrderRepositoryJpa")
public class OrderRepositoryJpa implements OrderRepository {

    private final EntityManager em;

    @Autowired
    public OrderRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Order> get(long id) {
        return Optional.ofNullable(em.find(Order.class, id));
    }

    @Override
    public List<Order> getAll(Pageable pageable) {
        return em.createNamedQuery("Order_getAll", Order.class).getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Order create(Order order) {
        em.persist(order);
        return order;
    }

    @Override
    public void update(Order order) {

    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public List<Order> getByUserId(long userId) {
        return em.createNamedQuery("Order_getByUserId", Order.class).setParameter("userId", userId).getResultList();
    }
}
