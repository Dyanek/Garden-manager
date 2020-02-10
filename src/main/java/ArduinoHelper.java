import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

public class ArduinoHelper {

    private SerialPort serialPort;

    ArduinoHelper() {
        serialPort = SerialPort.getCommPort("/dev/ttyACM0");
        serialPort.setComPortParameters(9600, 8, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

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
}
