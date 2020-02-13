import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.util.Scanner;

import static java.lang.Short.valueOf;

class ArduinoHelper {


    private SerialPort serialPort;

    int light;
    float PH;

    public int getLight() {
        return light;
    }



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

    public static int getValue(String str) {
        String res = "";
        for (int j = 0; j < str.length(); j++) {
            if (str.charAt(j) >= 48 && str.charAt(j) <= 57) {
                res += str.charAt(j);
            }
        }
        return valueOf(res);
    }

    void GetMessageFromArduino(GardenHCI gardenHCI)
    {
        try
        {
            while (true)
            {
                while (serialPort.bytesAvailable() == 0)
                {
                    Thread.sleep(1000);
                }

                Scanner data = new Scanner(serialPort.getInputStream());

                int i = 0;
                while (data.hasNext())
                {
                    String str = data.nextLine();
                    System.out.println(str);
                    if(str.contains("Light"))
                    {
                        light = getValue(str);
                        gardenHCI.uploadLuminosity(light);
//                        System.out.println("light value = " + this.light);
                    }

                    if(str.contains("PH"))
                    {
                        float tmp  = getValue(str);
                        PH = tmp/100;
                        gardenHCI.uploadPH(PH);
//                        System.out.println("PH value = " + this.PH);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
