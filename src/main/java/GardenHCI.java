import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Scanner;

public class GardenHCI extends JFrame {

    private float temperature = 100;
    private float luminosity = 100;
    private float ph = 100;
    private int waterSensorValueFloor1 = 101;
    private int waterSensorValueFloor2 = 102;
    private int waterSensorValueFloor3 = 103;

    private JFrame jFrame;
    private JPanel jPanel, panelCamera1, panelCamera2, panelCamera3, panelWelcomeInfo;
    private JLabel labelTemperature;
    private JLabel labelLuminosity;
    private JLabel labelPH;

    private JPanel panelWater, panelHelp;
    private JLabel jLabelWaterTitle;
    private JLabel jLabelWaterSubTitle;
    private JLabel jLabelWaterSensor1;
    private JLabel jLabelWaterSensor2;
    private JLabel jLabelWaterSensor3;

    private void initHCI(){
        final String HCITitle = "Jardin Vertical";
        this.jFrame = new JFrame(HCITitle);
        // -- Setting the width and height of frame --
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setSize(screenSize.width * 2 / 5, screenSize.height * 2 / 5); // TO DO

        jPanel = new JPanel();
        jFrame.add(jPanel);

        // set window visible and center
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initWelcome(){// TODO: Init with real value(temperapure, humidité, etc...)
        // camera 1
        this.panelCamera1 = new WebcamPanel(Webcam.getDefault());// TODO: to change with the real camera
        this.panelCamera1.setBorder(new LineBorder(Color.gray));
        // camera 2
        this.panelCamera2 = new JPanel();
        this.panelCamera2.setBorder(new LineBorder(Color.gray));
        JLabel labelCamera2 = new JLabel("Caméra 2");
        panelCamera2.add(labelCamera2);
        // camera 3
        this.panelCamera3 = new JPanel();
        this.panelCamera3.setBorder(new LineBorder(Color.gray));
        JLabel labelCamera3 = new JLabel("Caméra 3");
        panelCamera3.add(labelCamera3);
        // panelWelcomeInfo
        this.panelWelcomeInfo = new JPanel();
        this.panelWelcomeInfo.setBorder(new LineBorder(Color.gray));
        labelTemperature = new JLabel("Temperature : " + temperature + " °C", SwingConstants.CENTER);
        labelLuminosity = new JLabel("Luminosité : " + luminosity, SwingConstants.CENTER);
        labelPH = new JLabel("PH : " + ph, SwingConstants.CENTER);
        labelTemperature.setFont(new Font( "Serif", Font.BOLD, 12));
        labelLuminosity.setFont(new Font( "Serif", Font.BOLD, 12));
        labelPH.setFont(new Font( "Serif", Font.BOLD, 12));
        JLabel labelHelp = new JLabel("Dites \"Aide\" pour afficher la liste des commandes disponibles", SwingConstants.CENTER);
        panelWelcomeInfo.add(labelTemperature);
        panelWelcomeInfo.add(labelLuminosity);
        panelWelcomeInfo.add(labelPH);
        panelWelcomeInfo.add(labelHelp);
        panelWelcomeInfo.setLayout(new GridLayout(5, 0));
    }

    private void initHelp(){ // TODO: mis à jour avec les informations correspondants
        panelHelp = new JPanel();
        JLabel jLabelHelpTitle = new JLabel("------ Aide ------", SwingConstants.CENTER);
        jLabelHelpTitle.setFont(new Font( "Serif", Font.BOLD, 20));
        JLabel jLabelHelp1 = new JLabel("1. temperature [chiffre]", SwingConstants.CENTER);
        jLabelHelp1.setFont(new Font( "Serif", Font.BOLD, 15));
        JLabel jLabelHelp2 = new JLabel("2. arroser tout / arroser 1/2/3 / arreter arrosage", SwingConstants.CENTER);
        jLabelHelp2.setFont(new Font( "Serif", Font.BOLD, 15));
        JLabel jLabelHelp3 = new JLabel("3. acceuil", SwingConstants.CENTER);
        jLabelHelp3.setFont(new Font( "Serif", Font.BOLD, 15));
        panelHelp.add(jLabelHelpTitle);
        panelHelp.add(jLabelHelp1);
        panelHelp.add(jLabelHelp2);
        panelHelp.add(jLabelHelp3);
        panelHelp.setLayout(new GridLayout(5, 0));
    }

    public void initWater(){
        panelWater = new JPanel();
        panelWater.setLayout(new GridLayout(5,0));
        jLabelWaterSubTitle = new JLabel("Taux de humidite : \n");
        jLabelWaterSensor1 = new JLabel("    Etage 1 : " + waterSensorValueFloor1 + "%");
        jLabelWaterSensor2 = new JLabel("    Etage 2 : " + waterSensorValueFloor2 + "%");
        jLabelWaterSensor3 = new JLabel("    Etage 3 : " + waterSensorValueFloor3 + "%");
    }

    public void launchHCI(){
        initHCI();
        initWelcome();
        initHelp();
        initWater();
        displayWelcome();
    }

    public void uploadTemperature(float temperature){
        this.temperature = temperature;
        labelTemperature.setText("Temperature : " + temperature + " °C");
        System.out.println(">>> temperature uploaded : " + temperature);
    }

    void uploadWaterSensorValueFloor1(int waterSensorValueFloor1){
        this.waterSensorValueFloor1 = waterSensorValueFloor1;
        jLabelWaterSensor1.setText("    Etage 1 : " + waterSensorValueFloor1 + "%");
        System.out.println(">>> waterSensorValueFloor1 uploaded : " + waterSensorValueFloor1);
    }

    void uploadWaterSensorValueFloor2(int waterSensorValueFloor2){
        this.waterSensorValueFloor2 = waterSensorValueFloor2;
        jLabelWaterSensor2.setText("    Etage 2 : " + waterSensorValueFloor2 + "%");
        System.out.println(">>> waterSensorValueFloor2 uploaded : " + waterSensorValueFloor2);
    }

    void uploadWaterSensorValueFloor3(int waterSensorValueFloor3){
        this.waterSensorValueFloor3 = waterSensorValueFloor3;
        jLabelWaterSensor3.setText("    Etage 3 : " + waterSensorValueFloor3 + "%");
        System.out.println(">>> waterSensorValueFloor3 uploaded : " + waterSensorValueFloor3);
    }

    void uploadLuminosity(float luminosity){
        this.luminosity = luminosity;
        labelLuminosity.setText("Luminosité : " + luminosity);
    }

    public void uploadPH(float ph){
        this.ph = ph;
        labelPH.setText("ph : " + ph);
    }

    private void loadWaterPanel(String waterTitle, JLabel jLabelWaterSensor, JPanel panelCamera){
        jLabelWaterTitle = new JLabel(waterTitle);
        jLabelWaterTitle.setFont(new Font( "Serif", Font.BOLD, 20));

        panelWater.add(jLabelWaterTitle);
        panelWater.add(jLabelWaterSubTitle);
        panelWater.add(jLabelWaterSensor);

        jPanel.setLayout(new GridLayout(0, 2));
        jPanel.add(panelCamera);
        jPanel.add(panelWater);
    }

    public void waterFloor(String numberFloor){
        panelWater.removeAll();
        jPanel.removeAll();
        switch (numberFloor){
            case "tout":
                jLabelWaterTitle = new JLabel("Arrosage tout les étages en cours");
                jLabelWaterTitle.setFont(new Font( "Serif", Font.BOLD, 20));
                panelWater.add(jLabelWaterTitle);
                panelWater.add(jLabelWaterSubTitle);
                panelWater.add(jLabelWaterSensor1);
                panelWater.add(jLabelWaterSensor2);
                panelWater.add(jLabelWaterSensor3);
                panelWater.setLayout(new GridLayout(5,0));
                this.jPanel.setLayout(new GridLayout(2,2));
                this.jPanel.add(panelCamera1);
                this.jPanel.add(panelCamera2);
                this.jPanel.add(panelCamera3);
                this.jPanel.add(panelWater);
                break;
            case "1":
                loadWaterPanel(" Arrosage de premier étage en cours", jLabelWaterSensor1, panelCamera1);
                break;
            case "2":
                loadWaterPanel(" Arrosage de deuxieme étage en cours", jLabelWaterSensor2, panelCamera2);
                break;
            case "3":
                loadWaterPanel(" Arrosage de troisieme étage en cours", jLabelWaterSensor3, panelCamera3);
                break;
            default:
                System.out.println("Ops, Erreur de Arrosage");
        }

        this.jPanel.repaint();
        this.jFrame.validate();
    }

    public void stopWater(){// TODO: stopWater -> arreter quel étage ...
        displayWelcome();
    }


    public void displayWelcome(){
        this.jPanel.removeAll();
        jPanel.add(panelCamera1);
        jPanel.add(panelCamera2);
        jPanel.add(panelCamera3);
        jPanel.add(panelWelcomeInfo);
        jPanel.setLayout(new GridLayout(2,2));
        jPanel.repaint();
        this.jFrame.validate();
    }

    public void displayHelp(){
        this.jPanel.removeAll();
        jPanel.add(panelHelp);
        jPanel.setLayout(new GridLayout(1,0));
        jPanel.repaint();
        jFrame.validate();
    }

    // -- TEST a l'aide console --
    public void getCommande(){
        Scanner scanner = new Scanner( System.in );
        String input = "";
        while(!input.equals("exit")){
            System.out.print("Dit-moi, qu'est-ce que tu veux hen ???(\"help\" if u need help O.O) : ");
            input = scanner.nextLine();
            traiterCommande(input);
        }
        System.out.println("Bye, a plus ^^");
        System.exit(0);
    }

    public void traiterCommande(String c){
        String[] list = c.split(" ");

        if(list.length == 2){
            String firstWord = list[0].toLowerCase();
            switch(firstWord){
                case "temperature":
                    uploadTemperature(Float.valueOf(list[1]));
                    break;
                case "watersensorvaluefloor1":
                    uploadWaterSensorValueFloor1(Integer.valueOf(list[1]));
                    break;
                case "watersensorvaluefloor2":
                    uploadWaterSensorValueFloor2(Integer.valueOf(list[1]));
                    break;
                case "watersensorvaluefloor3":
                    uploadWaterSensorValueFloor3(Integer.valueOf(list[1]));
                case "ph":
                    uploadPH(Float.valueOf(list[1]));
                    break;
                case "luminosity":
                    uploadLuminosity(Float.valueOf(list[1]));
                    break;
                case "arroser":
                    waterFloor(list[1].toLowerCase());
                    break;
                case "arreter":
                    if(list[1].toLowerCase().equals("arrosage")) // TO DO : arroser quel étage ...
                        stopWater();
                    break;
                default:
                    System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + c);
                    break;
            }
        }else if(list.length == 1){
            switch(list[0].toLowerCase()){
                case "aide":
                    displayHelp();
                    break;
                case "accueil":
                    displayWelcome();
                    break;
                default:
                    System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + c);
                    break;
            }
        }
        else
            System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + c);

    }

    public static void main(String[] args) {
        GardenHCI myGradinHCI = new GardenHCI();
        myGradinHCI.launchHCI();
        myGradinHCI.getCommande();
    }
};

