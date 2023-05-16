package com.epam.esm.repository.rowmappers;

import com.epam.esm.domain.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Deprecated
public class TagRowMapper implements RowMapper<Tag> {
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Tag.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}
