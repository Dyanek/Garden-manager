import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class GardenHCI extends JFrame {

    private Garden garden;

    private JFrame jFrame;
    private JPanel jPanel, panelWelcomeInfo;

    private JPanel[] cameraJPanels = new JPanel[3];

    private List<GardenSensorLabel> gardenSensorLabels = new ArrayList<>();
    private List<FloorSensorLabel> floorSensorLabels = new ArrayList<>();

    private JPanel panelWater, panelHelp;
    private JLabel labelWaterTitle;
    private JLabel labelWaterSubTitle;

    GardenHCI(Garden garden) {
        this.garden = garden;
    }

    void launchHCI() {
        initHCI();
        initWelcome();
        initHelp();
        initFloorsSensors();
        initWaterPanel();
        displayWelcome();
        getCommande();
    }

    private void initHCI() {
        final String HCITitle = "Jardin Vertical";
        this.jFrame = new JFrame(HCITitle);

        // -- Setting the width and height of frame --
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setSize(screenSize.width * 2 / 5, screenSize.height * 2 / 5); // TODO: Make screen size at 100% ratio

        jPanel = new JPanel();
        jFrame.add(jPanel);

        // set window visible and center
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initWelcome() {

        int index = 0;

        for (Floor floor : garden.getAllFloors()) {
            Webcam floorCamera = floor.getCamera();

            if (floorCamera != null) {
                this.cameraJPanels[index] = new WebcamPanel(floorCamera);
            } else {
                this.cameraJPanels[index] = new JPanel();
            }

            this.cameraJPanels[index].setBorder(new LineBorder(Color.gray));
            this.cameraJPanels[index].add(new JLabel("Camera " + (++index)));
        }

        // panelWelcomeInfo
        this.panelWelcomeInfo = new JPanel();
        this.panelWelcomeInfo.setBorder(new LineBorder(Color.gray));

        initGardenSensors();

        JLabel labelHelp = new JLabel("Dites \"Aide\" pour afficher la liste des commandes disponibles", SwingConstants.CENTER);

        panelWelcomeInfo.add(gardenSensorLabels.get(0).getLabel());
        panelWelcomeInfo.add(gardenSensorLabels.get(1).getLabel());
        panelWelcomeInfo.add(labelHelp);
        panelWelcomeInfo.setLayout(new GridLayout(5, 0));
    }

    private void initGardenSensors() {
        GardenSensorLabel temperatureLabel = new GardenSensorLabel(this.garden.getTemperatureSensor(), "Température", "°C");
        GardenSensorLabel humidityLabel = new GardenSensorLabel(this.garden.getHumiditySensor(), "Humidité", "%");

        gardenSensorLabels.add(temperatureLabel);
        gardenSensorLabels.add(humidityLabel);
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
        JLabel jLabelHelp4 = new JLabel("<html><body>4. \"temperature/watersensorvaluefloor1(2/3)/ph/humidite\" [chiffre]<br/>Pour mis a jour la valeur correspondante(pour developper).</body></html>");//TODO: (pour developper), a supprimer
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

    private void initFloorsSensors() {
        for (Floor floor : garden.getAllFloors()) {
            floorSensorLabels.add(new FloorSensorLabel(floor.getFloorId(), floor.getAciditySensor(), "g/L"));
            floorSensorLabels.add(new FloorSensorLabel(floor.getFloorId(), floor.getBrightnessSensor(), "cd/m²"));
            floorSensorLabels.add(new FloorSensorLabel(floor.getFloorId(), floor.getWaterSensor(), "%"));
        }
    }

    void refreshGardenSensorLabel(Class sensorType) {
        gardenSensorLabels.stream()
                .filter(gardenSensorLabel -> gardenSensorLabel.getSensor().getClass().equals(sensorType))
                .forEach(GardenSensorLabel::refreshLabel);
    }

    void refreshFloorSensorLabel(Class sensorType, int floorId) {
        floorSensorLabels.stream()
                .filter(floorSensorLabels -> floorSensorLabels.getSensor().getClass().equals(sensorType))
                .filter(floorSensorLabels -> floorSensorLabels.getFloorId() == floorId)
                .forEach(FloorSensorLabel::refreshLabel);
    }

    private void initWaterPanel() {
        panelWater = new JPanel();
        panelWater.setLayout(new GridLayout(5, 0));
        labelWaterSubTitle = new JLabel("Pourcentage d'eau :\n");
    }


    private void loadWaterPanel(String waterTitle, JLabel waterSensorLabel, JPanel panelCamera) {
        labelWaterTitle = new JLabel(waterTitle);
        labelWaterTitle.setFont(new Font("Serif", Font.BOLD, 20));

        labelWaterSubTitle = new JLabel("Pourcentage d'eau : \n");

        panelWater.add(labelWaterTitle);
        panelWater.add(labelWaterSubTitle);
        panelWater.add(waterSensorLabel);

        jPanel.setLayout(new GridLayout(0, 2));
        jPanel.add(panelCamera);
        jPanel.add(panelWater);
    }

    public void waterFloor(String numberFloor) {
        panelWater.removeAll();
        jPanel.removeAll();

        List<JLabel> waterSensorsLabels = floorSensorLabels.stream()
                .filter(floorSensorLabel -> floorSensorLabel.getSensor().getClass().equals(WaterSensor.class))
                .sorted((o1, o2) -> o1.getFloorId() < o2.getFloorId() ? -1 : 1)
                .map(SensorLabel::getLabel)
                .collect(Collectors.toList());

        switch (numberFloor) {
            case "tout":
                labelWaterTitle = new JLabel("Arrosage tout les étages en cours");
                labelWaterTitle.setFont(new Font("Serif", Font.BOLD, 20));
                panelWater.add(labelWaterTitle);
                panelWater.add(labelWaterSubTitle);


                panelWater.add(waterSensorsLabels.get(0));
                panelWater.add(waterSensorsLabels.get(1));
                panelWater.add(waterSensorsLabels.get(2));
                panelWater.setLayout(new GridLayout(5, 0));
                this.jPanel.setLayout(new GridLayout(2, 2));

                // refactor cameraJPanels
                for (JPanel cameraJPanel : this.cameraJPanels) {
                    this.jPanel.add(cameraJPanel);
                }
                this.jPanel.add(panelWater);
                break;
            //TODO: to refactor jLabelWaterSensor to array
            case "1":
                loadWaterPanel(" Arrosage de premier étage en cours", waterSensorsLabels.get(0), cameraJPanels[0]);
                break;
            case "2":
                loadWaterPanel(" Arrosage de deuxieme étage en cours", waterSensorsLabels.get(1), cameraJPanels[1]);
                break;
            case "3":
                loadWaterPanel(" Arrosage de troisieme étage en cours", waterSensorsLabels.get(2), cameraJPanels[2]);
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
            System.out.print("Dis-moi, qu'est-ce que tu veux hen ???(\"help\" if u need help O.O) : ");

            input = scanner.nextLine();
            processCommand(input);
        }
        System.out.println("Bye, à plus ^^");
        System.exit(0);
    }

    private void processCommand(String c) {
        String[] list = c.split(" ");

        if (list.length == 2) {
            String firstWord = list[0].toLowerCase();
            switch (firstWord) {
                case "temperature":
                    garden.getTemperatureSensor().addValue(Float.valueOf(list[1]));
                    refreshGardenSensorLabel(TemperatureSensor.class);
                    break;
                case "humidite":
                    garden.getHumiditySensor().addValue(Float.valueOf(list[1]));
                    refreshGardenSensorLabel(HumiditySensor.class);
                    break;
                case "watersensorvaluefloor1":
                    garden.getFloor(1).getWaterSensor().addValue(Float.valueOf(list[1]));
                    refreshFloorSensorLabel(WaterSensor.class, 1);
                    break;
                case "watersensorvaluefloor2":
                    garden.getFloor(2).getWaterSensor().addValue(Float.valueOf(list[1]));
                    refreshFloorSensorLabel(WaterSensor.class, 2);
                    break;
                case "watersensorvaluefloor3":
                    garden.getFloor(3).getWaterSensor().addValue(Float.valueOf(list[1]));
                    refreshFloorSensorLabel(WaterSensor.class, 3);
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
}
