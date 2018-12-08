package de.deepcyber.icot;

import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MqttConnector implements MqttCallback {
    private IMqttClient mqttClient;
    private String publisherId;
    private String realm = "default";
    private Pattern topicPattern;

    public MqttConnector() {
        publisherId = UUID.randomUUID().toString();
        topicPattern = Pattern.compile("IcoT/"+realm+"/([^/]{1,32})/rs");
    }

    @Override
    public void connectionLost(Throwable e) {
        IcoT.logger.error("Connection to mqtt-broker lost", e);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        IcoT.logger.info("topic:'{}', payload:'{}'", topic, message.toString());
        Matcher m = topicPattern.matcher(topic);
        if (m.matches()) {
            String device = m.group(1);
            String stringValue = message.toString();
            switch (stringValue) {
                case "low":
                    IcoT.logger.info("Thing {}<--LOW", device);
                    IcoT.activationMap.put(device, false);
                    break;
                case "high":
                    IcoT.logger.info("Thing {}<--HIGH", device);
                    IcoT.activationMap.put(device, true);
                    break;
                default:
                    IcoT.logger.warn("Invalid redstone value received for thing {}: '{}'", device, stringValue);
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken t) { }

    public boolean isConnected() {
        return mqttClient!=null;
    }

    public void connect() {
        IcoT.logger.info("Connecting to mqtt broker");
        if (isConnected()) {
            IcoT.logger.error("Connecting already connected MqttConnector");
        }
        try {
            mqttClient = new MqttClient("tcp://localhost:1883", publisherId);
            mqttClient.setCallback(this);
            mqttClient.connect();
            mqttClient.subscribe(String.format("IcoT/%s/+/rs", realm));
        } catch (MqttException e) {
            IcoT.logger.error("Failed to connect to mqtt broker", e);
        }
    }

    public void disconnect() {
        if (!isConnected()) {
            IcoT.logger.error("Tried to disconnect unconnected MqttConnector");
            return;
        }
        IcoT.logger.info("Disconnecting from mqtt broker");
        try {
            mqttClient.disconnect();
            mqttClient = null;
        } catch ( MqttException e ) {
            IcoT.logger.error("Error on mqtt disconnect", e);
        }
    }

    public void publish(String topic, MqttMessage message) {
        if (mqttClient==null) {
            IcoT.logger.error("Tried to publish via mqtt while mqttClient was null");
            return;
        }
        try {
            mqttClient.publish(topic, message);
        } catch ( MqttException  e) {
            IcoT.logger.error("Could not publish mqtt message", e);
        }
    }

    public void publishRedstone(String device, boolean state) {
        String topic = String.format("IcoT/%s/%s/rs", realm, device);
        MqttMessage message = new MqttMessage((state?"high":"low").getBytes());
        publish(topic, message);
    }
}
