import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

class GardenHCI extends JFrame {

    private Garden garden;

    private JFrame jFrame;
    private JPanel mainPanel, informationPanel;

    private JPanel[] cameraPanels = new JPanel[3];

    private List<GardenSensorLabel> gardenSensorLabels = new ArrayList<>();
    private List<FloorSensorLabel> floorSensorLabels = new ArrayList<>();

    private enum ActionType {
        WATERING,
        LIGHTING
    }

    GardenHCI(Garden garden) {
        this.garden = garden;
    }

    void launchHCI() {
        initHCI();
        initCameras();
        initGardenSensors();
        initFloorsSensors();

        displayWelcome();
        getCommand();
    }

    private void initHCI() {
        final String HCITitle = "Jardin Vertical";
        this.jFrame = new JFrame(HCITitle);

        // -- Setting the width and height of frame --
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setSize(screenSize.width * 2 / 5, screenSize.height * 2 / 5); // TODO: Make screen size at 100% ratio

        mainPanel = new JPanel();
        jFrame.add(mainPanel);

        // set window visible and center
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        informationPanel = new JPanel();
    }

    private void initCameras() {
        int index = 0;

        for (Floor floor : garden.getAllFloors()) {
            Webcam floorCamera = floor.getCamera();

            if (floorCamera != null) {
                this.cameraPanels[index] = new WebcamPanel(floorCamera);
            } else {
                this.cameraPanels[index] = new JPanel();
            }

            this.cameraPanels[index].setBorder(new LineBorder(Color.gray));
            this.cameraPanels[index].add(new JLabel("Camera " + (++index)));
        }
    }

    private void initGardenSensors() {
        GardenSensorLabel temperatureLabel = new GardenSensorLabel(this.garden.getTemperatureSensor(), "Température", "°C");
        GardenSensorLabel humidityLabel = new GardenSensorLabel(this.garden.getHumiditySensor(), "Humidité", "%");

        gardenSensorLabels.add(temperatureLabel);
        gardenSensorLabels.add(humidityLabel);
    }

    private void initFloorsSensors() {
        for (Floor floor : garden.getAllFloors()) {
            floorSensorLabels.add(new FloorSensorLabel(floor.getFloorId(), floor.getAciditySensor(), "g/L"));
            floorSensorLabels.add(new FloorSensorLabel(floor.getFloorId(), floor.getBrightnessSensor(), "cd/m²"));
            floorSensorLabels.add(new FloorSensorLabel(floor.getFloorId(), floor.getWaterSensor(), "%"));
        }
    }

    private void displayWelcome() {
        mainPanel.removeAll();

        // refactor cameraPanels
        for (JPanel cameraJPanel : this.cameraPanels) {
            this.mainPanel.add(cameraJPanel);
        }

        // panelWelcomeInfo
        informationPanel.removeAll();
        informationPanel.setBorder(new LineBorder(Color.gray));

        JLabel helpLabel = new JLabel("Dites \"Aide\" pour afficher la liste des commandes disponibles", SwingConstants.CENTER);

        informationPanel.add(gardenSensorLabels.get(0).getLabel());
        informationPanel.add(gardenSensorLabels.get(1).getLabel());
        informationPanel.add(helpLabel);
        informationPanel.setLayout(new GridLayout(5, 0));

        mainPanel.add(informationPanel);
        mainPanel.setLayout(new GridLayout(2, 2));
        mainPanel.repaint();
        this.jFrame.validate();
    }

    private void displayHelp() {
        mainPanel.removeAll();

        JLabel jLabelHelpTitle = new JLabel("------ Aide ------", SwingConstants.CENTER);
        jLabelHelpTitle.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel jLabelHelp1 = new JLabel("<html><body>1. \"accueil\"<br/>Pour retourner à la page d'accueil.</body></html>");
        jLabelHelp1.setFont(new Font("Serif", Font.BOLD, 15));
        JLabel jLabelHelp2 = new JLabel("<html><body>2. \"arroser\" [tout/1/2/3]<br/>Pour arroser un ou tous les étages.</body></html>");
        jLabelHelp2.setFont(new Font("Serif", Font.BOLD, 15));
        JLabel jLabelHelp3 = new JLabel("<html><body>3. \"arreter arrosage\"<br/>Pour arreter arrosage.</body></html>");
        jLabelHelp3.setFont(new Font("Serif", Font.BOLD, 15));
        JLabel jLabelHelp4 = new JLabel("<html><body>4. \"temperature/watersensorvaluefloor1(2/3)/ph/humidite\" [chiffre]<br/>Pour mettre à jour la valeur correspondante (pour developper).</body></html>");//TODO: (pour developper), a supprimer
        jLabelHelp4.setFont(new Font("Serif", Font.BOLD, 15));//TODO: (pour developper), a supprimer
        JLabel jLabelHelp5 = new JLabel("<html><body>5. \"exit\" [chiffre]<br/>Pour quitter.</body></html>");//TODO: (pour developper), a supprimer
        jLabelHelp5.setFont(new Font("Serif", Font.BOLD, 15));//TODO: (pour developper), a supprimer

        mainPanel.add(jLabelHelpTitle);
        mainPanel.add(jLabelHelp1);
        mainPanel.add(jLabelHelp2);
        mainPanel.add(jLabelHelp3);

        mainPanel.add(jLabelHelp4); // TODO: (pour developper), a supprimer
        mainPanel.add(jLabelHelp5); // TODO: (pour developper), a supprimer

        mainPanel.setLayout(new GridLayout(6, 0));

        mainPanel.repaint();
        jFrame.validate();
    }

