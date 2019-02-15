package com.hackorama.flags.data.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hackorama.flags.data.DataStore;

/**
 * DataStore implementation using JDBC
 *
 * @author Kishan Thomas (kishan.thomas@gmail.com)
 *
 */
public class JDBCDataStore implements DataStore {

    private static final Logger logger = LoggerFactory.getLogger(JDBCDataStore.class);

    private static final String JDBC_DML_FILE = "/jdbc.ddl";
    private static final String POSTGRESQL_DML_FILE = "/postgresql.ddl";
    private static final String DEFAULT_URL = "jdbc:h2:mem:";
    private static final String DEFAULT_DRIVER = "org.h2.Driver";
    private static final int DEFAULT_KEY_SIZE = 64;
    private static final int DEFAULT_VALUE_SIZE = 256;

    private Set<String> singleKeyStores = new HashSet<String>();
    private Set<String> multiKeyStores = new HashSet<String>();
    private QueryRunner queryRunner = new QueryRunner();
    private Connection conn;
    private boolean usingPostgresql;
    private Properties DML = new Properties();

    public JDBCDataStore() throws SQLException {
        this(DEFAULT_URL);
    }

    public JDBCDataStore(String url) throws SQLException {
        this(url, DEFAULT_DRIVER);
    }

    public JDBCDataStore(String url, String driver) throws SQLException {
        connect(url, driver);
    }

    public JDBCDataStore(String url, String driver, String user, String password) throws SQLException {
        connect(url, driver, user, password);
    }

    @Override
    public void clear() {
        // TODO
    }

    private void connect(String url, String driver) throws SQLException {
        connect(url, driver, null, null);
    }

    private void connect(String url, String driver, String user, String password) throws SQLException {
        DbUtils.loadDriver(driver);
        conn = (user == null || password == null) ? DriverManager.getConnection(url)
                : DriverManager.getConnection(url, user, password);
        logger.info("Using Database {} {}", conn.getMetaData().getDatabaseProductName(), conn.getMetaData().getDatabaseProductVersion());
        logger.info("AUDIT : Connecting to database at {} as {}", url, user == null ? "default user" : user);
        usingPostgresql = conn.getMetaData().getURL().toLowerCase().contains("postgresql"); // TODO improve
        initDML();
    }

    @Override
    public boolean contains(String store, String key) {
        if (!ifTableExists(store)) {
            return false; // TODO info log
        }
        String selectSQL = DML.getProperty("contains").replace("_STORE_", store);
        logger.debug(selectSQL + " [" + key + "]");
        List<Object[]> result = new ArrayList<>();
        try {
            result = queryRunner.query(conn, selectSQL, new ArrayListHandler(), key);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO Custom exception and logging
        }
        return !result.isEmpty();
    }

    private void createMultiTable(String table) {
        if (!singleKeyStores.contains(table)) {
            if (multiKeyStores.contains(table)) {
                throw new RuntimeException("Another store already exists with the same name " + table);
            }
            String createTableSQL = DML.getProperty("createMultiTable").replace("_TABLE_", table)
                    .replace("_KEY_SIZE_", String.valueOf(DEFAULT_KEY_SIZE))
                    .replace("_VALUE_SIZE_", String.valueOf(DEFAULT_VALUE_SIZE));
            logger.debug(createTableSQL);
            singleKeyStores.add(table);
            try {
                queryRunner.execute(conn, createTableSQL);
            } catch (SQLException e) {
                e.printStackTrace(); // TODO Custom exception and logging
            }
        }
    }

    private void createTable(String table) {
        if (!multiKeyStores.contains(table)) {
            if (singleKeyStores.contains(table)) {
                throw new RuntimeException("Another store already exists with the same name " + table);
            }
            String createTableSQL = DML.getProperty("createTable").replace("_TABLE_", table)
                    .replace("_KEY_SIZE_", String.valueOf(DEFAULT_KEY_SIZE))
                    .replace("_VALUE_SIZE_", String.valueOf(DEFAULT_VALUE_SIZE));
            logger.debug(createTableSQL);
            multiKeyStores.add(table);
            try {
                queryRunner.execute(conn, createTableSQL);
            } catch (SQLException e) {
                e.printStackTrace(); // TODO Custom exception and logging
            }
        }
    }

    @Override
    public List<String> get(String store) {
        return getColumnValues(store, "v");
    }

    @Override
    public String get(String store, String key) {
        String result = null;
        if (!ifTableExists(store)) {
            return result; // TODO info log
        }
        String selectSQL = DML.getProperty("get").replace("_STORE_", store);
        logger.debug(selectSQL + " [" + key + "]");
        try {
            result = queryRunner.query(conn, selectSQL, new ScalarHandler<String>(), key);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO Custom exception and logging
        }
        return result;
    }

