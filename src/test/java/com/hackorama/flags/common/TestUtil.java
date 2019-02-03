package com.hackorama.flags.common;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hackorama.flags.data.DataStore;
import com.hackorama.flags.data.MemoryDataStore;
import com.hackorama.flags.server.Server;
import com.hackorama.flags.server.spring.SpringServer;
import com.hackorama.flags.service.Service;
import com.hackorama.flags.service.flag.FlagService;

public class TestUtil {

    private static Logger logger = LoggerFactory.getLogger(TestUtil.class);

    private static final long DEFUALT_WAIT_SECONDS = 1;
    private static final int DEFAULT_SERVER_PORT = 8080;
    private static final String DEFAULT_SERVER_ENDPOINT = "http://127.0.0.1:" + DEFAULT_SERVER_PORT;

    private static Server server = null;
    private static DataStore dataStore = null;
    private static volatile Service service = null;

    public static void clearDataOfServiceInstance() {
        if (dataStore != null) {
            dataStore.clear();
        }
    }

    public static String defaultServerEndpoint() {
        return DEFAULT_SERVER_ENDPOINT;
    }

    public static boolean getEnv(String envName) {
        return System.getenv(envName) != null;
    }

    public static String getEnv(String envName, String defaultValue) {
        String value = System.getenv(envName);
        return (value == null) ? defaultValue : value;
    }

    public static Server getServer() {
        initServer();
        return server;
    }


    public static Service initFlagServiceInstance() {
        initServer();
        if (service == null) {
            service = new FlagService().configureUsing(server).configureUsing(dataStore).start();
            TestUtil.waitForService();
            logger.info("Started Flag Service on server {}", server.getName());
        }
        return service;
    }

    private static synchronized void initServer() {
        if (server == null) {
            server = new SpringServer("Spring Server", DEFAULT_SERVER_PORT);
            logger.info("Created Spring Server {} on {}", server.getName(), DEFAULT_SERVER_PORT);
        }
        if (dataStore == null) {
            dataStore = new MemoryDataStore();
        }
    }

    public static Service initServiceInstance() {
        return initFlagServiceInstance();
    }

    public static void stopServiceInstance() {
        if (server != null) {
            server.stop();
            TestUtil.waitForService();
        }
        dataStore = null;
        service = null;
        server = null;
    }

    public static boolean waitForSeconds(long seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    public static boolean waitForService() {
        return waitForSeconds(DEFUALT_WAIT_SECONDS);
    }

    // Don't let anyone else instantiate this class
    private TestUtil() {
    }

    public static DataStore getDataStore() {
        return dataStore;
    }

}
