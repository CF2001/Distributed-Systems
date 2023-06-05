package serverSide.objects;

import java.rmi.registry.*;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import genclass.GenericIO;
import java.util.*;

/**
 *  Assault Party.
 *
 *    Is implemented as an implicit monitor.
 *    All public methods are executed in mutual exclusion.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class AssaultParty implements AssaultPartyInterface
{
    /**
     *  Array of ordinary thieves members to the assault party.
     */
    private ArrayList <Integer> assaultP_Thieves;

    /**
     *  Assault Party id.
     */
    private int assaultParty_ID;

    /**
     *  Identification of the room assigned to the Assault Party.
     */
    private int assaultP_roomID; 

    /**
    *  Array distance from outside gathering site of a Room in the museum.
    */
    private ArrayList <Integer> rooms_distFromOutside;

    /** 
     * List of thieves that are Currently Crawling Out
     */
    private ArrayList <Integer> crawlingThieves;

    /** 
     * Shared Array , to know if a certain thief can move or not
     */
    private Boolean [] myTurnToMove;

    /** 
     * Counter for thieves that Serves to reset Variables
     */
    private int crawlingOutReady;

    /** 
     * boolean to check if movement is crawling out or not
     */
    private Boolean moveCrawlOut;

    /** 
     * Counter for thieves that have Reversed Direction 
     */
    private int goingToCrawlOut;

    /**
     *  Number of thieves that reached the room 
     */
    private int inRoom;

    /**
     *  Number of thieves that reached the collection site
     */
    private int inCollectionSite;
    
    /**
     *   Reference to the stub of the general repository.
     */
    private final GeneralReposInterface reposStub;

    /**
     *   Number of entity groups requesting the shutdown for assault party.
     */
    private int nEntities;


    /**
     *  Instantiation of a Assault Party object.
     *
     *      @param reposStub reference to the stub of the assault party
     */
    public AssaultParty (GeneralReposInterface reposStub)
    {
        assaultP_Thieves = new ArrayList<>(SimulPar.K);
        myTurnToMove = new Boolean[SimulPar.K];
        crawlingThieves = new ArrayList<>(SimulPar.K);
        for (int i = 0; i < SimulPar.K; i++)
        {
            assaultP_Thieves.add(-1);
            myTurnToMove[i] = false;
            crawlingThieves.add(0);
        }


        this.assaultP_roomID = -1;

        this.moveCrawlOut = false;
        this.crawlingOutReady = 0;
        this.goingToCrawlOut = 0;

        inRoom = 0;
        inCollectionSite = 0;

        this.nEntities = 0;
        this.reposStub = reposStub;
    }

    /**
     *   Get Assault Party id.
     * 
     *     @return Assault Party id
     */
    public synchronized int getAssaultParty_ID() 
    {
        return assaultParty_ID;
    }

    /**
     *   Set Assault Party id.
     * 
     *     @param assaultParty_ID New Assault Party id
     */
    public synchronized void setAssaultParty_ID(int assaultParty_ID) 
    {
        this.assaultParty_ID = assaultParty_ID;
    }

    /**
     *   Get Assault Party Room id.
     * 
     *      @return Assault Party Room id
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                              service fails
     */
    @Override
    public synchronized int getAssaultP_roomID() throws RemoteException
    {
        return assaultP_roomID;
    }

    /**
     *   Set Assault Party Room id.
     * 
     *      @param assaultP_roomID New Assault Party Room id
     */
    public synchronized void setAssaultP_roomID(int assaultP_roomID) 
    {
        this.assaultP_roomID = assaultP_roomID;
    }


    /**
     *  Getting distance from outiside of a room.
     * 
     *      @return distance from outiside of a room.
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized int getDistFromOutside() throws RemoteException
    {
        return rooms_distFromOutside.get(assaultP_roomID);
    }

    /**
     *  Array list of the assault party
     * 
     *   @return  assault party list
     */
    public synchronized ArrayList <Integer> getAssaultP_Thieves()
    {
        return assaultP_Thieves; 
    }


    /**
     *  Initialize the assault party identification
     *      
     *      @param assaultParty_ID  assault party identification.
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
     */
    @Override
    public synchronized void initAssaultPartyID(int assaultParty_ID) throws RemoteException
    {
        this.assaultParty_ID = assaultParty_ID;
    }

    /**
     *  Initialization of the assault party id and the distance from the rooms to outside.
     *  
     *      @param distFromOutside  distances of the rooms from outside 
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void initAssaultPartyDist(int [] distFromOutside) throws RemoteException
    {
        // this.assaultParty_ID = assaultParty_ID;

        rooms_distFromOutside = new ArrayList<>(SimulPar.NUM_ROOMS);
        for (int i = 0; i < SimulPar.NUM_ROOMS; i++)
        {
            rooms_distFromOutside.add(distFromOutside[i]);
            reposStub.setMRoom_DT_outsideG(i, rooms_distFromOutside.get(i));
        }
    }

    /**
     *  Indicates whether the first member of the party has been notified by the master 
     *  on sendAssaultParty to proceed.
     * 
     *      @param firstPartyMember movement status of the first member of the assault party
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void moveFirstPartyMember(Boolean firstPartyMember) throws RemoteException
    {
        myTurnToMove[0] = firstPartyMember;
    }

    /**
     *  Add the thieves chosen by the master to the created assault party.
     * 
     *      @param assaultParty     set of thieves to be added to the assault party
     *      @param roomID       identification of the room assigned to the assault party   
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails     
     */
    @Override
    public synchronized void addThievesToParty(int [] assaultParty, int roomID) throws RemoteException
    {
        //System.out.print("Assault party: " + assaultParty_ID);
        for (int i = 0; i < SimulPar.K; i++)
        {
            assaultP_Thieves.set(i, assaultParty[i]); 
            //System.out.print(assaultP_Thieves.get(i) + " ");
        }
        //System.out.println("\nRoom ID: " + roomID + " \n");
        assaultP_roomID = roomID;
    }

    /**
     *  Reset the values of the created assault party and the room associated with it.
     * 
     *  @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails   
     */
    @Override
    public synchronized void resetAssaultParty() throws RemoteException
    {
        assaultP_roomID = -1;

        for (int i = 0; i < SimulPar.K; i++)
        {
            assaultP_Thieves.set(i, -1); 
        }

        // update general repository    -- info Assault Party 
        for (int i = 0; i < SimulPar.K; i++)
        {
            // Assault Party #, Elem #, Id # , Serves to remove the thief from the assault party (-1)
            reposStub.setAParty_Elem_ID(assaultParty_ID, i, -1);
        }
        //update general repository    -- info Room ID of the assault party
        reposStub.setAssaultParty_RId(assaultParty_ID, -1);
    }

    /**
     *  Transition crawling inwards in the life cycle of the Ordinary Thief.
     *  (CRAWLING_INWARDS -> AT_A_ROOM)
     * 
     *      @param thiefID      thief id
     *      @param thiefMaxDisp thief maximum displacemente
     *      @param AParty_position    thief assault party position
     *      @param AParty_ID          thief assault party id 
     *      @return new thief position
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized int crawlIN(int thiefID, int thiefMaxDisp, int AParty_position, int AParty_ID) throws RemoteException
    {
        //int thiefPosition = 0;
        
        while(!myTurnToMove[getThiefIndex(thiefID)])
        {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        int thiefIndex = getThiefIndex(thiefID); // id do thief atual 
        int currentPos  = AParty_position;
        int maxDisplacement = thiefMaxDisp;
    
        //this block of code is used to determine the maximum displacement of the thief Starting
        //from maximum displacement, it will count down until it finds a valid position

        int newPosition = 0;
        boolean validPosition = true;

        for (int i = maxDisplacement; i > 0; i--)
        {
            validPosition = true;
            newPosition =  currentPos + i;

            //create sorted array of crawling thieves
            ArrayList<Integer> sortedCrawlingThieves = new ArrayList<Integer>(crawlingThieves.size());

            //copy all elements to the new array
            for (int j = 0; j < SimulPar.K; j++)
            {
                sortedCrawlingThieves.add(crawlingThieves.get(j));
            }            
            sortedCrawlingThieves.set(thiefIndex,newPosition);
            Collections.sort(sortedCrawlingThieves, Collections.reverseOrder());

        //iterate and subtract each element from the next one
            for (int j = 0; j < SimulPar.K - 1; j++)
            { 
                int distance = sortedCrawlingThieves.get(j) - sortedCrawlingThieves.get(j+1);                
                if (distance > 3)
                {
                    validPosition = false;
                    break;
                }

                //Distance can be 0 if its the room or the starting position
                if ((distance==0) && (sortedCrawlingThieves.get(j+1) != getDistFromOutside()) && (sortedCrawlingThieves.get(j+1) != 0))
                {
                    validPosition = false;
                    break;
                }
            }

            if (validPosition){
                break;
            }
        }
        //safety check - if the currentThief cant move
        if (validPosition == false)
        {
            newPosition = currentPos;
            validPosition = true;
        }

        int nextThiefIndex = 0;
        if(validPosition)
        {
            if (newPosition > getDistFromOutside())
            {
                newPosition = getDistFromOutside(); // reached the room
            }

            if (newPosition == getDistFromOutside())
            {
                inRoom++;   // number of thieves in the room 
            } 

            crawlingThieves.set(thiefIndex, newPosition);
            //ordinaryThief.setAParty_position(newPosition);
            //thiefPosition = newPosition; 
            //reposStub.setAParty_Elem_POS(ordinaryThief.getAParty_ID(), thiefIndex, ordinaryThief.getAParty_position()); 
            reposStub.setAParty_Elem_POS(AParty_ID, thiefIndex, newPosition); 

            // Predict Next To Move 

            if (inRoom == 2 && newPosition != getDistFromOutside())
            {
                nextThiefIndex = thiefIndex;
                myTurnToMove[nextThiefIndex] = true;    // wake up next to move
            }else{
                if (inRoom == 3)    // If everyone is in the room and there is no longer nextToMove
                {
                    myTurnToMove[thiefIndex] = false;
                    inRoom = 0;
                }else{
                    //create sorted array of crawling thieves
                    ArrayList<Integer> sortedCrawlingThieves = new ArrayList<Integer>(crawlingThieves.size());
                    //copy all elements to the new array
                    for (int j = 0; j < SimulPar.K; j++)
                    {
                        sortedCrawlingThieves.add(crawlingThieves.get(j));
                    }            
                    Collections.sort(sortedCrawlingThieves, Collections.reverseOrder());

                    int maxPosition = sortedCrawlingThieves.get(0);
                    int middlePosition = sortedCrawlingThieves.get(1);
                    int minPosition = sortedCrawlingThieves.get(2);
                    // when 2 thieves are in the room and the minPosition needs to be the next
                    if (newPosition == getDistFromOutside() && maxPosition == getDistFromOutside() && middlePosition == getDistFromOutside())
                    {
                        nextThiefIndex = getThiefIndex_position(minPosition);
                        
                    }else{
                        if (newPosition == maxPosition) // if the currentThief is the first in line
                        {   
                            // find the Thief index with middlePosition in the crawlingThieves 
                            nextThiefIndex = getThiefIndex_position(middlePosition);
                        
                        }else if (newPosition == middlePosition)    // if the currentThief is in the middle 
                        {
                            // find the Thief index with minPosition in the crawlingThieves 
                            nextThiefIndex = getThiefIndex_position(minPosition);
                        
                        }else if (newPosition == minPosition){  // if the currentThief is the last in line

                            int position = maxPosition; // find the Thief index with maxPosition (first in line) in the crawlingThieves

                            // if the first thief is already in the room, so the max becomes the middle
                            if (maxPosition == getDistFromOutside())   
                            {
                                position = middlePosition;
                            }
                            nextThiefIndex = getThiefIndex_position(position);
                        }
                    }
                    myTurnToMove[nextThiefIndex] = true;    // wake up next to move
                    myTurnToMove[thiefIndex] = false;   // blocks if he can not generate a new increment of position 
                }
            }
        }
        //After moving and predicting the next to move, the thief will notify the AssaultParty
        notifyAll();

        return newPosition;
    }

    /**
     *  Transition crawling inwards in the life cycle of the Ordinary Thief.
     * 
     *      @param thiefID      thief id
     *      @param AParty_hasCanvas true if ordinary thief has a canvas     
     *      @return ordinary thief state
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized int reverseDirection(int thiefID, int AParty_hasCanvas) throws RemoteException
    {
        int ordinaryThiefState;

        ordinaryThiefState = OrdinaryThiefStates.CRAWLING_OUTWARDS;

        // update general repository  -- info hasACanvas of the ordinary Thief
        reposStub.setAParty_Elem_CV(assaultParty_ID, getThiefIndex(thiefID) , AParty_hasCanvas); 

        // update general repository  -- info state of the ordinary Thief
        reposStub.setOTState(thiefID, ordinaryThiefState);
        
        goingToCrawlOut++;

        while((goingToCrawlOut != 3) && (moveCrawlOut == false))
        {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        
        if ((goingToCrawlOut == 3) && (moveCrawlOut == false))  // so entra a ultima thread a chegar 
        {
            moveFirstPartyMember(true);
            moveCrawlOut = true;
            notifyAll();
        }

        crawlingOutReady++;
        if (crawlingOutReady == 3)
        {
            goingToCrawlOut = 0;
            crawlingOutReady = 0;
            moveCrawlOut = false;
        }

        return ordinaryThiefState;
    }


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
    @Override
    public synchronized int crawlOUT(int thiefID, int thiefMaxDisp, int AParty_position, int AParty_ID) throws RemoteException
    {
        while(!myTurnToMove[getThiefIndex(thiefID)])
        {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        int thiefIndex = getThiefIndex(thiefID); // id do thief atual 
        int currentPos  = AParty_position;
        int maxDisplacement = thiefMaxDisp;

        // determinar a distancia maxima ate o thief bloquear 
        int newPosition = 0;
        boolean validPosition = true;
        for (int i = maxDisplacement; i > 0; i--)
        {
            validPosition = true;
            newPosition =  currentPos - i;

            //create sorted array of crawling thieves
            ArrayList<Integer> sortedCrawlingThieves = new ArrayList<Integer>(crawlingThieves.size());

            //copy all elements to the new array
            for (int j = 0; j < SimulPar.K; j++)
            {
                sortedCrawlingThieves.add(crawlingThieves.get(j));
            }            
            sortedCrawlingThieves.set(thiefIndex,newPosition);
            Collections.sort(sortedCrawlingThieves);

        //iterate and subtract each element from the next one
            for (int j = 0; j < SimulPar.K - 1; j++)
            { 
                int distance = sortedCrawlingThieves.get(j) - sortedCrawlingThieves.get(j+1);

                //distance is absolute value
                if (distance < 0)
                    distance = distance * -1;

                if (distance > 3)
                {
                    validPosition = false;
                    break;
                }

                if ((distance==0) && (sortedCrawlingThieves.get(j+1) != getDistFromOutside()) && (sortedCrawlingThieves.get(j+1) != 0))
                {
                    validPosition = false;
                    break;
                }
            }
            
            if (validPosition)
            break;
        }

        if (validPosition == false)
        {
            newPosition = currentPos;
            validPosition = true;
        }

        int nextThiefIndex = 0;
        if(validPosition)
        {
            if(newPosition<0)
            {
                newPosition = 0;
            }

            if (newPosition == 0)
            {
                inCollectionSite++;
            }

            // update newPosition
            crawlingThieves.set(thiefIndex, newPosition);
            //ordinaryThief.setAParty_position(newPosition);

            reposStub.setAParty_Elem_POS(AParty_ID, thiefIndex, newPosition); 

            if (inCollectionSite == 2 && crawlingThieves.get(thiefIndex) != 0)    // just one element is active in the crawling thieves
            {
                nextThiefIndex = thiefIndex;
                myTurnToMove[nextThiefIndex] = true;    // wake up next to move
            }else {    
                if (inCollectionSite == 3)    // they are all at the collection site and there is no nextToMove 
                {
                    myTurnToMove[thiefIndex] = false;
                    inCollectionSite = 0;
                }else{
                    //create sorted array of crawling thieves
                    ArrayList<Integer> sortedCrawlingThieves = new ArrayList<Integer>(crawlingThieves.size());
                    //copy all elements to the new array
                    for (int j = 0; j < SimulPar.K; j++)
                    {
                        sortedCrawlingThieves.add(crawlingThieves.get(j));
                    }            
                    Collections.sort(sortedCrawlingThieves);

                    int minPosition = sortedCrawlingThieves.get(0); // first in line
                    int middlePosition = sortedCrawlingThieves.get(1);
                    int maxPosition = sortedCrawlingThieves.get(2); // last in line

                    // when 2 thieves are in the collection site and the maxPosition needs to be the next
                    if (newPosition == 0 && minPosition == 0 && middlePosition == 0)
                    {
                        nextThiefIndex = getThiefIndex_position(maxPosition); 
                    }else{
                        if (newPosition == minPosition) // if the currentThief is the first in line
                        {   
                            // find the Thief index with middlePosition in the crawlingThieves 
                            nextThiefIndex = getThiefIndex_position(middlePosition);
                        
                        }else if (newPosition == middlePosition)    // if the currentThief is in the middle 
                        {
                            // find the Thief index with maxPosition in the crawlingThieves 
                            nextThiefIndex = getThiefIndex_position(maxPosition);
                        
                        }else if (newPosition == maxPosition){  // if the currentThief is the last in line

                            int position = minPosition; // find the Thief index with minPosition (first in line) in the crawlingThieves

                            // if the first thief is already in the collection site, so the min becomes the middle
                            if (minPosition == 0)   
                            {
                                position = middlePosition;
                            }
                            nextThiefIndex = getThiefIndex_position(position);
                        }
                    }
                    myTurnToMove[nextThiefIndex] = true;    // wake up next to move
                    myTurnToMove[thiefIndex] = false;   // blocks if he can not generate a new increment of position 
                }
            }
            notifyAll();
        }
        return newPosition;
    }


    /**
     *  Get the assault party thief index.
     * 
     *      @param thiefID ordinary thief identification
     *      @return  index of array in the assault party that the thief belongs to
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized int getThiefIndex(int thiefID)  throws RemoteException
    {
        for (int i = 0; i < SimulPar.K; i++)
        {
            if (assaultP_Thieves.get(i) == thiefID)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     *  Gets the index of the position array.
     * 
     *      @param position position
     *      @return  index of the position array.
     */
    public synchronized int getThiefIndex_position(int position)
    {
        for (int i = 0; i < SimulPar.K; i++)
        {
            if (crawlingThieves.get(i) == position)
            {
                return i;
            }
        }
        return -1;
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
        // System.out.println("AssaultParty ID : " + assaultParty_ID + " nEntities: " + nEntities);
        if (nEntities >= SimulPar.E_ASSAULTP)
        {
            if (assaultParty_ID == 0)
                ServerHeistMuseumAssaultP0.shutdown();
            else
                ServerHeistMuseumAssaultP1.shutdown();
        }
    } 

}