package com.terfezio;

import com.google.gson.Gson;
import com.terfezio.model.Sensor;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Main {
    public static final String BROKER = "tcp://192.168.1.146:1883";
    public static final String USER_NAME = "";
    public static final String PASSWORD = "";
    public static final String CLIENT_ID = "mqttToSQL";
    public static void main(String[] args) throws MqttException {
        MQTTConnection mqttConnection = new MQTTConnection(BROKER, USER_NAME, PASSWORD, CLIENT_ID);
        BME280ToSQL bme280ToSQL = new BME280ToSQL(mqttConnection, "tele/tasmota_1C1E96/SENSOR", 0, "Primer sensor");
        bme280ToSQL.start();

    }

}