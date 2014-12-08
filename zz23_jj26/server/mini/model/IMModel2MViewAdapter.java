package zz23_jj26.server.mini.model;

import java.awt.Component;

/**
 * Adapter from a chatroom minimodel to its miniview
 * @author austurela
 *
 */
public interface IMModel2MViewAdapter{
	/**
	 * Print text to the miniview
	 * @param text - the text to print
	 */
	public void printToView(String text);

	public void addComponent(Component component);
}