    @Override
    public List<String> getByValue(String store, String value) {
        List<String> resultList = new ArrayList<>();
        if (!ifTableExists(store)) {
            return resultList; // TODO info log
        }
        String selectSQL = DML.getProperty("getByValue").replace("_STORE_", store);
        logger.debug(selectSQL + " [" + value + "]");
        try {
            resultList = queryRunner.query(conn, selectSQL, new ColumnListHandler<String>(), value);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO Custom exception and logging
        }
        return resultList;
    }

    private List<String> getColumnValues(String store, String column) {
        List<String> resultList = new ArrayList<>();
        if (!ifTableExists(store)) {
            return resultList; // TODO info log
        }
        String selectSQL = DML.getProperty("getColumnValues").replace("_STORE_", store).replace("_COLUMN_", column);
        logger.debug(selectSQL);
        try {
            resultList = queryRunner.query(conn, selectSQL, new ColumnListHandler<String>());
        } catch (SQLException e) {
            e.printStackTrace(); // TODO Custom exception and logging
        }
        return resultList;
    }

    @Override
    public Set<String> getKeys(String store) {
        return new HashSet<String>(getColumnValues(store, "k"));
    }

    @Override
    public List<String> getMultiKey(String store, String key) {
        List<String> resultList = new ArrayList<>();
        if (!ifTableExists(store)) {
            return resultList; // TODO info log
        }
        String selectSQL = DML.getProperty("getMultiKey").replace("_STORE_", store);
        logger.debug(selectSQL + " [" + key + "]");
        try {
            resultList = queryRunner.query(conn, selectSQL, new ColumnListHandler<String>(), key);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO Custom exception and logging
        }
        return resultList;
    }

    private boolean ifTableExists(String table) {
        return singleKeyStores.contains(table) || multiKeyStores.contains(table); // TODO Check DB
    }

    private void replace(String store, String key, String value) {
        String insertSQL = DML.getProperty("replace").replace("_STORE_", store);
        logger.debug(insertSQL + " [" + key + ", " + value + "]");
        try {
            if (usingPostgresql) {
                queryRunner.execute(conn, insertSQL, key, value, value);
            } else {
                queryRunner.execute(conn, insertSQL, key, value);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // TODO Custom exception and logging
        }
    }

    private void initDML() {
        try (InputStream inputStream = getClass().getResourceAsStream(JDBC_DML_FILE)) {
            logger.info("Loading JDBC DML from {} ...", JDBC_DML_FILE);
            DML.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace(); //TODO log and throw custom exception
        }
        if (usingPostgresql) {
            logger.info("Loading Posgressql DML from {} ...", POSTGRESQL_DML_FILE);
            Properties pgProperties = new Properties();
            try (InputStream inputStream = getClass().getResourceAsStream(POSTGRESQL_DML_FILE)) {
                pgProperties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace(); // TODO log and throw custom exception
            }
            DML.putAll(pgProperties);
        }
    }

    private void insert(String store, String key, String value) {
        String insertSQL = DML.getProperty("insert").replace("_STORE_", store);
        logger.debug(insertSQL + " [" + key + ", " + value + "]");
        try {
            queryRunner.execute(conn, insertSQL, key, value);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO Custom exception and logging
        }
    }

    @Override
    public void put(String store, String key, String value) {
        createTable(store);
        replace(store, key, value);
    }

    @Override
    public void putMultiKey(String store, String key, String value) {
        createMultiTable(store);
        insert(store, key, value);
    }

    @Override
    public void remove(String store, String key) {
        if (!ifTableExists(store)) {
            return; // TODO info log
        }
        String deleteSQL = DML.getProperty("removeKey").replace("_STORE_", store);
        logger.debug(deleteSQL + " [" + key + "]");
        try {
            queryRunner.execute(conn, deleteSQL, key);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO Custom exception and logging
        }
    }

    @Override
    public void remove(String store, String key, String value) {
        if (!ifTableExists(store)) {
            return; // TODO info log
        }
        String deleteSQL = DML.getProperty("removeKeyValue").replace("_STORE_", store);
        logger.debug(deleteSQL + " [" + key + ", " + value + "]");
        try {
            queryRunner.execute(conn, deleteSQL, key, value);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO Custom exception and logging
        }
    }

    @Override
    public void close() {
        try {
            DbUtils.close(conn);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO Custom exception and logging
        }
    }

}
