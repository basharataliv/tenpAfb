package com.afba.imageplus.dao.impl;

import com.afba.imageplus.constants.ApplicationConstants;
import com.afba.imageplus.dao.TestDao;
import com.afba.imageplus.dto.res.TestRes;
import com.afba.imageplus.utilities.JdbcTemplateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SqlServerTestDaoImpl implements TestDao {

    private static final Logger logger = LoggerFactory.getLogger(TestDao.class);

    // Auto-wire primary jdbcTemplate for DB2
    @Autowired
    @Qualifier(ApplicationConstants.PRIMARY_JDBC_TEMPLATE_BEAN)
    JdbcTemplate JdbcTemplate;

    @Autowired
    private JdbcTemplateHelper jdbcHelper;

    private static final String TEST_TABLE_NAME = "TEST";

    private static final String INSERT_TEST_DATA = "INSERT INTO dbo." + TEST_TABLE_NAME
            + " (id, name, company) VALUES (?,?,?);";
    private static final String GET_TEST_DATA = "select * from dbo." + TEST_TABLE_NAME + ";";

    private static final RowMapper<TestRes> ROW_MAPPER_GETDATA = new RowMapper<TestRes>() {
        @Override
        public TestRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TestRes(rs.getInt("id"), rs.getString("name"), rs.getString("company"));

        }
    };

    @Override
    public void insertTestData(int id, String name, String company) {
        jdbcHelper.update0orN(JdbcTemplate, INSERT_TEST_DATA, new Object[] { id, name, company });
    }

    @Override
    public List<TestRes> get() {
        return jdbcHelper.queryN(JdbcTemplate, GET_TEST_DATA, new Object[] {}, ROW_MAPPER_GETDATA);
    }

}
