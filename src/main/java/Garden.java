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
        humiditySensor.addValue(25.7f);

        this.temperatureSensor = new TemperatureSensor();
        temperatureSensor.addValue(17.1f);
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

    Set<Floor> getAllFloors() {
        return allFloors;
    }

    Floor getFloor(int floorId) {
        for (Floor floor : allFloors) {
            if (floor.getFloorId() == floorId)
                return floor;
        }

        return null;
    }
}
