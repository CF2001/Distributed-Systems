package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type ConcentrationSite.
 *
 *     It provides the functionality to access the Concentration Site.
 */
public interface AssaultPartyInterface extends Remote
{

    /**
     *  Initialize the assault party identification
     *      
     *      @param assaultParty_ID  assault party identification.
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void initAssaultPartyID (int assaultParty_ID) throws RemoteException;

    /**
     *  Initialization of the assault party id and the distance from the rooms to outside.
     *  
     *      @param distFromOutside  distances of the rooms from outside 
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void initAssaultPartyDist (int [] distFromOutside) throws RemoteException;

    /**
     *  Getting distance from outiside of a room.
     * 
     *      @return distance from outiside of a room.
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public int getDistFromOutside() throws RemoteException;

    /**
     *   Get Assault Party Room id.
     * 
     *      @return Assault Party Room id
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                              service fails
     */
    public int getAssaultP_roomID() throws RemoteException;

    /**
     *  Indicates whether the first member of the party has been notified by the master 
     *  on sendAssaultParty to proceed.
     * 
     *      @param firstPartyMember movement status of the first member of the assault party
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void moveFirstPartyMember(Boolean firstPartyMember) throws RemoteException;

    /**
     *  Add the thieves chosen by the master to the created assault party.
     * 
     *      @param assaultParty     set of thieves to be added to the assault party
     *      @param roomID       identification of the room assigned to the assault party   
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails     
     */
    public void addThievesToParty(int [] assaultParty, int roomID) throws RemoteException;


    /**
     *  Reset the values of the created assault party and the room associated with it.
     * 
     *  @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails   
     */
    public void resetAssaultParty() throws RemoteException;


    /**
     *  Transition crawling inwards in the life cycle of the Ordinary Thief.
     *  (CRAWLING_INWARDS -> AT_A_ROOM)
     * 
     *      @param thiefID      thief id
     *      @param thiefMaxDisp thief maximum displacemente
     *      @param AParty_position    thief assault party position
     *      @param AParty_ID          thief assault party id 
     *      @return             new thief position
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public int crawlIN(int thiefID, int thiefMaxDisp, int AParty_position, int AParty_ID) throws RemoteException;


    /**
     *  Transition crawling inwards in the life cycle of the Ordinary Thief.
     * 
     *      @param thiefID      thief id
     *      @param AParty_hasCanvas true if ordinary thief has a canvas
     *      @return ordinary thief state 
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public int reverseDirection(int thiefID, int AParty_hasCanvas) throws RemoteException;

    /**
     *  Transition crawling outwards in the life cycle of the Ordinary Thief.
     *  (CRAWLING_OUTWARDS -> COLLECTION_SITE)
     * 
     *      @param thiefID      thief id
     *      @param thiefMaxDisp thief maximum displacemente
     *      @param AParty_position    thief assault party position
     *      @param AParty_ID          thief assault party id 
     *      @return new thief position
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public int crawlOUT(int thiefID, int thiefMaxDisp, int AParty_position, int AParty_ID) throws RemoteException;


    /**
     *  Get the assault party thief index.
     * 
     *      @param thiefID ordinary thief identification
     *      @return  index of array in the assault party that the thief belongs to
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public int getThiefIndex(int thiefID)  throws RemoteException;


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
