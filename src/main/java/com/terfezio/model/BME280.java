package com.terfezio.model;

public class BME280 {
    private float Temperature;
    private float Humidity;
    private float DewPoint;
    private float Pressure;

    public BME280(float temperature, float humidity, float dewPoint, float pressure) {
        this.Temperature = temperature;
        this.Humidity = humidity;
        this.DewPoint = dewPoint;
        this.Pressure = pressure;
    }
    public float getTemperature() {
        return Temperature;
    }

    public void setTemperature(float temperature) {
        this.Temperature = temperature;
    }

    public float getHumidity() {
        return Humidity;
    }

    public void setHumidity(float humidity) {
        this.Humidity = humidity;
    }

    public float getDewPoint() {
        return DewPoint;
    }

    public void setDewPoint(float dewPoint) {
        this.DewPoint = dewPoint;
    }

    public float getPressure() {
        return Pressure;
    }

    public void setPressure(float pressure) {
        this.Pressure = pressure;
    }
}
