package model;

public class RowTelma {
    private float speed;
    private float torque;
    private float power;

    public RowTelma(float speed, float torque, float power) {
        this.speed = speed;
        this.torque = torque;
        this.power = power;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getTorque() {
        return torque;
    }

    public void setTorque(float torque) {
        this.torque = torque;
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }
}
