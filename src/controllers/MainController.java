package controllers;

import eu.hansolo.medusa.Gauge;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Const;
import model.TableTelma;
import model.Telma;
import org.controlsfx.control.ToggleSwitch;
import threads.SimpleSocketServer;

import java.io.IOException;
import java.net.*;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private HBox mainPane;
    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Gauge ggEngineSpeed;
    @FXML
    private Gauge ggEngineTorque;
    @FXML
    private Gauge ggEnginePower;

    @FXML
    private Gauge ggRightSpeed;
    @FXML
    private Gauge ggLeftSpeed;

    @FXML
    private Gauge ggRightTorque;
    @FXML
    private Gauge ggLeftTorque;

    @FXML
    private ToggleSwitch tglRun;
    @FXML
    private ToggleSwitch tglStable;

    @FXML
    private TextField tfRefEngine;
    @FXML
    private Button btnCalc;
    @FXML
    private TextField tfGearRatio;

    @FXML
    private TextField tfStable;

    @FXML
    private TextField tfStartSpeed;
    @FXML
    private TextField tfFinalSpeed;
    @FXML
    private TextField tfRiseTime;

    @FXML
    private ToggleSwitch tglBtnWriteData;

    @FXML
    private Button btnStartServer;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnWriteData;

    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private XYChart.Series<Number, Number> serSpeed, serToque, serPower, serRefSpeed;

    private SimpleSocketServer serverTCP;

    private boolean isWriteData = false;
    private boolean isActionMode = false;
    private boolean isRun = false;
    private boolean isStable = false;

    private int refSpeedStable = 0;

    private int stepVal = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set marker off
        lineChart.setCreateSymbols(false);

        // create new symbols
        serSpeed = new XYChart.Series<>();
        serSpeed.setName("Скорость двигателя, об/мин");
        serToque = new XYChart.Series<>();
        serToque.setName("Момент, кг*м");
        serPower = new XYChart.Series<>();
        serPower.setName("Мощность, ЛС");
        serRefSpeed = new XYChart.Series<>();
        serRefSpeed.setName("Задание, об/мин");

        // add series
        lineChart.getData().add(serRefSpeed);
        lineChart.getData().add(serSpeed);
        lineChart.getData().add(serToque);
        lineChart.getData().add(serPower);

        //progressIndicator.setVisible(false);

        //set gear ratio text
        tfGearRatio.setText("1.0");

        // tglRun.setDisable(true);
        //tglStable.setDisable(true);

        //tfRefEngine.setDisable(true);
        //tfStable.setDisable(true);
        //btnCalc.setDisable(true);

        progressIndicator.setVisible(false);
        tglBtnWriteData.setDisable(false);

        btnStartServer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startServer();
            }
        });

        btnExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeProgram();
            }
        });

        btnCalc.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int refSpeedEngine = Integer.parseInt(tfRefEngine.getText());
                Telma.setGearRatio(refSpeedEngine / Telma.getAveSpeedRPM());

                tfGearRatio.setText(String.valueOf(Telma.getGearRatio()));
            }
        });

        tglBtnWriteData.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                isWriteData = tglBtnWriteData.isSelected();
                System.out.println(isWriteData + "");
                if(isWriteData){
                    serSpeed.getData().clear();
                    serToque.getData().clear();
                    serPower.getData().clear();
                    serRefSpeed.getData().clear();

                    TableTelma.clearRefSpeed();
                    TableTelma.clearAveSpeed();

                    TableTelma.clearLeftSpeed();
                    TableTelma.clearRightSpeed();

                    TableTelma.clearAveTorque();
                    TableTelma.clearLeftTorque();
                    TableTelma.clearRightTorque();

                    TableTelma.clearPower();

                    stepVal = -1;
                }
            }
        });

        tglRun.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                int startSpeed = Math.abs(Integer.parseInt(tfStartSpeed.getText()));
                int finalSpeed = Math.abs(Integer.parseInt(tfFinalSpeed.getText()));
                float riseTime = Math.abs(Float.parseFloat(tfRiseTime.getText()));

                isRun = tglRun.isSelected();

                if(startSpeed < 500 && isRun){
                    MessageBox.showError("Стартовая скорость должна быть больше 500 об/мин!");
                    tglRun.setSelected(false);
                    return;
                }

                if((finalSpeed - startSpeed) < 0 && isRun){
                    MessageBox.showError("Начальная скорость должна быть меньше финальной!");
                    tglRun.setSelected(false);
                    return;
                }

                if(riseTime < 2.0f && isRun){
                    MessageBox.showError("Темп нарастания должен быть больше 2 с!");
                    tglRun.setSelected(false);
                    return;
                }

                Telma.setStartSpeedEngine(startSpeed);
                Telma.setFinalSpeedEngine(finalSpeed);
                Telma.setRiseTime(riseTime);

                if(isRun && isStable){
                    tglStable.setSelected(false);
                }
                isActionMode = true;
            }
        });

        tglStable.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                isStable = tglStable.isSelected();
                if(isStable && isRun){
                    tglRun.setSelected(false);
                }
                isActionMode = true;

                if(isStable){
                    refSpeedStable = Math.abs(Integer.parseInt(tfStable.getText()));
                }else{
                    refSpeedStable = 0;
                    tfStable.setText("0");
                }
            }
        });


        btnWriteData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/fxml/result-data.fxml"));

                    Stage mainStage = new Stage();
                    mainStage.setTitle("DATA");

                    Scene scene = new Scene(root);

                    mainStage.setScene(scene);
                    mainStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        tfGearRatio.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    float gearR = Float.parseFloat(tfGearRatio.getText());
                    Telma.setGearRatio(gearR);
                    tfGearRatio.setStyle("-fx-background-color: #7CFC00;");
                }
            }
        });

        tfGearRatio.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tfGearRatio.setStyle("-fx-background-color: white;");
            }
        });

    }

    //---------------------------------------------------------------

    private void startServer(){
        activateProgrInd(true);

        btnStartServer.setDisable(true);

        serverTCP = new SimpleSocketServer(Const.PORT_SRC, this);
        serverTCP.startServer();

    }

    private void stopServer(){
        if(serverTCP != null) {
            serverTCP.stopServer();
        }
    }

    private void closeProgram(){
        stopServer();
        System.exit(0);
    }

    public void activateProgrInd(boolean isActive){
        if(isActive){
            mainPane.setDisable(true);
            progressIndicator.setVisible(true);
        }else{
            mainPane.setDisable(false);
            progressIndicator.setVisible(false);
        }
    }

    public void uiLineChartUpdate(Const.TYPE_LINE type){
        ObservableList<XYChart.Data<Number, Number>> list = null;

        switch (type){
            case SPEED: list = serSpeed.getData(); break;
            case REF: list = serRefSpeed.getData(); break;
            case TORQUE: list = serToque.getData(); break;
            case POWER: list = serPower.getData(); break;
            default: break;
        }

        float yValue = 0.0f;

        switch (type){
            case SPEED: yValue = Telma.getEngineSpeedRPM(); break;
            case REF: yValue = Telma.getRefSpeedRPM(); break;
            case TORQUE: yValue = Telma.getEngineTorque(); break;
            case POWER: yValue = Telma.getPowerHP(); break;
            default: break;
        }

        if(list == null) return;

        list.add(new XYChart.Data<>(stepVal, yValue));

        int lowBound = stepVal - 1000;
        int tickUnit = 100;
        if(lowBound < 0){
            lowBound = 0;
            tickUnit = 50;
        }

        xAxis.setLowerBound(lowBound);
        xAxis.setUpperBound(stepVal);

        xAxis.setTickUnit(tickUnit);
    }

    public void incValStep(){
        ++stepVal;
    }

    public boolean isActionMode() {
        return isActionMode;
    }

    public void setActionMode(boolean actionMode) {
        isActionMode = actionMode;
    }

    public boolean isRun() {
        return isRun;
    }

    public boolean isStable() {
        return isStable;
    }

    public int getRefSpeedStable() {
        return refSpeedStable;
    }

    public Gauge getGgEngineSpeed() {
        return ggEngineSpeed;
    }

    public Gauge getGgEnginePower() {
        return ggEnginePower;
    }

    public Gauge getGgEngineTorque() {
        return ggEngineTorque;
    }

    public Gauge getGgRightSpeed() {
        return ggRightSpeed;
    }

    public Gauge getGgLeftSpeed() {
        return ggLeftSpeed;
    }

    public Gauge getGgRightTorque() {
        return ggRightTorque;
    }

    public Gauge getGgLeftTorque() {
        return ggLeftTorque;
    }

    public boolean isWriteData() {
        return isWriteData;
    }

}
