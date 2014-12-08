package zz23_jj26.client.mini.model;

/**
 * An adapter from a mini chat room model to the main model that takes care of it
 * @author austurela
 *
 */
public interface IMiniModel2MainModelAdapter {
	/**
	 * Tell the main model that you're quitting your room
	 * @param roomToQuit - the room you're quitting
	 */
	public void quit(IChatroom roomToQuit);
}
