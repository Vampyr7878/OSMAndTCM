package net.osmand.plus.tramplugin;

/**
 * Created by Wojciech on 2016-04-14.
 */
public class TramStop {
    private String name;
    private float lat1;
    private float lon1;
    private float lat2;
    private float lon2;

    public TramStop(String name, float lat1, float lon1, float lat2, float lon2) {
        this.name = name;
        this.lat1 = lat1;
        this.lon1 = lon1;
        this.lat2 = lat2;
        this.lon2 = lon2;
    }

    public String getName() {
        return name;
    }

    public float getLat1() {
        return lat1;
    }

    public float getLon1() {
        return lon1;
    }

    public float getLat2() {
        return lat2;
    }

    public float getLon2() {
        return lon2;
    }
}
