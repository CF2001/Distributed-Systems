package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type Museum.
 *
 *     It provides the functionality to access the Museum.
 */
public interface MuseumInterface extends Remote
{
    /**
     *  Initializing the number of paintings in the museum rooms.
     * 
     *      @param nPaintingsArray  number of paintings in the rooms of the museum
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void initInfoMuseum (int [] nPaintingsArray) throws RemoteException; 

    /**
    * Transition roll a canvas in the life cycle of the Ordinary Thief.
    *  (AT_A_ROOM -> AT_A_ROOM) 
    *
    *       @param roomID  room id
    *       @param thiefID  ordinary thief identification
    *        @return true if has a canvas and ordinary thied state 
    *       @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */
    public ReturnInt rollACanvas(int roomID, int thiefID) throws RemoteException;

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
