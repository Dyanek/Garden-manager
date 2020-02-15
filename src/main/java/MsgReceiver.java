import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

class MsgReceiver implements MqttCallback {
    private static final String MQTTSERVER = "tcp://localhost:12345";
    private static final String SPEECH_TO_TEXT = "speech-to-text";
    private static final int QOS = 2;

    private MqttClient client;

    public MsgReceiver(){
        try {
            client = new MqttClient(MQTTSERVER, MqttClient.generateClientId());
            client.setCallback(this);
            client.connect();
            client.subscribe(SPEECH_TO_TEXT, QOS);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("-------------------------------------------------");
        System.out.println("Connection lost");
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("-------------------------------------------------");
        System.out.println("Delivery complete");
        System.out.println("-------------------------------------------------");
    }
}