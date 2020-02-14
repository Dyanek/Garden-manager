import java.util.HashSet;
import java.util.Set;

class Garden {
    private Set<Floor> allFloors;
    private Set<Floor> selectedFloors;

    private HumiditySensor humiditySensor;
    private TemperatureSensor temperatureSensor;

    public HumiditySensor getHumiditySensor() {
        return humiditySensor;
    }

    public TemperatureSensor getTemperatureSensor() {
        return temperatureSensor;
    }

    Garden(Set<Floor> allFloors) {
        this.allFloors = allFloors;
        this.selectedFloors = new HashSet<>();
        this.humiditySensor = new HumiditySensor();
        this.temperatureSensor = new TemperatureSensor();
    }

    void selectFloor(Floor floor) {
        selectedFloors.add(floor);
    }

    void deselectFloor(Floor floor) {
        selectedFloors.remove(floor);
    }

    void selectAllFloors() {
        selectedFloors.addAll(allFloors);
    }
}
