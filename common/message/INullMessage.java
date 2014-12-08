package common.message;

import common.message.chat.IChatMessage;
import common.message.connect.IConnectMessage;

/**
 * Interface for NullMessage message, which does not need a response.
 *
 * @author Wilson Beebe, Jake Kornblau, Derek Peirce
 */
public interface INullMessage extends IChatMessage, IConnectMessage {

}
