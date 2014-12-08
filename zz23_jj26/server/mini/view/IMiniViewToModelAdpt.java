package zz23_jj26.server.mini.view;

/**
 * An adapter from the chatroom's miniview to its minimodel
 * 
 * @author austurela
 *
 */
public interface IMiniViewToModelAdpt {

	/**
	 * Tell the model you're quitting 
	 */
	public void quit();


	/**
	 * Send the model some text
	 * @param text - text to send
	 */
	public void sendText(String text);


	/**
	 * Tell the model that you pressed the smilie button
	 */
	public void sendSmilie();


	public void sendStartGame();
}
