/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package zz23_jj26.server.earth;

import gov.nasa.worldwind.event.*;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.pick.PickedObject;
import zz23_jj26.server.earth.Airspaces;

/**
 * Illustrates how to cause all elements under the cursor in a WorldWindow to be reported in <code>{@link
 * SelectEvent}s</code>. This prints all elements under the cursor to the console in response to a <code>HOVER</code>
 * SelectEvent.
 * <p/>
 * In order to enable deep picking, any batch picking for the desired elements must be disabled and the
 * SceneController's deep picking property must be enabled. See <code>{@link gov.nasa.worldwind.SceneController#setDeepPickEnabled(boolean)}</code>.
 *
 * @author tag
 * @version $Id: DeepPicking.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class DeepPicking extends Airspaces
{
    public static class AppFrame extends Airspaces.AppFrame
    {
		private static final long serialVersionUID = -3044150158683924353L;

		public AppFrame()
        {
            // Prohibit batch picking for the airspaces.
            this.controller.aglAirspaces.setEnableBatchPicking(false);
            this.controller.amslAirspaces.setEnableBatchPicking(false);

            // Tell the scene controller to perform deep picking.
            this.controller.getWwd().getSceneController().setDeepPickEnabled(true);

            // Register a select listener to print the class names of the items under the cursor.
            this.controller.getWwd().addSelectListener(new SelectListener()
            {
                public void selected(SelectEvent event)
                {
                    if (event.getEventAction().equals(SelectEvent.HOVER) && event.getObjects() != null)
                    {
                        System.out.printf("%d objects\n", event.getObjects().size());
                        if (event.getObjects().size() > 1)
                        {
                            for (PickedObject po : event.getObjects())
                            {
                                System.out.println(po.getObject().getClass().getName());
                                System.out.println(po.getEntries().toString());
                                if (po.getObject() instanceof SurfaceImage){
                                	System.out.println("yay we are here with" + po.getObject().getClass().getName());
                                	if (po.getParentLayer() instanceof RenderableLayer){
                                		System.out.println("yay we are here with " + po.getParentLayer().getClass().getName());
                                		((RenderableLayer)po.getParentLayer()).removeRenderable((SurfaceImage)po.getObject());
                                	}
                                	else{
                                		System.out.println("It's type is " + po.getParentLayer().getClass().getName());
                                	}
                                	
                                }
                            }
                        }
                    }
                }
            });
        }
    }
    
    public AppFrame createAppFrame(){
    	return new AppFrame();
    }

    public static void main(String[] args)
    {
        start("World Wind Deep Picking", AppFrame.class);
    }
}
