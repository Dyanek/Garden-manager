import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.util.Scanner;

class Arduino {

    private SerialPort serialPort;

    Arduino() {
        serialPort = SerialPort.getCommPort("/dev/ttyACM0");
        serialPort.setComPortParameters(9600, 8, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 3527, 3527);

        try {
            serialPort.openPort();
            System.out.println("Port is open");
        } catch (Exception ex) {
            System.out.println("Failed to open port: " + ex);
        }
    }

    void SendMessageToArduino(byte[] message) {
        try {
            serialPort.getOutputStream().write(message);
            serialPort.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static float getValue(String str) {
        StringBuilder res = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            if (str.charAt(j) >= 48 && str.charAt(j) <= 57) {
                res.append(str.charAt(j));
            }
        }
        return res.toString().equals("") ? 0.0f : Float.parseFloat(res.toString());
    }

    void getMessageFromArduino(Garden garden, GardenHCI gardenHCI) {
        try {
            while (true) {
                while (serialPort.bytesAvailable() == 0) {
                    Thread.sleep(1000);
                }

                Scanner data = new Scanner(serialPort.getInputStream());

                while (data.hasNext()) {
                    String str = data.nextLine();
                    System.out.println(str);

                    if (str.contains("PHThree")) {
                        float ph = getValue(str) / 100;
                        garden.getFloor(3).getAciditySensor().addValue(ph);
                    }

                    if (str.contains("LightOne")) {
                        float light = getValue(str);
                        garden.getFloor(1).getBrightnessSensor().addValue(light);
                        gardenHCI.refreshFloorSensorLabel(BrightnessSensor.class, 1);
                    }

                    if (str.contains("WaterOne")) {
                        float water = getValue(str) / 100;
                        garden.getFloor(1).getWaterSensor().addValue(water);
                    }

                    if (str.contains("WaterTwo")) {
                        float water = getValue(str) / 100;
                        garden.getFloor(2).getWaterSensor().addValue(water);
                    }

                    if (str.contains("Temperature")) {
                        float temperature = getValue(str);
                        garden.getFloor(2).getAciditySensor().addValue(temperature);
                        gardenHCI.refreshGardenSensorLabel(TemperatureSensor.class);
                    }

                    if (str.contains("Humidity")) {
                        float humidity = getValue(str);
                        garden.getHumiditySensor().addValue(humidity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
