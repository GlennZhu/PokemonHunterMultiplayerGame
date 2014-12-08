package common.message.chat;

/**
 * A basic IChatMessage for sending text. Should be responded to with an
 * INullMessage
 *
 * @author Jake Kornblau
 */
public interface ITextMessage extends IChatMessage {

    /**
     * Get the text stored by this message
     *
     * @return the message's text
     */
    public String getText();

}