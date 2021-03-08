package threads;

import controllers.MainController;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import model.Const;
import model.TableTelma;
import model.Telma;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread{
    byte[] bufIn = new byte[16];
    byte[] bufOut = new byte[16];
    private Socket socket;
    private MainController mainCtrl;

    RequestHandler( Socket socket,  MainController mainCtrl) {
        this.socket = socket;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void run() {

        if(mainCtrl.isActionMode()){
            boolean isRun = mainCtrl.isRun();
            boolean isStable = mainCtrl.isStable();
            int refSpeedStable = mainCtrl.getRefSpeedStable();

            setUpOutBuffer(isRun, isStable, refSpeedStable);
            mainCtrl.setActionMode(false);
        }

        try {

            InputStream input = socket.getInputStream();
            int is = input.read(bufIn);

            readData();

            OutputStream out = socket.getOutputStream();
            out.write(bufOut);
            out.flush();

            // Close our connection
            out.close();
            input.close();
            socket.close();

            clearData();

        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }

    private void readData(){
        int refSpeed = 0;
        int speedLeft = 0;
        int speedRight = 0;

        int torqLeft = 0;
        int torqRight = 0;

        int currLeft = 0;
        int currRight = 0;

        // speed
        refSpeed = ((bufIn[0] & 0xff) << 8) | (bufIn[1] & 0xff);
        speedLeft = ((bufIn[2] & 0xff) << 8) | (bufIn[3] & 0xff);
        speedRight = ((bufIn[4] & 0xff) << 8) | (bufIn[5] & 0xff);
        Telma.setSpeed(refSpeed, speedLeft, speedRight);

        // torque
        torqLeft = ((bufIn[6] & 0xff) << 8) | (bufIn[7] & 0xff);
        torqRight = ((bufIn[8] & 0xff) << 8) | (bufIn[9] & 0xff);
        Telma.setTorque(torqLeft, torqRight);

        // current
        currLeft = ((bufIn[10] & 0xff) << 8) | (bufIn[11] & 0xff);
        currRight = ((bufIn[12] & 0xff) << 8) | (bufIn[13] & 0xff);
        Telma.setCurrent(currLeft, currRight);


        TableTelma.addRefSpeed();
        TableTelma.addAveSpeed();

        TableTelma.addLeftSpeed();
        TableTelma.addRightSpeed();

        TableTelma.addAveTorque();
        TableTelma.addLeftTorque();
        TableTelma.addRightTorque();

        TableTelma.addPower();

        Platform.runLater(() -> //switches to GUI Thread
        {
            uiSpeedUpdate();
            uiTorqueUpdate();
            uiPowerUpdate();

            if(mainCtrl.isWriteData()) {
                mainCtrl.incValStep();
                mainCtrl.uiLineChartUpdate(Const.TYPE_LINE.SPEED);
                mainCtrl.uiLineChartUpdate(Const.TYPE_LINE.TORQUE);
                mainCtrl.uiLineChartUpdate(Const.TYPE_LINE.POWER);
                mainCtrl.uiLineChartUpdate(Const.TYPE_LINE.REF);
            }
        });

    }

    private void setUpOutBuffer(boolean isRun, boolean isStable, int refSpeedStable){

        // send to the client work MODE
        if(isRun){
            bufOut[0] = 'r';
        }else if(isStable){
            bufOut[0] = 's';
        }else{
            bufOut[0] = 'i';
        }

        // send to the client gear ratio
        int iRatio = (int)(Math.round(Telma.getGearRatio() * Const.TO_I_RATIO));
        bufOut[1] = (byte)  (iRatio >> 24);
        bufOut[2] = (byte) ((iRatio & 0x00FF0000) >> 16);
        bufOut[3] = (byte) ((iRatio & 0x0000FF00) >> 8);
        bufOut[4] = (byte)  (iRatio & 0x000000FF);

        // send to the client reference speed in impulses
        int iSpeed = (int) (Math.round(refSpeedStable / Telma.getGearRatio() / Telma.TO_RPM));
        if(iSpeed > 10000){
            iSpeed = 0;
        }

        bufOut[5] = (byte) ((iSpeed & 0x0000FF00) >> 8);
        bufOut[6] = (byte)  (iSpeed & 0x000000FF);

        // send start speed engine
        int startSpeed = Telma.getStartSpeedEngine();
        bufOut[7] = (byte) ((startSpeed & 0x0000FF00) >> 8);
        bufOut[8] = (byte)  (startSpeed & 0x000000FF);

        // send final speed engine
        int finalSpeed = Telma.getFinalSpeedEngine();
        bufOut[9] = (byte) ((finalSpeed & 0x0000FF00) >> 8);
        bufOut[10] = (byte)  (finalSpeed & 0x000000FF);

        // send rise time in /100 seconds
        int riseTimeI = (int) Math.round(Telma.getRiseTime() * Const.TO_TIME_xK);
        bufOut[11] = (byte) ((riseTimeI & 0x0000FF00) >> 8);
        bufOut[12] = (byte)  (riseTimeI & 0x000000FF);


    }

    private void uiSpeedUpdate(){
        Platform.runLater(() -> //switches to GUI Thread
        {
            mainCtrl.getGgLeftSpeed().setValue(Telma.getLeftSpeedRPM());
            mainCtrl.getGgRightSpeed().setValue(Telma.getRightSpeedRPM());
            mainCtrl.getGgEngineSpeed().setValue(Telma.getEngineSpeedRPM());
        });
    }

    private void uiTorqueUpdate(){
        Platform.runLater(() -> //switches to GUI Thread
        {
            mainCtrl.getGgLeftTorque().setValue(Telma.getLeftTorque());
            mainCtrl.getGgRightTorque().setValue(Telma.getRightTorque());
            mainCtrl.getGgEngineTorque().setValue(Telma.getEngineTorque());
        });
    }

    private void uiPowerUpdate(){
        mainCtrl.getGgEnginePower().setValue(Telma.getPowerHP());
    }

    private void clearData(){
        for(int i = 0; i < bufIn.length; ++i){
            bufIn[i] = 0;
        }

        for(int i = 0; i < bufOut.length; ++i){
            bufOut[i] = 0;
        }
    }
}
