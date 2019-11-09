package com.prafful.data.model;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Line")
public class Line implements Serializable {

    private String id;
    private String lineData;

    public Line(final String id, final String lineData) {
        this.id = id;
        this.lineData = lineData;
    }

    @Override
    public String toString() {
        return "Line{" + "id='" + id + '\'' + ", lineData='" + lineData + '}';
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getLineData() {
        return lineData;
    }

    public void setLineData(final String lineData) {
        this.lineData = lineData;
    }
}