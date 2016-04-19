package net.osmand.plus.tramplugin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import net.osmand.data.LatLon;
import net.osmand.data.PointDescription;
import net.osmand.data.RotatedTileBox;
import net.osmand.plus.R;
import net.osmand.plus.views.ContextMenuLayer;
import net.osmand.plus.views.OsmandMapLayer;
import net.osmand.plus.views.OsmandMapTileView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wojciech on 2016-04-16.
 */
public class TramLayer extends OsmandMapLayer implements ContextMenuLayer.IContextMenuProvider {

    private TramPlugin plugin;
    private OsmandMapTileView view;
    private Paint bitmapPaint;
    private Paint linePaint;
    private Bitmap icon;

    public TramLayer(TramPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void collectObjectsFromPoint(PointF point, RotatedTileBox tileBox, List<Object> o) {

    }

    @Override
    public LatLon getObjectLocation(Object o) {
        return null;
    }

    @Override
    public String getObjectDescription(Object o) {
        return null;
    }

    @Override
    public PointDescription getObjectName(Object o) {
        return null;
    }

    @Override
    public boolean disableSingleTap() {
        return false;
    }

    @Override
    public boolean disableLongPressOnMap() {
        return false;
    }

    @Override
    public void initLayer(OsmandMapTileView view) {
        this.view = view;
        icon = BitmapFactory.decodeResource(view.getResources(), R.drawable.map_transport_stop_tram);
        bitmapPaint = new Paint();
        bitmapPaint.setDither(true);
        bitmapPaint.setAntiAlias(true);
        bitmapPaint.setFilterBitmap(true);
        linePaint = new Paint();
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(5);
    }

    @Override
    public void onDraw(Canvas canvas, RotatedTileBox tileBox, DrawSettings settings) {
        ArrayList<TramStop> stops = plugin.getActiveStops();
        for(int i = 1; i < stops.size(); i++) {
            switch (plugin.getDirection()) {
                case "1":
                    DrawLine(canvas, tileBox, stops.get(i-1).getLon1(), stops.get(i-1).getLat1(), stops.get(i).getLon1(), stops.get(i).getLat1());
                    break;
                case "2":
                    DrawLine(canvas, tileBox, stops.get(i-1).getLon2(), stops.get(i-1).getLat2(), stops.get(i).getLon2(), stops.get(i).getLat2());
                    break;
            }
        }
        for(TramStop stop : stops) {
            switch (plugin.getDirection()) {
                case "1":
                    DrawStop(canvas, tileBox, stop.getLon1(), stop.getLat1());
                    break;
                case "2":
                    DrawStop(canvas, tileBox, stop.getLon2(), stop.getLat2());
                    break;
            }
        }
    }

    private void DrawStop(Canvas canvas, RotatedTileBox tileBox, float lon, float lat) {
        if (isLocationVisible(tileBox, lat, lon)) {
            int marginX = icon.getWidth() / 2;
            int marginY = icon.getHeight() / 2;
            int locationX = tileBox.getPixXFromLonNoRot(lon);
            int locationY = tileBox.getPixYFromLatNoRot(lat);
            canvas.rotate(-view.getRotate(), locationX, locationY);
            canvas.drawBitmap(icon, locationX - marginX, locationY - marginY, bitmapPaint);
        }
    }

    private void DrawLine(Canvas canvas, RotatedTileBox tileBox, float lon1, float lat1, float lon2, float lat2) {
        if (isLocationVisible(tileBox, lat1, lon1) || isLocationVisible(tileBox, lat2, lon2)) {
            int location1X = tileBox.getPixXFromLonNoRot(lon1);
            int location1Y = tileBox.getPixYFromLatNoRot(lat1);
            int location2X = tileBox.getPixXFromLonNoRot(lon2);
            int location2Y = tileBox.getPixYFromLatNoRot(lat2);
            //canvas.rotate(-view.getRotate(), locationX, locationY);
            //canvas.drawBitmap(icon, locationX - marginX, locationY - marginY, bitmapPaint);
            canvas.drawLine(location1X, location1Y, location2X, location2Y, linePaint);
        }
    }

    private boolean isLocationVisible(RotatedTileBox tb, double latitude, double longitude){
        if(view == null) {
            return false;
        }
        return tb.containsLatLon(latitude, longitude);
    }

    @Override
    public void destroyLayer() {

    }

    @Override
    public boolean drawInScreenPixels() {
        return false;
    }
}
