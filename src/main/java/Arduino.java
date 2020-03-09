import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.Short.valueOf;

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
        if (!res.toString().equals("")) {
            return Float.parseFloat(res.toString());
        } else {
            return (float) -1.0;
        }
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
//                    System.out.println(str);

                    if (!str.equals("")) {
                        if (str.contains("PHThree")) {
                            float ph = getValue(str) / 100;
                            garden.getFloor(3).getAciditySensor().addValue(ph);
                            gardenHCI.refreshFloorSensorLabel(AciditySensor.class, 3);
                            System.out.println("ph = " + ph);
                        }

                        if (str.contains("LightOneValue")) {
                            float light = getValue(str);
                            garden.getFloor(1).getBrightnessSensor().addValue((float) light);
                            gardenHCI.refreshFloorSensorLabel(BrightnessSensor.class, 1);
                            System.out.println("lignt = " + light);
                        }

                        if (str.contains("WaterOneValue")) {
                            float water = getValue(str);
                            garden.getFloor(1).getWaterSensor().addValue(water);
                            gardenHCI.refreshFloorSensorLabel(WaterSensor.class, 1);
                            System.out.println("water 1 = " + water);
                        }

                        if (str.contains("WaterTwoValue")) {
                            float water = getValue(str);
                            garden.getFloor(2).getWaterSensor().addValue(water);
                            gardenHCI.refreshFloorSensorLabel(WaterSensor.class, 2);
                            System.out.println("water 2 = " + water);
                        }

                        if (str.contains("Temperature")) {
                            float temperature = getValue(str) / 100;
                            garden.getTemperatureSensor().addValue(temperature);
                            gardenHCI.refreshGardenSensorLabel(TemperatureSensor.class);
                        }

                        if (str.contains("Humidity")) {
                            float humidity = getValue(str) / 100;
                            garden.getHumiditySensor().addValue(humidity);
                            gardenHCI.refreshGardenSensorLabel(HumiditySensor.class);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
