import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

class ArduinoHelper {

    private SerialPort serialPort;

    ArduinoHelper() {
        Config cfg = new Config();
        final String port = cfg.getProperty("port");
        final int baudRate = Integer.valueOf(cfg.getProperty("baudRate"));

        serialPort = SerialPort.getCommPort(port);
        serialPort.setComPortParameters(baudRate, 8, 1, 0);
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
