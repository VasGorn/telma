package model;

import java.util.LinkedList;
import java.util.List;

public class TriangleData {
    private List<Float> upSpeedLeft;
    private List<Float> upSpeedRight;
    private List<Float> upSpeedAve;

    private List<Float> downSpeedLeft;
    private List<Float> downSpeedRight;
    private List<Float> downSpeedAve;

    private List<Float> upTorqueLeft;
    private List<Float> upTorqueRight;
    private List<Float> upTorqueAve;

    private List<Float> downTorqueLeft;
    private List<Float> downTorqueRight;
    private List<Float> downTorqueAve;

    private List<Float> upRefSpeed;
    private List<Float> downRefSpeed;

    private List<Float> upPower;
    private List<Float> downPower;

    private List<Float> aveSpeed;
    private List<Float> aveTorque;
    private List<Float> avePower;

    public TriangleData(List<Float> allLeftSpeed, List<Float> allRightSpeed, List<Float> allAveSpeed,
                        List<Float> allLeftTorque, List<Float> allRightTorque, List<Float> allAveTorque,
                        List<Float> allPower, List<Float> allRefSpeed){
        int listSize = allRefSpeed.size();

        if(listSize == 0){
            return;
        }

        //find max value and it index
        float maxRefVal = 0;
        int maxRefInd = 0;

        for(int i = 0; i < listSize; ++i){
            float val = allRefSpeed.get(i);
            if(val > maxRefVal){
                maxRefVal = val;
                maxRefInd = i;
            }
        }

        upSpeedLeft = getUpList(allLeftSpeed, maxRefInd, listSize);
        upSpeedRight = getUpList(allRightSpeed, maxRefInd, listSize);
        upSpeedAve = getUpList(allAveSpeed, maxRefInd, listSize);

        downSpeedLeft = getDownList(allLeftSpeed, maxRefInd, listSize);
        downSpeedRight = getDownList(allRightSpeed, maxRefInd, listSize);
        downSpeedAve = getDownList(allAveSpeed, maxRefInd, listSize);

        upTorqueLeft = getUpList(allLeftTorque, maxRefInd, listSize);
        upTorqueRight = getUpList(allRightTorque, maxRefInd, listSize);
        upTorqueAve = getUpList(allAveTorque, maxRefInd, listSize);

        downTorqueLeft = getDownList(allLeftTorque, maxRefInd, listSize);
        downTorqueRight = getDownList(allRightTorque, maxRefInd, listSize);
        downTorqueAve = getDownList(allAveTorque, maxRefInd, listSize);

        upPower = getUpList(allPower, maxRefInd, listSize);
        downPower = getDownList(allPower, maxRefInd, listSize);

        upRefSpeed = getUpList(allRefSpeed, maxRefInd, listSize);
        downRefSpeed = getDownList(allRefSpeed, maxRefInd, listSize);

        int size = upSpeedAve.size();
        aveSpeed = new LinkedList<>();
        aveTorque = new LinkedList<>();
        avePower = new LinkedList<>();
        for(int i = 0; i < size; ++i){
            aveSpeed.add((upSpeedAve.get(i) + downSpeedAve.get(i)) / 2.0f);
            aveTorque.add((upTorqueAve.get(i) + downTorqueAve.get(i)) / 2.0f);
            avePower.add((upPower.get(i) + downPower.get(i)) / 2.0f);
        }

    }

    //=======================================================================

    private List<Float> getUpList(List<Float> allList, int mavValIndex, int listSize){
        List<Float> upList = new LinkedList<Float>();


        int startIndex = (2 * mavValIndex - listSize);
        if(startIndex < 0){
            startIndex = 0;
        }

        for(int i = startIndex + 1; i <= mavValIndex; ++i){
            upList.add(allList.get(i));
        }

        return upList;
    }

    private List<Float> getDownList(List<Float> allList, int mavValIndex, int listSize){
        List<Float> upList = new LinkedList<Float>();


        int startIndex = 2 * mavValIndex;
        if(startIndex > listSize){
            startIndex = listSize - 1;
        }

        for(int i = startIndex; i >= mavValIndex; --i){
            upList.add(allList.get(i));
        }

        return upList;
    }

