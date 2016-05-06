package net.osmand.plus.tramplugin;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import net.osmand.plus.ApplicationMode;
import net.osmand.plus.GPXUtilities;
import net.osmand.plus.OsmandApplication;
import net.osmand.plus.OsmandPlugin;
import net.osmand.plus.OsmandSettings;
import net.osmand.plus.OsmandSettings.CommonPreference;
import net.osmand.plus.R;
import net.osmand.plus.activities.MapActivity;
import net.osmand.plus.osmo.OsMoPlugin;
import net.osmand.plus.routepointsnavigation.RoutePointsPlugin;
import net.osmand.plus.routing.RoutingHelper;
import net.osmand.plus.views.MapInfoLayer;
import net.osmand.plus.views.OsmandMapTileView;
import net.osmand.plus.views.mapwidgets.TextInfoWidget;

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
    private ArrayList<TramStop> activeStops;
    private TextInfoWidget tramControl;
    private TramLayer tramLayer;
    private String line;
    private String direction;
    private TramVariant variant;
    private ArrayList distance;

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
        for (int i = 0; i < stops.size(); i++){
        dist = Math.sqrt((lat - stops.get(i).getLat1()) * (lat - stops.get(i).getLat1()) + (lon - stops.get(i).getLon1())*(lon - stops.get(i).getLon1()));
        distance.add(dist);
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
        tramControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLineDialog(activity);
            }
        });
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
        ArrayList<String> route = variant.getRoute();
        int index;
        for(int i = 1; i < names.size(); i++) {
            Log.d(TramPlugin.class.getSimpleName(), route.get(i));
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

    public ArrayList<TramStop> getActiveStops() {
        return activeStops;
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
