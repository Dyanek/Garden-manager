import java.util.Hashtable;
import java.util.function.Function;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryEvent;
import com.github.sarxos.webcam.WebcamDiscoveryListener;

/**
 * Encapsulation of sarxos' webcam lib
 * webcam list
 * webcam connection / disconnection event listener
 * 
 * @package main/java
 * @see WebcamExample.java
 */
class WebcamUtil implements WebcamDiscoveryListener{
    private Function <WebcamDiscoveryEvent,Void> webcamConnectionEvent;
    private Function <WebcamDiscoveryEvent, Void> webcamDisconnectionEvent;

    /**
     * webcam connection event listener
     * @param {Function<WebcamDiscoveryEvent>} func
     * @return {WebcamStream}
     */
    public WebcamUtil onConnection(Function <WebcamDiscoveryEvent, Void> func){
        this.webcamConnectionEvent = func;
        return this;
    }

    /**
     * webcam disconnection event listener
     * @param {Function<WebcamDiscoveryEvent>} func
     * @return {WebcamStream}
     */
    public WebcamUtil onDisonnection(Function <WebcamDiscoveryEvent, Void> func){
        this.webcamDisconnectionEvent = func;
        return this;
    }

    /**
     * get webcam list
     * @static
     * @return
     */
    public static Hashtable<String, Webcam> getWebcamList(){
        Hashtable<String, Webcam> webcamList = new Hashtable<>();
        for(Webcam webcam : Webcam.getWebcams()){
            webcamList.put(webcam.getName(), webcam);
        }
        return webcamList;
    }

    @Override
	public void webcamFound(WebcamDiscoveryEvent event) {
		this.webcamConnectionEvent.apply(event);
	}

	@Override
	public void webcamGone(WebcamDiscoveryEvent event) {
        this.webcamDisconnectionEvent.apply(event);
    }  
}
