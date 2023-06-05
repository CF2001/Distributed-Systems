package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type ControlCollectionSite.
 *
 *     It provides the functionality to access the Control and Collection Site.
 */
public interface ControlCollecSiteInterface extends Remote
{
    /**
     *  Operation Take A Rest.
     * 
     *      @return new master thief state
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public int takeARest() throws RemoteException;

     /**
     *  Transition hand a canvas in the life cycle of the ordinary Thief.
     *  (COLLECTION_SITE -> COLLECTION_SITE)
     * 
     *  The thief hands the master the paintings he stole.
     *  The ordinary thief is waken up by the operation of the master thief: collectACanvas.
     * 
     *      @param roomID               room id
     *      @param currentThiefIndex    current thief index
     *      @param thiefID  ordinary thief identification
     *      @param AParty_ID    ordinary thief assault party id
     *      @param AParty_hasCanvas true if ordinary thief has a canvas
     *      @return true if ordinary thief has a canvas
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public int handACanvas(int roomID, int currentThiefIndex, int thiefID, int AParty_ID, int AParty_hasCanvas) throws RemoteException;

    /**
     *  Operation collect a canvas.
     * 
     *      @return  assault parties for reseting and collectedCanvas
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public ReturnArrayBoolean collectACanvas() throws RemoteException;

     /**
     *   Operation server shutdown.
     *
     *   New operation.
     *
     *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void shutdown () throws RemoteException;
}
