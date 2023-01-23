package com.zdf.client.boot;

public interface Launch {
    /**
     * Start the server.
     */
    int start();

    /**
     * Destroy the server.
     */
    int destroy();

    int init();

}
