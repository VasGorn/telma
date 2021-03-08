package model;

import java.util.LinkedList;
import java.util.List;


public class TableTelma {
    private static List<Float> listAveSpeed = new LinkedList<Float>();
    private static List<Float> listLeftSpeed = new LinkedList<>();
    private static List<Float> listRightSpeed = new LinkedList<>();

    private static List<Float> listAveTorque = new LinkedList<Float>();
    private static List<Float> listLeftTorque = new LinkedList<>();
    private static List<Float> listRightTorque = new LinkedList<>();

    private static List<Float> listPower = new LinkedList<Float>();
    private static List<Float> listRefSpeed = new LinkedList<Float>();

    public static void setListLeftSpeed(List<Float> listLeftSpeed) {
        TableTelma.listLeftSpeed = listLeftSpeed;
    }

    public static void setListRightSpeed(List<Float> listRightSpeed) {
        TableTelma.listRightSpeed = listRightSpeed;
    }

    public static void setListLeftTorque(List<Float> listLeftTorque) {
        TableTelma.listLeftTorque = listLeftTorque;
    }

    public static void setListRightTorque(List<Float> listRightTorque) {
        TableTelma.listRightTorque = listRightTorque;
    }

    public static void setListRefSpeed(List<Float> listRefSpeed) {
        TableTelma.listRefSpeed = listRefSpeed;
    }

    //==============================================================
    public static void addAveSpeed(){
        if(listAveSpeed == null){
            listAveSpeed = new LinkedList<Float>();
        }

        listAveSpeed.add(Telma.getEngineSpeedRPM());
    }

    public static void clearAveSpeed(){
        if(listAveSpeed != null){
            listAveSpeed.clear();
        }
    }

    public static List<Float> getAveSpeedList(){
        return listAveSpeed;
    }

    //==============================================================

    public static List<Float> getLeftSpeedList(){
        return listLeftSpeed;
    }

    public static void addLeftSpeed(){
        if(listLeftSpeed == null){
            listLeftSpeed = new LinkedList<Float>();
        }

        listLeftSpeed.add(Telma.getLeftSpeedRPM());
    }

    public static void clearLeftSpeed(){
        if(listLeftSpeed != null){
            listLeftSpeed.clear();
        }
    }

    //==============================================================

    public static List<Float> getRightSpeedList(){
        return listRightSpeed;
    }

    public static void addRightSpeed(){
        if(listRightSpeed == null){
            listRightSpeed = new LinkedList<Float>();
        }

        listRightSpeed.add(Telma.getRightSpeedRPM());
    }

    public static void clearRightSpeed(){
        if(listRightSpeed != null){
            listRightSpeed.clear();
        }
    }

    //==============================================================

    public static void addAveTorque(){
        if(listAveTorque == null){
            listAveTorque = new LinkedList<Float>();
        }
        listAveTorque.add(Telma.getEngineTorque());
    }

    public static void clearAveTorque(){
        if(listAveTorque != null){
            listAveTorque.clear();
        }
    }

    public static List<Float> getAveTorqueList(){
        return listAveTorque;
    }

    //==============================================================

    public static void addLeftTorque(){
        if(listLeftTorque == null){
            listLeftTorque = new LinkedList<Float>();
        }
        listLeftTorque.add(Telma.getLeftTorque());
    }

    public static void clearLeftTorque(){
        if(listLeftTorque != null){
            listLeftTorque.clear();
        }
    }

    public static List<Float> getLeftTorqueList(){
        return listLeftTorque;
    }

    //==============================================================

    public static void addRightTorque(){
        if(listRightTorque == null){
            listRightTorque = new LinkedList<Float>();
        }
        listRightTorque.add(Telma.getRightTorque());
    }

    public static void clearRightTorque(){
        if(listRightTorque != null){
            listRightTorque.clear();
        }
    }

    public static List<Float> getRightTorqueList(){
        return listRightTorque;
    }

    //==============================================================

    public static void setListAveSpeed(List<Float> list) {
        listAveSpeed = list;
    }

    public static void setListAveTorque(List<Float> list) {
        listAveTorque = list;
    }

    public static void setListPower(List<Float> list) {
        listPower = list;
    }

    //==============================================================

    public static void addPower(){
        if(listPower == null){
            listPower = new LinkedList<Float>();
        }
        listPower.add(Telma.getPowerHP());
    }

    public static void clearPower(){
        if(listPower != null){
            listPower.clear();
        }
    }

    public static List<Float> getPowerList(){
        return listPower;
    }

    //==============================================================

    public static void addRefSpeed(){
        if(listRefSpeed == null){
            listRefSpeed = new LinkedList<Float>();
        }

        listRefSpeed.add(Telma.getRefSpeedRPM());
    }

    public static void clearRefSpeed(){
        if(listRefSpeed != null){
            listRefSpeed.clear();
        }
    }

    public static List<Float> getRefSpeedList(){
        return listRefSpeed;
    }
}
