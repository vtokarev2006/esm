package com.epam.esm.domain;

import com.epam.esm.domain.dto.TagSummaryDto;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.Instant;

@NamedQuery(name = "Tag.getTagSumOrdersPrice", query = """
        select new com.epam.esm.domain.dto.TagOrdersPriceDto(t.id, t.name, sum(o.price))
        from Order o
            join o.user u
            join o.certificate c
            join c.tags t
        where u.id = :userId
        group by t""")

@NamedNativeQuery(name = "Tag.findTagSummaryByUserId", query = """
        select tf.id as tagId, tf.name as tagName, tf.create_date as tagCreateDate, tf.last_update_date as tagLastUpdateDate, max(o1.price) as highestCost
        from (
            select t.*
            from certificates c, certificates_have_tags cht, tags t, orders o, users u
            where c.id = cht.certificate_id and cht.tag_id = t.id and o.certificate_id = c.id and o.user_id = u.id and u.id = :userId
            group by t.id
            order by count(t.id) desc, t.id
            limit 1
        ) as tf, orders o1, certificates c1, certificates_have_tags cht1, users u1
        where o1.certificate_id = c1.id and c1.id = cht1.certificate_id and cht1.tag_id = tf.id and o1.user_id = u1.id and u1.id = :userId""",
        resultSetMapping = "Mapping.TagSummaryDto")
@SqlResultSetMapping(name = "Mapping.TagSummaryDto", classes = @ConstructorResult(
        targetClass = TagSummaryDto.class,
        columns = {@ColumnResult(name = "tagId", type = Long.class),
                @ColumnResult(name = "tagName", type = String.class),
                @ColumnResult(name = "tagCreateDate", type = Instant.class),
                @ColumnResult(name = "tagLastUpdateDate", type = Instant.class),
                @ColumnResult(name = "highestCost",  type = Double.class)}))
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "tags")
public class Tag extends BaseEntity implements Serializable {
    private String name;
}