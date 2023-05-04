package com.epam.esm.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "certificates")
public class Certificate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String description;

    private Double price;

    private Integer duration;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "last_update_date")
    private Instant lastUpdateDate;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "certificates_have_tags",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
}
