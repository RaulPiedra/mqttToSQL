package com.terfezio;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class FakeTasmotaBME280 {
    public static final String BROKER = "tcp://192.168.1.146:1883";
    public static final String BROKER_USER_NAME = "";
    public static final String BROKER_PASSWORD = "";
    public static final String CLIENT_ID = "Fake_mqttToSQL";
    public static void main(String[] args) throws MqttException, InterruptedException {
        MqttClient mqttClient = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(BROKER_USER_NAME);
        options.setPassword(BROKER_PASSWORD.toCharArray());
        options.setConnectionTimeout(60);
        options.setKeepAliveInterval(60);

        mqttClient.connect(options);
        String message = "{\"Time\":\"2023-04-03T22:10:47\",\"BME280\":{\"Temperature\":25.1,\"Humidity\":51.8,\"DewPoint\":14.5,\"Pressure\":1009.4},\"PressureUnit\":\"hPa\",\"TempUnit\":\"C\"}";
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(0);
        int index = 0;
        while (index < 6) {

            mqttClient.publish("tele/tasmota_1B1E96/SENSOR", mqttMessage);
            Thread.sleep(10000);
            index++;
        }
        mqttClient.disconnect();
        mqttClient.close();

    }
}
