import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Jardin extends JFrame {

    private int temperature = 26;
    private int humidite1 = 21;
    private int humidite2 = 22;
    private int humidite3 = 23;

    JFrame jFrame;
    JPanel jPanel, pageAcceuil_panel_1, pageAcceuil_panel_2, pageAcceuil_panel_3, pageAccueil_info;
    JLabel label2, label3, labelTemperature, labelAide;

    JPanel panelWater, panelHelp;
    JLabel jLabelWaterTitle, jLabelWaterSubTitle, jLabelHumidite1, jLabelHumidite2, jLabelHumidite3, jLabelTitle, jLabelHelp1, jLabelHelp2, jLabelHelp3;

    public void laucheInterface(){

        this.jFrame = new JFrame("Jardin Vertical");

        // -- Setting the width and height of frame --
        // Setting the width and height of frame
        // 1.get the screen size as a java dimension
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // 2.get 2/3 of the height, and 2/3 of the width
        int height = screenSize.height * 4 / 5;
        int width = screenSize.width * 4 / 5;
        // 3.set the jframe height and width
        jFrame.setSize(width, height);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -- jPanel : 4 Components --
        this.pageAcceuil_panel_1 = new WebcamPanel(Webcam.getDefault());

        this.pageAcceuil_panel_2 = new JPanel();
        label2 = new JLabel("Caméra 2");
        pageAcceuil_panel_2.add(label2);

        this.pageAcceuil_panel_3 = new JPanel();
        label3 = new JLabel("Caméra 3");
        pageAcceuil_panel_3.add(label3);

        this.pageAccueil_info = new JPanel();
        labelTemperature = new JLabel("Temperature : " + temperature + " °C");
        labelTemperature.setFont(new Font( "Serif", Font.BOLD, 20));
        pageAccueil_info.add(labelTemperature);
        labelAide = new JLabel("Dites \"Aide\" pour afficher la liste \n" +
                "des commandes disponibles");
        pageAccueil_info.add(labelAide);

        // add components
        jPanel = new JPanel();

        // Loading Page d'acceuil
        activeAcceuil();

        jFrame.add(jPanel);

        // Panel Help
        panelHelp = new JPanel();
        jLabelTitle = new JLabel("--- Aide ---");
        jLabelTitle.setFont(new Font( "Serif", Font.BOLD, 15));
        jLabelHelp1 = new JLabel("1. temperature [chiffre]");
        jLabelHelp2 = new JLabel("2. arroser tout");
        jLabelHelp3 = new JLabel("3. acceuil");
        panelHelp.add(jLabelTitle);
        panelHelp.add(jLabelHelp1);
        panelHelp.add(jLabelHelp2);
        panelHelp.add(jLabelHelp3);
        panelHelp.setLayout(new GridLayout(5, 0));


        // set window visible and center
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void uploadTemperature(int temperature){
        this.temperature = temperature;
        labelTemperature.setText("Temperature : " + temperature + " °C");
        System.out.println(">>> temperature uploaded : " + temperature);
    }

    public void waterAll(){
        this.jPanel.removeAll();
        this.jPanel.setLayout(new GridLayout(2,2));
        this.jPanel.add(pageAcceuil_panel_1);
        this.jPanel.add(pageAcceuil_panel_2);
        this.jPanel.add(pageAcceuil_panel_3);

        panelWater = new JPanel();
        jLabelWaterTitle = new JLabel("Arrosage tout les étages en cours\n");
        jLabelWaterTitle.setFont(new Font( "Serif", Font.BOLD, 20));
        jLabelWaterSubTitle = new JLabel("Taux de humidite : \n");
        jLabelHumidite1 = new JLabel("    Etage 1 : " + humidite1 + "%");
        jLabelHumidite2 = new JLabel("    Etage 2 : " + humidite2 + "%");
        jLabelHumidite3 = new JLabel("    Etage 3 : " + humidite3 + "%");

        panelWater.add(jLabelWaterTitle);
        panelWater.add(jLabelWaterSubTitle);
        panelWater.add(jLabelHumidite1);
        panelWater.add(jLabelHumidite2);
        panelWater.add(jLabelHumidite3);
        panelWater.setLayout(new GridLayout(5,0));
        this.jPanel.add(panelWater);
        this.jFrame.validate();
    }
    public void waterFirst(){
        this.jPanel.removeAll();
        this.jPanel.setLayout(new GridLayout(0,2));
        this.jPanel.add(pageAcceuil_panel_1);
        panelWater.removeAll();
        panelWater.add(jLabelHumidite1);
        this.jPanel.add(panelWater);
        this.jFrame.validate();
    }
    public void waterSecond(){
        this.jPanel.removeAll();
        this.jPanel.setLayout(new GridLayout(0,2));
        this.jPanel.add(pageAcceuil_panel_2);
        panelWater.removeAll();
        panelWater.add(jLabelHumidite2);
        this.jPanel.add(panelWater);
        this.jFrame.validate();
    }
    public void waterThird(){
        this.jPanel.removeAll();
        this.jPanel.setLayout(new GridLayout(0,2));
        this.jPanel.add(pageAcceuil_panel_3);
        panelWater.removeAll();
        panelWater.add(jLabelHumidite3);
        this.jPanel.add(panelWater);
        this.jFrame.validate();
    }


    public void activeAcceuil(){
        this.jPanel.removeAll();
        jPanel.add(pageAcceuil_panel_1);
        jPanel.add(pageAcceuil_panel_2);
        jPanel.add(pageAcceuil_panel_3);
        jPanel.add(pageAccueil_info);
        jPanel.setLayout(new GridLayout(2,2));
        this.jFrame.validate();
    }

    public void launchHelp(){
        jPanel.removeAll();
        jPanel.add(pageAcceuil_panel_1);
        jPanel.add(pageAcceuil_panel_2);
        jPanel.add(pageAcceuil_panel_3);
        jPanel.add(panelHelp);
        jPanel.setLayout(new GridLayout(0,3));
        jFrame.validate();
    }

    // -- TEST --

    public void getCommande(){
        Scanner scanner = new Scanner( System.in );
        String input = "";
        while(!input.equals("exit")){
            System.out.println("Dit-moi, qu'est-ce que tu veux hen ???(\"help\" if u need help O.O)");
            input = scanner.nextLine();
            traiterCommande(input);
        }
        System.out.println("Bye ^^");
        System.exit(0);
    }

    public void traiterCommande(String c){
        String[] list = c.split(" ");

        if (list[0].toLowerCase().equals("temperature"))
            uploadTemperature(Integer.valueOf(list[1]));
        else if(list[0].toLowerCase().equals("aide")){
            launchHelp();
            System.out.println("Page de l'aide affiche...");
        }
        else if (list.length >= 2 && list[0].toLowerCase().equals("arroser")){
            if(list[1].toLowerCase().equals("tout")){
                waterAll();
                System.out.println("Arrosage tout les étages en cours...");
            }
            else if(list[1].toLowerCase().equals("1")){
                waterFirst();
                System.out.println(" Arrosage de premier étage en cours...(TO DO)");
            }
            else if(list[1].toLowerCase().equals("2")){
                waterSecond();
                System.out.println(" Arrosage de deuxieme étage en cours...(TO DO)");
            }
            else if(list[1].toLowerCase().equals("3")){
                waterThird();
                System.out.println(" Arrosage de troisieme étage en cours...(TO DO)");
            }

        }
        else if (list[0].toLowerCase().equals("acceuil"))
            activeAcceuil();
        else{
            System.out.println(">> " + c);
            System.out.println("C'est vraiment trop difficile à comprendre votre commande...");
        }

    }

    public static void main(String[] args) {
        Jardin myJardin = new Jardin();
        myJardin.laucheInterface();

        myJardin.getCommande();
    }
};

