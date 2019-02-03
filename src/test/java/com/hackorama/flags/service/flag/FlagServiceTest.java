package com.hackorama.flags.service.flag;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import com.hackorama.flags.common.TestUtil;
import com.hackorama.flags.data.MemoryDataStore;

/**
 * Tests for Flag service using default in memory data store
 *
 * @author Kishan Thomas (kishan.thomas@gmail.com)
 *
 */
public class FlagServiceTest {

    private static final String DEFAULT_SERVER_ENDPOINT = TestUtil.defaultServerEndpoint();

    protected void setDataStore() throws SQLException {
        TestUtil.setDataStore(new MemoryDataStore());
    }

    @Before
    public void setUp() throws Exception {
        setDataStore();
        TestUtil.initFlagServiceInstance();
    }

    @After
    public void tearDown() throws Exception {
        TestUtil.clearDataOfServiceInstance();
    }

    @AfterClass
    public static void afterAllTests() throws Exception {
        TestUtil.stopServiceInstance();
    }

    @Test
    public void getFlagsThroughDifferentAPIs_expectsAllFlagsReturnedToBeSame() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse;
        jsonResponse = Unirest.get(DEFAULT_SERVER_ENDPOINT + "/flags").asJson();
        String flagFromGetAll = jsonResponse.getBody().getObject().getJSONObject("America").get("USA").toString();
        System.out.println(flagFromGetAll);
        jsonResponse = Unirest.get(DEFAULT_SERVER_ENDPOINT + "/flags/USA").asJson();
        String flagFromGetByCountry = jsonResponse.getBody().getObject().get("USA").toString();
        System.out.println(flagFromGetByCountry);
        jsonResponse = Unirest.get(DEFAULT_SERVER_ENDPOINT + "/flags/America").asJson();
        String flagFromContinent = jsonResponse.getBody().getObject().get("USA").toString();
        System.out.println(flagFromContinent);
        assertEquals(flagFromGetByCountry, flagFromContinent, flagFromGetAll);
    }

}
