package zz23_jj26.server.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import javax.imageio.spi.IIORegistry;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.formats.tiff.GeotiffImageReaderSpi;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.render.Renderable;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import zz23_jj26.server.earth.SurfaceImage;
import common.ICmd2ModelAdapter;
import common.chatroom.IChatroomAdapter;
import common.message.INullMessage;
import common.message.NullMessage;
import common.message.chat.IChatMessage;

public class StartGameCmd extends ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, IChatMessage, IChatroomAdapter>{
    static
    {
        IIORegistry reg = IIORegistry.getDefaultInstance();
        reg.registerServiceProvider(GeotiffImageReaderSpi.inst());
    }

    
	private static final long serialVersionUID = 6147448840505287844L;

	/**
	 * A adapter to the model that is not passed
	 */
	private transient ICmd2ModelAdapter cmdAdpt;
	
	public StartGameCmd(ICmd2ModelAdapter cmdAdpt) {
		this.cmdAdpt = cmdAdpt;
	}
	
	@Override
	public DataPacket<? extends IChatMessage> apply(Class<?> index,
			DataPacket<IChatMessage> host, IChatroomAdapter... params) {
		// Worker thread
		SwingUtilities.invokeLater(new Runnable() {  // Put this Runnable on the GUI event thread
		    public void run() {
		        try
		        {
		        	// TODO Send images...
		        	
			        // Test code to let images show, will replace. 
			        // Put images into the map
			        Map<UUID, Renderable> globalMap = zz23_jj26.server.Util.Util.idToImage;
			        UUID thisID = UUID.randomUUID();
	                SurfaceImage si1 = new SurfaceImage("zz23_jj26/server/image/georss.png", new ArrayList<LatLon>(Arrays.asList(
	                        LatLon.fromDegrees(20d, -115d),
	                        LatLon.fromDegrees(20d, -105d),
	                        LatLon.fromDegrees(32d, -102d),
	                        LatLon.fromDegrees(30d, -115d)
	                    )), thisID);
	                globalMap.put(thisID, si1);
	                
	                thisID = UUID.randomUUID();
                    SurfaceImage si2 = new SurfaceImage("zz23_jj26/server/image/pikachu.png", new ArrayList<LatLon>(Arrays.asList(
                        LatLon.fromDegrees(37.8677, -105.1668),
                        LatLon.fromDegrees(37.8677, -104.8332),
                        LatLon.fromDegrees(38.1321, -104.8326),
                        LatLon.fromDegrees(38.1321, -105.1674)
                    )), thisID);
                    // Test end
                    
                    // *** images in the globalMap will show *** 
		        	// Start game frame
			    	String appName = "Game frame";
			        if (Configuration.isMacOS()){
			            System.setProperty("com.apple.mrj.application.apple.menu.about.name", appName);
			        }
                    globalMap.put(thisID, si2);
			        final zz23_jj26.server.earth.DeepPicking.AppFrame frame = new zz23_jj26.server.earth.DeepPicking.AppFrame();
		            frame.setTitle(appName);
		            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		            java.awt.EventQueue.invokeLater(new Runnable()
		            {
		                public void run()
		                {
		                    frame.setVisible(true);
		                }
		            });
		        }
		        catch (Exception e)
		        {
		            e.printStackTrace();
		        }
		        System.out.println("Started game");
		    }
		});
		
		return new DataPacket<INullMessage>(
				INullMessage.class,
				NullMessage.SINGLETON);
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		cmdAdpt = cmd2ModelAdpt;
	}
}