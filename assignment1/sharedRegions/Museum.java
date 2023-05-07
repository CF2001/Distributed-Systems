package sharedRegions;

import main.*;
import entities.*;
import java.util.*;

public class Museum {
   /**
    *  Array of rooms ID in the museum.
    */
   private ArrayList <Integer> rooms_Paitings;
   
   /**
   *   Reference to the general repository.
   */
   private final GeneralRepository repos;

   /**
   *  Museum instantiation.
   *
   *    @param repos reference to the general repository
   */
   public Museum(GeneralRepository repos)
   {
      this.repos = repos;

      rooms_Paitings = new ArrayList<>(SimulPar.NUM_ROOMS); 
      for (int i = 0; i < SimulPar.NUM_ROOMS; i++)
      {
         rooms_Paitings.add(SimulPar.generateRandom(SimulPar.MAX_NUM_PAITINGS, SimulPar.MIN_NUM_PATTINGS));
         repos.setMRoom_NPaintings( i, rooms_Paitings.get(i)); // update general Repository  
      }
   }

   /**
    * Transition roll a canvas in the life cycle of the Ordinary Thief.
   *  (AT_A_ROOM -> AT_A_ROOM) 
    *
    * @param roomID  room id
    */
   public synchronized void rollACanvas(int roomID)
   {
      OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
      ordinaryThief.setOTState(OrdinaryThiefStates.AT_A_ROOM);

      // update general repository  -- info state ordinary Thief 
      repos.setOTState(ordinaryThief.getThiefID(), ordinaryThief.getOTState());

      int currentPaintings = rooms_Paitings.get(roomID);
      int hasACanvas = 0;   

      if (currentPaintings > 0)
      {
         rooms_Paitings.set(roomID, currentPaintings-1);
         hasACanvas = 1;  
      }
      ordinaryThief.setAParty_hasCanvas(hasACanvas);

      // update general repository  -- info number of paintings in the roomID
      repos.setMRoom_NPaintings(roomID, rooms_Paitings.get(roomID));

   }

   public ArrayList <Integer> nPaintingsRooms()
   {
      return rooms_Paitings;
   }
}
