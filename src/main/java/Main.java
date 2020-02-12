import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Main implements MqttCallback {

    private static final String SERVER_URI = "tcp://127.0.0.1";
    private static final String TOPIC_NAME = "topic";
    private static ArduinoHelper arduinoHelper;

    public static void main(String[] args) {
        MqttClient client;

        try {
            client = new MqttClient(SERVER_URI, MqttClient.generateClientId());
            client.setCallback(new Main());
            client.connect();
            client.subscribe(TOPIC_NAME);
        } catch (MqttException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        Jardin myJardin = new Jardin();
        myJardin.laucheInterface();

        arduinoHelper = new ArduinoHelper();
        arduinoHelper.GetMessageFromArduino(myJardin);
//        System.out.println(arduinoHelper.getLight());
//        System.out.println("oooooooooooo");
//
//        while(true){
//            System.out.println(arduinoHelper.getLight() + "----------------");
//        }


    }

    public void connectionLost(Throwable cause) {
        System.out.println("-------------------------------------------------");
        System.out.println("Connection lost");
        System.out.println("-------------------------------------------------");
    }

    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");

        arduinoHelper.SendMessageToArduino(message.getPayload());
    }

    public void deliveryComplete(IMqttDeliveryToken imdt) {
        System.out.println("-------------------------------------------------");
        System.out.println("Delivery complete");
        System.out.println("-------------------------------------------------");
    }
}