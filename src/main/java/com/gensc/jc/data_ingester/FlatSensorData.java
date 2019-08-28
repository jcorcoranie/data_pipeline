package com.gensc.jc.data_ingester;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class FlatSensorData implements Serializable {

    @JsonProperty("id")
    private long id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("temperature_f")
    private double temperature_f;
    @JsonProperty("temperature_c")
    private double temperature_c;

    public FlatSensorData() {
    }

    public FlatSensorData(int id, String type, double tempf, double tempc) {
        this.id = id;
        this.type = type;
        this.temperature_f = tempf;
        this.temperature_c = tempc;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getTemperature_f() {
        return temperature_f;
    }


    public void setTemperature_f(double temperature_f) {
        this.temperature_f = temperature_f;
    }

    public double getTemperature_c() {
        return temperature_c;
    }

    public void setTemperature_c(double temperature_c) {
        this.temperature_c = temperature_c;
    }

    public String createKafkaRecordField(){
        return id + "," + type + "," + temperature_f + "," + temperature_c;
    }

    @Override
    public String toString() {
        return "FlatSensorData{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", temperature_f=" + temperature_f +
                ", temperature_c=" + temperature_c +
                '}';
    }
}
