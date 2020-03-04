import com.github.sarxos.webcam.Webcam;
import org.eclipse.paho.client.mqttv3.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GardenManager implements MqttCallback {

    private static final String SERVER_URI = "tcp://127.0.0.1";
    private static final String TOPIC_NAME = "speech";

    private Garden garden;
    private GardenHCI gardenHCI;
    private ArduinoHelper arduinoHelper;


    public static void main(String[] args) {
        MqttClient client;

        try {
            client = new MqttClient(SERVER_URI, MqttClient.generateClientId());
            client.setCallback(new GardenManager());
            client.connect();
            client.subscribe(TOPIC_NAME);
        } catch (MqttException mqttException) {
            Logger.getLogger(GardenManager.class.getName()).log(Level.SEVERE, null, mqttException);
        }
    }

    private GardenManager() {
        this.garden = initGarden();
        this.gardenHCI = new GardenHCI(garden);

        this.arduinoHelper = new ArduinoHelper();
        arduinoHelper.GetMessageFromArduino(garden, gardenHCI);

        gardenHCI.launchHCI();
    }

    private static Garden initGarden() {
        List<Floor> floors = new ArrayList<>();
        List<Webcam> webcams = Webcam.getWebcams();

        for (int i = 1; i <= 3; i++) {
            Webcam webcam = webcams.size() >= i ? webcams.get(i - 1) : null;
            floors.add(new Floor(i, webcam));
        }

        Set<Floor> allFloors = new HashSet<>(floors);

        return new Garden(allFloors);
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("-------------------------------------------------");
        System.out.println("Connection lost");
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");
        processCommand(new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken deliveryToken) {
        System.out.println("-------------------------------------------------");
        System.out.println("Delivery complete");
        System.out.println("-------------------------------------------------");
    }

    private void processCommand(String command) {
        String[] words = command.split(" ");

        int floorId = -1;

        if (words.length == 2) {
            String firstWord = words[0].toLowerCase();
            switch (firstWord) {
                case "temperature":
                    garden.getTemperatureSensor().addValue(Float.valueOf(words[1]));
                    gardenHCI.refreshGardenSensorLabel(TemperatureSensor.class);
                    break;
                case "humidite":
                    garden.getHumiditySensor().addValue(Float.valueOf(words[1]));
                    gardenHCI.refreshGardenSensorLabel(HumiditySensor.class);
                    break;
                case "watersensorvaluefloor1":
                    garden.getFloor(1).getWaterSensor().addValue(Float.valueOf(words[1]));
                    gardenHCI.refreshFloorSensorLabel(WaterSensor.class, 1);
                    break;
                case "watersensorvaluefloor2":
                    garden.getFloor(2).getWaterSensor().addValue(Float.valueOf(words[1]));
                    gardenHCI.refreshFloorSensorLabel(WaterSensor.class, 2);
                    break;
                case "watersensorvaluefloor3":
                    garden.getFloor(3).getWaterSensor().addValue(Float.valueOf(words[1]));
                    gardenHCI.refreshFloorSensorLabel(WaterSensor.class, 3);
                case "arroser":
                    if (words[1].toLowerCase().equals("tout")) {
                        arduinoHelper.SendMessageToArduino("a".getBytes());
                        gardenHCI.displayActionOnAllFloorsPanel(GardenHCI.ActionType.WATERING);
                    }
                    else {
                        floorId = Integer.parseInt(words[1]);
                        gardenHCI.displayActionOnSpecificFloorPanel(GardenHCI.ActionType.WATERING, floorId);
                    }
                    break;
                case "allumer":
                    if (words[1].toLowerCase().equals("tout")) {
                        arduinoHelper.SendMessageToArduino("e".getBytes());
                        gardenHCI.displayActionOnAllFloorsPanel(GardenHCI.ActionType.LIGHTING);
                    }
                    else {
                        floorId = Integer.parseInt(words[1]);
                        gardenHCI.displayActionOnSpecificFloorPanel(GardenHCI.ActionType.LIGHTING, floorId);
                    }
                case "arreter":
                    if (words[1].toLowerCase().equals("arrosage"))
                        arduinoHelper.SendMessageToArduino("s".getBytes());
                    else if (words[1].toLowerCase().equals("eclairage"))
                        arduinoHelper.SendMessageToArduino("t".getBytes());
                    gardenHCI.stopAction();
                    break;
                default:
                    System.out.println("C'est vraiment trop difficile de comprendre votre commande -> " + command);
                    break;
            }
        } else if (words.length == 1) {
            switch (words[0].toLowerCase()) {
                case "aide":
                    gardenHCI.displayHelp();
                    break;
                case "accueil":
                    gardenHCI.displayWelcome();
                    break;
                case "afficher":
                    gardenHCI.displayChart(TemperatureSensor.class, 0);
                    break;
                case "test": // Code pour tester Interface sans Arduino
                    garden.getTemperatureSensor().getLastValues().put(Instant.now(), 5.5f);
                    gardenHCI.displayChart(TemperatureSensor.class, 0);
                    break;
                default:
                    System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + command);
                    break;
            }
        } else
            System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + command);
    }
}