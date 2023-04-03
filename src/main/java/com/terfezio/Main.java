package com.terfezio;

import com.terfezio.model.Sensor;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.*;

public class Main {
    public static final String DB_HOST = "127.0.0.1";
    public static final String DB_NAME = "sensors";
    public static final String DB_PORT = "3306";
    public static final String DB_USER_NAME = "root";
    public static final String DB_PASSWORD = "";
    public static final String[] SENSOR_TOPICS = {
            "tele/tasmota_1C1E96/SENSOR",
            "tele/tasmota_1B1E96/SENSOR"
    };
    public static final String BROKER = "tcp://192.168.1.146:1883";
    public static final String BROKER_USER_NAME = "";
    public static final String BROKER_PASSWORD = "";
    public static final String CLIENT_ID = "mqttToSQL";
    public static void main(String[] args) throws MqttException {
        final String JDBC_URL = "jdbc:mariadb://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
        System.out.println(JDBC_URL);



        for (String topic : SENSOR_TOPICS) {
            try {
                Connection dbConnection = DriverManager.getConnection(JDBC_URL, DB_USER_NAME, DB_PASSWORD);
                MQTTConnection mqttConnection = new MQTTConnection(dbConnection, BROKER, BROKER_USER_NAME, BROKER_PASSWORD, CLIENT_ID + topic);
                BME280ToSQL bme280ToSQL = new BME280ToSQL(dbConnection, mqttConnection, topic, 0, "Primer sensor");
                bme280ToSQL.start();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }





    }


}