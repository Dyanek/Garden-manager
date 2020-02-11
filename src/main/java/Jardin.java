import javax.swing.*;
import java.awt.*;

public class Jardin extends JFrame {

    private int temperature = 26;
    private int humidite1 = 21;
    private int humidite2 = 22;
    private int humidite3 = 23;

    public void laucheInterface(){
        JFrame frame = new JFrame("Jardin Vertical");

        // Setting the width and height of frame

        // Setting the width and height of frame
        // 1.get the screen size as a java dimension
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 2.get 2/3 of the height, and 2/3 of the width
        int height = screenSize.height * 4 / 5;
        int width = screenSize.width * 4 / 5;
        System.out.println(height + " - " +width);
        // 3.set the jframe height and width
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel panel1 = new JPanel();
        panel1.setSize(width/4,height/4);
        JLabel label1 = new JLabel("Caméra 1");
        panel1.add(label1);

        JPanel panel2 = new JPanel();
        panel2.setSize(width/4,height/4);
        JLabel label2 = new JLabel("Caméra 2");
        panel2.add(label2);

        JPanel panel3 = new JPanel();
        panel3.setSize(width/4,height/4);
        JLabel label3 = new JLabel("Caméra 3");
        panel3.add(label3);

        JPanel panel4 = new JPanel();
        panel4.setSize(width/4,height/4);
        JLabel label4 = new JLabel("Panel 4");
        panel4.add(label4);

        // add components
        frame.add(panel1);
        frame.add(panel2);
        frame.add(panel3);
        frame.add(panel4);
        frame.setLayout(new GridLayout(2,2));

        // set window visible and center
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /*public JPanel panelAccueil(){
        JPanel panelAccueil = new JPanel();
        panelAccueil.setSize(width/4,height/4);
        JLabel label4 = new JLabel("Panel 4");
        panelAccueil.add(label4);
    }*/

    public static void main(String[] args) {
        Jardin myJardin = new Jardin();
        myJardin.laucheInterface();
    }
};

