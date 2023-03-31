package com.terfezio.model;

public class Sensor {
    private String Time;
    private BME280 BME280;

    public Sensor(String time, BME280 bme280) {
        this.Time = time;
        this.BME280 = bme280;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public BME280 getBme280() {
        return BME280;
    }

    public void setBme280(BME280 bme280) {
        this.BME280 = bme280;
    }
}