    //=======================================================================

    public List<Float> getUpSpeedLeft() {
        return upSpeedLeft;
    }

    public void setUpSpeedLeft(List<Float> upSpeedLeft) {
        this.upSpeedLeft = upSpeedLeft;
    }

    public List<Float> getUpSpeedRight() {
        return upSpeedRight;
    }

    public void setUpSpeedRight(List<Float> upSpeedRight) {
        this.upSpeedRight = upSpeedRight;
    }

    public List<Float> getUpSpeedAve() {
        return upSpeedAve;
    }

    public void setUpSpeedAve(List<Float> upSpeedAve) {
        this.upSpeedAve = upSpeedAve;
    }

    public List<Float> getDownSpeedLeft() {
        return downSpeedLeft;
    }

    public void setDownSpeedLeft(List<Float> downSpeedLeft) {
        this.downSpeedLeft = downSpeedLeft;
    }

    public List<Float> getDownSpeedRight() {
        return downSpeedRight;
    }

    public void setDownSpeedRight(List<Float> downSpeedRight) {
        this.downSpeedRight = downSpeedRight;
    }

    public List<Float> getDownSpeedAve() {
        return downSpeedAve;
    }

    public void setDownSpeedAve(List<Float> downSpeedAve) {
        this.downSpeedAve = downSpeedAve;
    }

    public List<Float> getUpTorqueLeft() {
        return upTorqueLeft;
    }

    public void setUpTorqueLeft(List<Float> upTorqueLeft) {
        this.upTorqueLeft = upTorqueLeft;
    }

    public List<Float> getUpTorqueRight() {
        return upTorqueRight;
    }

    public void setUpTorqueRight(List<Float> upTorqueRight) {
        this.upTorqueRight = upTorqueRight;
    }

    public List<Float> getUpTorqueAve() {
        return upTorqueAve;
    }

    public void setUpTorqueAve(List<Float> upTorqueAve) {
        this.upTorqueAve = upTorqueAve;
    }

    public List<Float> getDownTorqueLeft() {
        return downTorqueLeft;
    }

    public void setDownTorqueLeft(List<Float> downTorqueLeft) {
        this.downTorqueLeft = downTorqueLeft;
    }

    public List<Float> getDownTorqueRight() {
        return downTorqueRight;
    }

    public void setDownTorqueRight(List<Float> downTorqueRight) {
        this.downTorqueRight = downTorqueRight;
    }

    public List<Float> getDownTorqueAve() {
        return downTorqueAve;
    }

    public void setDownTorqueAve(List<Float> downTorqueAve) {
        this.downTorqueAve = downTorqueAve;
    }

    public List<Float> getUpPower() {
        return upPower;
    }

    public void setUpPower(List<Float> upPower) {
        this.upPower = upPower;
    }

    public List<Float> getDownPower() {
        return downPower;
    }

    public void setDownPower(List<Float> downPower) {
        this.downPower = downPower;
    }

    public List<Float> getAveSpeed() {
        return aveSpeed;
    }

    public void setAveSpeed(List<Float> aveSpeed) {
        this.aveSpeed = aveSpeed;
    }

    public List<Float> getAveTorque() {
        return aveTorque;
    }

    public void setAveTorque(List<Float> aveTorque) {
        this.aveTorque = aveTorque;
    }

    public List<Float> getAvePower() {
        return avePower;
    }

    public void setAvePower(List<Float> avePower) {
        this.avePower = avePower;
    }
    public List<Float> getUpRefSpeed() {
        return upRefSpeed;
    }

    public void setUpRefSpeed(List<Float> upRefSpeed) {
        this.upRefSpeed = upRefSpeed;
    }

    public List<Float> getDownRefSpeed() {
        return downRefSpeed;
    }

    public void setDownRefSpeed(List<Float> downRefSpeed) {
        this.downRefSpeed = downRefSpeed;
    }


}
