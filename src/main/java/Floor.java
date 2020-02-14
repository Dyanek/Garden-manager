import com.github.sarxos.webcam.Webcam;

class Floor {
    private int floorId;
    private boolean pumpIsActive; // Useful ?
    private boolean lightIsActive; // Useful ?
    private Webcam camera;

    private AciditySensor aciditySensor;
    private BrightnessSensor brightnessSensor;
    private WaterSensor waterSensor;

    public int getFloorId() {
        return floorId;
    }

    public Webcam getCamera() {
        return camera;
    }

    public AciditySensor getAciditySensor() {
        return aciditySensor;
    }

    public BrightnessSensor getBrightnessSensor() {
        return brightnessSensor;
    }

    public WaterSensor getWaterSensor() {
        return waterSensor;
    }

    public Floor(int floorId, String webcamName) {
        this.floorId = floorId;
        this.pumpIsActive = false;
        this.lightIsActive = false;
        this.camera = Webcam.getWebcamByName(webcamName);

        this.aciditySensor = new AciditySensor();
        this.brightnessSensor = new BrightnessSensor();
        this.waterSensor = new WaterSensor();
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
