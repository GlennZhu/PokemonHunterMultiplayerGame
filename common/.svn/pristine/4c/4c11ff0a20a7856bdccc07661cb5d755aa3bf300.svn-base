package common.chatroom;

import java.io.Serializable;

/**
 * Interface for identifiers for chatroomID, an object holding the identifiers
 * for chatrooms.
 *
 * @author Derek Peirce, Jake Kornblau
 *
 */
public interface IChatroomID extends Serializable {

	/**
	 * Returns the human-readable name for the chatroom as specified by the
	 * creating user.
	 *
	 * @return The human-readable name for the chatroom as specified by the
	 *         creating user.
	 */
	public String toString();

	/**
	 * Compares two IChatroomIDs for equality. Two IChatroomIDs can only be
	 * equal if they are the same class, with further equality checking
	 * depending on that class implementation. It must be the case that
	 * deserializing the same IChatroomID twice gives two equal IChatroomIDs,
	 * and that two IChatroomIDs that are each created by new are not equal.
	 * @return true if other is equal to this, false otherwise
	 */
	public boolean equals(Object other);

	/**
	 * Generates a hash code corresponding to equals()
	 * @return hash code
	 */
	public int hashCode();
}