package zz23_jj26.client.mini.controller;

import java.awt.Component;

import zz23_jj26.client.mini.model.Chatroom;
import zz23_jj26.client.mini.model.IChatroom;
import zz23_jj26.client.mini.model.IMModel2MViewAdapter;
import zz23_jj26.client.mini.model.IMiniModel2MainModelAdapter;
import zz23_jj26.client.mini.view.ChatRoomGui;
import zz23_jj26.client.mini.view.IMiniViewToModelAdpt;
import common.chatroom.IChatroomID;
import common.user.IUser;

/**
 * Mini controller for the mini-mvc system of a single chatroom.
 * @author austurela
 *
 */
public class MiniController {
	/**
	 * The chatroom model
	 */
	private IChatroom miniModel;
	/**
	 * The chatroom view
	 */
	private ChatRoomGui miniView;

	/**
	 * Constructor
	 * @param toJoin - the room to join
	 * @param me - the new user
	 * @param adapterToMe - An adapter to that user
	 */
	public MiniController(IChatroomID toJoin, IUser me, IMiniModel2MainModelAdapter adapterToMe){
		miniModel = new Chatroom(toJoin, me, new IMModel2MViewAdapter() {
			
			@Override
			public void printToView(String text) {
				miniView.printToView(text);
				
			}

			@Override
			public void addComponent(Component component) {
				miniView.addComponent(component);
				
			}
		}, adapterToMe);
		
		miniView = new ChatRoomGui(new IMiniViewToModelAdpt() {
			
			@Override
			public void sendText(String text) {
				miniModel.sendTextMessage(text);
			}
			
			@Override
			public void quit() {
				miniModel.quit();
			}

			@Override
			public void sendSmilie() {
				miniModel.sendSmilie();
				
			}

			@Override
			public void sendStartGame() {
				miniModel.sendStartGame();
			}
		}, toJoin.toString());
	}

	/**
	 * Start the mini mvc
	 */
	public void start(){
		miniModel.start();
		miniView.start();
	}

	/**
	 * Get the model of the chatroom
	 * @return
	 */
	public IChatroom getModel() {
		return miniModel;
	}
}
