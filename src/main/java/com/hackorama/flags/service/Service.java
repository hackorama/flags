package com.hackorama.flags.service;

import com.hackorama.flags.data.DataStore;
import com.hackorama.flags.server.Server;

public interface Service {
    public Service attach(Service service);

    public default Service configureUsing(DataStore dataStore) {
        return this;
    }

    public Service configureUsing(Server server);

    public Service start();

    public void stop();
}
