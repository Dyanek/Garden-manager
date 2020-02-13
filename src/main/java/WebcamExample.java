import com.github.sarxos.webcam.WebcamDiscoveryEvent;

class WebcamExample {

    private static Void connected(WebcamDiscoveryEvent e){
        System.out.println(e.getWebcam().getName() + " connected");
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