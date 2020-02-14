import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.LineUnavailableException;

class MicrophoneExample {

    public static void main(String[] args) throws IOException, LineUnavailableException {
        MicrophoneUtil mic = new MicrophoneUtil();
        Thread recording = new Thread(new Runnable(){
        
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                System.out.println("write to file");
                try {
                    mic.record(AudioFileFormat.Type.WAVE, new File("audio.wav"));
                    System.out.println("done");
                    mic.finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        recording.start();
        System.out.println("recording start");
        mic.capture();
        
    }
}