package com.gensc.jc.data_creation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorData {

    @JsonProperty("id")
    private long id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("content")
    private Content content;

    public SensorData() {
    }

    public SensorData(long id, String type, double temp) {
        this.id = id;
        this.type = type;
        this.content = new Content(temp);
    }

    public SensorData(long id, String type, double temp, String time_of_measurement) {
        this.id = id;
        this.type = type;
        this.content = new Content(temp, time_of_measurement);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", content=" + content +
                '}';
    }

}
