package com.hackorama.flags.service.flag;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hackorama.flags.common.HttpMethod;
import com.hackorama.flags.common.Request;
import com.hackorama.flags.common.Response;
import com.hackorama.flags.common.Util;
import com.hackorama.flags.data.DataStore;
import com.hackorama.flags.data.MemoryDataStore;
import com.hackorama.flags.metrics.MetricsTracker;
import com.hackorama.flags.server.Server;
import com.hackorama.flags.service.Service;
import com.hackorama.flags.service.flag.data.FlagRepository;
import com.hackorama.flags.service.flag.data.loader.DataLoader;

public class FlagService implements Service {

    private static Logger logger = LoggerFactory.getLogger(FlagService.class);

    private static final String FLAGS_DATA_FILE = "continents.txt";
    private static DataStore dataStore = new MemoryDataStore();
    private static FlagRepository repository;
    private static Server server;

    public static Response createFlag(Request request) {
        return new Response(Util.toJsonString("error", "Not allowed"), HttpURLConnection.HTTP_BAD_METHOD);
    }

    public static Response deleteFlag(Request request) {
        return new Response(Util.toJsonString("error", "Not allowed"), HttpURLConnection.HTTP_BAD_METHOD);
    }

    public static Response editFlag(Request request) {
        return new Response(Util.toJsonString("error", "Not allowed"), HttpURLConnection.HTTP_BAD_METHOD);
    }

    public static Response getFlag(Request request) {
        String id = request.getParams().get("id");
        if (id == null) {
            return new Response(repository.getAllFlags());
        } else {
            String resultJson = repository.getFlagByCountryOrContinent(id);
            if (resultJson != null) {
                updateMetrics(id);
                return new Response(resultJson);
            }
        }
        return new Response(Util.toJsonString("error", "Invalid country or continent"),
                HttpURLConnection.HTTP_BAD_REQUEST);
    }

    private static void setServer(Server server) {
        FlagService.server = server;
    }

    private static void setStore(DataStore dataStore) {
        FlagService.dataStore = dataStore;
    }

    private static void updateMetrics(String countryOrContinent) {
        MetricsTracker.getInstance().updateFlagCount(countryOrContinent);
    }

    @Override
    public Service attach(Service service) {
        service.configureUsing(server);
        return this;
    }

    @Override
    public Service configureUsing(DataStore dataStore) {
        FlagService.setStore(dataStore);
        return this;
    }

    @Override
    public FlagService configureUsing(Server server) {
        FlagService.setServer(server);
        server.setRoutes(HttpMethod.GET, "/flags", FlagService::getFlag);
        server.setRoutes(HttpMethod.GET, "/flags/", FlagService::getFlag);
        server.setRoutes(HttpMethod.GET, "/flags/{id}", FlagService::getFlag);
        server.setRoutes(HttpMethod.POST, "/flags", FlagService::createFlag);
        server.setRoutes(HttpMethod.POST, "/flags/", FlagService::createFlag);
        server.setRoutes(HttpMethod.PUT, "/flags/{id}", FlagService::editFlag);
        server.setRoutes(HttpMethod.DELETE, "/flags/{id}", FlagService::deleteFlag);
        return this;
    }

    private void initData() {
        try {
            DataLoader.initializeData(repository, FLAGS_DATA_FILE);
        } catch (IOException e) {
            throw new RuntimeException("FATAL Data load failed", e);
        }
    }

    private void initMetrics() {
        MetricsTracker.getInstance().report(1);
    }

    private void initRepository() {
        repository = new FlagRepository(dataStore);
    }

    @Override
    public Service start() {
        if (server == null) {
            throw new RuntimeException("Please configure a server before starting the servoce");
        }
        logger.info("Starting flag service using server {}, data store {}", server.getClass().getName(),
                dataStore == null ? "NULL" : dataStore.getClass().getName());
        initRepository();
        initData();
        initMetrics();
        server.start();
        return this;
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

}
