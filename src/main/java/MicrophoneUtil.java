import javax.sound.sampled.*;
import java.io.*;

/**
 * Microphone Util
 * @see MicrophoneExample.java
 */
class MicrophoneUtil {

    // the line from which audio data is captured
    private TargetDataLine line;

    private AudioInputStream ais;

    /**
     * Defines an audio format
     */
    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        return format;
    }

    /**
     * Capture sound
     */
    public AudioInputStream capture() throws IOException, LineUnavailableException {

        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        // checks if system supports the data line
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            System.exit(0);
        }
        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        ais = new AudioInputStream(line);

        return ais;
    }

    public void record(AudioFileFormat.Type fileType, File audioFile) throws IOException {
        AudioSystem.write(ais, fileType, audioFile);
    }

    /**
     * Closes the target data line to finish capturing and recording
     */
    public void finish() {
        line.stop();
        line.close();
    }
}
