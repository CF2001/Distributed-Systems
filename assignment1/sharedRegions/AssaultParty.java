package sharedRegions;

import entities.*;
import main.*;
import java.util.*;

public class AssaultParty 
{    
    /**
    *  Array distance from outside gathering site of a Room in the museum.
    */
    private ArrayList <Integer> rooms_distFromOutside;

    /**
     *  Array of ordinary thieves members to the assault group.
     */
    private ArrayList <Integer> assaultP_Thieves;

    /** 
     * Shared Array , to know if a certain thief can move or not
     */
    private Boolean [] myTurnToMove;

    /** 
     * List of thieves that are Currently Crawling Out
     */
    private ArrayList <Integer> crawlingThieves;

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
     *  True if the assault group has already been created.
     */
    private Boolean assaultParty_Created;

    /**
     *  Assault Party id.
     */
    private int assaultParty_ID;

     /**
     *  Id of the room assigned to the Assault Party.
     */
    private int assaultP_roomID; 

    /**
     *  True if the assault party is in operation.
     */
    private Boolean assaultP_inAction;

    /**
     *  Number of thieves that reached the room 
     */
    private int inRoom;

    /**
     *  Number of thieves that reached the collection site
     */
    private int inCollectionSite;

    /**
     *   Reference to the general repository.
     */
    private final GeneralRepository repos;
        
    /**
     *  Assault Party instantiation.
     *
     *    @param repos reference to the general repository
     */


    public AssaultParty(GeneralRepository repos, int assaultParty_ID, int [] distFromOutside)
    {
        rooms_distFromOutside = new ArrayList<>(SimulPar.NUM_ROOMS);
        for (int i = 0; i < SimulPar.NUM_ROOMS; i++)
        {
            rooms_distFromOutside.add(distFromOutside[i]);
            repos.setMRoom_DT_outsideG(i, rooms_distFromOutside.get(i));
        } 
        assaultP_Thieves = new ArrayList<>(SimulPar.K);
        myTurnToMove = new Boolean[SimulPar.K];
        crawlingThieves = new ArrayList<>(SimulPar.K);

        
        this.assaultParty_Created = false;
        this.assaultParty_ID = assaultParty_ID;
        this.assaultP_roomID = -1;
        this.assaultP_inAction = false;

        this.moveCrawlOut = false;
        this.crawlingOutReady = 0;
        this.goingToCrawlOut = 0;

        this.repos = repos;

        inRoom = 0;
        inCollectionSite = 0;
        for (int i = 0; i < SimulPar.K; i++)
        {
            assaultP_Thieves.add(-1);
            myTurnToMove[i] = false;
            crawlingThieves.add(0);
        }
    }

    /**
     *   Get Assault Party id.
     *     @return Assault Party id
     */
    public int getAssaultParty_ID() 
    {
        return assaultParty_ID;
    }

    /**
     *   Set Assault Party id.
     *     @param AssaultParty_ID New Assault Party id
     */
    public void setAssaultParty_ID(int assaultParty_ID) 
    {
        this.assaultParty_ID = assaultParty_ID;
    }

    /**
     *   Get Assault Party Room id.
     *     @return Assault Party Room id
     */
    public int getAssaultP_roomID() 
    {
        return assaultP_roomID;
    }

    /**
     *   Set Assault Party Room id.
     *  @param assaultP_roomID New Assault Party Room id
     */
    public void setAssaultP_roomID(int assaultP_roomID) 
    {
        this.assaultP_roomID = assaultP_roomID;
    }

    // /**
    //  *  Get state of the assault party.
    //  * 
    //  * @return  True if in action 
    //  */
    // public Boolean getAssaultP_inAction()
    // {
    //     return assaultP_inAction;
    // }

    // /**
    //  *  Set state of the assault party.    
    //  *  @param assaultP_inAction    
    //  */
    // public void setAssaultP_inAction(Boolean assaultP_inAction)
    // {
    //     this.assaultP_inAction = assaultP_inAction;
    // }

    /**
     *  Array list of the assault party
     * @return  assault party list
     */
    public ArrayList <Integer> getAssaultP_Thieves()
    {
        return assaultP_Thieves; 
    }

    // /**
    //  *  Check if the assault party is created.
    //  * 
    //  * @return  True if assault party is created 
    //  */
    // public Boolean getAParty_Created() 
    // {
    //     return assaultParty_Created;
    // }

    // /**
    //  * 
    //  * @param assaultParty_Created
    //  */
    // public void setAParty_Created(Boolean assaultParty_Created) 
    // {
    //     this.assaultParty_Created = assaultParty_Created;  
    // }

