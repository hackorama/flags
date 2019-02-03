package com.hackorama.flags.server;


import java.util.function.Function;

import com.hackorama.flags.common.HttpMethod;

/**
 * Web server with REST handler routing
 *
 * @author Kishan Thomas (kishan.thomas@gmail.com)
 *
 */
public interface Server {

    public boolean start();

    public void stop();

    public String getName();

    public void setRoutes(HttpMethod method, String path, Function<com.hackorama.flags.common.Request, com.hackorama.flags.common.Response> handler);

}