    private void displayActionOnAllFloorsPanel(ActionType actionType) {
        mainPanel.removeAll();
        informationPanel.removeAll();

        mainPanel.setLayout(new GridLayout(2, 2));

        JLabel actionTitleLabel = new JLabel();
        actionTitleLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JLabel actionSubTitleLabel = new JLabel();

        Class sensorClass = null;

        for (JPanel cameraJPanel : this.cameraPanels) {
            mainPanel.add(cameraJPanel);
        }

        switch (actionType) {
            case WATERING:
                actionTitleLabel.setText("Arrosage de tous les étages en cours");
                actionSubTitleLabel.setText("Pourcentage d'eau :\n");
                sensorClass = WaterSensor.class;
                garden.getAllFloors().forEach(Floor::startWater);
                break;
            case LIGHTING:
                actionTitleLabel.setText("Éclairage de tous les étages en cours");
                actionSubTitleLabel.setText("Taux de luminosité :\n");
                sensorClass = BrightnessSensor.class;
                garden.getAllFloors().forEach(Floor::turnOnLight);
                break;
        }


        informationPanel.add(actionTitleLabel);
        informationPanel.add(actionSubTitleLabel);

        final Class finalSensorClass = sensorClass;

        floorSensorLabels.stream()
                .filter(floorSensorLabel -> floorSensorLabel.getSensor().getClass().equals(finalSensorClass))
                .sorted((o1, o2) -> o1.getFloorId() < o2.getFloorId() ? -1 : 1)
                .map(SensorLabel::getLabel)
                .forEach(jLabel -> informationPanel.add(jLabel));

        informationPanel.setLayout(new GridLayout(5, 0));

        mainPanel.add(informationPanel);

        this.mainPanel.repaint();
        this.jFrame.validate();
    }

    private void displayActionOnSpecificFloorPanel(ActionType actionType, int floorId) {
        mainPanel.removeAll();
        informationPanel.removeAll();

        JLabel actionTitleLabel = new JLabel();
        actionTitleLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JLabel actionSubTitleLabel = new JLabel();
        JLabel sensorLabel;

        Class sensorClass = null;
        String floorName;

        if (floorId == 1)
            floorName = "premier";
        else if (floorId == 2)
            floorName = "deuxième";
        else
            floorName = "troisième";

        mainPanel.setLayout(new GridLayout(0, 2));

        switch (actionType) {
            case WATERING:
                actionTitleLabel.setText("Arrosage du " + floorName + " étage");
                actionSubTitleLabel.setText("Pourcentage d'eau :\n");
                sensorClass = WaterSensor.class;
                garden.getFloor(floorId).startWater();
                break;
            case LIGHTING:
                actionTitleLabel.setText("Éclairage du " + floorName + " étage");
                actionSubTitleLabel.setText("Taux de luminosité :\n");
                sensorClass = BrightnessSensor.class;
                garden.getFloor(floorId).turnOnLight();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + actionType);
        }

        Class finalSensorClass = sensorClass;

        sensorLabel = floorSensorLabels.stream()
                .filter(floorSensorLabel -> floorSensorLabel.getSensor().getClass().equals(finalSensorClass))
                .filter(floorSensorLabel -> floorSensorLabel.getFloorId() == floorId)
                .findFirst()
                .map(SensorLabel::getLabel)
                .get();

        informationPanel.add(actionTitleLabel);
        informationPanel.add(actionSubTitleLabel);
        informationPanel.add(sensorLabel);

        mainPanel.setLayout(new GridLayout(0, 2));
        mainPanel.add(cameraPanels[floorId - 1]);
        mainPanel.add(informationPanel);

