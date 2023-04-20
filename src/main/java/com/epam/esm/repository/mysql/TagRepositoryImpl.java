package com.epam.esm.repository.mysql;

import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.TagDuplicateNameException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.rowmappers.TagRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private static final String SQL_GET_BY_ID = "SELECT * FROM tags WHERE id = ?";
    private static final String SQL_GET_BY_NAME = "SELECT * FROM tags WHERE name = ?";
    private static final String SQL_GET_ALL = "SELECT * FROM tags";
    private static final String SQL_INSERT = "INSERT INTO tags (name) values (?)";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM tags WHERE id = ?";

    private static final String SQL_UPDATE_BY_ID = "UPDATE tags SET name = ? WHERE id = ?";

    final private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Tag> get(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_BY_ID, new TagRowMapper(), id));
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new TagRowMapper());
    }

    @Override
    public Tag create(Tag tag) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, tag.getName());
                return ps;
            }, generatedKeyHolder);
        } catch (DataIntegrityViolationException e) {
            throw new TagDuplicateNameException("Tag name='" + tag.getName() + "' is already exists");
        }
        tag.setId(Objects.requireNonNull(generatedKeyHolder.getKey()).longValue());
        return tag;
    }

    @Override
    public boolean update(Tag tag) {
        return jdbcTemplate.update(SQL_UPDATE_BY_ID, tag.getName(), tag.getId()) > 0;
    }

    @Override
    public boolean delete(long id) {
        return jdbcTemplate.update(SQL_DELETE_BY_ID, id) > 0;
    }

    @Override
    public Tag findByName(String name) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_NAME, new TagRowMapper(), name);
    }

}
