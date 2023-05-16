package com.epam.esm.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tags")
@NamedQuery(query = """
        select new com.epam.esm.domain.dto.TagOrdersPriceDto(t.id, t.name, sum(o.price))
        from Order o
            join o.user u
            join o.certificate c
            join c.tags t
        where u.id = :userId
        group by t""", name = "Tag_getTagSumOrdersPrice")
public class Tag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}