import javax.swing.*;

abstract class SensorLabel {

    private JLabel label;
    private Sensor sensor;
    private String unit;

    JLabel getLabel() {
        return label;
    }

    Sensor getSensor() {
        return sensor;
    }

    String getUnit() {
        return unit;
    }

    SensorLabel(Sensor sensor, String unit) {
        this.sensor = sensor;
        this.unit = unit;

        label = new JLabel();
    }

    void refreshLabel() throws Exception {
        throw new Exception("This method shouldn't be called.");
    }
}
