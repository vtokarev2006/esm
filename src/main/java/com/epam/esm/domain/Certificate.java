package com.epam.esm.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "certificates")
@EntityListeners(AuditingEntityListener.class)
public class Certificate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;

    @CreatedDate
    @Column(name = "create_date")
    private Instant createDate;

    @LastModifiedDate
    @Column(name = "last_update_date")
    private Instant lastUpdateDate;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "certificates_have_tags", joinColumns = @JoinColumn(name = "certificate_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
}