package com.terfezio;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.Connection;

public class MQTTConnection {
    private final Connection dbConnection;
    private final String username;
    private final String password;
    private final MqttClient mqttClient;

    public MQTTConnection(Connection dbConnection, String broker, String username, String password, String clientId) throws MqttException {
        this.dbConnection = dbConnection;
        this.username = username;
        this.password = password;
        mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
        connect();

    }
    private void connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(60);
        options.setKeepAliveInterval(60);
        mqttClient.setCallback(new OnMessageCallback(dbConnection));
        mqttClient.connect(options);
    }
    public MqttClient getMqttClient() {
        return mqttClient;
    }

}
