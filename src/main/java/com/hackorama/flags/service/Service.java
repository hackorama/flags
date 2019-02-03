package com.hackorama.flags.service;

import com.hackorama.flags.data.DataStore;
import com.hackorama.flags.server.Server;

/**
 * Micro Service
 *
 * @author Kishan Thomas (kishan.thomas@gmail.com)
 *
 */
public interface Service {

    /**
     * Inject a data store implementation to be used by the service
     *
     * @param dataStore A data store
     * @return
     */
    public Service configureUsing(DataStore dataStore);

    /**
     * Inject a web server implementation to be used by the service
     *
     * @param server A web server
     * @return
     */
    public Service configureUsing(Server server);

    /**
     * Start the service
     *
     * @return
     */
    public Service start();

    /**
     * Stop the service
     */
    public void stop();
}
