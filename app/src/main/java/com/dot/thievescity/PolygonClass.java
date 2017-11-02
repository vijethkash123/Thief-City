package com.dot.thievescity;

import android.nfc.Tag;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pramod joshi on 28-10-2017.
 */

public class PolygonClass{
    GoogleMap googleMap;
    //private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);

    public List<Polygon> permittedPolygons = new ArrayList<>();
    public List<Polygon> restrictedPolygons = new ArrayList<>();


    public PolygonClass(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
    }

    // More code goes here, including the onCreate() method described above.
         public void drawPolygons()
         {

            // Add polylines and polygons to the map. This section shows just
            // a single polyline. Read the rest of the tutorial to learn more.
             /*
            Polygon polygonMainarea = googleMap.addPolygon(new PolygonOptions()
                    .clickable(false)
                    .add(
                            new LatLng(13.022966,76.101004),
                            new LatLng(13.022716,76.10186),
                            new LatLng(13.02315,76.101634),
                            new LatLng(13.023043,76.102041),
                            new LatLng(13.022921,76.102262),
                            new LatLng(13.022283,76.1040842),
                            new LatLng(13.022415,76.104147),
                            new LatLng(13.02247,76.103598),
                            new LatLng(13.024543,76.10428),
                            new LatLng(13.025275,76.101856))
            );
             //polygonMainarea.setFillColor(COLOR_GREEN_ARGB);
             polygonMainarea.setTag("alpha");
            permittedPolygons.add(polygonMainarea);

            // Set listeners for click events
            //googleMap.setOnPolygonClickListener();

            Polygon polygonCs = googleMap.addPolygon (new PolygonOptions()
                    .clickable(false)
                    .add(
                            new LatLng(13.023302,76.102509),
                            new LatLng(13.022989,76.10200),
                            new LatLng(13.02259,76.102577),
                            new LatLng(13.022722,76.103576)));
             polygonCs.setTag("beta");
             restrictedPolygons.add(polygonCs);

             Polygon polygonControlRoom = googleMap.addPolygon (new PolygonOptions()
                     .clickable(false)
                     .add(
                             new LatLng(13.022864,76.103521),
                             new LatLng(13.0232,76.103625),
                             new LatLng(13.023307,76.10329),
                             new LatLng(13.022981,76.103167)));
             polygonControlRoom.setTag("alpha");
             restrictedPolygons.add(polygonControlRoom);

             Polygon polygonMainBlock = googleMap.addPolygon (new PolygonOptions()
                     .clickable(false)
                     .add(
                             new LatLng(13.023155,76.102638),
                             new LatLng(13.023261,76.102671),
                             new LatLng(13.023431,76.102189),
                             new LatLng(13.023945,76.102371),
                             new LatLng(13.023774,76.10286),
                             new LatLng(13.023892,76.102893),
                             new LatLng(13.024096,76.102298),
                             new LatLng(13.023363,76.102043),
                             new LatLng(13.0236042,76.102412),
                             new LatLng(13.02349,76.102344),
                             new LatLng(13.023456,76.102718),
                             new LatLng(13.023155,76.102638)));
             polygonMainBlock.setTag("beta");
             restrictedPolygons.add(polygonMainBlock);


             Polygon polygonSaCvAudi = googleMap.addPolygon (new PolygonOptions()
                     .clickable(false)
                     .add(
                             new LatLng(13.02403,76.10313),
                             new LatLng(13.02736,76.103903),
                             new LatLng(13.024032,76.103993),
                             new LatLng(13.0242331,76.103749),
                             new LatLng(13.024532,76.103848),
                             new LatLng(13.023982,76.103311)));
             polygonSaCvAudi.setTag("alpha");
             restrictedPolygons.add(polygonSaCvAudi);

             Polygon polygonGround2 = googleMap.addPolygon (new PolygonOptions()
                     .clickable(false)
                     .add(
                             new LatLng(13.022985,76.104902),
                             new LatLng(13.024096,76.105356),
                             new LatLng(13.024329,76.104322),
                             new LatLng(13.023226,76.103851)));
             polygonControlRoom.setTag("beta");
             permittedPolygons.add(polygonGround2);

             //Polygon polygonCvPg = googleMap.addPolygon (new PolygonOptions()
                  //   .clickable(false)
                //     .add(
                            // new LatLng(13.022864,76.103521),
                            // new LatLng(13.0232,76.103625),
                            // new LatLng(13.023307,76.10329),
                            // new LatLng(13.022981,76.103167)));
             //polygonCvPg.setTag("g");
             */

             Polygon perm = googleMap.addPolygon(new PolygonOptions()
             .clickable(false)
             .add(
                     new LatLng(13.022459,76.096867),
                     new LatLng(13.022386,76.097304),
                     new LatLng(13.021146,76.097441)
                     ));
             permittedPolygons.add(perm);

             Polygon rest = googleMap.addPolygon(new PolygonOptions()
                     .clickable(false)
                     .add(
                             new LatLng(13.021834,76.097218),
                             new LatLng(13.021875,76.097243),
                             new LatLng(13.021898,76.096966),
                             new LatLng(13.021845,76.096970)
                     ));
             restrictedPolygons.add(rest);
         }

            // if (Tag=="b,c,d,e,g")
              //   Toast.makeText(this,"Can't place here",Toast.LENGTH_SHORT).show();


    private void stylePolygon3(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        int strokeColor = COLOR_BLACK_ARGB;
        switch (type) {
            case "b":
            case "c":
            case "d":
            case "e":
            case "g":
            default:
                strokeColor = COLOR_BLACK_ARGB;
                break;
        }
    }

        private void stylePolygon(Polygon polygon) {
            String type = "";
            // Get the data object stored with the polygon.
            if (polygon.getTag() != null) {
                type = polygon.getTag().toString();
            }

            List<PatternItem> pattern = null;
            int strokeColor = COLOR_BLACK_ARGB;
            int fillColor = COLOR_WHITE_ARGB;

            switch (type) {
                // If no type is given, allow the API to use the default.
                case "alpha":
                    // Apply a stroke pattern to render a dashed line, and define colors.
                    pattern = PATTERN_POLYGON_ALPHA;
                    strokeColor = COLOR_GREEN_ARGB;
                    fillColor = COLOR_PURPLE_ARGB;
                    break;
                case "beta":
                    // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                    pattern = PATTERN_POLYGON_BETA;
                    strokeColor = COLOR_ORANGE_ARGB;
                    fillColor = COLOR_BLUE_ARGB;
                    break;
            }

            polygon.setStrokePattern(pattern);
            polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
            polygon.setStrokeColor(strokeColor);
            polygon.setFillColor(fillColor);
        }













    }



