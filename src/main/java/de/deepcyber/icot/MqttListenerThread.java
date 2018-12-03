package de.deepcyber.icot;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.Thread;

public class MqttListenerThread extends Thread {
    private boolean running = false;
    public void run() {
        IcoT.logger.info("Thread started");
        running = true;
        int cnt = 0;
        while (running) {
            cnt++;
            IcoT.logger.info("Thread running {}", cnt);
            try {
                MqttMessage msg = new MqttMessage(String.format("Pink #%d", cnt).getBytes());
                IcoT.mqttClient.publish("icot.out", msg);
            } catch (Exception e) {
                IcoT.logger.info("Could not publish");
            }
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                IcoT.logger.info("Thread got interrupted");
            }
        }
        IcoT.logger.info("Thread stopped");
    }

    public void notifyStop() {
        running = false;
    }
}
