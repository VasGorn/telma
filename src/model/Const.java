package model;

public final class Const {
    public static final int PORT_SRC = 52041;
    public static final long SLEEP_TIME = 20;
    public static final long LIST_SIZE = 5000;
    public static final int TO_I_RATIO = 10000000;
    public static final int TO_TIME_xK = 100;
    public static final String COMMA_DELIMITER = ",";

    public static enum TYPE_LINE {
        SPEED,
        REF,
        TORQUE,
        POWER
    }

}
