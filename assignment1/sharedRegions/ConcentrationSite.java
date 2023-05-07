package sharedRegions;

import main.*;
import entities.*;
import java.util.*;

public class ConcentrationSite
{
    /**
     *  Queue of ordinary thieves waiting to be summoned.
     */
    private Queue<Integer> waitingThieves; 

    /**
     *  Ordinary Thieves State. True if they are assaulting.
     */
    private Boolean [] assaultingThieves;

    private Boolean [] assignedAP;  
    private int [] assignedRoomID;
    private int [] assignedAPid_Thieves;

     /**
     *  True if the room has paintings on the walls.
     */
    private ArrayList<Boolean> roomStates;

    /**
     *  True if ordinary thieves are needed in the concentration site.
     */
    private Boolean thiefNeeded;

    /**
     *  True if the heist ended.
     */
    private Boolean heistFinished;

    /**
     *  Number of ordinary thieves arriving at the assault party.
     */
    private int assaultParty_Ready;

    /**
     *  True if the assault party was send.
     */
    private Boolean sendAssaultParty;

    /**
     *  Number of thieves going to the crawl in. 
     */
    private int gointToCrawlIN;

    /**
     *   Reference to the Assault Party.
     */
    //private final AssaultParty [] assaultParties;

    /**
     *   Reference to the general repository.
     */
    private final GeneralRepository repos;

    /**
     *  Ordinary Thieves - Concentration Site instantiation.
     *
     *    @param repos reference to the general repository
     */
    //public ConcentrationSite(GeneralRepository repos, AssaultParty [] assaultParties)
    public ConcentrationSite(GeneralRepository repos)
    {
        assignedAP = new Boolean[SimulPar.NUM_ASSAULT_PARTIES];
        assignedRoomID = new int[SimulPar.NUM_ASSAULT_PARTIES];
        for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
        {   
            assignedAP[i] = false;  
            assignedRoomID[i] = -1;
        }

        waitingThieves = new LinkedList<>();
        roomStates = new ArrayList<>(SimulPar.NUM_ROOMS); 
        for (int i = 0; i < SimulPar.NUM_ROOMS; i++)
        {   
            roomStates.add(true);
        }

        assignedAPid_Thieves = new int [SimulPar.NUM_ORD_THIEVES];

        assaultingThieves = new Boolean[SimulPar.NUM_ORD_THIEVES];
        for (int i = 0; i < SimulPar.NUM_ORD_THIEVES; i++)
        {
            assaultingThieves[i] = false;

            assignedAPid_Thieves[i] = -1;
        }

        this.heistFinished = false;
        this.thiefNeeded = true;
        this.assaultParty_Ready = 0;
        this.sendAssaultParty = false;
        this.gointToCrawlIN = 0;

        //this.assaultParties = assaultParties;
        this.repos = repos;
    }

