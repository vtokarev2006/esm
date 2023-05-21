package com.epam.esm.repository.jdbctemplate;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import com.epam.esm.exceptions.BadRequestException;
import com.epam.esm.exceptions.ResourceDoesNotExistException;
import com.epam.esm.exceptions.TagDuplicateNameException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.rowmappers.CertificateWithTagsResultSetExtractor;
import jakarta.persistence.EntityManager;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

@Deprecated
@Repository
@Profile("dev")
public class CertificateRepositoryJdbcTemplate implements CertificateRepository {

    final static private String SQL_GET_ALL_CERTIFICATES_WITH_TAGS = """
            SELECT c.*, t.id as tag_id, t.name as tag_name
            FROM certificates c
                left outer join certificates_have_tags cht
                on(c.id = cht.certificate_id)
                left outer join tags t
                on(cht.tag_id = t.id)""";

    final static private String SQL_GET_CERTIFICATE_BY_ID_WITH_TAGS = """
            SELECT c.*, t.id as tag_id, t.name as tag_name
            FROM certificates c
                left outer join certificates_have_tags cht
                on(c.id = cht.certificate_id)
                left outer join tags t
                on(cht.tag_id = t.id)
            where c.id = ?""";

    final static private String SQL_DELETE_BY_ID = "DELETE FROM certificates WHERE id = ?";
    final private JdbcTemplate jdbcTemplate;
    final private TagRepository tagRepository;
    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final EntityManager em;
    final private DataSource dataSource;

    @Autowired
    public CertificateRepositoryJdbcTemplate(JdbcTemplate jdbcTemplate, @Qualifier("TagRepositoryJdbcTemplate") TagRepository tagRepository, NamedParameterJdbcTemplate namedParameterJdbcTemplate, EntityManager em, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRepository = tagRepository;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.em = em;
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Certificate> fetchById(long id) {
        List<Certificate> list = jdbcTemplate.query(SQL_GET_CERTIFICATE_BY_ID_WITH_TAGS, new CertificateWithTagsResultSetExtractor(), id);
        if (list == null || list.isEmpty()) {
            throw new EmptyResultDataAccessException(1);
        } else {
            return Optional.of(list.get(0));
        }
    }

    public List<Certificate> fetchAll() {
        return fetchCertificatesBySearchParams(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), "ASC");
    }

    public List<Certificate> fetchCertificatesBySearchParams(Optional<String> tagName, Optional<String> name, Optional<String> description, Optional<String> orderBy, String orderDirection) {
        String sqlStr = SQL_GET_ALL_CERTIFICATES_WITH_TAGS;

        String whereStr = "";
        Map<String, Object> params = new HashMap<>();

        if (tagName.isPresent()) {
            whereStr = " WHERE c.id in (SELECT cht2.certificate_id FROM tags t2, certificates_have_tags cht2 WHERE t2.name = :tag_name and cht2.tag_id=t2.id  and c.id = cht2.certificate_id)     ";
            params.put("tag_name", tagName.get());
        }

        if (name.isPresent()) {
            whereStr = whereStr.isEmpty() ? "WHERE (c.name like :name)" : whereStr + " AND (c.name like :name)";
            params.put("name", "%" + name.get() + "%");
        }

        if (description.isPresent()) {
            whereStr = whereStr.isEmpty() ? "WHERE (c.description like :description)" : whereStr + " AND (c.description like :description)";
            params.put("description", "%" + description.get() + "%");
        }

        sqlStr += whereStr;

        sqlStr += orderBy.isEmpty() ? " ORDER BY c.id" : " ORDER BY " + orderBy.get() + " " + orderDirection + ", c.id";
        return namedParameterJdbcTemplate.query(sqlStr, params, new CertificateWithTagsResultSetExtractor());
    }

    @Transactional
    @Override
    public Certificate updateById(long id, Certificate certificate) {
        Certificate certificateToUpdate = em.find(Certificate.class, id);
        if (certificateToUpdate == null) {
            throw new ResourceDoesNotExistException("Certificate doesnt exist, id = ");
        }
        boolean nothingToUpdate = true;
        if (certificate.getName() != null) {
            certificateToUpdate.setName(certificate.getName());
            nothingToUpdate = false;
        }
        if (certificate.getDescription() != null) {
            certificateToUpdate.setDescription(certificate.getDescription());
            nothingToUpdate = false;
        }
        if (certificate.getDuration() != null) {
            certificateToUpdate.setDuration(certificate.getDuration());
            nothingToUpdate = false;
        }
        if (certificate.getPrice() != null) {
            certificateToUpdate.setPrice(certificate.getPrice());
            nothingToUpdate = false;
        }
        if (certificate.getTags() != null) {
            certificateToUpdate.setTags(certificate.getTags());
            nothingToUpdate = false;
        }
        if (nothingToUpdate) {
            throw new BadRequestException("Nothing to update for certificateToUpdate id = " + id);
        }
        return certificateToUpdate;
    }

    @Transactional
    @Override
    public Certificate create(Certificate certificate) {
        Instant now = Instant.now();
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("certificates").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", certificate.getName());
        parameters.put("description", certificate.getDescription());
        parameters.put("price", certificate.getPrice());
        parameters.put("duration", certificate.getDuration());
        parameters.put("create_date", now);
        parameters.put("last_update_date", now);
        certificate.setId(simpleJdbcInsert.executeAndReturnKey(parameters).longValue());
        certificate.setCreateDate(now);
        certificate.setLastUpdateDate(now);

        final List<Tag> tagsInCertificate = certificate.getTags();
        final Map<Long, Tag> tagsInDB = fetchTagsInDbMap();
        final Set<Long> tagIdsInCertsHaveTags = Collections.emptySet();
        updateTagsInDb(certificate, tagsInCertificate, tagsInDB, tagIdsInCertsHaveTags);

        return certificate;
    }

