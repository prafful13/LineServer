package com.prafful.controllers;

import com.prafful.MainApplicationClass;
import com.prafful.server.Server;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShutdownController {

    private static final Logger log = MainApplicationClass.getLog();
    @Autowired
    private Server server;

    @RequestMapping("/shutdown")
    public void shutDown() {
        server.shutdown();
        log.info("Server Emptied");

        MainApplicationClass.shutdown();
        log.info("Application ended");
    }
}
