package com.hackorama.flags.server;


import java.util.function.Function;

import com.hackorama.flags.common.HttpMethod;

public interface Server {

    public boolean start();

    public void stop();

    public String getName();

    public void setRoutes(HttpMethod method, String path, Function<com.hackorama.flags.common.Request, com.hackorama.flags.common.Response> handler);

}
