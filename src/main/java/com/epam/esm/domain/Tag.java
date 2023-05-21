package com.epam.esm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "tags")
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(name = "create_date")
    private Instant createDate;

    @LastModifiedDate
    @Column(name = "last_update_date")
    private Instant lastUpdateDate;
}