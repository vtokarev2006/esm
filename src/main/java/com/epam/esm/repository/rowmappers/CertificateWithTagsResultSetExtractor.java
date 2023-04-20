package com.epam.esm.repository.rowmappers;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CertificateWithTagsResultSetExtractor implements ResultSetExtractor<List<Certificate>> {
    @Override
    public List<Certificate> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Certificate> certs = new ArrayList<>();
        long id = 0;
        int index = -1;
        while (rs.next()) {
            if (rs.getLong("id") == id) {
                Tag tag = Tag.builder()
                        .id(rs.getLong("tag_id"))
                        .name(rs.getString("tag_name"))
                        .build();
                certs.get(index).getTags().add(tag);
            } else {
                List<Tag> tags = new ArrayList<>();
                long tag_id = rs.getLong("tag_id");
                if (tag_id > 0) {
                    tags.add(Tag.builder()
                            .id(tag_id)
                            .name(rs.getString("tag_name"))
                            .build());
                }
                certs.add(Certificate.builder().id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .price(rs.getDouble("price"))
                        .duration(rs.getInt("duration"))
                        .createDate(rs.getTimestamp("create_date").toInstant())
                        .lastUpdateDate(rs.getTimestamp("last_update_date").toInstant())
                        .tags(tags)
                        .build());
                index++;
            }
            id = rs.getLong("id");
        }
        return certs;
    }
}