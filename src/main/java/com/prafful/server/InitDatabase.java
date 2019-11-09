package com.prafful.server;

import com.prafful.MainApplicationClass;
import com.prafful.data.model.Line;
import com.prafful.data.repo.LineRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

@Configuration
public class InitDatabase {

    private static final Logger log = LogManager.getLogger(MainApplicationClass.class);
    private static final String fileName = "TestFile_45MB";
    private static final File file = new File(MainApplicationClass.getFileName());
    private static int totalNumberOfLines;
    private static String line;
    private static FileInputStream inputStream = null;
    private static Scanner sc = null;
    private static ClassLoader classLoader;
    private static Line tempLine;
    @Autowired
    private LineRepository lineRepository;

    @Bean
    public boolean fillDatabase() {
        try {
            classLoader = MainApplicationClass.class.getClassLoader();
            System.out.println(classLoader.getResource(".").getFile());
            log.info("File loaded successfully");

            try {
                inputStream = new FileInputStream(file.getPath());
                sc = new Scanner(inputStream, "UTF-8");

                totalNumberOfLines = 0;

                while (sc.hasNextLine()) {
                    line = sc.nextLine();
                    totalNumberOfLines++;

                    tempLine = new Line(Integer.toString(totalNumberOfLines), line);

                    lineRepository.save(tempLine);
                }

                System.out.println(totalNumberOfLines);
                System.out.println(lineRepository.count());

                // note that Scanner suppresses exceptions
                if (sc.ioException() != null) {
                    throw sc.ioException();
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (sc != null) {
                    sc.close();
                }
            }
        } catch (final FileNotFoundException e) {
            log.error("File not found\n", e);
        } catch (final IOException e) {
            log.error("IOException occurred\n", e);
        }
        return true;
    }
}
