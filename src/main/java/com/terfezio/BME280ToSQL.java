package com.terfezio;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.Connection;

public class BME280ToSQL extends Thread{
    private MQTTConnection mqttConnection;
    private String topic;
    private int qos;
    private String name;
    public  BME280ToSQL(MQTTConnection mqttConnection, String topic, int qos, String name) {
        this.mqttConnection = mqttConnection;
        this.topic = topic;
        this.qos = qos;
        this.name = name;
    }
    public void run() {
        try {
            mqttConnection.getMqttClient().subscribe(topic, qos);
        } catch (MqttException e) {
            e.getCause();
        }
    }

}
