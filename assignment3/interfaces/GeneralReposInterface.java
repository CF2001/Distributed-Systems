package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type ConcentrationSite.
 *
 *     It provides the functionality to access the Concentration Site.
 */
public interface GeneralReposInterface extends Remote
{
    /**
     *   Operation initialization of simulation.
     *
     *   New operation.
     *
     *     @param logFileName name of the logging file
     *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void initSimul (String logFileName) throws RemoteException;

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     *
     *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void shutdown () throws RemoteException; 

    /**
     * Write in the logging file the stolen paitings.
     * 
     *      @param stolenPaitings number of stolen paintings
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setStolenPaitings(int stolenPaitings)  throws RemoteException; 

    /**
     * Write in the logging file the state of the master Thief.
     * 
     *      @param masterThiefState  master thief state
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setMasterThiefState(int masterThiefState) throws RemoteException; 

    /**
     *  Write in the logging file the state of the ordinary Thief.
     * 
     *      @param thiefID              ordinary thief id
     *      @param ordinaryThiefState    ordinary thief state
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setOTState(int thiefID, int ordinaryThiefState) throws RemoteException; 

    /**
     * Write in the logging file the situation of the ordinary Thief.
     * 
     *      @param thiefID   ordinary thief id
     *      @param ordinaryThiefSituation    ordinary thief situation
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setOTSituation(int thiefID, String ordinaryThiefSituation) throws RemoteException;

    /**
     *  Write in the logging file the max displacement of the ordinary Thief.
     *
     *      @param thiefID    ordinary thief id
     *      @param maxDisp    ordinary thief maximum displacement
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setOrdinartThief_MaxDisp (int thiefID, int maxDisp) throws RemoteException; 

    /**
     *  Write in the logging file the room id of an assault party.
     * 
     *      @param assaultParty_ID   assault party id
     *      @param roomID    room id
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setAssaultParty_RId(int assaultParty_ID, int roomID) throws RemoteException;

    /**
     *  Write in the logging file the id of an element in the assault party.
     * 
     *      @param assaultParty_ID   assault party id
     *      @param elemID    element id
     *      @param thiefID   thief id
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setAParty_Elem_ID(int assaultParty_ID, int elemID, int thiefID) throws RemoteException;

    /**
     * Set the assault party element position 
     * 
     *      @param assaultParty_ID    assault party id
     *      @param elemID    element id
     *      @param thiefPos  thief id
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setAParty_Elem_POS(int assaultParty_ID, int elemID, int thiefPos) throws RemoteException;

    /**
     * Set the assault party element canvas state  
     * 
     *      @param assaultParty_ID   assault party id
     *      @param elemID    element id
     *      @param thiefCV   thief id
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setAParty_Elem_CV(int assaultParty_ID, int elemID, int thiefCV) throws RemoteException;

    /**
     *  Set the number of paintings in a museum Room.
     * 
     *      @param roomID        room identification
     *      @param nPaintings    number of pautings
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setMRoom_NPaintings(int roomID, int nPaintings) throws RemoteException;

    /**
     *  Set the distance from outside gathering site of a museum Room.
     * 
     *      @param roomID            room identification
     *      @param distFromOutside   distance from outside
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public void setMRoom_DT_outsideG(int roomID, int distFromOutside) throws RemoteException;
}
