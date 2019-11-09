package com.prafful.controllers;

import com.prafful.MainApplicationClass;
import com.prafful.server.Server;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ShutdownController {

    private static final Logger log = MainApplicationClass.getLog();
    @Autowired
    private Server server;

    /**
     * Controller to shutdown the service
     * 1. first empties the redis database
     * 2. second shut downs the server
     */
    @RequestMapping("/shutdown")
    public void shutDown() {
        server.shutdown();
        log.info("Server Emptied");

        final String[] args = new String[]{"/bin/bash", "-c", "redis-cli", "shutdown"};
        try {
            Runtime.getRuntime().exec("redis-cli shutdown").waitFor();
            log.info("Redis Server shutdown");
        } catch (final IOException | InterruptedException e) {
            log.error("error occurred shutting down redis-server", e);
        } finally {
            MainApplicationClass.shutdown();
            log.info("Application ended");
        }
    }
}
