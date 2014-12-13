/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package zz23_jj26.server.earth;

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.AirspaceLayer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.pick.PickedObjectList;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.render.airspaces.*;
import gov.nasa.worldwind.render.airspaces.Box;
import gov.nasa.worldwind.render.airspaces.Polygon;
import gov.nasa.worldwind.util.*;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwindx.examples.Airspaces.AirspacesPanel;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwindx.examples.FlatWorldPanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Illustrates how to configure and display World Wind <code>{@link Airspace}</code> shapes. Airspace shapes are
 * extruded 3D volumes defined by geographic coordinates and upper- and lower- altitude boundaries. The interior of
 * airspace shapes always conforms to the curvature of the globe, and optionally also conform to the underlying
 * terrain.
 * <p/>
 * This shows how to use all 11 types of standard airspace shapes: <ul> <li><code>{@link Orbit}</code> - a rectangle
 * with rounded end caps.</li> <li><code>{@link Curtain}</code> - a vertically extruded wall.</li> <li><code>{@link
 * Polygon}</code> - a vertically extruded polygon.</li> <li><code>{@link PolyArc}</code> - a vertically extruded
 * polygon with the first and last vertices connected by an arc.</li> <li><code>{@link Cake}</code> - one or more
 * vertically stacked cylinders.</li> <li><code>{@link CappedCylinder}</code> - a vertically extruded cylinder.</li>
 * <li><code>{@link PartialCappedCylinder}</code> - a vertically extruded cylinder with part of its interior
 * removed.</li> <li><code>{@link SphereAirspace}</code> - a sphere defined by a geographic location.</li>
 * <li><code>{@link TrackAirspace}</code> - one or more vertically extruded rectangles defined by pairs of geographic
 * locations.</li> <li><code>{@link Route}</code> - a sequence of connected and vertically extruded rectangles defined
 * by a list of geographic locations.</li> <li><code>{@link Box}</code> - a rectangle defined by a pair of geographic
 * locations.</li> </ul>
 *
 * @author dcollins
 * @version $Id: Airspaces.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class Airspaces extends ApplicationTemplate{
	
    public static final String ACTION_COMMAND_ANTIALIAS = "gov.nasa.worldwind.avkey.ActionCommandAntialias";
    public static final String ACTION_COMMAND_DEPTH_OFFSET = "gov.nasa.worldwind.avkey.ActionCommandDepthOffset";
    public static final String ACTION_COMMAND_DRAW_EXTENT = "gov.nasa.worldwind.avkey.ActionCommandDrawExtent";
    public static final String ACTION_COMMAND_DRAW_WIREFRAME = "gov.nasa.worldwind.avkey.ActionCommandDrawWireframe";
    public static final String ACTION_COMMAND_LOAD_DATELINE_CROSSING_AIRSPACES
        = "ActionCommandLoadDatelineCrossingAirspaces";
    public static final String ACTION_COMMAND_LOAD_DEMO_AIRSPACES = "ActionCommandLoadDemoAirspaces";
    public static final String ACTION_COMMAND_LOAD_INTERSECTING_AIRSPACES = "ActionCommandLoadIntersectingAirspaces";
    public static final String ACTION_COMMAND_ZOOM_TO_DEMO_AIRSPACES = "ActionCommandZoomToDemoAirspaces";
    public static final String ACTION_COMMAND_SAVE_AIRSPACES = "ActionCommandSaveAirspaces";
    public static final String ACTION_COMMAND_READ_AIRSPACES = "ActionCommandReadAirspaces";

    public static class AppFrame extends ApplicationTemplate.AppFrame{
    	
		private static final long serialVersionUID = -7456230205583333227L;
		protected AirspacesController controller;
        protected AirspacesPanel airspacesPanel;
        protected FlatWorldPanel flatWorldPanel;
        protected JPanel statusPanel;

        public AppFrame(){
            this.controller = new AirspacesController(this);
            this.controller.actionPerformed(new ActionEvent(this, 0, ACTION_COMMAND_LOAD_DEMO_AIRSPACES));
            // Add the scroll bar and name panel to a titled panel that will resize with the main window.
            this.remove(layerPanel);
            statusPanel = new JPanel();
            statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    		statusPanel.setSize(new Dimension(200,400));
//    		statusPanel.add(pnlWest);
            this.getContentPane().add(statusPanel, BorderLayout.WEST);
            this.pack();
           
        }
    }

    public static class AirspacesController implements ActionListener {
    	
        protected AppFrame frame;
        // World Wind stuff.
        protected AirspaceLayer aglAirspaces;
        protected AirspaceLayer amslAirspaces;
        protected RenderableLayer imageLayer;
        protected Airspace lastHighlit;
        protected AirspaceAttributes lastAttrs;
        protected Annotation lastAnnotation;
        protected BasicDragger dragger;
        protected Map<Position, Renderable> images;


        public AirspacesController(AppFrame appFrame)
        {
            this.frame = appFrame;
//            this.images = images;
            // Construct a layer that will hold the airspaces and annotations.
            this.aglAirspaces = new AirspaceLayer();
            this.amslAirspaces = new AirspaceLayer();
            this.imageLayer = new RenderableLayer();
            this.aglAirspaces.setName("AGL Airspaces");
            this.amslAirspaces.setName("AMSL Airspaces");
            this.aglAirspaces.setEnableBatchPicking(false);
            this.amslAirspaces.setEnableBatchPicking(false);
            insertBeforePlacenames(this.frame.getWwd(), this.aglAirspaces);
            insertBeforePlacenames(this.frame.getWwd(), this.amslAirspaces);
            imageLayer.setPickEnabled(true);
            insertBeforePlacenames(this.frame.getWwd(), this.imageLayer);

            this.initializeSelectionMonitoring();
        }

        public WorldWindow getWwd()
        {
            return this.frame.getWwd();
        }

        public void actionPerformed(ActionEvent e)
        {
            if (ACTION_COMMAND_LOAD_DEMO_AIRSPACES.equalsIgnoreCase(e.getActionCommand()))
            {
                this.doLoadDemoAirspaces();
            }
            else if (ACTION_COMMAND_LOAD_DATELINE_CROSSING_AIRSPACES.equalsIgnoreCase(e.getActionCommand()))
            {
                this.doLoadDatelineCrossingAirspaces();
            }
           
            else if (ACTION_COMMAND_ANTIALIAS.equalsIgnoreCase(e.getActionCommand()))
            {
                JCheckBox cb = (JCheckBox) e.getSource();
                this.aglAirspaces.setEnableAntialiasing(cb.isSelected());
                this.amslAirspaces.setEnableAntialiasing(cb.isSelected());
                this.getWwd().redraw();
            }
            else if (ACTION_COMMAND_DEPTH_OFFSET.equalsIgnoreCase(e.getActionCommand()))
            {
                JCheckBox cb = (JCheckBox) e.getSource();
                this.aglAirspaces.setEnableDepthOffset(cb.isSelected());
                this.amslAirspaces.setEnableDepthOffset(cb.isSelected());
                this.getWwd().redraw();
            }
            else if (ACTION_COMMAND_DRAW_WIREFRAME.equalsIgnoreCase(e.getActionCommand()))
            {
                JCheckBox cb = (JCheckBox) e.getSource();
                this.aglAirspaces.setDrawWireframe(cb.isSelected());
                this.amslAirspaces.setDrawWireframe(cb.isSelected());
                this.getWwd().redraw();
            }
            else if (ACTION_COMMAND_DRAW_EXTENT.equalsIgnoreCase(e.getActionCommand()))
            {
                JCheckBox cb = (JCheckBox) e.getSource();
                this.aglAirspaces.setDrawExtents(cb.isSelected());
                this.amslAirspaces.setDrawExtents(cb.isSelected());
                this.getWwd().redraw();
            }
        }
        public void setSurfaceImages(Map<Position, Renderable> imageMap){
        	System.out.println("have we been here?");
        	this.imageLayer.removeAllRenderables();
        	if (imageMap != null) {
        		for (Entry<Position, Renderable> imageEntry: imageMap.entrySet()){
        			imageLayer.addRenderable(imageEntry.getValue());
        		}
        	}
        }
        public void setAirspaces(Collection<Airspace> airspaces)
        {
            this.aglAirspaces.removeAllAirspaces();
            this.amslAirspaces.removeAllAirspaces();

            if (airspaces != null)
            {
                for (Airspace a : airspaces)
                {
                    if (a == null)
                        continue;

                    if (a.getAltitudeDatum()[0].equals(AVKey.ABOVE_MEAN_SEA_LEVEL) &&
                        a.getAltitudeDatum()[1].equals(AVKey.ABOVE_MEAN_SEA_LEVEL))
                    {
                        this.amslAirspaces.addAirspace(a);
                    }
                    else
                    {
                        this.aglAirspaces.addAirspace(a);
                    }
                }
            }

            this.getWwd().redraw();
        }

        public void initializeSelectionMonitoring()
        {
            this.dragger = new BasicDragger(this.getWwd());
            this.getWwd().addSelectListener(new SelectListener()
            {
                public void selected(SelectEvent event)
                {
                    // Have rollover events highlight the rolled-over object.
                    if (event.getEventAction().equals(SelectEvent.ROLLOVER) && !dragger.isDragging())
                    {
                        if (AirspacesController.this.highlight(event.getTopObject()))
                            AirspacesController.this.getWwd().redraw();
                    }
                    // Have drag events drag the selected object.
                    else if (event.getEventAction().equals(SelectEvent.DRAG_END)
                        || event.getEventAction().equals(SelectEvent.DRAG))
                    {
                        // Delegate dragging computations to a dragger.
                        dragger.selected(event);

                        // We missed any roll-over events while dragging, so highlight any under the cursor now,
                        // or de-highlight the dragged shape if it's no longer under the cursor.
                        if (event.getEventAction().equals(SelectEvent.DRAG_END))
                        {
                            PickedObjectList pol = AirspacesController.this.getWwd().getObjectsAtCurrentPosition();
                            if (pol != null)
                            {
                                AirspacesController.this.highlight(pol.getTopObject());
                                AirspacesController.this.getWwd().redraw();
                            }
                        }
                    }
                }
            });
        }

        protected boolean highlight(Object o)
        {
            if (this.lastHighlit == o)
                return false; // Same thing selected

            // Turn off highlight if on.
            if (this.lastHighlit != null)
            {
                this.lastHighlit.setAttributes(this.lastAttrs);
                this.lastHighlit = null;
                this.lastAttrs = null;
            }

            // Turn on highlight if selected object is a SurfaceImage.
            if (o instanceof Airspace)
            {
                this.lastHighlit = (Airspace) o;
                this.lastAttrs = this.lastHighlit.getAttributes();
                BasicAirspaceAttributes highlitAttrs = new BasicAirspaceAttributes(this.lastAttrs);
                highlitAttrs.setMaterial(Material.WHITE);
                this.lastHighlit.setAttributes(highlitAttrs);
            }

            return true;
        }

        protected void setupDefaultMaterial(Airspace a, Color color)
        {
            a.getAttributes().setDrawOutline(true);
            a.getAttributes().setMaterial(new Material(color));
            a.getAttributes().setOutlineMaterial(new Material(WWUtil.makeColorBrighter(color)));
            a.getAttributes().setOpacity(0.8);
            a.getAttributes().setOutlineOpacity(0.9);
            a.getAttributes().setOutlineWidth(3.0);
        }

        public void doLoadDatelineCrossingAirspaces()
        {
            ArrayList<Airspace> airspaces = new ArrayList<Airspace>();

            // Curtains of different path types crossing the dateline.
            Curtain curtain = new Curtain();
            curtain.setLocations(Arrays.asList(LatLon.fromDegrees(27.0, -112.0), LatLon.fromDegrees(35.0, 138.0)));
            curtain.setAltitudes(1000.0, 100000.0);
            curtain.setTerrainConforming(false, false);
            curtain.setValue(AVKey.DISPLAY_NAME, "Great arc Curtain from America to Japan.");
            this.setupDefaultMaterial(curtain, Color.MAGENTA);
            airspaces.add(curtain);

            curtain = new Curtain();
            curtain.setLocations(Arrays.asList(LatLon.fromDegrees(27.0, -112.0), LatLon.fromDegrees(35.0, 138.0)));
            curtain.setPathType(AVKey.RHUMB_LINE);
            curtain.setAltitudes(1000.0, 100000.0);
            curtain.setTerrainConforming(false, false);
            curtain.setValue(AVKey.DISPLAY_NAME, "Rhumb Curtain from America to Japan.");
            this.setupDefaultMaterial(curtain, Color.CYAN);
            airspaces.add(curtain);

            // Continent sized sphere
            SphereAirspace sphere = new SphereAirspace();
            sphere.setLocation(LatLon.fromDegrees(0.0, -180.0));
            sphere.setAltitude(0.0);
            sphere.setTerrainConforming(false);
            sphere.setRadius(1000000.0);
            this.setupDefaultMaterial(sphere, Color.RED);
            airspaces.add(sphere);

            this.setAirspaces(airspaces);
        }

        public void doLoadDemoAirspaces()
        {      	
//        	try
//            {
//                imageLayer = new RenderableLayer();
//                imageLayer.setName("Surface Images");
//                imageLayer.setPickEnabled(false);
//        		for (SurfaceImage image: images){
//        			imageLayer.addRenderable(image);
//        		}
//        		imageLayer.setPickEnabled(true);
//                insertBeforeCompass(this.getWwd(), imageLayer);
//                this.getWwd().redraw();
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
        }
    }


    public static void main(String[] args)
    {
        start("Pokemon Hunter", AppFrame.class);
    }

    protected static Iterable<LatLon> makeLatLon(double[] src, int offset, int length)
    {
        int numCoords = (int) Math.floor(length / 2.0);
        LatLon[] dest = new LatLon[numCoords];
        for (int i = 0; i < numCoords; i++)
        {
            double lonDegrees = src[offset + 2 * i];
            double latDegrees = src[offset + 2 * i + 1];
            dest[i] = LatLon.fromDegrees(latDegrees, lonDegrees);
        }
        return Arrays.asList(dest);
    }

    protected static Iterable<LatLon> makeLatLon(double[] src)
    {
        return makeLatLon(src, 0, src.length);
    }
  
}