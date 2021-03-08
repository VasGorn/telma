package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class DataResult implements Initializable {

    @FXML
    private LineChart lineChart;

    @FXML
    private TableView<RowTelma> tblStaticTelma;

    @FXML
    private TableColumn<RowTelma, Float> clmSpeed;
    @FXML
    private TableColumn<RowTelma, Float> clmTorque;
    @FXML
    private TableColumn<RowTelma, Float> clmPower;

    @FXML
    private TextField tfMaxTorq;
    @FXML
    private TextField tfSpeedTorq;

    @FXML
    private TextField tfMaxPower;
    @FXML
    private TextField tfSpeedPower;

    @FXML
    private TextField tfEfficiency;

    @FXML
    private TextField tfDelBefore;
    @FXML
    private TextField tfDelAfter;

    @FXML
    private TextField tfFileName;

    @FXML
    private TextField tfFileToLoad;

    @FXML
    private Button btnDelBefore;
    @FXML
    private Button btnDelAfter;

    @FXML
    private Button btnFileChooser;


    @FXML
    private Button btnSaveToFile;
    @FXML
    private Button btnSaveScreen;
    @FXML
    private Button btnClose;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    //private XYChart.Series<Number, Number> serTorqueUp, serTorqueDown;
    //private XYChart.Series<Number, Number> serPowerUp, serPowerDown;
    private XYChart.Series<Number, Number> serTorqueAve;
    private XYChart.Series<Number, Number> serPowerAve;


    private XYChart.Series<Number, Number> serTorqueF, serPowerF;

    private ObservableList<RowTelma> table;

    private Map<controllers.DataResult.T_FIELD, List> dataTelma;


    private final char DELIMETER = ',';

    private enum FIELD {
        REF_SPEED("Ref speed rpm"),
        AVE_SPEED("Ave speed rpm"),
        LEFT_SPEED("Left speed rpm"),
        RIGHT_SPEED("Right speed rpm"),
        AVE_TORQUE("Ave torque kg*m"),
        LEFT_TORQUE("Left torque kg*m"),
        RIGHT_TORQUE("Right torque kg*m"),
        POWER("Power HP");

        private String name;
        FIELD(String name){
            this.name = name;
        }

        @Override
        public String toString(){
            return this.name;
        }
    }

    private enum T_FIELD{
        UP_REF_SPEED("Up Ref speed rpm"),
        DOWN_REF_SPEED("Down Ref speed rpm"),

        UP_AVE_SPEED("Up Ave speed rpm"),
        UP_LEFT_SPEED("Up Left speed rpm"),
        UP_RIGHT_SPEED("Up Right speed rpm"),

        DOWN_AVE_SPEED("Down Ave speed rpm"),
        DOWN_LEFT_SPEED("Down Left speed rpm"),
        DOWN_RIGHT_SPEED("Down Right speed rpm"),

        UP_AVE_TORQUE("Up Ave torque kg*m"),
        UP_LEFT_TORQUE("Up Left torque kg*m"),
        UP_RIGHT_TORQUE("Up Right torque kg*m"),

        DOWN_AVE_TORQUE("Down Ave torque kg*m"),
        DOWN_LEFT_TORQUE("Down Left torque kg*m"),
        DOWN_RIGHT_TORQUE("Down Right torque kg*m"),

        UP_POWER("Up Power HP"),
        DOWN_POWER("Down Power HP"),

        AVE_SPEED("Ave speed rpm"),
        AVE_TORQUE("Ave torque kg*m"),
        AVE_POWER("Ave Power HP");

        private String name;
        T_FIELD(String name){
            this.name = name;
        }

        @Override
        public String toString(){
            return this.name;
        }
    }

    private enum LIMIT{
        BEFORE,
        AFTER
    }

    private float EFFICIENCY = 1.0f;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set test data
        //setTestData();

        // set marker off
        lineChart.setCreateSymbols(false);

        // create new symbols
        /*serTorqueUp = new XYChart.Series<>();
        serTorqueUp.setName("Момент при подъеме, Н*м");
        serTorqueDown = new XYChart.Series<>();
        serTorqueDown.setName("Момент при спуске, Н*м");*/
        serTorqueAve = new XYChart.Series<>();
        serTorqueAve.setName("Момент средний, Н*м");

        /*serPowerUp = new XYChart.Series<>();
        serPowerUp.setName("Мощность при подъеме, ЛС");
        serPowerDown = new XYChart.Series<>();
        serPowerDown.setName("Мощность при спуске, ЛС");*/
        serPowerAve = new XYChart.Series<>();
        serPowerAve.setName("Мощность средняя, ЛС");

        serTorqueF = new XYChart.Series<>();
        serTorqueF.setName("Момент средний (из файла), Н*м");
        serPowerF = new XYChart.Series<>();
        serPowerF.setName("Мощность средняя (из файла), ЛС");

        // add series
        /*lineChart.getData().add(serTorqueUp);
        lineChart.getData().add(serTorqueDown);*/
        lineChart.getData().add(serTorqueAve);

        /*lineChart.getData().add(serPowerUp);
        lineChart.getData().add(serPowerDown);*/
        lineChart.getData().add(serPowerAve);

        lineChart.getData().add(serTorqueF);
        lineChart.getData().add(serPowerF);




        TriangleData triangleData =
                new TriangleData(TableTelma.getLeftSpeedList(), TableTelma.getRightSpeedList(), TableTelma.getAveSpeedList(),
                                 TableTelma.getLeftTorqueList(), TableTelma.getRightTorqueList(), TableTelma.getAveTorqueList(),
                                 TableTelma.getPowerList(), TableTelma.getRefSpeedList());

        //==========================================================================

        int sizeR = TableTelma.getRefSpeedList().size();
        if(sizeR == 0 ){
            return;
        }

        dataTelma = new HashMap<>();

        dataTelma.put(DataResult.T_FIELD.UP_REF_SPEED, triangleData.getUpRefSpeed());
        dataTelma.put(DataResult.T_FIELD.DOWN_REF_SPEED, triangleData.getDownRefSpeed());

        dataTelma.put(DataResult.T_FIELD.UP_LEFT_SPEED, triangleData.getUpSpeedLeft());
        dataTelma.put(DataResult.T_FIELD.UP_RIGHT_SPEED, triangleData.getUpSpeedRight());
        dataTelma.put(DataResult.T_FIELD.UP_AVE_SPEED, triangleData.getUpSpeedAve());

        dataTelma.put(DataResult.T_FIELD.DOWN_LEFT_SPEED, triangleData.getDownSpeedLeft());
        dataTelma.put(DataResult.T_FIELD.DOWN_RIGHT_SPEED, triangleData.getDownSpeedRight());
        dataTelma.put(DataResult.T_FIELD.DOWN_AVE_SPEED, triangleData.getDownSpeedAve());

        dataTelma.put(DataResult.T_FIELD.UP_LEFT_TORQUE, triangleData.getUpTorqueLeft());
        dataTelma.put(DataResult.T_FIELD.UP_RIGHT_TORQUE, triangleData.getUpTorqueRight());
        dataTelma.put(DataResult.T_FIELD.UP_AVE_TORQUE, triangleData.getUpTorqueAve());

        dataTelma.put(DataResult.T_FIELD.DOWN_LEFT_TORQUE, triangleData.getDownTorqueLeft());
        dataTelma.put(DataResult.T_FIELD.DOWN_RIGHT_TORQUE, triangleData.getDownTorqueRight());
        dataTelma.put(DataResult.T_FIELD.DOWN_AVE_TORQUE, triangleData.getDownTorqueAve());

        dataTelma.put(DataResult.T_FIELD.UP_POWER, triangleData.getUpPower());
        dataTelma.put(DataResult.T_FIELD.DOWN_POWER, triangleData.getDownPower());

        dataTelma.put(DataResult.T_FIELD.AVE_SPEED, triangleData.getAveSpeed());
        dataTelma.put(DataResult.T_FIELD.AVE_TORQUE, triangleData.getAveTorque());
        dataTelma.put(DataResult.T_FIELD.AVE_POWER, triangleData.getAvePower());

        //==========================================================================

        /*setLine(serTorqueUp.getData(), dataTelma.get(T_FIELD.UP_AVE_SPEED), dataTelma.get(T_FIELD.UP_AVE_TORQUE), 9.8f);
        setLine(serTorqueDown.getData(), dataTelma.get(T_FIELD.DOWN_AVE_SPEED), dataTelma.get(T_FIELD.DOWN_AVE_TORQUE), 9.8f);*/
        setLine(serTorqueAve.getData(), dataTelma.get(T_FIELD.AVE_SPEED), dataTelma.get(T_FIELD.AVE_TORQUE), 9.8f);

        /*setLine(serPowerUp.getData(), dataTelma.get(DataResult.T_FIELD.UP_AVE_SPEED), dataTelma.get(T_FIELD.UP_POWER), 1.0f);
        setLine(serPowerDown.getData(), dataTelma.get(DataResult.T_FIELD.DOWN_AVE_SPEED), dataTelma.get(T_FIELD.DOWN_POWER), 1.0f);*/
        setLine(serPowerAve.getData(), dataTelma.get(DataResult.T_FIELD.AVE_SPEED), dataTelma.get(T_FIELD.AVE_POWER), 1.0f);

        //==========================================================================

        clmSpeed.setCellValueFactory(new PropertyValueFactory<>("speed"));
        clmTorque.setCellValueFactory(new PropertyValueFactory<>("torque"));
        clmPower.setCellValueFactory(new PropertyValueFactory<>("power"));

        table = getRows();
        tblStaticTelma.setItems(table);

        //==========================================================================

        // set date as file name
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String dateFile = sdf.format(timestamp);
        tfFileName.setText(dateFile);

        //==========================================================================

        // choose file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл с предыдущем замером мощности");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV files", "*.csv")
        );

        fileChooser.setInitialDirectory(new File("."));

        //==========================================================================

        btnFileChooser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = fileChooser.showOpenDialog(btnFileChooser.getScene().getWindow());
                if(selectedFile != null) {
                    tfFileToLoad.setText(selectedFile.getName());
                    List<Float> listAveSpeed = getListFromFile(selectedFile, T_FIELD.AVE_SPEED.toString());
                    List<Float> listAveTorque = getListFromFile(selectedFile, T_FIELD.AVE_TORQUE.toString());
                    List<Float> listPower = getListFromFile(selectedFile, T_FIELD.AVE_POWER.toString());

                    if(listAveSpeed == null || listAveTorque == null || listPower == null){
                        return;
                    }

                    serTorqueF.getData().clear();
                    serPowerF.getData().clear();

                    EFFICIENCY = 1.0f;
                    setLine(serTorqueF.getData(), listAveSpeed, listAveTorque, 9.8f);
                    setLine(serPowerF.getData(), listAveSpeed, listPower, 1.0f);
                }

            }
        });


        btnClose.setOnAction(event -> {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            stage.close();
        });


        btnSaveToFile.setOnAction(event -> {
            String fileName = tfFileName.getText().trim();

            try {
                FileWriter writer = new FileWriter("./" + fileName + ".csv");

                String sRow;

                int rowSize = dataTelma.size();
                String[] arrRow = new String[rowSize];

                //===========================================
                // set name column
                int num = 0;
                for(Map.Entry<T_FIELD, List> entry: dataTelma.entrySet()){
                    arrRow[num] = entry.getKey().toString();

                    ++num;
                }

                sRow = concat(arrRow);
                writer.write(sRow);
                writer.write('\n'); // newline

                //===========================================
                // set through all data list
                int listSize = dataTelma.get(T_FIELD.AVE_SPEED).size();

                for(int i = 0; i < listSize; ++i){

                    num = 0;
                    for (Map.Entry<T_FIELD, List> entry: dataTelma.entrySet()) {

                        //if list is empty
                        if(entry.getValue().size() == 0){
                            arrRow[num] = "0.0";
                            ++num;
                            continue;
                        }

                        arrRow[num] = String.valueOf(entry.getValue().get(i));
                        ++num;
                    }

                    sRow = concat(arrRow);
                    writer.write(sRow);
                    writer.write('\n'); // newline

                }

                writer.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        btnDelBefore.setOnAction(event -> {
            float delSpeedBefore = Float.parseFloat(tfDelBefore.getText());

            deleteData(delSpeedBefore, LIMIT.BEFORE);
        });

        btnDelAfter.setOnAction(event -> {
            float delSpeedAfter = Float.parseFloat(tfDelAfter.getText());

            deleteData(delSpeedAfter, LIMIT.AFTER);
        });

        btnSaveScreen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String fileName = tfFileName.getText().trim();

                // saves screenshot to desired path
                try {
                    Robot r = new Robot();

                    String path = "./" + fileName + ".png";

                    Rectangle capture = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                    BufferedImage Image = r.createScreenCapture(capture);
                    ImageIO.write(Image, "png", new File(path));
                    System.out.println("Screenshot saved");

                } catch (IOException | AWTException e) {
                    e.printStackTrace();
                }
            }
        });

        tfEfficiency.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    EFFICIENCY = Float.parseFloat(tfEfficiency.getText());

                    /*serTorqueUp.getData().clear();
                    serTorqueDown.getData().clear();*/
                    serTorqueAve.getData().clear();

                    /*serPowerUp.getData().clear();
                    serPowerDown.getData().clear();*/
                    serPowerAve.getData().clear();

                    /*setLine(serTorqueUp.getData(), dataTelma.get(T_FIELD.UP_AVE_SPEED), dataTelma.get(T_FIELD.UP_AVE_TORQUE), 9.8f);
                    setLine(serTorqueDown.getData(), dataTelma.get(T_FIELD.DOWN_AVE_SPEED), dataTelma.get(T_FIELD.DOWN_AVE_TORQUE), 9.8f);*/
                    setLine(serTorqueAve.getData(), dataTelma.get(T_FIELD.AVE_SPEED), dataTelma.get(T_FIELD.AVE_TORQUE), 9.8f);


                    /*setLine(serPowerUp.getData(), dataTelma.get(DataResult.T_FIELD.UP_AVE_SPEED), dataTelma.get(T_FIELD.UP_POWER), 1.0f);
                    setLine(serPowerDown.getData(), dataTelma.get(DataResult.T_FIELD.DOWN_AVE_SPEED), dataTelma.get(T_FIELD.DOWN_POWER), 1.0f);*/
                    setLine(serPowerAve.getData(), dataTelma.get(DataResult.T_FIELD.AVE_SPEED), dataTelma.get(T_FIELD.AVE_POWER), 1.0f);


                    tblStaticTelma.getItems().clear();
                    table = getRows();
                    tblStaticTelma.setItems(table);

                    findMaxTorquePoint();

                    tfEfficiency.setStyle("-fx-background-color: #7CFC00;");
                }
            }
        });

        tfEfficiency.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tfEfficiency.setStyle("-fx-background-color: white;");
            }
        });

        findMaxTorquePoint();

    }


    private void setLine(ObservableList<XYChart.Data<Number, Number>> list, List listX, List listY, float xK){
        if(listX == null || listY == null){
            return;
        }

        for(int i = 0; i < listX.size(); ++i){
            float x = (float) listX.get(i);
            float y = (float) listY.get(i);
            y = y * xK / EFFICIENCY;
            list.add(new XYChart.Data<Number, Number>(x, y));
        }
    }

    private ObservableList<RowTelma> getRows(){
        ObservableList<RowTelma> rows = FXCollections.observableArrayList();

        List lSpeed = dataTelma.get(T_FIELD.AVE_SPEED);
        List lTorque = dataTelma.get(T_FIELD.AVE_TORQUE);
        List lPower = dataTelma.get(T_FIELD.AVE_POWER);

        float speedVal = 0.0f;
        float torqueVal = 0.0f;
        float powerVal = 0.0f;

        for(int i = 0; i < lSpeed.size(); ++i){
            speedVal = Math.round((float)lSpeed.get(i) * 10) / 10.0f;
            torqueVal = ((float)lTorque.get(i)) / EFFICIENCY;
            powerVal = ((float)lPower.get(i)) / EFFICIENCY;

            rows.add(new RowTelma(speedVal, torqueVal, powerVal));
        }

        return rows;
    }

    private void scaleData(ObservableList<XYChart.Data<Number, Number>> list,
                           List originList,
                           float scale){

        int size = list.size();

        for (int i = 0; i < size; ++i) {
            float yOldVal = (float) originList.get(i);
            list.get(i).setYValue(yOldVal * scale);
        }
    }

    private String concat(String[] arrS) {
        StringBuilder row = new StringBuilder(arrS[0]);
        row.append(DELIMETER);

        for(int i = 1; i < arrS.length; ++i){
            row.append(arrS[i]).append(DELIMETER);
        }

        return row.toString();
    }

    private void setTestData(){
        File file = new File("C:/Users/kings/Documents/idea-projects/telmaServerTCP2/2020.06.03.18.14.51 tiangle.csv");

        List<Float> listAveSpeed = getListFromFile(file, FIELD.AVE_SPEED.toString());
        List<Float> listLeftSpeed = getListFromFile(file, FIELD.LEFT_SPEED.toString());
        List<Float> listRightSpeed = getListFromFile(file, FIELD.RIGHT_SPEED.toString());

        List<Float> listAveTorque = getListFromFile(file, FIELD.AVE_TORQUE.toString());
        List<Float> listLeftTorque = getListFromFile(file, FIELD.LEFT_TORQUE.toString());
        List<Float> listRightTorque = getListFromFile(file, FIELD.RIGHT_TORQUE.toString());

        List<Float> listPower = getListFromFile(file, FIELD.POWER.toString());
        List<Float> listRefSpeed = getListFromFile(file, FIELD.REF_SPEED.toString());


        /*for(int i = 0; i < 8000; ++i){
            float speed = (float) i;
            float torque = 0;

            if(i < 4000){
                torque = 0.005f * i;
            }else{
                torque = 0.005f * (8000 - i);
            }

            testAveSpeedData.add(speed);
            testAveTorqueData.add(torque);
            testPowerData.add((torque * 9.8f * speed * 2.0f * 3.14159f / 60.0f) / 746.0f);
        }*/

        TableTelma.setListAveSpeed(listAveSpeed);
        TableTelma.setListLeftSpeed(listLeftSpeed);
        TableTelma.setListRightSpeed(listRightSpeed);

        TableTelma.setListAveTorque(listAveTorque);
        TableTelma.setListLeftTorque(listLeftTorque);
        TableTelma.setListRightTorque(listRightTorque);

        TableTelma.setListPower(listPower);
        TableTelma.setListRefSpeed(listRefSpeed);

    }

    private void findMaxTorquePoint() {
        List lSpeed = dataTelma.get(T_FIELD.AVE_SPEED);
        List lTorque = dataTelma.get(T_FIELD.AVE_TORQUE);
        List lPower = dataTelma.get(T_FIELD.AVE_POWER);

        int size = lSpeed.size();

        float maxTorque = 0;
        float speedTorq = 0;

        float maxPower = 0;
        float speedPower = 0;

        for (int i = 0; i < size; ++i) {
            float currSpeed = (float)lSpeed.get(i);
            float currTorque = ((float)lTorque.get(i)) / EFFICIENCY;
            float currPower = ((float)lPower.get(i)) / EFFICIENCY;

            if(currTorque > maxTorque){
                maxTorque = currTorque;
                speedTorq = currSpeed;
            }

            if(currPower > maxPower){
                maxPower = currPower;
                speedPower = currSpeed;
            }

        }

        tfMaxTorq.setText(String.valueOf(maxTorque));
        tfSpeedTorq.setText(String.valueOf(speedTorq));

        tfMaxPower.setText(String.valueOf(maxPower));
        tfSpeedPower.setText(String.valueOf(speedPower));
        
    }

    private void deleteData (float fLimit, LIMIT type){
        boolean isFind = false;

        int index = 0;

        for (int i = 0; i < dataTelma.get(T_FIELD.AVE_SPEED).size(); ++i) {
            float currSpeed =(float) dataTelma.get(T_FIELD.AVE_SPEED).get(i);
            if(currSpeed >= fLimit){
                isFind = true;
                index = i;
                break;
            }
        }

        if(!isFind){
            return;
        }

        Iterator<Map.Entry<T_FIELD, List>> itMap = dataTelma.entrySet().iterator();

        while (itMap.hasNext()){
            Map.Entry<T_FIELD, List> entry = itMap.next();

            Iterator<Float> itList = entry.getValue().iterator();
            int curInd = 0;
            while (itList.hasNext()){
                itList.next();

                if(type.equals(LIMIT.AFTER)) {
                    if (curInd > index) {
                        itList.remove();
                    }
                }else{
                    if (curInd < index) {
                        itList.remove();
                    }
                }

                ++curInd;
            }
        }

        findMaxTorquePoint();

        /*serTorqueUp.getData().clear();
        serTorqueDown.getData().clear();*/
        serTorqueAve.getData().clear();

        /*serPowerUp.getData().clear();
        serPowerDown.getData().clear();*/
        serPowerAve.getData().clear();


        /*setLine(serTorqueUp.getData(), dataTelma.get(T_FIELD.UP_AVE_SPEED), dataTelma.get(T_FIELD.UP_AVE_TORQUE), 9.8f);
        setLine(serTorqueDown.getData(), dataTelma.get(T_FIELD.DOWN_AVE_SPEED), dataTelma.get(T_FIELD.DOWN_AVE_TORQUE), 9.8f);*/
        setLine(serTorqueAve.getData(), dataTelma.get(T_FIELD.AVE_SPEED), dataTelma.get(T_FIELD.AVE_TORQUE), 9.8f);

        /*setLine(serPowerUp.getData(), dataTelma.get(DataResult.T_FIELD.UP_AVE_SPEED), dataTelma.get(T_FIELD.UP_POWER), 1.0f);
        setLine(serPowerDown.getData(), dataTelma.get(DataResult.T_FIELD.DOWN_AVE_SPEED), dataTelma.get(T_FIELD.DOWN_POWER), 1.0f);*/
        setLine(serPowerAve.getData(), dataTelma.get(DataResult.T_FIELD.AVE_SPEED), dataTelma.get(T_FIELD.AVE_POWER), 1.0f);


        tblStaticTelma.getItems().clear();
        table = getRows();
        tblStaticTelma.setItems(table);
    }

    private List<Float> getListFromFile(File file, String nameColumn){
        List<Float> records = new LinkedList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

            // first row
            String line = br.readLine();
            String[] row;

            //find number of column by name
            int numColumn = -1;
            if(line != null){
                row = line.split(Const.COMMA_DELIMITER);

                for(int i = 0; i < row.length; ++i){
                    if(nameColumn.equals(row[i])){
                        numColumn = i;
                    }
                }

            }

            if(numColumn == -1){
                br.close();
                return null;
            }

            while ((line = br.readLine()) != null) {
                row = line.split(Const.COMMA_DELIMITER);
                records.add(Float.parseFloat(row[numColumn]));
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

}
