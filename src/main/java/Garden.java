import java.util.Set;

class Garden {
    private Set<Floor> floors;

    private HumiditySensor humiditySensor;
    private TemperatureSensor temperatureSensor;

    HumiditySensor getHumiditySensor() {
        return humiditySensor;
    }

    TemperatureSensor getTemperatureSensor() {
        return temperatureSensor;
    }

    Garden(Set<Floor> floors) {
        this.floors = floors;

        this.humiditySensor = new HumiditySensor();
        humiditySensor.addValue(25.7f);

        this.temperatureSensor = new TemperatureSensor();
        temperatureSensor.addValue(17.1f);
    }

    Set<Floor> getFloors() {
        return floors;
    }

    Floor getFloor(int floorId) {
        for (Floor floor : floors) {
            if (floor.getFloorId() == floorId)
                return floor;
        }

        return null;
    }
}