    /**
     *  Indicates whether the first member of the party has been notified by the master 
     *  on sendAssaultParty to proceed.
     * 
     *  @param firstPartyMember movement status of the first member of the assault party
     */
    public synchronized void moveFirstPartyMember(Boolean firstPartyMember)
    {
        myTurnToMove[0] = firstPartyMember;
    }

    public synchronized void addThievesToParty(int [] assaultParty, int roomID)
    {
        //System.out.println("AssaultParty " + assaultParty_ID + " -- addThievesToParty \n");
        for (int i = 0; i < SimulPar.K; i++)
        {
            assaultP_Thieves.set(i, assaultParty[i]); 
        }

        assaultP_roomID = roomID;
        //assaultParty_Created = true;
        //assaultP_inAction = true;
    }

    public synchronized void resetAssaultParty()
    {
        //assaultParty_Created = false;
        //assaultP_inAction = false;
        assaultP_roomID = -1;

        for (int i = 0; i < SimulPar.K; i++)
        {
            assaultP_Thieves.set(i, -1); 
        }

        // update general repository    -- info Assault Party 
        for (int i = 0; i < SimulPar.K; i++)
        {
            // Assault Party #, Elem #, Id # , Serves to remove the thief from the assault party (-1)
            repos.setAParty_Elem_ID(assaultParty_ID, i, -1);
        }
        //update general repository    -- info Room ID of the assault party
        repos.setAssaultParty_RId(assaultParty_ID, -1);
    }

    /**
     *  Transition crawling inwards in the life cycle of the Ordinary Thief.
     *  (CRAWLING_INWARDS -> AT_A_ROOM)
     * 
     */
    public synchronized void crawlIN()
    {
        while ( crawlingThieves.get(getThiefIndex()) != getDistFromOutside())
        {
            OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
            while(!myTurnToMove[getThiefIndex()])
            {
                try {
                    wait();
                } catch (InterruptedException e) {}
            }

            int thiefIndex = getThiefIndex(); // id do thief atual 
            int currentPos  = ordinaryThief.getAParty_position();
            int maxDisplacement =   ordinaryThief.getThiefMaxDisp();
        
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
                ordinaryThief.setAParty_position(newPosition);
                repos.setAParty_Elem_POS(ordinaryThief.getAParty_ID(), thiefIndex, ordinaryThief.getAParty_position()); 

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
        }
    }

    /**
     *  Transition crawling inwards in the life cycle of the Ordinary Thief.
     *  (AT_A_ROOM -> CRAWLING_OUTWARDS)
     * 
     */
    public synchronized void reverseDirection()
    {
        OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();

        // update general repository  -- info hasACanvas of the ordinary Thief
        repos.setAParty_Elem_CV(assaultParty_ID, getThiefIndex() , ordinaryThief.getAParty_hasCanvas()); 

        //set the thief state to CRAWLING_OUTWARDS
        ordinaryThief.setOTState(OrdinaryThiefStates.CRAWLING_OUTWARDS);

        // update general repository  -- info state of the ordinary Thief
        repos.setOTState(ordinaryThief.getThiefID(), ordinaryThief.getOTState());
        
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
    }

    /**
     *  Transition crawling outwards in the life cycle of the Ordinary Thief.
     *  (CRAWLING_OUTWARDS -> COLLECTION_SITE)
     * 
     */
    public synchronized void crawlOUT()
    {
        while( crawlingThieves.get(getThiefIndex()) != 0)
        {
            OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread(); 
            while(!myTurnToMove[getThiefIndex()])
            {
                try {
                    wait();
                } catch (InterruptedException e) {}
            }

            int thiefIndex = getThiefIndex(); // id do thief atual 
            int currentPos  = ordinaryThief.getAParty_position();
            int maxDisplacement =   ordinaryThief.getThiefMaxDisp();

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
                ordinaryThief.setAParty_position(newPosition);

                repos.setAParty_Elem_POS(ordinaryThief.getAParty_ID(), thiefIndex, ordinaryThief.getAParty_position()); 

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
        }
    }
    
    /**
     *  Get the assault party thief index.
     * 
     * @return  index of array in the assault party that the thief belongs to
     */
    public synchronized int getThiefIndex()
    {
        OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
        for (int i = 0; i < SimulPar.K; i++)
        {
            if (assaultP_Thieves.get(i) == ordinaryThief.getThiefID())
            {
                return i;
            }
        }
        return -1;
    }

    /**
     *  Gets the index of the position array.
     * 
     * @return  index of the position array.
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

    public synchronized int getDistFromOutside()
    {
        return rooms_distFromOutside.get(assaultP_roomID);
    }

    public ArrayList <Integer> distRooms()
    {
      return rooms_distFromOutside;
    }

    public synchronized void showAssaultParty()
    {
        for (int i = 0; i < SimulPar.K; i++)
        {
            System.out.println("ElemID: " + assaultP_Thieves.get(i));
        }
    }

}
