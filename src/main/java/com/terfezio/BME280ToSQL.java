package com.terfezio;

import com.terfezio.model.Sensor;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.*;

public class BME280ToSQL extends Thread{
    private Connection dbConnection;
    private final MQTTConnection mqttConnection;
    private final String topic;
    private final int qos;
    private String name;
    public  BME280ToSQL(Connection dbConnection, MQTTConnection mqttConnection, String topic, int qos, String name) {
        this.dbConnection = dbConnection;
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
