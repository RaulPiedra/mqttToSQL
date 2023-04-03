package com.terfezio;

import com.google.gson.Gson;
import com.terfezio.model.Sensor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.*;
import java.util.StringTokenizer;

public class OnMessageCallback implements MqttCallback {
    private Connection dbConnection;
    public OnMessageCallback(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("connection lost: " + throwable.getCause());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

        System.out.println("topic: " + s);
        System.out.println("Qos: " + mqttMessage.getQos());
        //System.out.println("message content: " + new String(mqttMessage.getPayload()));
        String tableName = getTableName(s);
        String message = new String(mqttMessage.getPayload());
        System.out.println(message);
        Gson gson = new Gson();
        Sensor sensor = gson.fromJson(message, Sensor.class);
        createTable(tableName);
        writeToDB(sensor, tableName);
        System.out.println("Temperature: " + sensor.getBme280().getTemperature());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

        System.out.println("delivery complete: " + iMqttDeliveryToken.isComplete());
    }
    public static String getTableName(String topic) {
        String[] splits = topic.split("/");
        return splits[1];
    }
    public void writeToDB(Sensor sensor, String tableName) throws SQLException {
        String query = "INSERT INTO " + tableName + " VALUES (null, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
        preparedStatement.setString(1, sensor.getTime());
        preparedStatement.setFloat(2, sensor.getBme280().getTemperature());
        preparedStatement.setFloat(3, sensor.getBme280().getHumidity());
        preparedStatement.setFloat(4, sensor.getBme280().getDewPoint());
        preparedStatement.setFloat(5, sensor.getBme280().getPressure());
        preparedStatement.executeUpdate();
        preparedStatement.close();

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

}
