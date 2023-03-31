package com.terfezio;

import com.google.gson.Gson;
import com.terfezio.model.Sensor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class OnMessageCallback implements MqttCallback {
    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("connection lost: " + throwable.getCause());
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

        System.out.println("topic: " + s);
        System.out.println("Qos: " + mqttMessage.getQos());
        //System.out.println("message content: " + new String(mqttMessage.getPayload()));
        String message = new String(mqttMessage.getPayload());
        Gson gson = new Gson();
        Sensor sensor = gson.fromJson(message, Sensor.class);
        System.out.println("Temperature: " + sensor.getBme280().getTemperature());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

        System.out.println("delivery complete: " + iMqttDeliveryToken.isComplete());
    }
}
