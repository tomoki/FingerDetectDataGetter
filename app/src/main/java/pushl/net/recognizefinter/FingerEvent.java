package pushl.net.recognizefinter;

public class FingerEvent {
    String finger;
    int type;
    float pressure;
    float size;
    float x;
    float y;
    float orientation;
    float major;
    float minor;

    public FingerEvent(String finger, int type, float pressure, float size,
                       float x, float y, float orientation, float major, float minor) {
        this.finger = finger;
        this.type = type;
        this.pressure = pressure;
        this.size = size;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.major = major;
        this.minor = minor;
    }

    @Override
    public String toString() {
        return "FingerEvent{" +
                "finger='" + finger + '\'' +
                ", type=" + type +
                ", pressure=" + pressure +
                ", size=" + size +
                ", x=" + x +
                ", y=" + y +
                ", orientation=" + orientation +
                ", major=" + major +
                ", minor=" + minor +
                '}';
    }
    public String toCSVColumn() {
        return  finger      + "," + type  + "," + pressure + "," +
                size        + "," + x     + "," + y        + "," +
                orientation + "," + major + "," + minor;
    }
    public static String genCSVHeader(){
        return  "Finger"      + "," + "Type"  + "," + "Pressure" + "," +
                "Size"        + "," + "X"     + "," + "Y"        + "," +
                "Orientation" + "," + "Major" + "," + "Minor";
    }
}
