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

    public Service configureUsing(DataStore dataStore);

    public Service configureUsing(Server server);

    public Service start();

    public void stop();
}
