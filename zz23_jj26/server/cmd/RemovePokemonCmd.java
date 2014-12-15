package zz23_jj26.server.cmd;

import java.util.Map;

import javax.imageio.spi.IIORegistry;

import gov.nasa.worldwind.formats.tiff.GeotiffImageReaderSpi;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Renderable;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import zz23_jj26.server.message.game.RemovePokemon;
import common.ICmd2ModelAdapter;
import common.chatroom.IChatroomAdapter;
import common.message.INullMessage;
import common.message.NullMessage;
import common.message.chat.IChatMessage;

/**
 * This command will ask the user to remove certain pokemon from the map
 * @author Jiafang Jiang, Wei Zeng, Ziliang Zhu
 *
 */
public class RemovePokemonCmd extends ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, RemovePokemon, IChatroomAdapter>{
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
	 * Constructor of GameOver cmd
	 * @param cmdAdpt the user's command to model adapter
	 */
	public RemovePokemonCmd(ICmd2ModelAdapter cmdAdpt) {
		this.cmdAdpt = cmdAdpt;
	}

	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		cmdAdpt = cmd2ModelAdpt;
	}

	@Override
	public DataPacket<? extends IChatMessage> apply(Class<?> index,
			DataPacket<RemovePokemon> host, IChatroomAdapter... params) {
		
		Position posToRemove = ((RemovePokemon)host.getData()).getPos();
		IMixedDataDictionary dict = cmdAdpt.getMixedDataDictionary(); 
		// Get images and image layer from MMD
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<Position, Renderable> imageMap = dict.get(new MixedDataKey<Map>(((RemovePokemon)host.getData()).getUUID(), "imageMap", Map.class));
		Renderable removeMe = imageMap.remove(posToRemove); // remove this image from my local mixDic		
		RenderableLayer layer = dict.get(new MixedDataKey<RenderableLayer>(((RemovePokemon)host.getData()).getUUID(), "ImageLayer", RenderableLayer.class));
		// Remove the pokemon
		layer.removeRenderable(removeMe);
		return new DataPacket<INullMessage>(INullMessage.class, NullMessage.SINGLETON);
	}
}