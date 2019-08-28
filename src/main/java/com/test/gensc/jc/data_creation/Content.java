package com.test.gensc.jc.data_creation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Content {

    @JsonProperty("temperature_f")
    private double temperature_f;

    @JsonProperty("time_of_measurement")
    private String time_of_measurement;


    public Content() {
    }


    public Content(double temperature_f) {
        this.temperature_f = temperature_f;
    }

    public Content(double temperature_f, String time_of_measurement) {
        this.temperature_f = temperature_f;
        this.time_of_measurement = time_of_measurement;
    }

    public double getTemperature_f() {
        return temperature_f;
    }


    public void setTemperature_f(double temperature_f) {
        this.temperature_f = temperature_f;
    }

    public String getTime_of_measurement() {
        return time_of_measurement;
    }

    public void setTime_of_measurement(String time_of_measurement) {
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
