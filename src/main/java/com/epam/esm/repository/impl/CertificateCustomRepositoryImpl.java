package com.epam.esm.repository.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.ErrorCode;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.repository.CertificateCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Slf4j
@RequiredArgsConstructor
@Profile("prod")
public class CertificateCustomRepositoryImpl implements CertificateCustomRepository {
    private final EntityManager em;

    /**
     * SELECT c.*
     * FROM certificates c
     * left outer join certificates_have_tags cht
     * on(c.id = cht.certificate_id)
     * left outer join tags t
     * on(t.id = cht.tag_id)
     * where (c.name like "%partOfCertName%") and (c.description like "%partOfCertDescription%") and
     * (t.name = :tagName1 or t.name = "tagName2 ... or t.name = "tagNameN")
     * group by c.id
     * having count(t.id) = :sizeOfTagNamesSet
     * order by c.id desc
     *
     * @param name        - search certificates with name equal the value of the param
     * @param description - search certificates with description contain the value of the param
     * @param tagNames    - search certificates with tags name (and condition)
     * @param pageable    - object determinate pageable behavior
     * @return - page of Certificates meet params above
     */

    @Override
    public Page<Certificate> findCertificatesByNameDescriptionTagNames(Optional<String> name, Optional<String> description, Set<String> tagNames, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Certificate> query = cb.createQuery(Certificate.class);
        Root<Certificate> root = query.from(Certificate.class);
        Join<Certificate, Tag> tags = root.join("tags", JoinType.LEFT);

        CriteriaQuery<Long> queryCount = cb.createQuery(Long.class);
        Root<Certificate> rootCount = queryCount.from(Certificate.class);
        Join<Certificate, Tag> tagsCount = rootCount.join("tags", JoinType.LEFT);

        if (!tagNames.isEmpty()) {
            Path<Long> tagId = tags.get("id");
            Path<Long> tagIdCount = tagsCount.get("id");

            query.select(root).groupBy(root).having(cb.equal(cb.count(tagId), (long) tagNames.size()));
            queryCount.select(cb.count(rootCount)).groupBy(rootCount).having(cb.equal(cb.count(tagIdCount), (long) tagNames.size()));
        } else {
            query.select(root);
            queryCount.select(cb.count(rootCount));
        }

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

        Optional<Predicate> predicate = predicates.stream().reduce(cb::and);
        Optional<Predicate> predicateCount = predicatesCount.stream().reduce(cb::and);

        List<Predicate> tagNamePredicates = new ArrayList<>();
        List<Predicate> tagNamePredicatesCount = new ArrayList<>();

        tagNames.forEach(s -> {
            tagNamePredicates.add(cb.equal(tags.get("name"), s));
            tagNamePredicatesCount.add(cb.equal(tagsCount.get("name"), s));
        });

        Optional<Predicate> predicateTagName = tagNamePredicates.stream().reduce(cb::or);
        Optional<Predicate> predicateTagNameCount = tagNamePredicatesCount.stream().reduce(cb::or);

        if (predicate.isPresent() && predicateTagName.isPresent()) {
            query.where(cb.and(predicate.get(), predicateTagName.get()));
            queryCount.where(cb.and(predicateCount.orElseThrow(), predicateTagNameCount.orElseThrow()));
        } else if (predicate.isPresent()) {
            query.where(predicate.get());
            queryCount.where(predicateCount.orElseThrow());
        } else {
            predicateTagName.ifPresent(query::where);
            predicateTagNameCount.ifPresent(queryCount::where);
        }

        TypedQuery<Certificate> q = em.createQuery(query);
        q.setFirstResult(pageable.getPageNumber());
        q.setMaxResults(pageable.getPageSize());

        List<Certificate> certificates;
        Long count;
        try {
            count = em.createQuery(queryCount).getSingleResult();
            log.debug("{}", count);
            certificates = q.getResultList();
        } catch (NoResultException e) {
            throw new ResourceDoesNotExistException("No certificates matching your request", ErrorCode.CertificateNotExist);
        }
        return new PageImpl<>(certificates, pageable, count);
    }

    public void refresh(Certificate certificate) {
        em.refresh(certificate);
    }
}