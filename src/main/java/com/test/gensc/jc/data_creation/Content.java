package com.test.gensc.jc.data_creation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Content {


    @JsonProperty("temperature_f")
    private double temperature_f;


    public Content() {
    }

    public Content(double temperature_f) {
        this.temperature_f = temperature_f;
    }

    public double getTemperature_f() {
        return temperature_f;
    }


    public void setTemperature_f(double temperature_f) {
        this.temperature_f = temperature_f;
    }

    @Override
    public String toString() {
        return "Content{" +
                "temperature_f=" + temperature_f +
                '}';
    }
}
