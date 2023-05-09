package com.epam.esm.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@NamedQuery(query = "select o from Order o", name = "Order_getAll")
@NamedQuery(query = "select o from Order o where o.user.id = :userId", name = "Order_getByUserId")

public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private double price;

    @Column(name = "create_date")
    private Instant createDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false) // doesn't work, thrown the exception is LAZY
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false) // doesn't work, thrown the exception is LAZY
    @JoinColumn(name = "certificate_id", nullable = false)
    private Certificate certificate;

}
