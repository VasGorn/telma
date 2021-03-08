package model;

import java.util.LinkedList;
import java.util.List;

public final class Telma {
    public static final float TO_RPM = 0.6f;
    public static final float TO_TRQ = 10.0f;

    private static float gearRatio = 1.0f;

    private static float refSpeedRPM = 0.0f;
    private static float leftSpeedRPM = 0.0f;
    private static float rightSpeedRPM = 0.0f;
    private static float aveSpeedRPM = 0.0f;

    private static float engineSpeedRPM = 0.0f;

    private static float leftTorque = 0.0f;
    private static float rightTorque = 0.0f;
    private static float aveTorque = 0.0f;
    private static float engineTorque = 0.0f;

    private static float powerHP = 0.0f;

    private static float leftCurrent = 0.0f;
    private static float rightCurrent = 0.0f;

    // for triangle reference
    private static int startSpeedEngine = 0;
    private static int finalSpeedEngine = 0;
    private static float riseTime = 0.0f;


    public static void setSpeed(int refSpeedImp, int leftSpeedImp, int rightSpeedImp){
        refSpeedRPM = refSpeedImp * TO_RPM * gearRatio;
        leftSpeedRPM = leftSpeedImp * TO_RPM;
        rightSpeedRPM = rightSpeedImp * TO_RPM;
        aveSpeedRPM = (leftSpeedImp + rightSpeedImp) / 2.0f * TO_RPM;
    }

    public static void setTorque(int torqInLeft, int torqInRight){
        leftTorque = torqInLeft / TO_TRQ;
        rightTorque = torqInRight / TO_TRQ;
        aveTorque = (torqInLeft + torqInRight) / TO_TRQ;
    }

    public static void setCurrent(int currInLeft, int currInRight){
        leftCurrent = currInLeft / TO_TRQ;
        rightCurrent = currInRight / TO_TRQ;
    }

    public static void setGearRatio(float gearR){
        if(gearR <= 0) return;

        gearRatio = gearR;
    }

    public static float getGearRatio() {
        return gearRatio;
    }

    public static float getLeftSpeedRPM() {
        return leftSpeedRPM;
    }

    public static float getRightSpeedRPM() {
        return rightSpeedRPM;
    }

    public static float getEngineSpeedRPM() {
        return aveSpeedRPM * gearRatio;
    }

    public static float getLeftTorque() {
        return leftTorque;
    }

    public static float getRightTorque() {
        return rightTorque;
    }

    public static float getAveTorque() {
        return aveTorque;
    }

    public static float getEngineTorque() {
        return aveTorque / gearRatio;
    }

    public static float getRefSpeedRPM(){
        return refSpeedRPM;
    }

    public static float getAveSpeedRPM(){
        return aveSpeedRPM;
    }

    public static float getPowerHP() {
        return (aveTorque * 9.8f) * (aveSpeedRPM * 2.0f * 3.14159f / 60.0f) / 746.0f;
    }

    public static int getStartSpeedEngine() {
        return startSpeedEngine;
    }

    public static void setStartSpeedEngine(int startSpeedEngine) {
        if(startSpeedEngine > 60000){
            Telma.startSpeedEngine = 60000;
            return;
        }
        Telma.startSpeedEngine = startSpeedEngine;
    }

    public static int getFinalSpeedEngine() {
        return finalSpeedEngine;
    }

    public static void setFinalSpeedEngine(int finalSpeedEngine) {
        if(finalSpeedEngine > 60000){
            Telma.finalSpeedEngine = 60000;
            return;
        }
        Telma.finalSpeedEngine = finalSpeedEngine;
    }

    public static float getRiseTime() {
        return riseTime;
    }

    public static void setRiseTime(float riseTime) {
        Telma.riseTime = riseTime;
    }
}
