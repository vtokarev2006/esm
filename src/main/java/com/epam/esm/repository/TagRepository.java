package com.epam.esm.repository;

import com.epam.esm.domain.Tag;
import com.epam.esm.domain.dto.TagSummaryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Deprecated
    @Query(value = """
            select  t.*
            from orders o, certificates c, certificates_have_tags cht, tags t, users u
            where o.certificate_id = c.id and c.id = cht.certificate_id and cht.tag_id = t.id and u.id = o.user_id and o.user_id =
                (
                select o1.user_id
                from orders o1
                    left outer join orders o2
                    on o1.price < o2.price
                where o2.id is null
                limit 1
                )
            group by t.id, t.name
            order by count(t.id) desc, t.name, t.id
            limit 1""", nativeQuery = true)
    Optional<Tag> findMostWidelyUsedTagOfUserWithHighestCostOfOrders();

    @Query(nativeQuery = true)
    Optional<TagSummaryDto> findTagSummaryByUserId(long userId);
}