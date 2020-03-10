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
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

class GardenHCI extends JFrame {

    private Garden garden;
    private User user;

    private JFrame jFrame;
    private JPanel mainPanel, informationPanel;

    private JPanel[] cameraPanels = new JPanel[3];

    private List<GardenSensorLabel> gardenSensorLabels = new ArrayList<>();
    private List<FloorSensorLabel> floorSensorLabels = new ArrayList<>();

    enum ActionType {
        WATERING,
        LIGHTING
    }

    GardenHCI(Garden garden, User user) {
        this.garden = garden;
        this.user = user;
    }

    void launchHCI() {
        initHCI();
        initCameras();
        initGardenSensors();
        initFloorsSensors();

        displayWelcome();
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

        for (Floor floor : garden.getFloors()) {
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
        for (Floor floor : garden.getFloors()) {
            floorSensorLabels.add(new FloorSensorLabel(floor.getFloorId(), floor.getAciditySensor(), "g/L"));
            floorSensorLabels.add(new FloorSensorLabel(floor.getFloorId(), floor.getBrightnessSensor(), "cd/m²"));
            floorSensorLabels.add(new FloorSensorLabel(floor.getFloorId(), floor.getWaterSensor(), "%"));
        }
    }

    void displayWelcome() {
        mainPanel.removeAll();

        // refactor cameraPanels
        for (JPanel cameraJPanel : this.cameraPanels) {
            this.mainPanel.add(cameraJPanel);
        }

        // panelWelcomeInfo
        informationPanel.removeAll();
        informationPanel.setBorder(new LineBorder(Color.gray));

        JLabel helpLabel = new JLabel("Dites \"help\" pour afficher la liste des commandes disponibles", SwingConstants.CENTER);

        informationPanel.add(gardenSensorLabels.get(0).getLabel());
        informationPanel.add(gardenSensorLabels.get(1).getLabel());
        informationPanel.add(helpLabel);
        informationPanel.setLayout(new GridLayout(5, 0));

        mainPanel.add(informationPanel);
        mainPanel.setLayout(new GridLayout(2, 2));
        mainPanel.repaint();
        this.jFrame.validate();
    }

    void displayHelp() {
        JLabel title = new JLabel("------ Aide ------", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel help1 = new JLabel("<html><body>1.\"spray [all/1/2/3]\"<br/>Pour arroser un étage ou tout les étages.</body></html>");
        JLabel help2 = new JLabel("<html><body>2. \"turn on light [all/1/2/3]\"<br/>Pour allumer la/les lumières.</body></html>");
        JLabel help3 = new JLabel("<html><body>3. \"stop spray / trun off light\"<br/>Pour arrêter l'arrosage ou l'éclairage.</body></html>");
        JLabel help4 = new JLabel("<html><body>4. \"display [1/2/3]\"<br/>Pour afficher les informations d'un étage.</body></html>");
        JLabel help5 = new JLabel("<html><body>5. \"history [temperature/humidite/luminosite/eau/acidite] ([1/2/3])\"<br/>Pour visualiser un historique.</body></html>");
        JLabel help6 = new JLabel("<html><body>6. \"display stats\"<br/>Pour afficher les statistiques.</body></html>");
        JLabel help7 = new JLabel("<html><body>7. \"home\"<br/>Pour retourner à la page d'accueil.</body></html>");
        JLabel help8 = new JLabel("<html><body>Notice : Il ne faut pas dire \"stop\" cela va arrêter la commande de reconnaissance vocal.</body></html>");

        displayTextPanel(Arrays.asList(title, help1, help2, help3, help4, help5, help6, help7, help8));
    }

    void displayStats() {
        JLabel title = new JLabel("Statistiques pour l'utilisateur : " + user.getUserName(), SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel executedCommands = new JLabel("<html><body>Nombre de commandes exécutées : " + user.getExecutedCommandsCount() + "</body></html>");
        executedCommands.setFont(new Font("Serif", Font.BOLD, 15));
        JLabel executedlightings = new JLabel("<html><body>Nombre d'éclairages exécutés : " + user.getExecutedLightingsCount() + "</body></html>");
        executedlightings.setFont(new Font("Serif", Font.BOLD, 15));
        JLabel executedCWaterings = new JLabel("<html><body>Nombre d'arrosages exécutés : " + user.getExecutedWateringsCount() + "</body></html>");
        executedCWaterings.setFont(new Font("Serif", Font.BOLD, 15));

        displayTextPanel(Arrays.asList(title, executedCommands, executedlightings, executedCWaterings));
    }

    private void displayTextPanel(List<JLabel> jLabels) {
        mainPanel.removeAll();

        jLabels.forEach(jLabel -> mainPanel.add(jLabel));

        mainPanel.setLayout(new GridLayout(jLabels.size(), 0));

        mainPanel.repaint();
        jFrame.validate();
    }

    void displayActionOnAllFloorsPanel(ActionType actionType) {
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
                break;
            case LIGHTING:
                actionTitleLabel.setText("Éclairage de tous les étages en cours");
                actionSubTitleLabel.setText("Taux de luminosité :\n");
                sensorClass = BrightnessSensor.class;
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

    void displayActionOnSpecificFloorPanel(ActionType actionType, int floorId) {
        mainPanel.removeAll();
        informationPanel.removeAll();

        JLabel actionTitleLabel = new JLabel();
        actionTitleLabel.setFont(new Font("Serif", Font.BOLD, 20));

        JLabel actionSubTitleLabel = new JLabel();
        JLabel sensorLabel;

        Class sensorClass;
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
                break;
            case LIGHTING:
                actionTitleLabel.setText("Éclairage du " + floorName + " étage");
                actionSubTitleLabel.setText("Taux de luminosité :\n");
                sensorClass = BrightnessSensor.class;
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

    void displayFloorPanel(int floorId) {
        mainPanel.removeAll();
        informationPanel.removeAll();
        mainPanel.setLayout(new GridLayout(0, 2));
        informationPanel.setLayout(new GridLayout(4, 0));

        JLabel titleLabel = new JLabel();
        if (floorId == 1)
            titleLabel.setText("  Premier étage");
        else if (floorId == 2)
            titleLabel.setText("  Deuxième étage");
        else
            titleLabel.setText("  Troisième étage");

        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        JLabel acidityJLabel = new JLabel("    Acitité : " + garden.getFloor(floorId).getAciditySensor().getCurrentValue());
        JLabel brightnessJLabel = new JLabel("    Luminosité : " + garden.getFloor(floorId).getBrightnessSensor().getCurrentValue());
        JLabel waterJLabel = new JLabel("    Eau : " + garden.getFloor(floorId).getWaterSensor().getCurrentValue());

        informationPanel.add(titleLabel);
        informationPanel.add(acidityJLabel);
        informationPanel.add(brightnessJLabel);
        informationPanel.add(waterJLabel);
        mainPanel.add(cameraPanels[floorId - 1]);
        mainPanel.add(informationPanel);

        this.mainPanel.repaint();
        this.jFrame.validate();
    }

    private static XYDataset createDataset(TreeMap<Instant, Float> lastValues, String titleChart) {
        double[][] data = {new double[lastValues.size()], new double[lastValues.size()]};
        int indexData = 0;
        for (Instant key : lastValues.keySet()) {
            Float value = lastValues.get(key);
            data[0][indexData] = (double) (indexData);
            data[1][indexData] = Double.valueOf(value);
            indexData++;
        }
        DefaultXYDataset ds = new DefaultXYDataset();
        ds.addSeries(titleChart, data);
        return ds;
    }

    void displayChart(final Class sensorType) {
        displayChart(sensorType, -1);
    }

    void displayChart(final Class sensorType, final int floorId) {
        Sensor sensor;
        String chartTitle;

        if (sensorType.equals(TemperatureSensor.class)) {
            sensor = garden.getTemperatureSensor();
            chartTitle = "Capteur de température";
        } else if (sensorType.equals(HumiditySensor.class)) {
            sensor = garden.getHumiditySensor();
            chartTitle = "Capteur d'humidité";
        } else if (sensorType.equals(WaterSensor.class)) {
            sensor = garden.getFloor(floorId).getWaterSensor();
            chartTitle = "Capteur d'eau étage " + floorId;
        } else if (sensorType.equals(BrightnessSensor.class)) {
            sensor = garden.getFloor(floorId).getBrightnessSensor();
            chartTitle = "Capteur de luminosité étage " + floorId;
        } else if (sensorType.equals(AciditySensor.class)) {
            sensor = garden.getFloor(floorId).getAciditySensor();
            chartTitle = "Capteur d'acidité étage " + floorId;
        } else
            throw new IllegalStateException("Unexpected value: " + sensorType);

        XYDataset ds = createDataset(sensor.getLastValues(), chartTitle);
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                "temps", "valeur", ds, PlotOrientation.VERTICAL, true, true,
                false);

        ChartPanel cp = new ChartPanel(chart);
        Panel chartInfoPanel = new Panel();
        Label infoCurrentLabel = new Label("Valeur courante : " + sensor.getCurrentValue());
        Label infoAverageLabel = new Label("Moyenne : " + sensor.getAverage());
        Label infoMintLabel = new Label("Valeur minimum : " + sensor.getMinValue());
        Label infoMaxLabel = new Label("Valeur maximum : " + sensor.getMaxValue());

        chartInfoPanel.add(infoCurrentLabel);
        chartInfoPanel.add(infoAverageLabel);
        chartInfoPanel.add(infoMintLabel);
        chartInfoPanel.add(infoMaxLabel);
        chartInfoPanel.setLayout(new GridLayout(5, 0));

        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(cp, BorderLayout.CENTER);
        mainPanel.add(chartInfoPanel, BorderLayout.EAST);
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

    void stopAction() {
        displayWelcome();
    }
}
