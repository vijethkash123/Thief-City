package com.dot.thievescity;

import android.graphics.Color;
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
    int resColor = Color.argb(80, 193, 66, 66);
    int permColor = Color.argb(80, 91, 63, 191);


    public PolygonClass(GoogleMap googleMap)
    {
        this.googleMap = googleMap;
    }

    // More code goes here, including the onCreate() method described above.
         public void drawPolygons() {

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
                             new LatLng(13.023114,76.102626),
                             new LatLng(13.023272,76.102684),
                             new LatLng(13.023436,76.102218),
                             new LatLng(13.023590,76.102328),
                             new LatLng(13.023716,76.102368),
                             new LatLng(13.023735,76.102315),
                             new LatLng(13.023916,76.102379),
                             new LatLng(13.023753,76.102862),
                             new LatLng(13.023878,76.102900),
                             new LatLng(13.024092,76.102297),
                             new LatLng(13.023767,76.102162),
                             new LatLng(13.023774,76.102110),
                             new LatLng(13.023700,76.102082),
                             new LatLng(13.023666,76.102140),
                             new LatLng(13.023341,76.102032),
                             new LatLng(13.023264,76.102038),
                             new LatLng(13.023229,76.102110),
                             new LatLng(13.023095,76.102490),
                             new LatLng(13.023120,76.102539),
                             new LatLng(13.023113.102573)));
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

             Polygon boundary = googleMap.addPolygon(new PolygonOptions()
             .clickable(false)
             .add(
                     new LatLng(13.022855,76.10097333),
                     new LatLng(13.02264,76.10181333),
                     new LatLng(13.0229,76.10185499),
                     new LatLng(13.02281,76.10211166),
                     new LatLng(13.02273333,76.10210833),
                     new LatLng(13.022725,76.102145000),
                     new LatLng(13.022878333,76.10224166),
                     new LatLng(13.02222499,76.10403833),
                     new LatLng(13.02226999,76.104065),
                     new LatLng(13.022461666,76.10354833),
                     new LatLng(13.02456666,76.10430333),
                     new LatLng(13.02503666,76.10188166)
                     ));
             permittedPolygons.add(boundary);
             boundary.setFillColor(permColor);

             Polygon mainBlock = googleMap.addPolygon(new PolygonOptions()
                     .clickable(false)
                     .add(
                             new LatLng(13.023114,76.102626),
                             new LatLng(13.023272,76.102684),
                             new LatLng(13.023436,76.102218),
                             new LatLng(13.023590,76.102328),
                             new LatLng(13.023716,76.102368),
                             new LatLng(13.023735,76.102315),
                             new LatLng(13.023916,76.102379),
                             new LatLng(13.023753,76.102862),
                             new LatLng(13.023878,76.102900),
                             new LatLng(13.024092,76.102297),
                             new LatLng(13.023767,76.102162),
                             new LatLng(13.023774,76.102110),
                             new LatLng(13.023700,76.102082),
                             new LatLng(13.023666,76.102140),
                             new LatLng(13.023341,76.102032),
                             new LatLng(13.023264,76.102038),
                             new LatLng(13.023229,76.102110),
                             new LatLng(13.023095,76.102490),
                             new LatLng(13.023120,76.102539),
                             new LatLng(13.023113,76.102573)));
             restrictedPolygons.add(mainBlock);
             mainBlock.setFillColor(resColor);

             Polygon csDept = googleMap.addPolygon(new PolygonOptions()
                     .clickable(false)
                     .add(
                             new LatLng(13.023034, 76.102486),
                             new LatLng(13.022697, 76.103511),
                             new LatLng(13.022576, 76.103470),
                             new LatLng(13.022924, 76.102433)
                     ));
             restrictedPolygons.add(csDept);
             csDept.setFillColor(resColor);

             Polygon SABlock = googleMap.addPolygon(new PolygonOptions()
                     .clickable(false)
                     .add(
                             new LatLng(13.02405833, 76.10312),
                             new LatLng(13.02428, 76.103165),
                             new LatLng(13.02439833, 76.1031766),
                             new LatLng(13.02470333, 76.1032733),
                             new LatLng(13.02453666, 76.10367),
                             new LatLng(13.02414333, 76.10371833),
                             new LatLng(13.02403666, 76.10400166),
                             new LatLng(13.02377666, 76.10393333),
                             new LatLng(13.02376833, 76.10383)
                     ));
             restrictedPolygons.add(SABlock);
             SABlock.setFillColor(resColor);

             Polygon ground = googleMap.addPolygon(new PolygonOptions()
                     .clickable(false)
                     .add(
                             new LatLng(13.02330333,76.10390166),
                             new LatLng(13.02298333,76.10474166),
                             new LatLng(13.02308999,76.10490499),
                             new LatLng(13.02397,76.10529333),
                             new LatLng(13.02438333,76.10421333)
                     ));
             permittedPolygons.add(ground);
             ground.setFillColor(permColor);
             
             
             Polygon networkroom =googleMap.addPolygon(new PolygonOptions()
                     .clickable(false)
                     .add(
                             new LatLng(13.023256,76.103338),
                             new LatLng(13.022937,76.103233),
                             new LatLng(13.022894,76.103470),
                             new LatLng(13.023173,76.103566)
                     ));
             restrictedPolygons.add(networkroom);
             networkroom.setFillColor(resColor);

             Polygon civilPG = googleMap.addPolygon(new PolygonOptions()
                     .clickable(false)
                     .add(
                             new LatLng(13.023567,76.103410),
                             new LatLng(13.023656,76.103585),
                             new LatLng(13.023458,76.103689),
                             new LatLng(13.023359,76.103531)             
                     ));
             restrictedPolygons.add(civilPG);
             civilPG.setFillColor(resColor);
             civilPG.setStrokeColor(resColor);

             for(Polygon polygon : permittedPolygons)
             {
                 polygon.setStrokeColor(permColor);
             }

             for (Polygon polygon: restrictedPolygons) {
                 polygon.setStrokeColor(resColor);
             }
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



