package com.terfezio;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTConnection {
    private final String[] DB_CREDENTIALS;
    private final String username;
    private final String password;
    private final MemoryPersistence memoryPersistence;
    private final MqttClient mqttClient;

    public MQTTConnection(String[] DB_CREDENTIALS, String broker, String username, String password, String clientId) throws MqttException {
        this.DB_CREDENTIALS = DB_CREDENTIALS;
        this.username = username;
        this.password = password;
        memoryPersistence = new MemoryPersistence();
        mqttClient = new MqttClient(broker, clientId, memoryPersistence);
        connect();
    }
    private void connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(60);
        options.setKeepAliveInterval(60);
        mqttClient.setCallback(new OnMessageCallback(DB_CREDENTIALS, memoryPersistence));
        mqttClient.connect(options);
    }
    public MqttClient getMqttClient() {
        return mqttClient;
    }

}
