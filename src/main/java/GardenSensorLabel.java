import javax.swing.*;
import java.awt.*;

class GardenSensorLabel extends SensorLabel {
    private String labelName;

    GardenSensorLabel(Sensor sensor, String labelName, String unit) {
        super(sensor, unit);
        this.labelName = labelName;

        JLabel sensorLabel = getLabel();
        sensorLabel.setFont(new Font("Serif", Font.BOLD, 12));
        sensorLabel.setText(labelName + " : " + sensor.getCurrentValue() + unit);
    }

    @Override
    void refreshLabel() {
        getLabel().setText(labelName + " : " + getSensor().getCurrentValue() + getUnit());
    }
}
