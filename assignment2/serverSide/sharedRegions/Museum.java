package serverSide.sharedRegions;

import serverSide.entities.MuseumClientProxy;
import serverSide.main.*;
import clientSide.main.*;
import clientSide.stubs.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;
import java.util.*;

/**
 *   Museum.
 *
 *    Is implemented as an implicit monitor.
 *    All public methods are executed in mutual exclusion.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class Museum 
{
    /**
    *  Array of rooms ID in the museum.
    */
    private ArrayList <Integer> rooms_Paintings;

    /**
     *   Reference to the stub of the general repository.
     */
    private final GeneralRepositoryStub reposStub;

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     *  Instantiation of a concentration site object.
     *
     *      @param reposStub reference to the stub of the concentration site 
     */
    public Museum (GeneralRepositoryStub reposStub)
    {
        nEntities = 0;
        this.reposStub = reposStub;
    }

    /**
     *  Initializing the number of paintings in the museum rooms.
     * 
     *      @param nPaintingsArray  number of paintings in the rooms of the museum
     */
    public synchronized void initInfoMuseum(int [] nPaintingsArray)
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
    */
    public synchronized void rollACanvas(int roomID)
    {
      MuseumClientProxy ordinaryThief = (MuseumClientProxy) Thread.currentThread();

      ordinaryThief.setOTState(OrdinaryThiefStates.AT_A_ROOM);

      // update general repository  -- info state ordinary Thief 
      reposStub.setOTState(ordinaryThief.getThiefID(), ordinaryThief.getOTState());

      int currentPaintings = rooms_Paintings.get(roomID);
      int hasACanvas = 0;   

      if (currentPaintings > 0)
      {
        rooms_Paintings.set(roomID, currentPaintings-1);
         hasACanvas = 1;  
      }
      ordinaryThief.setAParty_hasCanvas(hasACanvas);
    //   System.out.println("Museum: Thief ID = " + ordinaryThief.getThiefID() + " CV: " + ordinaryThief.getAParty_hasCanvas() + " \n");

      // update general repository  -- info number of paintings in the roomID
      reposStub.setMRoom_NPaintings(roomID, rooms_Paintings.get(roomID));
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
     */
    public synchronized void shutdown ()
    {
        nEntities += 1;
        if (nEntities >= SimulPar.E_MUSEUM)
            ServerHeistMuseumMuseum.waitConnection = false;
    }
}
