package com.prafful.controllers;

import com.prafful.data.model.Line;
import com.prafful.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class lineDataController {

    @Autowired
    private Server server;

    /**
     * Controller to return fetched line from the database
     *
     * @param index index of line received from the client
     * @return line data corresponding to the index or a error value with 413 status
     */
    @RequestMapping("/lines/{index}")
    public ResponseEntity<Line> lineData(@PathVariable("index") final String index) {
        server.lineData(index);
        if (server.getLineData().isPresent()) {
            return new ResponseEntity<>(server.getLineData().get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Line(index, "Index out of bound"), HttpStatus.valueOf(413));
        }
    }
}