package common;

import java.awt.Component;
import java.awt.Window;

import javax.swing.JFrame;

import provided.mixedData.IMixedDataDictionary;

import common.chatroom.IChatroomAdapter;

/**
 * Interface for how commands can interact with the model
 *
 * @author Jake Kornblau, Wilson Beebe, Derek Peirce
 *
 */
public interface ICmd2ModelAdapter {
    /**
     * Add a component to the GUI for the chatroom
     *
     * @param component
     *            the component to add to the chatroom GUI
     * @param label
     * 			  the label for the component
     */
    public void addComponent(Component component, String label);

    /**
     * Add a component to the GUI for the chatroom
     *
     * @param component
     *            the component to add to the chatroom GUI
     * @param label
     * 			  the label for the component
     * @return the window created for this component
     */
    public Window addComponentAsWindow(Component component, String label);

    /**
     * Add a simple string of text to the chatroom GUI as a new line.
     *
     * @param text
     *            the text to be added to the GUI as a new line.
     */
    public void append(String text);

    /**
     * Gets the mixed data dictionary
     *
     * @return the mixed data dictionary that commands can use to store any necessary information.
     */
    public IMixedDataDictionary getMixedDataDictionary();
    
    /**
     * Gets the chatroom adapter that installed this command
     * 
     * @return The chatroom adapter
     */
    public IChatroomAdapter getChatroomAdapter();

    public static final ICmd2ModelAdapter NULL_ADAPTER = new ICmd2ModelAdapter(){

		@Override
		public Window addComponentAsWindow(Component component, String label) {
			JFrame frame = new JFrame(label);
			frame.add(component);
			return frame;
		}
		
		@Override
		public void addComponent(Component component, String label) {
			addComponentAsWindow(component, label);
		}

		@Override
		public void append(String text) {
			System.out.println(text);
		}

		@Override
		public IMixedDataDictionary getMixedDataDictionary() {
			return null;
		}

		@Override
		public IChatroomAdapter getChatroomAdapter() {
			return null;
		}

    };
}