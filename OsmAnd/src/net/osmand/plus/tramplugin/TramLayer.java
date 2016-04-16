package net.osmand.plus.tramplugin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
    }

    @Override
    public void onDraw(Canvas canvas, RotatedTileBox tileBox, DrawSettings settings) {
        ArrayList<TramStop> stops = plugin.getStops();
        for(TramStop stop : stops) {
            DrawStop(canvas, tileBox, stop.getLon1(), stop.getLat1());
            DrawStop(canvas, tileBox, stop.getLon2(), stop.getLat2());
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
