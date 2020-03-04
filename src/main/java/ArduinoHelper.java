import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.Short.valueOf;

class ArduinoHelper {

    private SerialPort serialPort;

    ArduinoHelper() {
        serialPort = SerialPort.getCommPort("/dev/cu.usbmodem14101");
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

    private static int getValue(String str) {
        StringBuilder res = new StringBuilder();
        for (int j = 0; j < str.length(); j++) {
            if (str.charAt(j) >= 48 && str.charAt(j) <= 57) {
                res.append(str.charAt(j));
            }
        }
        return valueOf(res.toString());
    }

    void GetMessageFromArduino(Garden garden, GardenHCI gardenHCI) {
        try {
            while (true) {
                while (serialPort.bytesAvailable() == 0) {
                    Thread.sleep(1000);
                }

                Scanner data = new Scanner(serialPort.getInputStream());

                int i = 0;
                while (data.hasNext()) {
                    String str = data.nextLine();
                    System.out.println(str);

                    if (str.contains("Light")) {
                        int light = getValue(str);

                        garden.getFloor(1).getBrightnessSensor().addValue((float) light);
                        gardenHCI.refreshFloorSensorLabel(BrightnessSensor.class, 1);
                    }

                    if (str.contains("PH")) {
                        float tmp = getValue(str);
                        float ph = tmp / 100;

                        garden.getFloor(2).getAciditySensor().addValue(ph);
                    }

                    if (str.contains("temperature")) {
                        float temperature = getValue(str);
                        garden.getFloor(2).getAciditySensor().addValue(temperature);
                        gardenHCI.refreshGardenSensorLabel(TemperatureSensor.class);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
