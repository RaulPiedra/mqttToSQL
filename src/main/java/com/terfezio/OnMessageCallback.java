package com.terfezio;

import com.google.gson.Gson;
import com.terfezio.model.Sensor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OnMessageCallback implements MqttCallback {
    private final String[] DB_CREDENTIALS;
    private final MemoryPersistence memoryPersistence;
    public OnMessageCallback(String[] DB_CREDENTIALS, MemoryPersistence memoryPersistence) {
        this.DB_CREDENTIALS = DB_CREDENTIALS;
        this.memoryPersistence = memoryPersistence;
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
        String tableName = BME280ToSQL.getTableName(s);
        String message = new String(mqttMessage.getPayload());
        System.out.println(message);
        Gson gson = new Gson();
        Sensor sensor = gson.fromJson(message, Sensor.class);
        writeToDB(sensor, tableName);
        memoryPersistence.clear();
        System.out.println("Temperature: " + sensor.getBme280().getTemperature());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

        System.out.println("delivery complete: " + iMqttDeliveryToken.isComplete());
    }

    public void writeToDB(Sensor sensor, String tableName) throws SQLException {
        try (Connection dbConnection = DriverManager.getConnection(DB_CREDENTIALS[0], DB_CREDENTIALS[1], DB_CREDENTIALS[2])) {
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
    }
}
