package net.osmand.plus.tramplugin;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import net.osmand.Location;
import net.osmand.plus.ApplicationMode;
import net.osmand.plus.MapMarkersHelper.MapMarker;
import net.osmand.plus.OsmandApplication;
import net.osmand.plus.OsmandPlugin;
import net.osmand.plus.OsmandSettings;
import net.osmand.plus.OsmandSettings.CommonPreference;
import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.views.MapInfoLayer;
import net.osmand.plus.views.OsmandMapTileView;
import net.osmand.plus.views.mapwidgets.TextInfoWidget;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private ArrayList<TramStop> activeStops;
    private TextInfoWidget tramControl;
    private TramLayer tramLayer;
    private String line;
    private String direction;
    private TramVariant variant;
    private TramCourse course;
    private ArrayList<Float> distance;
    private Location myLocation;
    private List<MapMarker> destination;
    private int startStop;
    private int endStop;
    private String[] lines = {"2", "6", "11", "12"};
    private int currentTime;
    private int tripTime;
    private OsmandApplication app;

    public TramPlugin(OsmandApplication app) {
        this.app = app;
        direction = "";
        ApplicationMode.regWidget("tram.widget", ApplicationMode.DEFAULT);
        OsmandSettings set = app.getSettings();
        tramLat = set.registerFloatPreference(TRAM_LAT, 0f).makeGlobal();
        tramLon = set.registerFloatPreference(TRAM_LON, 0f).makeGlobal();
        xmlParser = app.getApplicationContext().getResources().getXml(R.xml.stops);
        stops = new ArrayList<>();
        activeStops = new ArrayList<>();
        distance = new ArrayList<>();
        startStop = -1;
        endStop = -1;
        String name;
        float lat1, lon1;
        float lat2, lon2;
        while (true)
        {
            name = getString("name");
            //Log.d(TramPlugin.class.getSimpleName(), name);
            if(name.equals("")) {
                break;
            }
            lat1 = getFloat("lat");
            lon1 = getFloat("lon");
            lat2 = getFloat("lat");
            lon2 = getFloat("lon");
            stops.add(new TramStop(name, lat1, lon1, lat2, lon2));
        }
    }

    private void getDistance(float lat, float lon) {
        distance.clear();
        double dist;
        for (int i = 0; i < stops.size(); i++) {
            dist = Math.sqrt((lat - stops.get(i).getLat1()) * (lat - stops.get(i).getLat1()) + (lon - stops.get(i).getLon1())*(lon - stops.get(i).getLon1()));
            distance.add((float)dist);
        }
    }

    private String getString(String tag) {
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

    private float getFloat(String tag) {
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

    private int min(ArrayList<Float> list) {
        int index = 0;
        for(int i = 1; i < list.size(); i++) {
            if(list.get(index) > list.get(i)) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public void updateLocation(Location location) {
        if(myLocation == null) {
            myLocation = location;
        } else {
            getDistance((float) myLocation.getLatitude(), (float) myLocation.getLongitude());
            startStop = min(distance);
        }
        if(destination == null) {
            destination = app.getMapMarkersHelper().getActiveMapMarkers();
        } else if(destination.size() == 1) {
            getDistance((float) destination.get(0).getLatitude(), (float) destination.get(0).getLongitude());
            endStop = min(distance);
        }
        else if(destination.size() > 1) {
            app.getMapMarkersHelper().removeMapMarker(destination.size() - 1);
        }
        if(startStop >= 0 && endStop >= 0) {
            Calendar c = Calendar.getInstance();
            currentTime = 60 * c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE);
            FindRoute();
            Log.d("TRAM", Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(c.get(Calendar.MINUTE)));
        }
    }

    private void FindRoute() {
        int start, end, time, hours, minutes;
        activeStops.clear();
        for(int i = 0; i < 4; i++) {
            for(int j = 1; j < 3; j++) {
                variant = new TramVariant(lines[i], Integer.toString(j), app.getApplicationContext());
                start = variant.getNames().indexOf(stops.get(startStop).getName());
                end = variant.getNames().indexOf(stops.get(endStop).getName());
                if(start >= 0 && end >= 0 && end > start)
                {
                    tripTime = 0;
                    for(int k = start; k <= end; k++)
                    {
                        activeStops.add(stops.get(FindStop(variant.getNames().get(k))));
                        tripTime += variant.getRoute().get(k);
                    }
                    course = new TramCourse(lines[i], Integer.toString(j), app.getApplicationContext());
                    time = FindTime();
                    hours = time / 60;
                    minutes = time - hours * 60;
                    direction = Integer.toString(j);
                    tramControl.setText("(" + lines[i] + ")" + Integer.toString(hours) + ":" +  Integer.toString(minutes), "");
                    return;
                }
            }
        }
    }

    private int FindTime() {
        ArrayList<Integer> times = course.getTimes();
        int current = currentTime - tripTime + 5;
        for(int i = 0; i < times.size(); i++) {
            if(times.get(i) > current) {
                return times.get(i) + tripTime;
            }
        }
        return -1;
    }

    @Override
     public void updateLayers(OsmandMapTileView mapView, MapActivity activity) {
        if(isActive()) {
            if(tramLayer == null) {
                registerLayers(activity);
            }
            if(!mapView.isLayerVisible(tramLayer)) {
                activity.getMapView().addLayer(tramLayer, 4.5f);
            }
            if(tramControl == null) {
                registerWidget(activity);
            }
        } else {
            MapInfoLayer mapInfoLayer = activity.getMapLayers().getMapInfoLayer();
            if(tramLayer != null) {
                activity.getMapView().removeLayer(tramLayer);
            }
            if (mapInfoLayer != null && tramControl != null ) {
                mapInfoLayer.removeSideWidget(tramControl);
                mapInfoLayer.recreateControls();
                tramControl = null;
            }
        }
    }

    @Override
    public void registerLayers(MapActivity activity) {
        // remove old if existing
        if(tramLayer != null) {
            activity.getMapView().removeLayer(tramLayer);
        }
        tramLayer = new TramLayer(this);
        activity.getMapView().addLayer(tramLayer, 4.5f);
        registerWidget(activity);
    }

    private void registerWidget(MapActivity activity) {
        MapInfoLayer mapInfoLayer = activity.getMapLayers().getMapInfoLayer();
        if (mapInfoLayer != null) {
            tramControl = createTramControl(activity);
            mapInfoLayer.registerSideWidget(tramControl, R.drawable.mm_tram_stop_small, R.string.osmand_tram_plugin_widget , "tram.widget", false, 21);
            mapInfoLayer.recreateControls();
            tramControl.setText("Tram", "");
        }
    }

    private TextInfoWidget createTramControl(final MapActivity activity) {
        final TextInfoWidget tramControl = new TextInfoWidget(activity);
//        tramControl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showLineDialog(activity);
//            }
//        });
        tramControl.setIcons(R.drawable.mx_railway_tram_stop, R.drawable.mm_railway_tram_stop);
        return tramControl;
    }

    private void showLineDialog(final MapActivity activity) {
        AlertDialog.Builder bld = new AlertDialog.Builder(activity);
        final ArrayList<String> list = new ArrayList<String>();
        list.add("2");
        list.add("6");
        list.add("11");
        list.add("12");
        String[] items = new String[list.size()];
        for(int i = 0; i < items.length; i++) {
            items[i] = list.get(i);
        }
        bld.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                line = list.get(which);
                showDirectionDialog(activity);
            }
        });
        bld.show();
    }

    private void showDirectionDialog(final MapActivity activity) {
        AlertDialog.Builder bld = new AlertDialog.Builder(activity);
        final ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        String[] items = new String[list.size()];
        for(int i = 0; i < items.length; i++) {
            items[i] = list.get(i);
        }
        bld.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                direction = list.get(which);
                tramControl.setText(line + "(" + direction + ")", "");
                variant = new TramVariant(line, direction, app.getApplicationContext());
                fillActiveStops();
            }
        });
        bld.show();
    }

    private void fillActiveStops() {
        activeStops.clear();
        ArrayList<String> names = variant.getNames();
        ArrayList<Integer> route = variant.getRoute();
        int index;
        for(int i = 1; i < names.size(); i++) {
            //Log.d(TramPlugin.class.getSimpleName(), route.get(i));
            if(!route.get(i).equals("")) {
                index = FindStop(names.get(i));
                if(index >= 0) {
                    activeStops.add(stops.get(index));
                }
            }
        }
    }

    private int FindStop(String name) {
        for(int i = 0; i < stops.size(); i++)
        {
            if(stops.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<TramStop> getStops() {
        return stops;
    }

    public ArrayList<TramStop> getActiveStops() {
        return activeStops;
    }

    public int getStartStop() {
        return startStop;
    }

    public int getEndStop() {
        return endStop;
    }

    public String getDirection() {
        return direction;
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
