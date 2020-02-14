import org.eclipse.paho.client.mqttv3.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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

        GardenHCI myGradinHCI = new GardenHCI();
        myGradinHCI.launchHCI(1, 2, 3, 4, 5, 6);

        Set<Floor> allFloors = new HashSet<>(Arrays.asList(
                new Floor(1, ""),
                new Floor(2, ""),
                new Floor(3, "")));

        Garden garden = new Garden(allFloors);

        GardenHCI myGardenHCI = new GardenHCI();
        myGardenHCI.laucheInterface();

        arduinoHelper = new ArduinoHelper();
        arduinoHelper.GetMessageFromArduino(myGardenHCI);
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