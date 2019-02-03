package com.hackorama.flags.service.flag;

import java.sql.SQLException;

import com.hackorama.flags.common.TestUtil;
import com.hackorama.flags.data.mapdb.MapdbDataStore;

/**
 * Tests for Flag service using a key value data store
 *
 * @author Kishan Thomas (kishan.thomas@gmail.com)
 *
 */
public class KeyValueFlagServiceTest extends FlagServiceTest {

    @Override
    protected void setDataStore() throws SQLException {
        TestUtil.setDataStore(new MapdbDataStore());
    }

}
