package com.test.gensc.jc.data_creation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Content {

    @JsonProperty("temperature_f")
    private double temperature_f;

    @JsonProperty("time_of_measurement")
    private Timestamp time_of_measurement;


    public Content() {
    }


    public Content(double temperature_f) {
        this.temperature_f = temperature_f;
    }

    public Content(double temperature_f, Timestamp time_of_measurement) {
        this.temperature_f = temperature_f;
        this.time_of_measurement = time_of_measurement;
    }

    public double getTemperature_f() {
        return temperature_f;
    }


    public void setTemperature_f(double temperature_f) {
        this.temperature_f = temperature_f;
    }

    public Timestamp getTime_of_measurement() {
        return time_of_measurement;
    }

    public void setTime_of_measurement(Timestamp time_of_measurement) {
        this.time_of_measurement = time_of_measurement;
    }

    @Override
    public String toString() {
        return "Content{" +
                "temperature_f=" + temperature_f +
                ", time_of_measurement=" + time_of_measurement +
                '}';
    }

}
