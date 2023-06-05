package serverSide.objects;

import java.rmi.registry.*;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import genclass.GenericIO;
import java.util.*;

/**
 *  Concentration Site.
 *
 *    Is implemented as an implicit monitor.
 *    All public methods are executed in mutual exclusion.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class ConcentrationSite implements ConcSiteInterface
{
    /**
     *  Queue of ordinary thieves waiting to be summoned.
     */
    private Queue<Integer> waitingThieves; 

    /**
     *  True if the room has paintings on the walls.
     */
    private ArrayList<Boolean> roomStates;

    /**
     *  Ordinary Thieves State. True if they are assaulting.
     */
    private Boolean [] assaultingThieves;

    /**
     *  Indicates whether the assault parties are already assigned to a group.
     */
    private Boolean [] assignedAP;  
    
    /**
     *  Indicates which rooms are assigned to each assault party. 
     *  If not assigned it has the value -1
     */
    private int [] assignedRoomID;
    
    /**
     *  Indicates which assault party the thief belongs t.
     */
    private int [] assignedAPid_Thieves;

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
    public ConcentrationSite (GeneralReposInterface reposStub)
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

        nEntities = 0;
        this.reposStub = reposStub;
    }

    /** 
    *   New state of the assault party. True if assigned, false otherwise.
    *
    *       @param assaultPartyID   assault party id
    *       @param state            status of the assault party
    *       @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    */
    @Override
    public synchronized void setAssigedAPid(int assaultPartyID, Boolean state)  throws RemoteException
    {
        assignedAP[assaultPartyID] = state;
    }

    /**
     *  New room assigned to the assault party.
     * 
     *      @param assaultPartyID   assault party id
     *      @param roomID           assigned room id 
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void setAssignedRoomID(int assaultPartyID, int roomID)  throws RemoteException
    {
        assignedRoomID[assaultPartyID] = roomID;
    }


    /**
     *  Update room status. True if empty.
     * 
     *      @param roomID   room identification.
     *      @param roomState    room status.
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void setRoomStates(int roomID, Boolean roomState) throws RemoteException 
    {
        roomStates().set(roomID, roomState);
    }

    /** 
     *  Operation start operations.
     *  
     *  It is called by the Master Thief when he wants to start the heist operation in the museum.
     * 
     *      @return state of the master thief
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
    */
    @Override
    public synchronized int startOperations () throws RemoteException
    {
        // System.out.println("\n Start Operations ! \n");
        return MasterThiefStates.DECIDING_WHAT_TO_DO;
    }

    /**
     *  Transition appraise situation in the life cycle of the Master Thief.
     * 
     *  As long as the master thief remains in this transition -> state : DECIDING_WHAT_TO_DO
     * 
     *      @return  next transition (DECIDING_WHAT_TO_DO or ASSEMBLING_A_GROUP or WAITING_FOR_ARRIVAL or PRESENTING_THE_REPORT)
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized ReturnInt appraiseSit() throws RemoteException
    {
        int masterThiefState,   // master thief state
            transition;         // next transition

        transition = MasterThiefStates.DECIDING_WHAT_TO_DO;
         
        masterThiefState = MasterThiefStates.DECIDING_WHAT_TO_DO;
        
        reposStub.setMasterThiefState(masterThiefState);

        // se houver um grupo de assalto disponível e as salas do museu ainda tiverem quadros
        if ((getSizeWaitingThieves() >= SimulPar.K) && !(allRoomsEmpty()) && (checkRoom() == false))
        {
            transition = MasterThiefStates.ASSEMBLING_A_GROUP;

        }else if (thievesInAction())     // se todos os grupos de assalto ainda estiverem em operação, ou não houver mais salas a pesquisar
        {
            transition =  MasterThiefStates.WAITING_FOR_ARRIVAL;
            
        }else if( (thievesInAction() == false) && allRoomsEmpty() ) // se não houver grupos de assalto em operação e todas as salas do museu estiverem vazias
        {
            transition =  MasterThiefStates.PRESENTING_THE_REPORT;
        }

        return new ReturnInt (transition, masterThiefState);
    }

    /**
     *  Master blocks until enough ordinary thieves have arrived the concentration site.
     *  Master is notified whenever a thief arrives at the concentration site (amINeeded()).
     * 
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     * 
     */
    @Override
    public synchronized void waitForThieves() throws RemoteException
    {
        while(waitingThieves.size() < SimulPar.K)   // wait for 3 thieves
        {
            try
            {  
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
     *      @param  thiefID ordinary thief id
     *      @return true if the heist is not finished and the ordinary thieves are still needed and OT state
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized ReturnBoolean amINeeded(int thiefID) throws RemoteException
    {
        int ordinaryThiefState = OrdinaryThiefStates.CONCENTRATION_SITE;

        reposStub.setOTState(thiefID, ordinaryThiefState);

        // try {
        //     Thread.sleep(100);
        // } catch (InterruptedException e) { }
        
        assignedAPid_Thieves[thiefID] = -1;
        // ordinaryThief.setAParty_ID(-1); 

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
        // System.out.println("OT - AmINeeded a sair ---- thief ID " + thiefID + "\n");

        // if heistFinished = true means that the heist is finished and the ordinary thieves are no longer needed
        return new ReturnBoolean(!heistFinished, ordinaryThiefState);
    }

    /**
     *  Master creates a 3-member assault party
     *      
     *      @return assault party with 3 thieves and master thief state
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized ReturnArrayInt buildParty() throws RemoteException
    {      
        int masterThiefState;  // master thief state

        masterThiefState = MasterThiefStates.ASSEMBLING_A_GROUP;
        
        reposStub.setMasterThiefState(masterThiefState); 

        // Create an Assaul Party of three elements
        int [] assaultParty = new int [SimulPar.K];
        for (int i = 0; i < SimulPar.K; i++)
        {
            assaultParty[i] = waitingThieves.poll();  // retirar o OT da waiting queue
        }
        return new ReturnArrayInt(assaultParty, masterThiefState);
    } 


    /**
     *  Transition prepare Assault Party in the life cycle of the Master Thief.
     *  DECIDING_WHAT_TO_DO -> ASSEMBLING_A_GROUP
     * 
     *  The master starts by creating a group of 3 elements, then notifies the thieves waiting 
     *  on the queue. 
     * 
     *      @param assaultParty     list of ids of the 3 elements of the assault party   
     *      @param assaultPartyID   identification assigned to the assault party
     *      @param roomID           room id assigned to the assault party
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void prepareAssaultParty(int[] assaultParty, int assaultPartyID, int roomID) throws RemoteException
    {
        //System.out.println("Concentration Site - prepareAssaultParty \n");
        // Update Assaul Party of three elements and notify ordinary thieves
        int ordinaryT;
        int assaultPid = assaultPartyID;
        for (int i = 0; i < SimulPar.K; i++)
        {
            //ordinaryT = waitingThieves.poll();  // retirar o OT da waiting queue
            ordinaryT = assaultParty[i];
            assignedAPid_Thieves[ordinaryT] = assaultPartyID;
            assaultingThieves[ordinaryT] = true; 
        }
        
        // update general repository    -- info Assault Party + info Room ID of the assault party
        for (int i = 0; i < SimulPar.K; i++)
        {
            // Assault Party #, Elem #, Id #
            reposStub.setAParty_Elem_ID(assaultPid, i,  assaultParty[i]);
        }
        reposStub.setAssaultParty_RId(assaultPartyID, roomID);
        
        notifyAll();    // notify -> amINeeded 
    }


    /**
     *  Transition send Assault Party in the life cycle of the Master Thief.
     *  ASSEMBLING_A_GROUP -> DECIDING_WHAT_TO_DO
     * 
     *  After creating a group of 3, the master blocks and waits for the last thief to reach the group to wake it up. 
     *  He is notified by the thief's prepare Excursion function.
     * 
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void sendAssaultParty() throws RemoteException
    {
        // System.out.println("Concentration Site - sendAssaultParty \n");
        while(assaultParty_Ready != SimulPar.K)
        {
            try {
                //System.out.println("Concentration Site - sendAssaultParty Master is WAITING \n");
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
     * 
     *      @param thiefID ordinary thief identification
     *      @return OT assault party id and OT state
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized ReturnInt prepareExcursion(int thiefID) throws RemoteException
    {
        //System.out.println("Concentration Site - prepareExcursion --> thief ID = " + thiefID + " \n");       

        int assaultPartyID = getAssaultPartyID_Thief(thiefID); 
        // ordinaryThief.setAParty_ID(assaultPartyID);     

        assaultParty_Ready++;
        if (assaultParty_Ready == SimulPar.K)
        {
            notifyAll();    // notifica o wait de sendAssaultParty()
        }

        int ordinaryThiefState = OrdinaryThiefStates.CRAWLING_INWARDS;
       
        // update general repository    -- info situation + state ordinary Thief 
        reposStub.setOTSituation(thiefID, "P");     // FAZER SET NO PROPRIO OT !! 
        reposStub.setOTState(thiefID, ordinaryThiefState); 

        while(!sendAssaultParty)
        { 
            try {
                // System.out.println("Concentration Site - prepareExcursion WAITING --> thief ID = " + thiefID + " \n");    
                wait();     // notify -> prepareAssaultParty 
            } catch (InterruptedException e) {}
        }

        gointToCrawlIN++;
        if (gointToCrawlIN == SimulPar.K)
        {
            sendAssaultParty = false;
            gointToCrawlIN = 0; 
        }

        return new ReturnInt(assaultPartyID, ordinaryThiefState);
    }



    /**
     *  Transition sum Up Results  in the life cycle of the Master Thief.
     * 
     *  Master collects the canvas.
     *  Last function to be called by the master thief kills threads and ends the simulation.
     * 
     *      @param collectedCanvas number of canvas collected
     *      @return master Thief State
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized int sumUpResults(int collectedCanvas) throws RemoteException
    {
        int masterThiefState = MasterThiefStates.PRESENTING_THE_REPORT;

        // update general repository    -- info state master Thief 
        reposStub.setMasterThiefState(masterThiefState);

        thiefNeeded = false;
        notifyAll();
        heistFinished = true;

        reposStub.setStolenPaitings(collectedCanvas);

        return masterThiefState;
    }


    /**
     *   Operation server shutdown.
     *
     *   New operation.
     *
     *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void shutdown () throws RemoteException
    {
        nEntities += 1;
        if (nEntities >= SimulPar.E_CONCS)
            ServerHeistMuseumConcSite.shutdown();

    }

    /**
     *  Function that checks if assault parties have already been created. 
     *  If not, returns an id of the group that has not yet been created.
     * 
     *      @return assault party ID or -1 if groups have already been created.
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized int assignAssaultPartyID()  throws RemoteException
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
     *      @return  room ID or -1 if all used or empty
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized int assignRoomID()  throws RemoteException
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
     *      @param thiefID ordinary thief ID
     *      @return  assault Party ID
     */
    public synchronized int getAssaultPartyID_Thief(int thiefID)
    {
        for (int i = 0; i < SimulPar.NUM_ORD_THIEVES; i++)
        {
            if (i == thiefID)
            {
                return assignedAPid_Thieves[thiefID];
            }
        }
        return -1;
    }

     /**
     *  Checks if there are thieves in action by checking the state of the assault parties

     *      @return  True if thieves are in action
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
     *      @return  True if last room with paintings is already assigned to an assaultParty
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
     *  Checks if all rooms are empty Used to check if the heist is over.
     * 
     *      @return true if all rooms are empty
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

    /**
     * Indicates the number of thieves waiting on the concentration site.
     * 
     *      @return number of thieves waiting on the concentration site.
     */
    public synchronized int getSizeWaitingThieves()
    {
        return waitingThieves.size();
    }

    /**
     * Indicates which rooms have paintings.
     * 
     *      @return list of room states.
     */
    public synchronized ArrayList<Boolean> roomStates()
    {
        return roomStates;
    }
}