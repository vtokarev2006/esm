package com.epam.esm.repository.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.repository.CertificateCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CertificateCustomRepositoryImpl implements CertificateCustomRepository {
    private final EntityManager em;
    /**
     * SELECT c.*
     * FROM certificates c
     *
     * join certificates_have_tags cht1
     * on (c.id = cht1.certificate_id)
     * join tags t1
     * on (t1.id = cht.tag_id and t1.name="tagName1")
     *
     * ..........................
     *
     * join certificates_have_tags chtN
     * on (c.id = chtN.certificate_id)
     * join tags tN
     * on (tN.id = cht.tag_id and tN.name="tagNameN")
     *
     * where (c.name like "%partOfCertName%") and (c.description like "%partOfCertDescription%")
     * order by c.id desc
     *
     * @param name        - search certificates with name equal the value of the param
     * @param description - search certificates with description contain the value of the param
     * @param tagNames    - search certificates with tags name (and condition)
     * @param pageable    - object determinate pageable behavior
     * @return - page of Certificates meet params above
     */
    @Override
    public PageImpl<Certificate> findCertificatesByNameDescriptionTagNames(Optional<String> name, Optional<String> description, Set<String> tagNames, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Certificate> query = cb.createQuery(Certificate.class);
        Root<Certificate> root = query.from(Certificate.class);

        CriteriaQuery<Long> queryCount = cb.createQuery(Long.class);
        Root<Certificate> rootCount = queryCount.from(Certificate.class);

        tagNames.forEach(tagName -> {
            Join<Certificate, Tag> tag = root.join("tags");
            tag.on(cb.equal(tag.get("name"), tagName));

            Join<Certificate, Tag> tagCount = rootCount.join("tags");
            tagCount.on(cb.equal(tagCount.get("name"), tagName));
        });

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> predicatesCount = new ArrayList<>();
        name.ifPresent(s -> {
            predicates.add(cb.like(root.get("name"), "%" + s + "%"));
            predicatesCount.add(cb.like(rootCount.get("name"), "%" + s + "%"));
        });
        description.ifPresent(s -> {
            predicates.add(cb.like(root.get("description"), "%" + s + "%"));
            predicatesCount.add(cb.like(rootCount.get("description"), "%" + s + "%"));
        });

        query.select(root).distinct(true);
        queryCount.select(cb.countDistinct(rootCount));
        predicates.stream().reduce(cb::and).ifPresent(query::where);
        predicatesCount.stream().reduce(cb::and).ifPresent(queryCount::where);
        query.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));

        TypedQuery<Certificate> resultQuery = em.createQuery(query);
        resultQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        resultQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(resultQuery.getResultList(), pageable, em.createQuery(queryCount).getSingleResult());
    }
}