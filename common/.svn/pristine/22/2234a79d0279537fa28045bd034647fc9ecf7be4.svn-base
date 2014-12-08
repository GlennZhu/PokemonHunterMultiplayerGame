package common.message;

import common.message.chat.IChatMessage;
import common.message.connect.IConnectMessage;

/**
 * A message to be sent in the case of an exception when processing a message.
 * 
 * Expected usage is that whenever you process a message, you should have a
 * try-catch and you return an IErrorMessage with the exception you caught.
 * 
 * When processing returned messages, you should check for an IErrorMessage and
 * respond accordingly.
 * 
 * @author Wilson Beebe
 *
 */
public interface IErrorMessage extends IChatMessage, IConnectMessage {

	/**
	 * Gets the cause of the error message
	 * 
	 * @return the Throwable which contains the error
	 */
	Throwable getCause();

}
