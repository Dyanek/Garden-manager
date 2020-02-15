import com.github.sarxos.webcam.Webcam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        Garden garden = initGarden();

        GardenHCI gardenHCI = new GardenHCI(garden);

        ArduinoHelper arduinoHelper = new ArduinoHelper();
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
}