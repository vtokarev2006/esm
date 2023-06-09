package com.epam.esm.legacy.repository.rowmappers;

import com.epam.esm.domain.Certificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Deprecated
public class CertificateRowMapper implements RowMapper<Certificate> {
    @Override
    public Certificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Certificate.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .duration(rs.getInt("duration"))
                .price(rs.getDouble("price"))
                .description(rs.getString("description"))
                .createDate(rs.getTimestamp("create_date").toInstant())
                .lastUpdateDate(rs.getTimestamp("last_update_date").toInstant())
                .build();
    }
}
