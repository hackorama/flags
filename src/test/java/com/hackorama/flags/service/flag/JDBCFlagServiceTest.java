package com.hackorama.flags.service.flag;

import java.sql.SQLException;

import com.hackorama.flags.common.TestUtil;
import com.hackorama.flags.data.jdbc.JDBCDataStore;

/**
 * Tests for Flag service using JDBC data store
 *
 * @author Kishan Thomas (kishan.thomas@gmail.com)
 *
 */
public class JDBCFlagServiceTest extends FlagServiceTest {

    @Override
    protected void setDataStore() throws SQLException {
        TestUtil.setDataStore(new JDBCDataStore());
    }

}
