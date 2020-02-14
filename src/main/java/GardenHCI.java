import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Hashtable;
import java.util.Scanner;

class GardenHCI extends JFrame {

    private Garden garden;

    // TODO: init avec les vrais valeurs
    private int waterSensorValueFloor1 = 101;
    private int waterSensorValueFloor2 = 102;
    private int waterSensorValueFloor3 = 103;

    private Hashtable<String, Webcam> webcamList = new Hashtable<>();

    private JFrame jFrame;
    private JPanel jPanel, panelWelcomeInfo;
    // refactored : cameraJPanels
    private JPanel[] cameraJPanels = new JPanel[3];

    private JLabel labelTemperature;
    private JLabel labelLuminosity;

    private JPanel panelWater, panelHelp;
    private JLabel jLabelWaterTitle;
    private JLabel jLabelWaterSubTitle;
    private JLabel jLabelWaterSensor1;
    private JLabel jLabelWaterSensor2;
    private JLabel jLabelWaterSensor3;

    /**
     * get current webcam list
     */
    private void updateWebcamList() {
        this.setWebcamList(WebcamUtil.getWebcamList());
    }

    GardenHCI(Garden garden) {
        this.garden = garden;
    }


    void launchHCI() {
        initHCI();
        initWelcome(garden.getTemperatureSensor().getCurrentValue(), garden.getHumiditySensor().getCurrentValue());
        initHelp();
        initWater(waterSensorValueFloor1, waterSensorValueFloor2, waterSensorValueFloor3);
        displayWelcome();
    }

