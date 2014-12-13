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
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Renderable;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import zz23_jj26.server.earth.DeepPicking;
import zz23_jj26.server.earth.DeepPicking.AppFrame;
import zz23_jj26.server.earth.SurfaceImage;
import zz23_jj26.server.message.game.IRequestRemovePokemon;
import zz23_jj26.server.message.game.IStartGame;
import zz23_jj26.server.message.game.RemovePokemon;
import common.ICmd2ModelAdapter;
import common.chatroom.IChatroomAdapter;
import common.message.INullMessage;
import common.message.NullMessage;
import common.message.chat.IChatMessage;

public class RemovePokemonCmd extends ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, RemovePokemon, IChatroomAdapter>{
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
		IMixedDataDictionary dict = cmdAdpt.getMixedDataDictionary(); //TODO: Is this the mixDic of the client?
		Map<Position, Renderable> imageMap = dict.get(new MixedDataKey<Map>(((RemovePokemon)host.getData()).getUUID(), "imageMap", Map.class));
		Renderable removeMe = imageMap.remove(posToRemove); // remove this image from my local mixDic		
		//TODO: We also need to remove the image from the renderable layer.
		RenderableLayer layer = dict.get(new MixedDataKey<RenderableLayer>(((RemovePokemon)host.getData()).getUUID(), "ImageLayer", RenderableLayer.class));
		layer.removeRenderable(removeMe);
		return new DataPacket<INullMessage>(
				INullMessage.class,
				NullMessage.SINGLETON);
	}
}