    /**
     *  Transition start operations in the life cycle of the Master Thief.
     *  (PLANNING_THE_HEIST -> DECIDING_WHAT_TO_DO)
     * 
     *  It is called by the Master Thief when he wants to start the heist operation in the museum.
     */
    public synchronized void startOperations()
    {
        MasterThief masterThief = (MasterThief) Thread.currentThread();
        masterThief.setMTState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    /**
     * Transition appraise situation in the life cycle of the Master Thief.
     * 
     * As long as the master thief remains in this transition -> state : DECIDING_WHAT_TO_DO
     * 
     * @return  next transition (DECIDING_WHAT_TO_DO or ASSEMBLING_A_GROUP or WAITING_FOR_ARRIVAL or PRESENTING_THE_REPORT)
     */
    public synchronized int appraiseSit()
    {
        //System.out.println("MASTER - avaliar a situacao \n");

        MasterThief masterThief = (MasterThief) Thread.currentThread();
        masterThief.setMTState(MasterThiefStates.DECIDING_WHAT_TO_DO);
        // update general repository    - info state Master Thief
        repos.setMasterThiefState(masterThief.getMTState()); 

        // for (int i = 0; i < 5 ; i++)
        // {
        //     System.out.print(roomStates.get(i) + " ");
        // }
        // System.out.print("\n");

        // System.out.println("getSizeWaitingThieves:  " + getSizeWaitingThieves());
        // System.out.println("allRoomsEmpty:  " + allRoomsEmpty());
        // System.out.println("checkRoom:  " + checkRoom());
        // System.out.println("checkQueue:  " + checkQueue());
       
        // se houver um grupo de assalto disponível e as salas do museu ainda tiverem quadros
        //if ((getSizeWaitingThieves() >= SimulPar.K) && !(allRoomsEmpty()) && (checkRoom() == false) && (checkQueue() == true))
        if ((getSizeWaitingThieves() >= SimulPar.K) && !(allRoomsEmpty()) && (checkRoom() == false))
        {
            //System.out.println("A ir ... ASSEMBLING_A_GROUP \n ");
            return MasterThiefStates.ASSEMBLING_A_GROUP;
        
            // se todos os grupos de assalto ainda estiverem em operação, ou não houver mais salas a pesquisar
        }else if (thievesInAction()){
            //System.out.println("A ir ... WAITING_FOR_ARRIVAL \n ");
            return MasterThiefStates.WAITING_FOR_ARRIVAL;

            // se não houver grupos de assalto em operação e todas as salas do museu estiverem vazias
        }else if( (thievesInAction() == false) && allRoomsEmpty() )
        {
            //System.out.println("A ir ... PRESENTING_THE_REPORT \n ");
            return  MasterThiefStates.PRESENTING_THE_REPORT;
        }
        //System.out.println("A ir ... DECIDING_WHAT_TO_DO \n ");
        return MasterThiefStates.DECIDING_WHAT_TO_DO;
    }


    /**
     * Checks if An assault party can be created
     */ 
    // public synchronized Boolean checkQueue()
    // {
    //     int count = 0;
    //     Boolean createAP = true;
    //     if (getSizeWaitingThieves() >= SimulPar.K)  // garantir q tem pelo menos 3 elementos disponiveis
    //     { 
    //         for (Integer thiefID: waitingThieves) {

    //             for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
    //             {
    //                 for (int j = 0; j < SimulPar.K; j++)
    //                 {
    //                     if (assaultParties[i].getAssaultP_Thieves().get(j) == thiefID){
    //                         createAP = false;
    //                         break;
    //                     }
    //                 }
    //             }
    //             count++;
    //             if (count == 3) // 3 ultimos elementos da queue
    //                 break;
    //         }
    //     }
    //     return createAP;
    // }

    /**
     * Master blocks until enough ordinary thieves have arrived the concentration site.
     * Master is notified whenever a thief arrives at the concentration site (amINeeded()).
     */
    public synchronized void waitForThieves()
    {
        //while(getSizeWaitingThieves() < SimulPar.NUM_ORD_THIEVES)
        while(waitingThieves.size() < SimulPar.K)   // wait for 3 thieves
        {
            try
            {  
                //System.out.println("MASTER - waiting for thieves"); 
                wait();
            } catch (InterruptedException e) {}
        }
    }

    /**
     *  Transition am I Needed in the life cycle of the Ordinary Thief.
     *   
     *  When thieves are needed they are added to the waiting queue and notify the master. (waitForThieves())
     *  
     *  CONCENTRATION_SITE -> CONCENTRATION_SITE : 
     *  Thieves are blocked until they are called to form the assault groups.
     *  Or Thieves are blocked until they are no longer needed. 
     * 
     * @return  True if the heist is not finished and the ordinary thieves are still needed
     */
    public synchronized Boolean amINeeded()
    {
        OrdinaryThief ordinaryThief = ((OrdinaryThief) Thread.currentThread());
        ordinaryThief.setOTState(OrdinaryThiefStates.CONCENTRATION_SITE);
        int thiefID = ordinaryThief.getThiefID();

        // update general repository    -- info state Ordinary Thief 
        repos.setOTState(thiefID, ordinaryThief.getOTState());

        //System.out.println("OT - AmINeeded thief ID " + thiefID + "\n");
        
        assignedAPid_Thieves[thiefID] = -1;
        ordinaryThief.setAParty_ID(-1); 

        waitingThieves.add(thiefID);
        notifyAll();    // sinaliza a thread do master na função appraiseSit pq um novo thief foi add
        
        // Ordinary thieves esperam ate serem chamados para formar o assalto ou ate serem chamados pelo sumUpResults 
        while(!assaultingThieves[thiefID] && thiefNeeded)
        {   
            try {
                //System.out.println("OT - AmINeeded waiting ---- thief ID " + thiefID + "\n");
                wait();
            } catch (InterruptedException e) {}
        }
        assaultingThieves[thiefID] = false; 

        // if heistFinished = true means that the heist is finished and the ordinary thieves are no longer needed
        return !heistFinished;
    }


    public synchronized int [] buildParty()
    {
        // Create an Assaul Party of three elements
        int [] assaultParty = new int [SimulPar.K];
        for (int i = 0; i < SimulPar.K; i++)
        {
            assaultParty[i] = waitingThieves.poll();  // retirar o OT da waiting queue
            //System.out.print(assaultParty[i] + " ");
        }
        //System.out.print(" \n");

        return assaultParty;
    } 

    /**
     *  Transition prepare Assault Party in the life cycle of the Master Thief.
     *  DECIDING_WHAT_TO_DO -> ASSEMBLING_A_GROUP
     * 
     *  The master starts by creating a group of 3 elements, then notifies the thieves waiting 
     *  on the queue. 
     */
    public synchronized void prepareAssaultParty(int[] assaultParty, int assaultPartyID, int roomID)
    {
        MasterThief masterThief = (MasterThief) Thread.currentThread();
        masterThief.setMTState(MasterThiefStates.ASSEMBLING_A_GROUP);

        // update general repository    -- info state Master Thief
        repos.setMasterThiefState(masterThief.getMTState());   

        //System.out.println("Master - prepareAssaultParty \n");

        // Create an Assaul Party of three elements
        int ordinaryT;
        //int assaultPartyID = assignAssaultPartyID();
        int assaultPid = assaultPartyID;
        for (int i = 0; i < SimulPar.K; i++)
        {
            //ordinaryT = waitingThieves.poll();  // retirar o OT da waiting queue
            ordinaryT = assaultParty[i];
            assignedAPid_Thieves[ordinaryT] = assaultPartyID;
            assaultingThieves[ordinaryT] = true; 
            //assaultParties[assaultPartyID].getAssaultP_Thieves().set(i, ordinaryT);  // add OT no grupo de assalto 
        }

        //assaultParties[assaultPartyID].showAssaultParty();

        // Change state of the Assault Party it is Created and in Action
        // assaultParties[assaultPartyID].setAParty_Created(true);
        // assaultParties[assaultPartyID].setAssaultP_inAction(true); 
        
        // update general repository    -- info Assault Party 
        for (int i = 0; i < SimulPar.K; i++)
        {
            // Assault Party #, Elem #, Id #
            //repos.setAParty_Elem_ID(assaultPartyID, i, assaultParties[assaultPartyID].getAssaultP_Thieves().get(i));
            repos.setAParty_Elem_ID(assaultPid, i,  assaultParty[i]);
        }
        
        // Assign to each party a target room in the museum
        //int roomID = assignRoomID();
        //assaultParties[assaultPartyID].setAssaultP_roomID(roomID);  // set roomID do grupo de assalto

        //update general repository    -- info Room ID of the assault party
        repos.setAssaultParty_RId(assaultPartyID, roomID);
        
        notifyAll();    // notify -> amINeeded 
    }

    /**
     *  Transition send Assault Party in the life cycle of the Master Thief.
     *  ASSEMBLING_A_GROUP -> DECIDING_WHAT_TO_DO
     * 
     *  After creating a group of 3, the master blocks and waits for the last thief to reach the group to wake it up. 
     *  He is notified by the thief's prepare Excursion function.
     */
    public synchronized void sendAssaultParty()
    {
        while(assaultParty_Ready != SimulPar.K)
        {
            try {
                wait();     // notifyAll do prepareExcursion OT
            } catch (InterruptedException e) {}
        }

        assaultParty_Ready = 0;
        sendAssaultParty = true;
        notifyAll();     // wait do prepareExcursion OT - enviar o AP
    }

    /**
     *  Transition prepare Excursion in the life cycle of the Ordinary Thief.
     *  CONCENTRATION_SITE -> CRAWLING_INWARDS
     * 
     *  The thief notifies the master that the last member of the group has arrived.
     */
    public synchronized void prepareExcursion()
    {
        OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
        int thiefID = ordinaryThief.getThiefID();
     
        //System.out.println("prepareExcursion thiefID: " + thiefID + " \n");

        int assaultPartyID = getAssaultPartyID_Thief();
        ordinaryThief.setAParty_ID(assaultPartyID);     // update party ID do OT 

        assaultParty_Ready++;
        if (assaultParty_Ready == SimulPar.K)
        {
            notifyAll();    // notifica o wait de sendAssaultParty()
        }

        ordinaryThief.setOTState(OrdinaryThiefStates.CRAWLING_INWARDS);
        // update general repository    -- info situation ordinary Thief 
        repos.setOTSituation(thiefID, "P");
        // update general repository    -- info state ordinary Thief 
        repos.setOTState(thiefID, ordinaryThief.getOTState()); 

        while(!sendAssaultParty)
        { 
            try {
                wait();     // notify -> prepareAssaultParty 
            } catch (InterruptedException e) {}
        }
        // assaultParties[assaultPartyID].moveFirstPartyMember(true);  

        gointToCrawlIN++;
        if (gointToCrawlIN == SimulPar.K)
        {
            sendAssaultParty = false;
            gointToCrawlIN = 0; 
        }
    }
    
    /**
     *  Transition sum Up Results  in the life cycle of the Master Thief.
     * 
     *  Master collects the canvas.
     *  Last function to be called by the master thief kills threads and ends the simulation.
     */
    public synchronized void sumUpResults()
    {
        MasterThief masterThief = (MasterThief) Thread.currentThread();
        masterThief.setMTState(MasterThiefStates.PRESENTING_THE_REPORT);

        // update general repository    -- info state master Thief 
        repos.setMasterThiefState(masterThief.getMTState());

        thiefNeeded = false;
        notifyAll();
        heistFinished = true;

        repos.setStolenPaitings(masterThief.getCollectedCanvas());
    }

    // indica se a AP ja foi atribuida 
    public synchronized void setAssigedAPid(int assaultPartyID, Boolean state)
    {
        assignedAP[assaultPartyID] = state;
    }

    public synchronized void setAssignedRoomID(int assaultPartyID, int roomID)
    {
        assignedRoomID[assaultPartyID] = roomID;
    }

    /**
     *  Function that checks if assault parties have already been created. 
     *  If not, returns an id of the group that has not yet been created.
     * 
     *  @return assault party ID or -1 if groups have already been created.
     */
    public synchronized int assignAssaultPartyID()
    {
        int idParty = -1;
        for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
        {
            if (assignedAP[i] == false) 
            {
                idParty = i;
                assignedAP[i] = true;
                break;
            }
        }
        return idParty;
    }

    /**
     *  Get a room that has not yet been assigned and still has rooms.
     * 
     * @return  room ID or -1 if all used or empty
     */
    public int assignRoomID()
    {   
        int roomID = -1;

        for (int i = 0; i < SimulPar.NUM_ROOMS; i++)
        {
            if ((roomStates.get(i)) != false && (assignedRoomID[0] != i) && (assignedRoomID[1] != i) )
            {
                roomID = i;
                break;
            }
        }
        return roomID;
    }

    /**
     *  Get the ID of the assault party that the thief belongs to
     * 
     * @return  assault Party ID
     */
    public synchronized int getAssaultPartyID_Thief()
    {
        OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();

        for (int i = 0; i < SimulPar.NUM_ORD_THIEVES; i++)
        {
            if (i == ordinaryThief.getThiefID())
            {
                return assignedAPid_Thieves[ordinaryThief.getThiefID()];
            }
        }
        return -1;
    }

    /**
     *  Checks if there are thieves in action by checking the state of the assault parties

     * @return  True if thieves are in action
     */
    public synchronized Boolean thievesInAction()
    {
        for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
        {
            if (assignedAP[i] == true)
            {
                return true;
            }
        }
        return false;
    }

    
    /**
     *  Check if the last room with paintings is already assigned to an assaultParty
     * 
     * @return  True if last room with paintings is already assigned to an assaultParty
     */
    public synchronized Boolean checkRoom()
    {
        int count = 0;
        int indexRoom = 0;
        for (int i = 0; i < SimulPar.NUM_ROOMS; i++)
        {
            if (roomStates.get(i) != false)
            {
                count++;
                indexRoom = i;
            }
        }

        if (count == 1)
        {
            for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
            {
                if (assignedRoomID[i] == indexRoom)
                {
                    return true; 
                }
            }
        }
        return false;
    }

    /**
     * Indicates the number of thieves waiting on the concentration site.
     * 
     *  @return number of thieves waiting on the concentration site.
     */
    public synchronized int getSizeWaitingThieves()
    {
        return waitingThieves.size();
    }

    /**
     * Indicates which rooms have paintings.
     */
    public synchronized ArrayList<Boolean> roomStates()
    {
        return roomStates;
    }

    /**
     *  Checks if all rooms are empty Used to check if the heist is over.
     * 
     * @return true if all rooms are empty
     */
    public synchronized Boolean allRoomsEmpty()
    {
        for (int i = 0; i < roomStates.size(); i++)
        {
            if (roomStates.get(i) == true)
                return false;
        }
        return true;
    }
}
