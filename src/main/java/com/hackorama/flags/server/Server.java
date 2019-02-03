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

    /**
     * Start the server
     *
     * @return
     */
    public boolean start();

    /**
     * Stop the server
     */
    public void stop();

    /**
     * Get the server name for reporting
     *
     * @return
     */
    public String getName();

    /**
     * Register a route handler
     *
     * @param method  The HTTP method
     * @param path    The request path
     * @param handler The handler function
     */
    public void setRoutes(HttpMethod method, String path,
            Function<com.hackorama.flags.common.Request, com.hackorama.flags.common.Response> handler);

}
