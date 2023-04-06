package com.terfezio;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String DB_HOST = "127.0.0.1";
    private static final String DB_NAME = "sensors";
    private static final String DB_PORT = "3306";
    private static final String DB_USER_NAME = "root";
    private static final String DB_PASSWORD = "";
    private static final String[] SENSOR_TOPICS = {
            "tele/tasmota_1C1E96/SENSOR",
            "tele/tasmota_1B1E96/SENSOR"
    };
    private static final String BROKER = "tcp://192.168.1.146:1883";
    private static final String BROKER_USER_NAME = "";
    private static final String BROKER_PASSWORD = "";
    private static final String CLIENT_ID = "mqttToSQL";

    public static void main(String[] args)  {
        final String JDBC_URL = "jdbc:mariadb://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
        final String[] DB_CREDENTIALS = {JDBC_URL, DB_USER_NAME, DB_PASSWORD};
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Starting program...");

        List<BME280ToSQL> sensorsThreads = new ArrayList<>();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            for (BME280ToSQL bme280ToSQL: sensorsThreads) {
                try {
                    bme280ToSQL.closeConnections();

                } catch (SQLException | MqttException e) {
                    logger.error(e.getMessage());
                }
            }
        }));
        for (String topic : SENSOR_TOPICS) {
            try {
                //Connection dbConnection = DriverManager.getConnection(JDBC_URL, DB_USER_NAME, DB_PASSWORD);
                MQTTConnection mqttConnection = new MQTTConnection(DB_CREDENTIALS, BROKER, BROKER_USER_NAME, BROKER_PASSWORD, CLIENT_ID + topic);
                BME280ToSQL bme280ToSQL = new BME280ToSQL(DB_CREDENTIALS, mqttConnection, topic, 0);
                sensorsThreads.add(bme280ToSQL);
                bme280ToSQL.start();
                logger.info("Sensor topic: " + topic + " added.");

            } catch (MqttException e) {
                logger.error(e.getMessage());
            }
        }
    }
}