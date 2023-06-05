package serverSide.objects;

import java.rmi.registry.*;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import genclass.GenericIO;
import java.util.*;


/**
 *    Museum.
 *
 *    Is implemented as an implicit monitor.
 *    All public methods are executed in mutual exclusion.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class Museum implements MuseumInterface
{
    /**
    *  Array of rooms ID in the museum.
    */
    private ArrayList <Integer> rooms_Paintings;

    /**
     *   Reference to the stub of the general repository.
     */
    private final GeneralReposInterface reposStub;

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     *  Instantiation of a concentration site object.
     *
     *      @param reposStub reference to the stub of the concentration site 
     */
    public Museum(GeneralReposInterface reposStub)
    {
        nEntities = 0;
        this.reposStub = reposStub;
    }

    /**
     *  Initializing the number of paintings in the museum rooms.
     * 
     *      @param nPaintingsArray  number of paintings in the rooms of the museum
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void initInfoMuseum(int [] nPaintingsArray) throws RemoteException
    {
        rooms_Paintings = new ArrayList<>(SimulPar.NUM_ROOMS); 
        for (int i = 0; i < SimulPar.NUM_ROOMS; i++)
        {
            rooms_Paintings.add(nPaintingsArray[i]);
            reposStub.setMRoom_NPaintings( i, nPaintingsArray[i]); // update general Repository  
        }
    }

    /**
    * Transition roll a canvas in the life cycle of the Ordinary Thief.
    *  (AT_A_ROOM -> AT_A_ROOM) 
    *
    *       @param roomID  room id
    *       @param thiefID  ordinary thief identification
    *       @return true if has a canvas and ordinary thied state 
    *       @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */
    @Override
    public synchronized ReturnInt rollACanvas(int roomID, int thiefID) throws RemoteException
    {
        int ordinaryThiefState;

        ordinaryThiefState = OrdinaryThiefStates.AT_A_ROOM;

        // update general repository  -- info state ordinary Thief 
        reposStub.setOTState(thiefID, ordinaryThiefState);

        int currentPaintings = rooms_Paintings.get(roomID);
        int hasACanvas = 0;   

        if (currentPaintings > 0)
        {
            rooms_Paintings.set(roomID, currentPaintings-1);
            hasACanvas = 1;  
        } 

        // update general repository  -- info number of paintings in the roomID
        reposStub.setMRoom_NPaintings(roomID, rooms_Paintings.get(roomID));

        return new ReturnInt( hasACanvas, ordinaryThiefState );
   }

    /** 
     *  List of the number of paintings in each room of the museum.
     * 
     *      @return      List of the number of paintings in each room of the museum.
     */
    public ArrayList <Integer> nPaintingsRooms()
    {
        return rooms_Paintings;
    }

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     *      
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void shutdown () throws RemoteException
    {
        nEntities += 1;
        if (nEntities >= SimulPar.E_MUSEUM)
            ServerHeistMuseumMuseum.shutdown();
    }
}