    private void initHCI() {
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

    private void initWelcome(float temperature, float luminosity) {// TODO: Init with real value(temperature,

        // map cameras
        this.updateWebcamList();
        int cameraIndex = 0;

        for (String name : this.webcamList.keySet()) {
            if (cameraIndex < 3) {
                this.cameraJPanels[cameraIndex] = new WebcamPanel(this.webcamList.get(name));
                this.cameraJPanels[cameraIndex].setBorder(new LineBorder(Color.gray));
                cameraIndex++;
            }
        }

        // blank panels
        for (int i = cameraIndex; i < 3; i++) {
            this.cameraJPanels[i] = new JPanel();
            this.cameraJPanels[i].setBorder(new LineBorder(Color.gray));
            this.cameraJPanels[i].add(new JLabel("Camera " + i));
        }

        // panelWelcomeInfo
        this.panelWelcomeInfo = new JPanel();
        this.panelWelcomeInfo.setBorder(new LineBorder(Color.gray));
        labelTemperature = new JLabel("Temperature : " + temperature + " °C", SwingConstants.CENTER);
        labelLuminosity = new JLabel("Luminosité : " + luminosity, SwingConstants.CENTER);

        labelTemperature.setFont(new Font("Serif", Font.BOLD, 12));
        labelLuminosity.setFont(new Font("Serif", Font.BOLD, 12));
        JLabel labelHelp = new JLabel("Dites \"Aide\" pour afficher la liste des commandes disponibles", SwingConstants.CENTER);

        panelWelcomeInfo.add(labelTemperature);
        panelWelcomeInfo.add(labelLuminosity);
        panelWelcomeInfo.add(labelHelp);
        panelWelcomeInfo.setLayout(new GridLayout(5, 0));
    }

    private void initHelp() { // TODO: mis à jour avec les informations correspondants
        panelHelp = new JPanel();
        JLabel jLabelHelpTitle = new JLabel("------ Aide ------", SwingConstants.CENTER);
        jLabelHelpTitle.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel jLabelHelp1 = new JLabel("<html><body>1. \"accueil\"<br/>Pour retourner au page d'accueil.</body></html>");
        jLabelHelp1.setFont(new Font("Serif", Font.BOLD, 15));
        JLabel jLabelHelp2 = new JLabel("<html><body>2. \"arroser\" [tout/1/2/3]<br/>Pour arroser tous les étages ou tel étage.</body></html>");
        jLabelHelp2.setFont(new Font("Serif", Font.BOLD, 15));
        JLabel jLabelHelp3 = new JLabel("<html><body>3. \"arreter arrosage\"<br/>Pour arreter arrosage.</body></html>");
        jLabelHelp3.setFont(new Font("Serif", Font.BOLD, 15));
        JLabel jLabelHelp4 = new JLabel("<html><body>4. \"temperature/watersensorvaluefloor1(2/3)/ph/luminosite\" [chiffre]<br/>Pour mis a jour la valeur correspondante(pour developper).</body></html>");//TODO: (pour developper), a supprimer
        jLabelHelp4.setFont(new Font("Serif", Font.BOLD, 15));//TODO: (pour developper), a supprimer
        JLabel jLabelHelp5 = new JLabel("<html><body>5. \"exit\" [chiffre]<br/>Pour quitter.</body></html>");//TODO: (pour developper), a supprimer
        jLabelHelp5.setFont(new Font("Serif", Font.BOLD, 15));//TODO: (pour developper), a supprimer

        panelHelp.add(jLabelHelpTitle);
        panelHelp.add(jLabelHelp1);
        panelHelp.add(jLabelHelp2);
        panelHelp.add(jLabelHelp3);

        panelHelp.add(jLabelHelp4); // TODO: (pour developper), a supprimer
        panelHelp.add(jLabelHelp5); // TODO: (pour developper), a supprimer

        panelHelp.setLayout(new GridLayout(5, 0));
    }

    public void initWater(int waterSensorValueFloor1, int waterSensorValueFloor2, int waterSensorValueFloor3) {
        this.waterSensorValueFloor1 = waterSensorValueFloor1;
        this.waterSensorValueFloor2 = waterSensorValueFloor2;
        this.waterSensorValueFloor3 = waterSensorValueFloor3;

        panelWater = new JPanel();
        panelWater.setLayout(new GridLayout(5, 0));
        jLabelWaterSubTitle = new JLabel("Taux de humidite : \n");
        jLabelWaterSensor1 = new JLabel("    Etage 1 : " + waterSensorValueFloor1 + "%");
        jLabelWaterSensor2 = new JLabel("    Etage 2 : " + waterSensorValueFloor2 + "%");
        jLabelWaterSensor3 = new JLabel("    Etage 3 : " + waterSensorValueFloor3 + "%");
    }

    public void uploadTemperature(float temperature) {
        labelTemperature.setText("Temperature : " + temperature + " °C");
        System.out.println(">>> temperature uploaded : " + temperature);
    }

    void uploadWaterSensorValueFloor1(int waterSensorValueFloor1) {
        this.waterSensorValueFloor1 = waterSensorValueFloor1;
        jLabelWaterSensor1.setText("    Etage 1 : " + waterSensorValueFloor1 + "%");
        System.out.println(">>> waterSensorValueFloor1 uploaded : " + waterSensorValueFloor1);
    }

    void uploadWaterSensorValueFloor2(int waterSensorValueFloor2) {
        this.waterSensorValueFloor2 = waterSensorValueFloor2;
        jLabelWaterSensor2.setText("    Etage 2 : " + waterSensorValueFloor2 + "%");
        System.out.println(">>> waterSensorValueFloor2 uploaded : " + waterSensorValueFloor2);
    }

    void uploadWaterSensorValueFloor3(int waterSensorValueFloor3) {
        this.waterSensorValueFloor3 = waterSensorValueFloor3;
        jLabelWaterSensor3.setText("    Etage 3 : " + waterSensorValueFloor3 + "%");
        System.out.println(">>> waterSensorValueFloor3 uploaded : " + waterSensorValueFloor3);
    }

    void uploadLuminosity(float luminosity) {
        labelLuminosity.setText("Luminosité : " + luminosity);
    }

    private void loadWaterPanel(String waterTitle, JLabel jLabelWaterSensor, JPanel panelCamera) {
        jLabelWaterTitle = new JLabel(waterTitle);
        jLabelWaterTitle.setFont(new Font("Serif", Font.BOLD, 20));

        panelWater.add(jLabelWaterTitle);
        panelWater.add(jLabelWaterSubTitle);
        panelWater.add(jLabelWaterSensor);

        jPanel.setLayout(new GridLayout(0, 2));
        jPanel.add(panelCamera);
        jPanel.add(panelWater);
    }

    public void waterFloor(String numberFloor) {
        panelWater.removeAll();
        jPanel.removeAll();
        switch (numberFloor) {
            case "tout":
                jLabelWaterTitle = new JLabel("Arrosage tout les étages en cours");
                jLabelWaterTitle.setFont(new Font("Serif", Font.BOLD, 20));
                panelWater.add(jLabelWaterTitle);
                panelWater.add(jLabelWaterSubTitle);
                panelWater.add(jLabelWaterSensor1);
                panelWater.add(jLabelWaterSensor2);
                panelWater.add(jLabelWaterSensor3);
                panelWater.setLayout(new GridLayout(5, 0));
                this.jPanel.setLayout(new GridLayout(2, 2));
                // this.jPanel.add(panelCamera1);
                // this.jPanel.add(panelCamera2);
                // this.jPanel.add(panelCamera3);

                // refactor cameraJPanels
                for (JPanel cameraJPanel : this.cameraJPanels) {
                    this.jPanel.add(cameraJPanel);
                }
                this.jPanel.add(panelWater);
                break;
            //TODO: to refactor jLabelWaterSensor to array
            case "1":
                loadWaterPanel(" Arrosage de premier étage en cours", jLabelWaterSensor1, cameraJPanels[0]);
                break;
            case "2":
                loadWaterPanel(" Arrosage de deuxieme étage en cours", jLabelWaterSensor2, cameraJPanels[1]);
                break;
            case "3":
                loadWaterPanel(" Arrosage de troisieme étage en cours", jLabelWaterSensor3, cameraJPanels[2]);
                break;
            default:
                System.out.println("Ops, Erreur de Arrosage");
        }

        this.jPanel.repaint();
        this.jFrame.validate();
    }

    public void stopWater() {// TODO: stopWater -> arreter quel étage ...
        displayWelcome();
    }

    public void displayWelcome() {
        this.jPanel.removeAll();
        // this.jPanel.add(panelCamera1);
        // this.jPanel.add(panelCamera2);
        // this.jPanel.add(panelCamera3);

        // refactor cameraJPanels
        for (JPanel cameraJPanel : this.cameraJPanels) {
            this.jPanel.add(cameraJPanel);
        }
        jPanel.add(panelWelcomeInfo);
        jPanel.setLayout(new GridLayout(2, 2));
        jPanel.repaint();
        this.jFrame.validate();
    }

    public void displayHelp() {
        this.jPanel.removeAll();
        jPanel.add(panelHelp);
        jPanel.setLayout(new GridLayout(1, 0));
        jPanel.repaint();
        jFrame.validate();
    }

    // -- TEST a l'aide console --
    public void getCommande() {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals("exit")) {
            System.out.print("Dit-moi, qu'est-ce que tu veux hen ???(\"help\" if u need help O.O) : ");
            input = scanner.nextLine();
            traiterCommande(input);
        }
        System.out.println("Bye, a plus ^^");
        System.exit(0);
    }

    public void traiterCommande(String c) {
        String[] list = c.split(" ");

        if (list.length == 2) {
            String firstWord = list[0].toLowerCase();
            switch (firstWord) {
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
                case "luminosite":
                    uploadLuminosity(Float.valueOf(list[1]));
                    break;
                case "arroser":
                    waterFloor(list[1].toLowerCase());
                    break;
                case "arreter":
                    if (list[1].toLowerCase().equals("arrosage")) // TO DO : arroser quel étage ...
                        stopWater();
                    break;
                default:
                    System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + c);
                    break;
            }
        } else if (list.length == 1) {
            switch (list[0].toLowerCase()) {
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
        } else
            System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + c);
    }

    public Hashtable<String, Webcam> getWebcamList() {
        return webcamList;
    }

    public void setWebcamList(Hashtable<String, Webcam> webcamList) {
        this.webcamList = webcamList;
    }
}
