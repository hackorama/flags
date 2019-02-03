package com.hackorama.flags.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic HTTP request, to avoid dependency on framework specific versions
 *
 * @author Kishan Thomas (kishan.thomas@gmail.com)
 *
 */
public class Request {

    private String body;
    private Map<String, String> params = new HashMap<>();

    public Request(String body, Map<String, String> params) {
        this(body);
        this.params = params;
    }
    public Request(String body) {
        this.body = body;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public Map<String, String> getParams() {
        return params;
    }
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

}
