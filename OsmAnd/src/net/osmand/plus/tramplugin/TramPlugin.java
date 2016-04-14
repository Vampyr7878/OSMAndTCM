package net.osmand.plus.tramplugin;

import android.app.Activity;
import android.util.Log;

import net.osmand.plus.OsmandApplication;
import net.osmand.plus.OsmandPlugin;
import net.osmand.plus.OsmandSettings;
import net.osmand.plus.OsmandSettings.CommonPreference;
import net.osmand.plus.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Wojciech on 2016-04-10.
 */
public class TramPlugin extends OsmandPlugin {

    public static final String ID = "osmand.tram";
    public static final String TRAM_LAT = "tram_lat";
    public static final String TRAM_LON = "tram_lon";

    private final CommonPreference<Float> tramLat;
    private final CommonPreference<Float> tramLon;
    private XmlPullParser xmlParser;
    private ArrayList<TramStop> stops;

    private OsmandApplication app;

    public TramPlugin(OsmandApplication app) {
        this.app = app;
        OsmandSettings set = app.getSettings();
        tramLat = set.registerFloatPreference(TRAM_LAT, 0f).makeGlobal();
        tramLon = set.registerFloatPreference(TRAM_LON, 0f).makeGlobal();
        xmlParser = app.getApplicationContext().getResources().getXml(R.xml.stops);
        stops = new ArrayList<>();
        String name;
        float lat1, lon1;
        float lat2, lon2;
        while (true)
        {
            name = GetString("name");
            //Log.d(TramPlugin.class.getSimpleName(), name);
            if(name.equals("")) {
                break;
            }
            lat1 = GetFloat("lat");
            lon1 = GetFloat("lon");
            lat2 = GetFloat("lat");
            lon2 = GetFloat("lon");
            stops.add(new TramStop(name, lat1, lon1, lat2, lon2));
        }
    }

    private String GetString(String tag) {
        String text ="";
        try {
            int event = xmlParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    if (xmlParser.getName().equals(tag)) {
                        xmlParser.next();
                        text = xmlParser.getText();
                        break;
                    }
                }
                event = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    private float GetFloat(String tag) {
        float number = 0f;
        try {
            int event = xmlParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    if (xmlParser.getName().equals(tag)) {
                        xmlParser.next();
                        number = Float.valueOf(xmlParser.getText());
                        break;
                    }
                }
                event = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return number;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDescription() {
        return app.getString(R.string.osmand_tram_plugin_description);
    }

    @Override
    public String getName() {
        return app.getString(R.string.osmand_tram_plugin_name);
    }

    @Override
    public int getAssetResourceName() {
        return R.drawable.parking_position;
    }

    @Override
    public Class<? extends Activity> getSettingsActivity() {
        return null;
    }
}
