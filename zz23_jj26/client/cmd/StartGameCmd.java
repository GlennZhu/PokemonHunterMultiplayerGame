package zz23_jj26.client.cmd;

import javax.swing.SwingUtilities;

import gov.nasa.worldwind.globes.Earth;
import map.MapPanel;
import provided.datapacket.ADataPacketAlgoCmd;
import provided.datapacket.DataPacket;
import common.ICmd2ModelAdapter;
import common.chatroom.IChatroomAdapter;
import common.message.INullMessage;
import common.message.NullMessage;
import common.message.chat.IChatMessage;

public class StartGameCmd extends ADataPacketAlgoCmd<DataPacket<? extends IChatMessage>, IChatMessage, IChatroomAdapter>{
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
		    	MapPanel _mapPanel = new MapPanel(Earth.class);
				_mapPanel.setPreferredSize(new java.awt.Dimension(600, 400));
		    	_mapPanel.start();
				cmdAdpt.addComponent(_mapPanel, "Placeholder here");
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
