/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package zz23_jj26.server.earth;

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.event.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.render.airspaces.*;
import gov.nasa.worldwind.render.airspaces.Box;
import gov.nasa.worldwind.render.airspaces.Polygon;
import gov.nasa.worldwind.util.*;
import gov.nasa.worldwindx.examples.Airspaces.AirspacesPanel;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import gov.nasa.worldwindx.examples.FlatWorldPanel;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Illustrates how to configure and display World Wind <code>{@link Airspace}</code> shapes. Airspace shapes are
 * extruded 3D volumes defined by geographic coordinates and upper- and lower- altitude boundaries. The interior of
 * airspace shapes always conforms to the curvature of the globe, and optionally also conform to the underlying
 * terrain.
 * 
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
	
    public static final String ACTION_COMMAND_LOAD_DEMO_AIRSPACES = "ActionCommandLoadDemoAirspaces";

    public static class AppFrame extends ApplicationTemplate.AppFrame{
    	
		private static final long serialVersionUID = -7456230205583333227L;
		protected AirspacesController controller;
        protected AirspacesPanel airspacesPanel;
        protected FlatWorldPanel flatWorldPanel;
        protected JPanel statusPanel;

        public AppFrame(){
            this.controller = new AirspacesController(this);
            this.controller.actionPerformed(new ActionEvent(this, 0, ACTION_COMMAND_LOAD_DEMO_AIRSPACES));
            // Add status panel to the west of the window
            this.remove(layerPanel);
            statusPanel = new JPanel();
            statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    		statusPanel.setSize(new Dimension(300,400));
    		GridBagLayout gbl_statusPanel = new GridBagLayout();
    		gbl_statusPanel.columnWidths = new int[]{64, 0};
    		gbl_statusPanel.rowHeights = new int[]{16, 0, 0, 0};
    		gbl_statusPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
    		gbl_statusPanel.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
    		statusPanel.setLayout(gbl_statusPanel);
            this.getContentPane().add(statusPanel, BorderLayout.WEST);
            this.pack();
        }
    }

    public static class AirspacesController implements ActionListener {
    	
        protected AppFrame frame;
        // World Wind stuff.
        protected RenderableLayer imageLayer;
        protected Airspace lastHighlit;
        protected AirspaceAttributes lastAttrs;
        protected Annotation lastAnnotation;


        public AirspacesController(AppFrame appFrame)
        {
            this.frame = appFrame;
            // Construct a layer that will hold pokemon images.
            this.imageLayer = new RenderableLayer();
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
        }
        
        public void setSurfaceImages(Map<Position, Renderable> imageMap){
        	this.imageLayer.removeAllRenderables();
        	if (imageMap != null) {
        		for (Entry<Position, Renderable> imageEntry: imageMap.entrySet()){
        			imageLayer.addRenderable(imageEntry.getValue());
        		}
        	}
        }
        
        public void initializeSelectionMonitoring()
        {
            this.getWwd().addSelectListener(new SelectListener()
            {
                public void selected(SelectEvent event)
                {
                    // Have rollover events highlight the rolled-over object.
                    if (event.getEventAction().equals(SelectEvent.ROLLOVER))
                    {
                        if (AirspacesController.this.highlight(event.getTopObject()))
                            AirspacesController.this.getWwd().redraw();
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

        public void doLoadDemoAirspaces()
        {      	
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