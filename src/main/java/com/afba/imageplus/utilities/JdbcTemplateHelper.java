package com.afba.imageplus.utilities;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class JdbcTemplateHelper {

    @SuppressWarnings("deprecation")
    public <T> T queryOne(JdbcTemplate jdbcTemplate, String sql, Object[] args, RowMapper<T> rowMapper) {

        List<T> recs = jdbcTemplate.query(sql, args, rowMapper);

        if ((recs == null) || recs.isEmpty()) {
            return null;
        }

        if (recs.size() > 1) {
            throw new DataIntegrityViolationException("Query results in more than 1 record: " + recs.size());
        }

        return recs.get(0);
    }

    @SuppressWarnings("deprecation")
    public <T> List<T> queryN(JdbcTemplate jdbcTemplate, String sql, Object[] args, RowMapper<T> rowMapper) {

        List<T> recs = jdbcTemplate.query(sql, args, rowMapper);

        if ((recs == null) || recs.isEmpty()) {
            return Collections.EMPTY_LIST;
        }

        return recs;
    }

    public void updateOne(JdbcTemplate jdbcTemplate, String sql, Object[] args) {

        int rowsAffected = jdbcTemplate.update(sql, args);

        if (rowsAffected != 1) {
            throw new DataIntegrityViolationException("Update affected more than 1 row: " + rowsAffected);
        }
    }

    public void updateN(JdbcTemplate jdbcTemplate, String sql, Object[] args) {

        int rowsAffected = jdbcTemplate.update(sql, args);

        if (rowsAffected < 1) {
            throw new DataIntegrityViolationException("Update affected less than 1 row: " + rowsAffected);
        }
    }

    public void update0orN(JdbcTemplate jdbcTemplate, String sql, Object[] args) {
        jdbcTemplate.update(sql, args);
    }

    public void upsertOne(JdbcTemplate jdbcTemplate, String insertSql, Object[] insertArgs, String updateSql,
            Object[] updateArgs) {

        try {
            // Try INSERT first...
            int rowsAffected = jdbcTemplate.update(insertSql, insertArgs);

            if (rowsAffected != 1) {
                throw new DataIntegrityViolationException("Insert affected more than 1 row: " + rowsAffected);
            }

            return;

        } catch (DuplicateKeyException dke) {

            // ... then try UPDATE only if INSERT failed due to DuplicateKeyException
            int rowsAffected = jdbcTemplate.update(updateSql, updateArgs);

            if (rowsAffected != 1) {
                throw new DataIntegrityViolationException("Update affected more than 1 row: " + rowsAffected);
            }

            return;
        }
    }

    // the only difference from above method is .. it is doing update first and then
    // insert.
    public void upsert(JdbcTemplate jdbcTemplate, String insertSql, Object[] insertArgs, String updateSql,
            Object[] updateArgs) {
        // Try Update first...
        int rowsAffected = jdbcTemplate.update(updateSql, updateArgs);

        if (rowsAffected == 0) {
            int rowInsert = jdbcTemplate.update(insertSql, insertArgs);

            if (rowInsert != 1) {
                throw new DataIntegrityViolationException("Insert affected more than 1 row: " + rowsAffected);
            }
        }
    }
}
