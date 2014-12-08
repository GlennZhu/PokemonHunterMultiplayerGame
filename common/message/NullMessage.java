package common.message;


/**
 * Implementation for NullMessage message, which does not need a response.
 *
 * @author Wilson Beebe, Jake Kornblau, Derek Peirce
 */
public final class NullMessage implements INullMessage {
    /** Auto-generated serial UID */
    private static final long serialVersionUID = -8400347418366904973L;

    /** Singleton instance of a null message (to avoid excess classes) */
    public static final NullMessage SINGLETON = new NullMessage();

    /** Private constructor for singleton NullMessage */
    private NullMessage() {
    }

}