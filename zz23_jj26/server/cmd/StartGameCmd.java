package zz23_jj26.server.cmd;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.spi.IIORegistry;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.formats.tiff.GeotiffImageReaderSpi;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Renderable;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import zz23_jj26.server.earth.DeepPicking;
import zz23_jj26.server.earth.DeepPicking.AppFrame;
import zz23_jj26.server.earth.RandomPosition;
import zz23_jj26.server.earth.SurfaceImage;
import zz23_jj26.server.message.game.IRequestRemovePokemon;
import zz23_jj26.server.message.game.IStartGame;
import common.ICmd2ModelAdapter;
import common.chatroom.IChatroomAdapter;
import common.message.INullMessage;
import common.message.NullMessage;
import common.message.chat.IChatMessage;

/**
 * Command for StartGame Message
 * @author Jiafang Jiang, Ziliang Zhu, Wei Zeng
 */
public class StartGameCmd extends ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, IStartGame, IChatroomAdapter>{
    static
    {
        IIORegistry reg = IIORegistry.getDefaultInstance();
        reg.registerServiceProvider(GeotiffImageReaderSpi.inst());
    }

    /**
     * Generated UID
     */
	private static final long serialVersionUID = 6147448840505287844L;

	/**
	 * A adapter to the model that is not passed
	 */
	private transient ICmd2ModelAdapter cmdAdpt;
	
	/**
	 * Server's chatroom adapter
	 */
	private IChatroomAdapter adapterToChatroom;
	
	/**
	 * 
	 * @param cmdAdpt
	 * @param adapterToMe
	 */
	public StartGameCmd(ICmd2ModelAdapter cmdAdpt) {
		this.cmdAdpt = cmdAdpt;
	}
	
	@Override
	public DataPacket<? extends IChatMessage> apply(Class<?> index,
			DataPacket<IStartGame> host, IChatroomAdapter... params) {
		ArrayList<BufferedImage> images = host.getData().getImages();	
		ArrayList<RandomPosition> positions = host.getData().getPositions();
		IRequestRemovePokemon removeMsg = host.getData().getRemovePokemonMessage();
		UUID key = host.getData().getUUID();
		MixedDataKey<IRequestRemovePokemon> removeKey = new MixedDataKey<IRequestRemovePokemon>(
				key, "RequestRemoveMessage", IRequestRemovePokemon.class);
		IMixedDataDictionary dict = cmdAdpt.getMixedDataDictionary();
		Map<Position, Renderable> imageMap = new ConcurrentHashMap<Position, Renderable>();
		System.out.println(dict == null);
		System.out.println("Test test test");
		dict.put(removeKey,removeMsg);
		// Worker thread
		SwingUtilities.invokeLater(new Runnable() {  // Put this Runnable on the GUI event thread
		    public void run() {
		        try
		        {
		        	// Loading images to the map
		        	for (int i = 0; i < images.size(); i++){
		        		RandomPosition pos = positions.get(i);
		        		BufferedImage img = images.get(i);
		        		SurfaceImage si = new SurfaceImage(img, new ArrayList<LatLon>(Arrays.asList(
		                        LatLon.fromDegrees(pos.getLatitude(), pos.getLongtitude()),
		                        LatLon.fromDegrees(pos.getLatitude(), pos.getLongtitude() + pos.getOffset()),
		                        LatLon.fromDegrees(pos.getLatitude() + pos.getOffset(), pos.getLongtitude() + pos.getOffset()),
		                        LatLon.fromDegrees(pos.getLatitude() + pos.getOffset(), pos.getLongtitude())
		                    )));
		        		imageMap.put(si.getReferencePosition(), si);
		        	}
		        	
		        	// Start game frame
			    	String appName = "Game frame";

			        if (Configuration.isMacOS()){
			            System.setProperty("com.apple.mrj.application.apple.menu.about.name", appName);
			        }
                    DeepPicking dp = new DeepPicking(imageMap, params[0], cmdAdpt, key, dict);
			        AppFrame frame = dp.createAppFrame();
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