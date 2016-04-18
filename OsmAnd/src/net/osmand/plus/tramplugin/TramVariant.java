package net.osmand.plus.tramplugin;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Wojciech on 2016-04-17.
 */
public class TramVariant {

    private ArrayList<String> names;
    private ArrayList<String> route;

    public TramVariant(String line, String direction, Context context) {
        String[] next;
        names = new ArrayList<>();
        route = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(context.getAssets().open("0" + line + "_warianty" + direction + ".csv")));
            while(true) {
                next = reader.readNext();
                if(next == null) {
                    break;
                }
                else {
                    names.add(next[3]);
                    route.add(next[4]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public ArrayList<String> getRoute() {
        return route;
    }
}
