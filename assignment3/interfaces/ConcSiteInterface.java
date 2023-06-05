package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type ConcentrationSite.
 *
 *     It provides the functionality to access the Concentration Site.
 */
public interface ConcSiteInterface extends Remote
{

    /** 
    *   New state of the assault party. True if assigned, false otherwise.
    *
    *       @param assaultPartyID   assault party id
    *       @param state            status of the assault party
    *       @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */
    public void setAssigedAPid(int assaultPartyID, Boolean state)  throws RemoteException;

    /**
     *  New room assigned to the assault party.
     * 
     *      @param assaultPartyID   assault party id
     *      @param roomID           assigned room id 
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setAssignedRoomID(int assaultPartyID, int roomID)  throws RemoteException;

     /**
     *  Update room status. True if empty.
     * 
     *      @param roomID   room identification.
     *      @param roomState    room status.
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setRoomStates(int roomID, Boolean roomState) throws RemoteException;

    /** 
     *  Operation start operations.
     *  
     *  It is called by the Master Thief when he wants to start the heist operation in the museum.
     * 
     *      @return state of the master thief
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    public int startOperations () throws RemoteException;

    /**
     *  Operation appraise situation.
     * 
     *  As long as the master thief remains in this transition -> state : DECIDING_WHAT_TO_DO
     * 
     *      @return  master next transition or master thief state 
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public ReturnInt appraiseSit() throws RemoteException;

    /**
     *  Operation wait for thieves. 
     *  
     *  Master blocks until enough ordinary thieves have arrived the concentration site.
     * 
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void waitForThieves() throws RemoteException;


    /**
     *  Operation Am I Needed.
     *   
     *  When thieves are needed they are added to the waiting queue and notify the master. (waitForThieves())
     *  
     *      @param  thiefID ordinary thief id
     *      @return true if the heist is not finished and the ordinary thieves are still needed and OT state
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public ReturnBoolean amINeeded(int thiefID) throws RemoteException;

    /**
     *  Master creates a 3-member assault party
     *      
     *      @return assault party with 3 thieves
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public ReturnArrayInt buildParty() throws RemoteException;


    /**
     *  TOperation Prepare Assault Party.
     * 
     *      @param assaultParty     list of ids of the 3 elements of the assault party   
     *      @param assaultPartyID   identification assigned to the assault party
     *      @param roomID           room id assigned to the assault party
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void prepareAssaultParty(int[] assaultParty, int assaultPartyID, int roomID) throws RemoteException;

    /**
     *  Transition send Assault Party in the life cycle of the Master Thief.
     * 
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void sendAssaultParty() throws RemoteException;


    /**
     *  Transition prepare Excursion in the life cycle of the Ordinary Thief.
     *  CONCENTRATION_SITE -> CRAWLING_INWARDS
     * 
     *  The thief notifies the master that the last member of the group has arrived.
     * 
     *      @param thiefID  ordinary thief identification
     *      @return OT assault party id and OT state
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public ReturnInt prepareExcursion(int thiefID) throws RemoteException;

    /**
     *  Function that checks if assault parties have already been created. 
     *  If not, returns an id of the group that has not yet been created.
     * 
     *      @return assault party ID or -1 if groups have already been created.
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public int assignAssaultPartyID()  throws RemoteException;

    /**
     *  Get a room that has not yet been assigned and still has rooms.
     * 
     *      @return  room ID or -1 if all used or empty
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public int assignRoomID()  throws RemoteException;

    /**
     * Sum up Results of the Heist.
     * 
     *      @param collectedCanvas number of canvas collected
     *      @return  collected canvas
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     */
    public int sumUpResults(int collectedCanvas) throws RemoteException;

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
