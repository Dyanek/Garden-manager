import com.github.sarxos.webcam.Webcam;

class Floor {
    private int floorId;
    private Webcam camera;

    private AciditySensor aciditySensor;
    private BrightnessSensor brightnessSensor;
    private WaterSensor waterSensor;

    int getFloorId() {
        return floorId;
    }

    Webcam getCamera() {
        return camera;
    }

    AciditySensor getAciditySensor() {
        return aciditySensor;
    }

    BrightnessSensor getBrightnessSensor() {
        return brightnessSensor;
    }

    WaterSensor getWaterSensor() {
        return waterSensor;
    }

    Floor(int floorId, Webcam webcam) {
        this.floorId = floorId;
        this.camera = webcam;

        this.aciditySensor = new AciditySensor();
        aciditySensor.addValue(7.1f);

        this.brightnessSensor = new BrightnessSensor();
        brightnessSensor.addValue(72.2f);

        this.waterSensor = new WaterSensor();
        waterSensor.addValue(5.5f);
    }

    void turnOnLight() {
        // TODO
    }

    void turnOffLight() {
        // TODO
    }

    void startWater() {
        // TODO
    }

    void stopWater() {
        // TODO
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Floor)) return false;
        Floor floor = (Floor) o;
        return floorId == floor.floorId;
    }
}
