package provided.datapacket;


import provided.extvisitor.*;


/**
 * Abstract data packet that defines the use of a Class object as the index type.  
 * The type of data held by the data packet defines its type and thus what case it
 * calls on its processing visitors.
 * 
 * @author Stephen Wong (c) 2010
 * * ----------------------------------------------
 * Specifies use of Class<?> type for host ID
 */
public abstract class ADataPacket extends AExtVisitorHost<Class<?>, ADataPacket> {
	
	/**
	 * Version number for serialization
	 */
	private static final long serialVersionUID = 8005386928491056679L;


	/**
	 * Constructor for this abstract superclass
	 * @param c A Class object to be used as the index value defining this type of data packet.
	 */
	public ADataPacket(Class<?> c){
		super(c);

	}
	
}


