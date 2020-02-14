import java.util.Hashtable;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;

class WebcamExample {

    private Hashtable<String, Webcam> webcamList = new Hashtable<>();

    private void updateWebcamList(){
        this.webcamList = WebcamUtil.getWebcamList();
    }

    private static Void connected(WebcamDiscoveryEvent e){
        System.out.println(e.getWebcam().getName() + " connected, list updated");
        return null;
    }

    private static Void disconnected(WebcamDiscoveryEvent e){
        System.out.println(e.getWebcam().getName() + " disconnected");
        return null;
    }


    public static void main(String[] args){
        WebcamUtil webcamUtil = new WebcamUtil();
        webcamUtil.onConnection(e -> connected(e))
            .onDisonnection(e -> disconnected(e));
        while(true){}
    }
}