        this.mainPanel.repaint();
        this.jFrame.validate();
    }

    private static XYDataset createDataset(TreeMap<Instant, Float> lastValues, String titleChart) {
        double[][] data = {new double[lastValues.size()], new double[lastValues.size()]};
        int indiceData = 0;
        for (Instant key : lastValues.keySet()) {
            Float value = lastValues.get(key);
            data[0][indiceData] = (double) (indiceData);
            data[1][indiceData] = Double.valueOf(value);
            indiceData++;
        }
        DefaultXYDataset ds = new DefaultXYDataset();
        ds.addSeries(titleChart, data);
        return ds;
    }

    void displayChart(Class sensorType, int floorId) {
        Sensor sensor;
        String chartTitle;
        if (sensorType.equals(TemperatureSensor.class)) {
            sensor = garden.getTemperatureSensor();
            chartTitle = "Capteur température";
        } else if (sensorType.equals(HumiditySensor.class)) {
            sensor = garden.getHumiditySensor();
            chartTitle = "Capteur humidité";
        } else if (sensorType.equals(WaterSensor.class)) {
            sensor = garden.getFloor(floorId).getWaterSensor();
            chartTitle = "Capteur d'eau étage " + floorId;
        } else if (sensorType.equals(BrightnessSensor.class)) {
            sensor = garden.getFloor(floorId).getBrightnessSensor();
            chartTitle = "Capteur luminosité étage " + floorId;
        } else if (sensorType.equals(AciditySensor.class)) {
            sensor = garden.getFloor(floorId).getAciditySensor();
            chartTitle = "Capteur HP de l'étage " + floorId;
        } else
            throw new IllegalStateException("Unexpected value: " + sensorType);

        XYDataset ds = createDataset(sensor.getLastValues(), chartTitle);
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                "temps", "valeur", ds, PlotOrientation.VERTICAL, true, true,
                false);

        ChartPanel cp = new ChartPanel(chart);

        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(cp, BorderLayout.CENTER);
        this.mainPanel.repaint();
        this.jFrame.validate();
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

    private void stopAction(ActionType actionType) {// TODO: stopWatering -> arreter quel étage ...
        switch (actionType) {
            case WATERING:
                garden.getAllFloors()
                        .forEach(Floor::stopWater);
                break;
            case LIGHTING:
                garden.getAllFloors()
                        .forEach(Floor::turnOffLight);
                break;
        }
        displayWelcome();
    }

    // -- TEST a l'aide console --
    private void getCommand() {
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
        String[] words = c.split(" ");

        int floorId = -1;

        if (words.length == 2) {
            String firstWord = words[0].toLowerCase();
            switch (firstWord) {
                case "temperature":
                    garden.getTemperatureSensor().addValue(Float.valueOf(words[1]));
                    refreshGardenSensorLabel(TemperatureSensor.class);
                    break;
                case "humidite":
                    garden.getHumiditySensor().addValue(Float.valueOf(words[1]));
                    refreshGardenSensorLabel(HumiditySensor.class);
                    break;
                case "watersensorvaluefloor1":
                    garden.getFloor(1).getWaterSensor().addValue(Float.valueOf(words[1]));
                    refreshFloorSensorLabel(WaterSensor.class, 1);
                    break;
                case "watersensorvaluefloor2":
                    garden.getFloor(2).getWaterSensor().addValue(Float.valueOf(words[1]));
                    refreshFloorSensorLabel(WaterSensor.class, 2);
                    break;
                case "watersensorvaluefloor3":
                    garden.getFloor(3).getWaterSensor().addValue(Float.valueOf(words[1]));
                    refreshFloorSensorLabel(WaterSensor.class, 3);
                case "arroser":
                    if (words[1].toLowerCase().equals("tout"))
                        displayActionOnAllFloorsPanel(ActionType.WATERING);
                    else {
                        floorId = Integer.parseInt(words[1]);
                        displayActionOnSpecificFloorPanel(ActionType.WATERING, floorId);
                    }
                    break;
                case "allumer":
                    if (words[1].toLowerCase().equals("tout"))
                        displayActionOnAllFloorsPanel(ActionType.LIGHTING);
                    else {
                        floorId = Integer.parseInt(words[1]);
                        displayActionOnSpecificFloorPanel(ActionType.LIGHTING, floorId);
                    }
                case "arreter":
                    if (words[1].toLowerCase().equals("arrosage")) // TO DO : arroser quel étage ...
                        stopAction(ActionType.WATERING);
                    else if (words[1].toLowerCase().equals("eclairage"))
                        stopAction(ActionType.LIGHTING);
                    break;
                default:
                    System.out.println("C'est vraiment trop difficile à comprendre votre commande -> " + c);
                    break;
            }
        } else if (words.length == 1) {
            switch (words[0].toLowerCase()) {
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
