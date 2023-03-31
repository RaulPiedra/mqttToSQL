package com.terfezio;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTConnection {
    private String broker;
    private String username;
    private String password;
    private String clientId;
    private MqttClient mqttClient;

    public MQTTConnection(String broker, String username, String password, String clientId) {
        this.broker = broker;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        try {
            mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
            connect();
        } catch (MqttException e) {
            e.getCause();
        }
    }
    private void connect() throws MqttException {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(60);
        options.setKeepAliveInterval(60);
        mqttClient.setCallback(new OnMessageCallback());
        mqttClient.connect(options);
    }
    public MqttClient getMqttClient() {
        return mqttClient;
    }

}
