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
 */
class WebcamUtil{
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
}
