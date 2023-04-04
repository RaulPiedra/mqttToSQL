package com.terfezio;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.sql.*;

public class BME280ToSQL extends Thread{
    private final Connection dbConnection;
    private final MQTTConnection mqttConnection;
    private final String topic;
    private final int qos;

    public  BME280ToSQL(Connection dbConnection, MQTTConnection mqttConnection, String topic, int qos) {
        this.dbConnection = dbConnection;
        this.mqttConnection = mqttConnection;
        this.topic = topic;
        this.qos = qos;
    }
    public void run() {

        try {
            String tableName = getTableName(topic);
            createTable(tableName);
            mqttConnection.getMqttClient().subscribe(topic, qos);
        } catch (SQLException | MqttException e) {
             e.printStackTrace();
        }
    }
    public void closeConnections() throws SQLException, MqttException {
        dbConnection.close();
        mqttConnection.getMqttClient().disconnect();
        mqttConnection.getMqttClient().close();
        System.out.println("Connections closed");
    }
    public void createTable(String tableName) throws SQLException {
        DatabaseMetaData metaData = dbConnection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, new String[] {"TABLE"});
        if(!resultSet.next()) {
            String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INTEGER PRIMARY KEY AUTO_INCREMENT, time DATETIME, temperature FLOAT, humidity FLOAT, dewpoint FLOAT, pressure FLOAT)";
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate(query);
        }


    }
    public static String getTableName(String topic) {
        String[] splits = topic.split("/");
        return splits[1];
    }

}
