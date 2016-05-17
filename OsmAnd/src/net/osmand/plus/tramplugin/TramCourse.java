package net.osmand.plus.tramplugin;

import android.content.Context;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Wojciech on 2016-05-17.
 */
public class TramCourse {
    private ArrayList<Integer> times;

    public TramCourse(String line, String direction, Context context) {
        String[] next;
        String[] time;
        times = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(context.getAssets().open("0" + line + "_kursy" + direction + ".csv")));
            next = reader.readNext();
            while(true) {
                next = reader.readNext();
                if(next == null) {
                    break;
                }
                else {
                    time = next[0].split(":");
                    times.add(Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getTimes() {
        return times;
    }
}
