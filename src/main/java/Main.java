import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        Set<Floor> allFloors = new HashSet<>(Arrays.asList(
                new Floor(1, ""),
                new Floor(2, ""),
                new Floor(3, "")));

        Garden garden = new Garden(allFloors);

        ArduinoHelper arduinoHelper = new ArduinoHelper();
        arduinoHelper.GetMessageFromArduino(garden);

        GardenHCI myGardenHCI = new GardenHCI(garden);
        myGardenHCI.launchHCI();
    }
}