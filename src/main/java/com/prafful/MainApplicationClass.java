package com.prafful;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApplicationClass {

    private static final Logger log = LogManager.getLogger(MainApplicationClass.class);
    private static ConfigurableApplicationContext context;
    private static String fileName;

    public static String getFileName() {
        return fileName;
    }

    public static void main(final String[] args) {
        for (final String arg :
                args) {
            System.out.println(arg);
        }
        fileName = args[0];
        context = SpringApplication.run(MainApplicationClass.class, args);

        log.info("Server Started");
    }

    public static void shutdown() {
        SpringApplication.exit(context);
    }

    public static Logger getLog() {
        return log;
    }
}