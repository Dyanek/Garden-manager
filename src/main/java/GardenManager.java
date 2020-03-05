import com.github.sarxos.webcam.Webcam;

import java.time.Instant;
import java.util.*;

public class GardenManager {
    private Garden garden;
    private GardenHCI gardenHCI;
    private ArduinoHelper arduinoHelper;


    public static void main(String[] args) {
        GardenManager gardenManager = new GardenManager();

    }

    private GardenManager() {
        this.garden = initGarden();
        this.gardenHCI = new GardenHCI(garden);

        this.arduinoHelper = new ArduinoHelper();
        arduinoHelper.getMessageFromArduino(garden, gardenHCI);

        gardenHCI.launchHCI();
        getCommand();
    }

    // -- TEST a l'aide console --
    private void getCommand() {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals("exit")) {
            System.out.print("Dis-moi, qu'est-ce que tu veux hen ???(\"help\" if u need help O.O) : ");
            input = scanner.nextLine();
            processCommand(input);
        }
        System.out.println("Bye, à plus ^^");
        System.exit(0);
    }

    private void processCommand(String c) {
        String[] words = c.split(" ");

        int floorId = -1;

        if (words.length == 2) {
            String firstWord = words[0].toLowerCase();
            switch (firstWord) {
                case "temperature"://TODO : a supprimer à la fin, cela est sert à test sans arduino
                    garden.getTemperatureSensor().addValue(Float.valueOf(words[1]));
                    gardenHCI.refreshGardenSensorLabel(TemperatureSensor.class);
                    break;
                case "humidite"://TODO : a supprimer à la fin, cela est sert à test sans arduino
                    garden.getHumiditySensor().addValue(Float.valueOf(words[1]));
                    gardenHCI.refreshGardenSensorLabel(HumiditySensor.class);
                    break;
                case "watersensorvaluefloor1": //TODO : a supprimer à la fin, cela est sert à test sans arduino
                    garden.getFloor(1).getWaterSensor().addValue(Float.valueOf(words[1]));
                    gardenHCI.refreshFloorSensorLabel(WaterSensor.class, 1);
                    break;
                case "watersensorvaluefloor2": //TODO : a supprimer à la fin, cela est sert à test sans arduino
                    garden.getFloor(2).getWaterSensor().addValue(Float.valueOf(words[1]));
                    gardenHCI.refreshFloorSensorLabel(WaterSensor.class, 2);
                    break;
                case "watersensorvaluefloor3": //TODO : a supprimer à la fin, cela est sert à test sans arduino
                    garden.getFloor(3).getWaterSensor().addValue(Float.valueOf(words[1]));
                    gardenHCI.refreshFloorSensorLabel(WaterSensor.class, 3);
                case "arroser":
                    if (words[1].toLowerCase().equals("tout"))
                        gardenHCI.displayActionOnAllFloorsPanel(GardenHCI.ActionType.WATERING);
                    else {
                        floorId = Integer.parseInt(words[1]);
                        gardenHCI.displayActionOnSpecificFloorPanel(GardenHCI.ActionType.WATERING, floorId);
                    }
                    break;
                case "allumer":
                    if (words[1].toLowerCase().equals("tout"))
                        gardenHCI.displayActionOnAllFloorsPanel(GardenHCI.ActionType.LIGHTING);
                    else {
                        floorId = Integer.parseInt(words[1]);
                        gardenHCI.displayActionOnSpecificFloorPanel(GardenHCI.ActionType.LIGHTING, floorId);
                    }
                    break;
                case "afficher":
                    floorId = Integer.parseInt(words[1]);
                    gardenHCI.displayFloorPanel(floorId);
                    break;
                case "arreter":
                    if (words[1].toLowerCase().equals("arrosage")) // TODO : arroser quel étage ...
                        gardenHCI.stopAction(GardenHCI.ActionType.WATERING);
                    else if (words[1].toLowerCase().equals("éclairage"))
                        gardenHCI.stopAction(GardenHCI.ActionType.LIGHTING);
                    break;
                case "historique":
                    if (words[1].equals("humidité"))
                        gardenHCI.displayChart(HumiditySensor.class, -1);
                    else if (words[1].equals("température"))
                        gardenHCI.displayChart(TemperatureSensor.class, -1);
                    break;
                default:
                    System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + c);
                    break;
            }
        } else if (words.length == 3) {
            floorId = Integer.parseInt(words[2]);
            switch (words[0].toLowerCase()) {
                case "historique":
                    if (words[1].equals("luminosité"))
                        gardenHCI.displayChart(BrightnessSensor.class, floorId);
                    else if (words[1].equals("acidité"))
                        gardenHCI.displayChart(AciditySensor.class, floorId);
                    else if (words[1].equals("eau"))
                        gardenHCI.displayChart(WaterSensor.class, floorId);
                    else {
                        System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + c);
                    }
                    break;
                default:
                    System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + c);
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
                case "test": // TODO : a suprrimer (pour tester Interface sans Arduino)
                    garden.getTemperatureSensor().getLastValues().put(Instant.now(), 5.5f);
                    gardenHCI.displayChart(TemperatureSensor.class, 0);
                    break;
                default:
                    System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + c);
                    break;
            }
        } else
            System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + c);
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
}