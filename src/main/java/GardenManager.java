import com.github.sarxos.webcam.Webcam;
import org.eclipse.paho.client.mqttv3.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GardenManager implements MqttCallback {

    private static final String SERVER_URI = "tcp://127.0.0.1";
    private static final String TOPIC_NAME = "speech";


    private GardenHCI gardenHCI;
    private User user;
    private Garden garden;
    private Arduino arduino;


    private HashMap<String, Action> actions;

    public static void main(String[] args) {
        MqttClient client;

        GardenManager gardenManager = new GardenManager();

        try {
            client = new MqttClient(SERVER_URI, MqttClient.generateClientId());
            client.setCallback(gardenManager);
            client.connect();
            client.subscribe(TOPIC_NAME);
        } catch (MqttException mqttException) {
            Logger.getLogger(GardenManager.class.getName()).log(Level.SEVERE, null, mqttException);
        }

        gardenManager.runArduinoListener();
    }

    private GardenManager() {
        this.user = new User("Lorca");

        this.garden = initGarden();
        this.gardenHCI = new GardenHCI(garden, user);
        this.arduino = new Arduino();


        gardenHCI.launchHCI();

        initActions();
    }

    private void runArduinoListener() {
        arduino.getMessageFromArduino(garden, gardenHCI);
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

    private void initActions() {
        actions = new HashMap<>();

        actions.put("accueil", () -> gardenHCI.displayWelcome());
        actions.put("aide", () -> gardenHCI.displayHelp());

        actions.put("allumer tout", () -> {
            arduino.SendMessageToArduino("h".getBytes());
            gardenHCI.displayActionOnAllFloorsPanel(GardenHCI.ActionType.LIGHTING);
            user.increaseExecutedLightingsCount();
        });
        actions.put("arreter eclairage", () -> {
            arduino.SendMessageToArduino("i".getBytes());
            gardenHCI.stopAction();
            user.increaseExecutedCommandsCount();
        });
        actions.put("allumer 1", () -> {
            arduino.SendMessageToArduino("e".getBytes());
            gardenHCI.displayActionOnSpecificFloorPanel(GardenHCI.ActionType.LIGHTING, 1);
            user.increaseExecutedLightingsCount();
        });
        actions.put("allumer 2", () -> {
            arduino.SendMessageToArduino("e".getBytes());
            gardenHCI.displayActionOnSpecificFloorPanel(GardenHCI.ActionType.LIGHTING, 2);
            user.increaseExecutedLightingsCount();
        });
        actions.put("allumer 3", () -> {
            arduino.SendMessageToArduino("e".getBytes());
            gardenHCI.displayActionOnSpecificFloorPanel(GardenHCI.ActionType.LIGHTING, 3);
            user.increaseExecutedLightingsCount();
        });

        actions.put("arroser tout", () -> {
            arduino.SendMessageToArduino("j".getBytes());
            gardenHCI.displayActionOnAllFloorsPanel(GardenHCI.ActionType.WATERING);
            user.increaseExecutedWateringsCount();
        });
        actions.put("arreter arrosage", () -> {
            arduino.SendMessageToArduino("d".getBytes());
            gardenHCI.stopAction();
            user.increaseExecutedCommandsCount();
        });
        actions.put("arroser 1", () -> {
            arduino.SendMessageToArduino("a".getBytes());
            gardenHCI.displayActionOnSpecificFloorPanel(GardenHCI.ActionType.WATERING, 1);
            user.increaseExecutedWateringsCount();
        });
        actions.put("arroser 2", () -> {
            arduino.SendMessageToArduino("b".getBytes());
            gardenHCI.displayActionOnSpecificFloorPanel(GardenHCI.ActionType.WATERING, 2);
            user.increaseExecutedWateringsCount();
        });
        actions.put("arroser 3", () -> {
            arduino.SendMessageToArduino("c".getBytes());
            gardenHCI.displayActionOnSpecificFloorPanel(GardenHCI.ActionType.WATERING, 3);
            user.increaseExecutedWateringsCount();
        });

        actions.put("afficher statistiques", () -> gardenHCI.displayStats());

        actions.put("historique humidite", () -> gardenHCI.displayChart(HumiditySensor.class));
        actions.put("historique temperature", () -> gardenHCI.displayChart(TemperatureSensor.class));

        for (int i = 1; i <= 3; i++) {
            final int finalI = i;

            actions.put("historique acidite " + finalI, () -> gardenHCI.displayChart(AciditySensor.class, finalI));
            actions.put("historique luminosite " + finalI, () -> gardenHCI.displayChart(BrightnessSensor.class, finalI));
            actions.put("historique eau " + finalI, () -> gardenHCI.displayChart(WaterSensor.class, finalI));
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("-------------------------------------------------");
        System.out.println("Connection lost");
        System.out.println("Reason :");
        System.out.println(cause.getMessage());
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
        List<Map.Entry<String, Action>> actionsList = this.actions.entrySet().stream()
                .filter(action -> action.getKey().equals(command))
                .collect(Collectors.toList());

        if (actionsList.size() > 0)
            actionsList.forEach(action -> action.getValue().execute());
        else
            System.out.println("C'est vraiment trop difficile de comprendre votre commande -> " + command);
    }
}