    @Transactional
    @Override
    public void update(Certificate certificate) {
        if (certificate == null || certificate.getId() == 0) {
            throw new BadRequestException("Certificate object malformed in the request or it has id = 0");
        }

        List<Certificate> listForDbCert = jdbcTemplate.query(SQL_GET_CERTIFICATE_BY_ID_WITH_TAGS, new CertificateWithTagsResultSetExtractor(), certificate.getId());
        if (listForDbCert == null || listForDbCert.isEmpty()) {
            throw new ResourceDoesNotExistException("Certificate id=" + certificate.getId() + " does not exist");
        }

        jdbcTemplate.query(SQL_GET_CERTIFICATE_BY_ID_WITH_TAGS + " FOR UPDATE", new CertificateWithTagsResultSetExtractor(), listForDbCert.get(0).getId());

        Pair<String, MapSqlParameterSource> query = buildUpdateQuery(certificate);
        namedParameterJdbcTemplate.update(query.getValue0(), query.getValue1());

        final List<Tag> tagsInCertificate = certificate.getTags();
        final Map<Long, Tag> tagsInDB = fetchTagsInDbMap();
        final Set<Long> tagIdsInCertsHaveTags = listForDbCert.get(0).getTags().stream().map(Tag::getId).collect(Collectors.toSet());

        deleteTags(certificate, tagsInCertificate, tagIdsInCertsHaveTags);
        updateTagsInDb(certificate, tagsInCertificate, tagsInDB, tagIdsInCertsHaveTags);
    }

    private Map<Long, Tag> fetchTagsInDbMap() {
        return tagRepository.fetchAll().stream().collect(Collectors.toMap(Tag::getId, Function.identity()));
    }

    private void updateTagsInDb(Certificate newCertificate, List<Tag> tagsInCertificate, Map<Long, Tag> tagsInDB, Set<Long> tagIdsInCertsHaveTags) {
        for (Tag tagInCert : tagsInCertificate) {
            if (tagsInDB.containsKey(tagInCert.getId())) {
                Tag tagInDb = tagsInDB.get(tagInCert.getId());
                if (!Objects.equals(tagInDb.getName(), tagInCert.getName())) {
                    try {
                        tagRepository.update(tagInCert);
                    } catch (DataIntegrityViolationException e) {
                        throw new TagDuplicateNameException("Tag name='" + tagInCert.getName() + "' is already exists");
                    }
                }
                if (!tagIdsInCertsHaveTags.contains(tagInCert.getId())) {
                    createCertHasTag(newCertificate.getId(), tagInCert.getId());
                }
            } else {
                try {
                    Tag newTag = tagRepository.create(tagInCert);
                    createCertHasTag(newCertificate.getId(), newTag.getId());
                } catch (DataIntegrityViolationException e) {
                    throw new TagDuplicateNameException("Tag name='" + tagInCert.getName() + "' is already exists");
                }
            }
        }
    }

    private void deleteTags(Certificate newCertificate, List<Tag> tagsInCertificate, Set<Long> tagIdsInCertsHaveTags) {
        Set<Long> tagIdsInCertificate = tagsInCertificate.stream().map(Tag::getId).collect(Collectors.toSet());
        Set<Long> tagIdsToRemove = new HashSet<>(tagIdsInCertsHaveTags);
        tagIdsToRemove.removeAll(tagIdsInCertificate);
        tagIdsToRemove.forEach(id -> jdbcTemplate.update("DELETE FROM certificates_have_tags WHERE certificate_id = ? AND tag_id = ?", newCertificate.getId(), id));
    }

    private void createCertHasTag(long cert_id, long tag_id) {
        jdbcTemplate.update("INSERT INTO certificates_have_tags (certificate_id, tag_id) VALUES (?, ?)", cert_id, tag_id);
    }

    private Pair<String, MapSqlParameterSource> buildUpdateQuery(Certificate certificate) {
        List<String> listField = new ArrayList<>();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        if (certificate.getName() != null) {
            listField.add("name");
            mapSqlParameterSource.addValue("name", certificate.getName());
        }
        if (certificate.getDescription() != null) {
            listField.add("description");
            mapSqlParameterSource.addValue("description", certificate.getDescription());
        }
        if (certificate.getPrice() != null) {
            listField.add("price");
            mapSqlParameterSource.addValue("price", certificate.getPrice());
        }
        if (certificate.getDuration() != null) {
            listField.add("duration");
            mapSqlParameterSource.addValue("duration", certificate.getDuration());
        }

        listField.add("last_update_date");
        mapSqlParameterSource.addValue("last_update_date", Timestamp.from(Instant.now()));

        mapSqlParameterSource.addValue("id", certificate.getId());

        StringJoiner sj = new StringJoiner(", ");
        listField.stream().map(s -> s + "=:" + s).forEach(sj::add);

        String sb = "update certificates set " + sj + " where id=:id";
        return new Pair<>(sb, mapSqlParameterSource);
    }

    @Override
    public boolean delete(long id) {
        return jdbcTemplate.update(SQL_DELETE_BY_ID, id) > 0;
    }
}