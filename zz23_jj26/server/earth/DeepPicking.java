/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package zz23_jj26.server.earth;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import provided.datapacket.DataPacket;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import common.ICmd2ModelAdapter;
import common.chatroom.IChatroomAdapter;
import common.user.IUser;
import gov.nasa.worldwind.event.*;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.Renderable;
import zz23_jj26.server.earth.Airspaces;
import zz23_jj26.server.message.game.IRequestRemovePokemon;

/**
 * Illustrates how to cause all elements under the cursor in a WorldWindow to be reported in <code>{@link
 * SelectEvent}s</code>. This prints all elements under the cursor to the console in response to a <code>HOVER</code>
 * SelectEvent.
 * 
 * In order to enable deep picking, any batch picking for the desired elements must be disabled and the
 * SceneController's deep picking property must be enabled. See <code>{@link gov.nasa.worldwind.SceneController#setDeepPickEnabled(boolean)}</code>.
 *
 * @author WorldWind, Jiafang Jiang, Ziliang Zhu, Wei Zeng
 * @version $Id: DeepPicking.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class DeepPicking extends Airspaces
{
	/**
	 * A Mapping of images to their positions
	 */
	private Map<Position, Renderable> imageMap;
	
	/**
	 * Chatroom Adapter of the server
	 */
	private IChatroomAdapter server;
	
	/**
	 * Client's command to model adapter
	 */
	private ICmd2ModelAdapter cmd2modelAdpt;
	
	/**
	 * Common UUID for generating MixedDataKey
	 */
	private UUID keyUID;
	
	/**
	 * Mixed Data Dictionary of the user
	 */
	private IMixedDataDictionary mixedDict;
	
	/**
	 * The Mixed Data Key for the image layer
	 */
	private MixedDataKey<RenderableLayer> layerKey;
	
	/**
	 * The map storing the name of the team and its members
	 */
	private HashMap<String, ArrayList<IChatroomAdapter>> teamsMap;
	
	/**
	 * The lable for displaying each user's score
	 */
	private transient JLabel lblScore;
	
	/**
	 * Constructor of DeepPicking behavior class
	 * @param imageMap A Mapping of images to their positions
	 * @param server Chatroom Adapter of the server
	 * @param cmd2modelAdpt Client's command to model adapter
	 * @param keyUID Common UUID for generating MixedDataKey
	 * @param teamsMap The map storing the name of the team and its members
	 */
	public DeepPicking(Map<Position, Renderable> imageMap, IChatroomAdapter server, ICmd2ModelAdapter cmd2modelAdpt, UUID keyUID, HashMap<String, ArrayList<IChatroomAdapter>> teamsMap) {
		this.imageMap = imageMap;
		this.server = server;
		this.cmd2modelAdpt = cmd2modelAdpt;
		this.keyUID = keyUID;
		this.mixedDict = cmd2modelAdpt.getMixedDataDictionary();
		this.teamsMap = teamsMap;
        this.layerKey = new MixedDataKey<RenderableLayer>(keyUID, "ImageLayer", RenderableLayer.class);
	}
	
    public class AppFrame extends Airspaces.AppFrame
    {
		private static final long serialVersionUID = -3044150158683924353L;

		public AppFrame()
        {
			// Create new panel for displaying game status
            JPanel pnlStatus = new JPanel();
            pnlStatus.setBorder(BorderFactory.createTitledBorder("Game Status"));
            JLabel lblInfo = new JLabel("My Score: ");
            pnlStatus.add(lblInfo);
            lblScore = new JLabel("0");
            pnlStatus.add(lblScore);
            
    		GridBagConstraints gbc_pnlStatus = new GridBagConstraints();
    		gbc_pnlStatus.fill = GridBagConstraints.BOTH;
    		gbc_pnlStatus.gridx = 0;
    		gbc_pnlStatus.gridy = 1;
            this.statusPanel.add(pnlStatus, gbc_pnlStatus);
            
            // A panel for displaying team member
            JPanel pnlTeam = new JPanel();
            String teamName = "";
            ArrayList<IUser> teamMembers = new ArrayList<IUser>();
            for (Entry<String, ArrayList<IChatroomAdapter>> team: teamsMap.entrySet()){
            	teamName = team.getKey();
            	for (IChatroomAdapter adpt: team.getValue()){
            		teamMembers.add(adpt.getUser());
            	}
            }
            
            pnlTeam.setBorder(BorderFactory.createTitledBorder(teamName));
    		GridBagLayout gbl_pnlTeamLayout = new GridBagLayout();
    		gbl_pnlTeamLayout.columnWidths = new int[]{64, 0};
    		gbl_pnlTeamLayout.rowHeights = new int[]{16, 0, 0};
    		gbl_pnlTeamLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
    		gbl_pnlTeamLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
    		pnlTeam.setLayout(gbl_pnlTeamLayout);
    		for (int i = 0; i < teamMembers.size(); i++){
    			JLabel lblMemberName = new JLabel(teamMembers.get(i).toString());
    			GridBagConstraints gbc_lblMemberName = new GridBagConstraints();
    			gbc_lblMemberName.insets = new Insets(0, 0, 5, 5);
    			gbc_lblMemberName.anchor = GridBagConstraints.WEST;
    			gbc_lblMemberName.gridx = 0;
    			gbc_lblMemberName.gridy = i;
    			pnlTeam.add(lblMemberName, gbc_lblMemberName);
    		}
    		
    		GridBagConstraints gbc_pnlTeam = new GridBagConstraints();
    		gbc_pnlTeam.fill = GridBagConstraints.BOTH;
    		gbc_pnlTeam.gridx = 0;
    		gbc_pnlTeam.gridy = 2;
    		this.statusPanel.add(pnlTeam, gbc_pnlTeam);
            
    		// put the score lable into MMD
            MixedDataKey<JLabel> keyToScoreLabel = new MixedDataKey<JLabel>(keyUID, "score", JLabel.class);
            mixedDict.put(keyToScoreLabel, lblScore);
                    
            IMixedDataDictionary dict = cmd2modelAdpt.getMixedDataDictionary();
            dict.put(layerKey, this.controller.imageLayer);
            @SuppressWarnings("rawtypes")
			MixedDataKey<Map> keyToImageMap = new MixedDataKey<Map>(keyUID, "imageMap", Map.class);
            mixedDict.put(keyToImageMap, imageMap);
            
            this.controller.setSurfaceImages(imageMap);
            // Tell the scene controller to perform deep picking.
            this.controller.getWwd().getSceneController().setDeepPickEnabled(true);
            // Register a select listener to print the class names of the items under the cursor.
            this.controller.getWwd().addSelectListener(new SelectListener()
            {
                public void selected(SelectEvent event)
                {
                    if (event.getEventAction().equals(SelectEvent.HOVER) && event.getObjects() != null)
                    {
                        if (event.getObjects().size() > 1)
                        {
                            for (PickedObject po : event.getObjects())
                            {

                                if (po.getObject() instanceof SurfaceImage){
                                	if (po.getParentLayer() instanceof RenderableLayer){
                       
                                		MixedDataKey<IRequestRemovePokemon> removeKey = new MixedDataKey<IRequestRemovePokemon>(
                                				keyUID, "RequestRemoveMessage", IRequestRemovePokemon.class);
                                		IRequestRemovePokemon removeMsg = mixedDict.get(removeKey);
                                		// Get the position of the image to be removed
                                		double mylat = ((SurfaceImage)po.getObject()).getReferencePosition().latitude.degrees;
                                		double mylon = ((SurfaceImage)po.getObject()).getReferencePosition().longitude.degrees;
                                		removeMsg.setPosToRemove(mylat, mylon);
                                		removeMsg.setUUID(keyUID);
                                		// Send a message to server to request to remove the pokemon
                                		DataPacket<IRequestRemovePokemon> message = new DataPacket<IRequestRemovePokemon>(IRequestRemovePokemon.class, removeMsg);
                                		new Thread() {
                            				public void run() {
                            					try {
                            						server.sendChatroomMessage(message, cmd2modelAdpt.getChatroomAdapter());
                            					} catch (RemoteException e) {
                            						e.printStackTrace();
                            					}
                            				}
                            			}.start();
                                	}            	
                                }
                            }
                        }
                    }
                }
            });
        }
    }
    
    /**
     * Return the new WWJ App Frame
     * @return WWJ App frame
     */
    public AppFrame createAppFrame(){
    	return new AppFrame();
    }

}
