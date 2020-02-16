class FloorSensorLabel extends SensorLabel {
    private int floorId;

    public int getFloorId() {
        return floorId;
    }

    FloorSensorLabel(int floorId, Sensor sensor, String unit) {
        super(sensor, unit);
        this.floorId = floorId;

        getLabel().setText("    Étage " + floorId + " : " + sensor.getCurrentValue() + unit);
    }

    @Override
    void refreshLabel() {
        getLabel().setText("    Étage " + floorId + " : " + getSensor().getCurrentValue() + getUnit());
    }
}
