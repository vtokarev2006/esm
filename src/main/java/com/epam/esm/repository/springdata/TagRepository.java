package com.epam.esm.repository.springdata;

import com.epam.esm.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
        @Query("select t, count(o) as freq from Order o join User u on(o.user = u) join Certificate c on (o.certificate = c) join c.tags t where u.id = :userId group by t order by freq desc")

/*
    @Query(value = """
            SELECT t.id as id, t.name as name, count(c.id) as count
            FROM certificates_have_tags cht
                    
            inner join tags t
            on(t.id = cht.tag_id)
                    
            inner join certificates c
            on(c.id = cht.certificate_id)
                    
            inner join orders o
            on(o.certificate_id = c.id)
                    
            inner join users u
            on(o.user_id = u.id)
                    
            where u.id = ?1
                    
            group by t.id, t.name
            order by count desc
            limit 1""", nativeQuery = true)
*/


        List<Tag> getTagsOrderedDescByFreqUsageByUserId(long userId, Pageable pageable);

}