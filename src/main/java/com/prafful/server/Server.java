package com.prafful.server;

import com.prafful.data.model.Line;
import com.prafful.data.repo.LineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * Server class used to interact with the redis database
 */
@Configuration
public class Server {

    private static Optional<Line> lineData;

    @Autowired
    private LineRepository lineRepository;

    public void lineData(final String index) {
        lineData = lineRepository.findById(index);
    }

    public Optional<Line> getLineData() {
        return lineData;
    }

    public void shutdown() {
        final long size = lineRepository.count();
        for (int i = 1; i < size; i++) lineRepository.deleteById(Integer.toString(i));
        if (lineRepository.count() != 0) lineRepository.deleteAll();
    }
}