package clientSide.entities;

import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import genclass.GenericIO;

/**
 *    Master Thief thread.
 *
 *      It simulates the Master Thief life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on remote calls under Java RMI.
 */
public class MasterThief extends Thread
{
    /**
     *  State of the Master Thief.
     */
    private int MTState;

    /**
     *  Number of canvas collected by the Master Thief.
     */
    private int collectedCanvas;

    /**
     *   Remote reference to the Assault Party Stub.
     */
    private final AssaultPartyInterface [] assaultPartiesStub;

    /**
     *  Remote reference to the Concentration Site Stub.
     */
    private final ConcSiteInterface concentrationSiteStub;

    /**
     *  Remote reference to the Control & Collection Site Stub.
     */
    private final ControlCollecSiteInterface controlCollectionSiteStub;

    /**
     *   Master Thief Thread instantiation.
     *
     *     @param name                      Thread name
     *     @param concentrationSiteStub     Remote reference to the Concentration Site Stub
     *     @param controlCollectionSiteStub Remote reference to the Control and Collection Site Stub
     *     @param assaultPartiesStub        Remote reference to the Assault Party Stub
     */
    public MasterThief(String name, ConcSiteInterface concentrationSiteStub, ControlCollecSiteInterface controlCollectionSiteStub, AssaultPartyInterface [] assaultPartiesStub) 
    {
        super(name);
        this.collectedCanvas = 0;
        this.MTState = MasterThiefStates.PLANNING_THE_HEIST;
        this.concentrationSiteStub = concentrationSiteStub;
        this.controlCollectionSiteStub = controlCollectionSiteStub;
        this.assaultPartiesStub = assaultPartiesStub;
    }

    /**
     *   Life cycle of the Master Thief.
     */
    @Override
    public void run() 
    {
        Boolean heistFinished = false;
        
        startOperations();

        while(!heistFinished)
        {
            int transition = appraiseSit();

            if (transition == MasterThiefStates.DECIDING_WHAT_TO_DO)
            {
                //System.out.println("Master Thief: DECIDING_WHAT_TO_DO \n");
                waitForThieves();

            }else if (transition == MasterThiefStates.ASSEMBLING_A_GROUP)
            {
                //System.out.println("Master Thief: ASSEMBLING_A_GROUP \n");

                int assaultPartyID = assignAssaultPartyID();
                int roomID = assignRoomID();
                setAssignedRoomID(assaultPartyID, roomID);

                int [] assaultP = buildParty();
                addThievesToParty(assaultPartyID, assaultP, roomID);

                prepareAssaultParty(assaultP, assaultPartyID, roomID);

                sendAssaultParty();  

            }else if (transition == MasterThiefStates.WAITING_FOR_ARRIVAL){
                
                //System.out.println("Master Thief: WAITING_FOR_ARRIVAL \n");

                takeARest();

                //System.out.println("Master Thief: going to collect a canvas  \n");
                
                Boolean [] resetAP = collectACanvas();
                
                System.out.println("Master Thief: Paintings collected = " + collectedCanvas + " \n");

                //resetAP
                for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
                {
                    if (resetAP[i] == true)
                    {
                        resetAssaultParty(i);
                        setAssigedAPid(i, false);
                        setAssignedRoomID(i, -1); 
                    }
                }
            }else {
                //System.out.println("Master Thief: PRESENTING_THE_REPORT \n");
                sumUpResults(); 
                System.out.println("Master thief: Total collected paintings = " + collectedCanvas + " \n"); 
                heistFinished = true;
            }
        }
    }

    /**
     *  Transition start operations in the life cycle of the Master Thief.
     * 
     *  It is called by the Master Thief when he wants to start the heist operation in the museum.
     * 
     *  Remote operation.
     */
    public void startOperations()
    {
        try
        { 
            MTState = concentrationSiteStub.startOperations ();
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on startOperations: " + e.getMessage ());
            System.exit (1);
        }
    }

    /**
     *  Transition appraise situation in the life cycle of the Master Thief.
     * 
     *  As long as the master thief remains in this transition -> state : DECIDING_WHAT_TO_DO.
     * 
     *  Remote operation.
     * 
     *      @return  next transition (DECIDING_WHAT_TO_DO or ASSEMBLING_A_GROUP or WAITING_FOR_ARRIVAL or PRESENTING_THE_REPORT)
     */
    public int appraiseSit()
    {
        ReturnInt ret = null;     // return value
        try
        { 
            ret = concentrationSiteStub.appraiseSit ();
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on appraiseSit: " + e.getMessage ());
            System.exit (1);
        }

        MTState = ret.getIntStateVal();

        return ret.getIntVal();
    }

    /**
     *  Master blocks until enough ordinary thieves have arrived the concentration site.
     *  Master is notified whenever a thief arrives at the concentration site (amINeeded()).
     * 
     *  Remote operation.
     */
    public void waitForThieves()
    {
        try
        { 
            concentrationSiteStub.waitForThieves ();
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on waitForThieves: " + e.getMessage ());
            System.exit (1);
        }
    }

    /**
     *  Function that checks if assault parties have already been created. 
     *  If not, returns an id of the group that has not yet been created.
     * 
     *  Remote operation.
     * 
     *      @return assault party ID or -1 if groups have already been created.
     */
    public int assignAssaultPartyID()
    {
        int assaultPartyID = 0;
        try
        { 
            assaultPartyID = concentrationSiteStub.assignAssaultPartyID();
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on assignAssaultPartyID: " + e.getMessage ());
            System.exit (1);
        }

        return assaultPartyID;
    }

    /**
     *  Get a room that has not yet been assigned and still has rooms.
     * 
     *  Remote operation.
     * 
     *      @return  room ID or -1 if all used or empty
     */
    public int assignRoomID()
    {
        int roomID = 0;
        try
        { 
            roomID = concentrationSiteStub.assignRoomID();
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on assignRoomID: " + e.getMessage ());
            System.exit (1);
        }

        return roomID;
    }


    /**
     *  New room assigned to the assault party.
     * 
     *  Remote operation.
     * 
     *      @param assaultPartyID   assault party id
     *      @param roomID           assigned room id 
     */
    public void setAssignedRoomID(int assaultPartyID, int roomID)
    {
        try
        { 
            concentrationSiteStub.setAssignedRoomID(assaultPartyID, roomID);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on assignRoomID: " + e.getMessage ());
            System.exit (1);
        }
    }

     /**
     *  Master creates a 3-member assault party
     * 
     *  Remote operation.
     *      
     *      @return assault party with 3 thieves
     */
    public int [] buildParty()
    {
        ReturnArrayInt ret = null;     // return value
        try
        { 
            ret = concentrationSiteStub.buildParty();
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on buildParty: " + e.getMessage ());
            System.exit (1);
        }

        MTState = ret.getIntStateVal();

        return ret.getArrayInt();
    }

    /**
     *  Add the thieves chosen by the master to the created assault party.
     * 
     *  Remote operation.
     * 
     *      @param assaultPartyID   identification assault party
     *      @param assaultParty     set of thieves to be added to the assault party
     *      @param roomID       identification of the room assigned to the assault party        
     */
    public void addThievesToParty(int assaultPartyID, int [] assaultParty, int roomID)
    {
        try
        { 
            assaultPartiesStub[assaultPartyID].addThievesToParty(assaultParty, roomID);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on addThievesToParty: " + e.getMessage ());
            System.exit (1);
        }
    }


    /**
     *  Transition prepare Assault Party in the life cycle of the Master Thief.
     * 
     *  Remote operation. 
     * 
     *      @param assaultParty     list of ids of the 3 elements of the assault party   
     *      @param assaultPartyID   identification assigned to the assault party
     *      @param roomID           room id assigned to the assault party
     */
    public void prepareAssaultParty(int[] assaultParty, int assaultPartyID, int roomID)
    {
        try
        { 
            concentrationSiteStub.prepareAssaultParty(assaultParty, assaultPartyID, roomID);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on prepareAssaultParty: " + e.getMessage ());
            System.exit (1);
        }
    }


     /**
     *  Transition send Assault Party in the life cycle of the Master Thief.
     * 
     *  Remote operation. 
     */
    public void sendAssaultParty() 
    {
        try
        { 
            concentrationSiteStub.sendAssaultParty(); 
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on sendAssaultParty: " + e.getMessage ());
            System.exit (1);
        }
    } 


    /**
     *  Transition take A Rest in the life cycle of the Master Thief.
     *  (DECIDING_WHAT_TO_DO -> WAITING_FOR_ARRIVAL)
     * 
     *  Master thief is waken up by the operation handACanvas of one of
     *  the assault party members returning from the museum.
     * 
     *  Remote operation. 
     * 
     */
    public void takeARest()
    {
        try
        { 
            MTState = controlCollectionSiteStub.takeARest();
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on takeARest: " + e.getMessage ());
            System.exit (1);
        }
    }


    /**
     *  Transition collect a canvas in the life cycle of the Master Thief.
     * 
     *  Remote operation. 
     * 
     *      @return  assault parties for reseting and 
     */
    public Boolean [] collectACanvas()
    {
        ReturnArrayBoolean ret = null;     // return value
        try
        { 
            ret = controlCollectionSiteStub.collectACanvas();
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on collectACanvas: " + e.getMessage ());
            System.exit (1);
        }

        collectedCanvas = ret.getIntVal();  // masterThief.setCollectedCanvas(collectedCanvas);

        return ret.getArrayBoolean();
    }


    /**
     *  Reset the values of the created assault party and the room associated with it.
     * 
     *  Remote operation. 
     * 
     *      @param assaultPartyIndex assault Party Index
     */
    public void resetAssaultParty(int assaultPartyIndex)
    {
        try
        { 
            assaultPartiesStub[assaultPartyIndex].resetAssaultParty();
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on resetAssaultParty: " + e.getMessage ());
            System.exit (1);
        }
    }

    /**
     *      New state of the assault party. True if assigned, false otherwise.
     * 
     *      Remote operation. 
     * 
     *      @param assaultPartyID   assault party id
     *      @param state            status of the assault party
     */
    public void setAssigedAPid(int assaultPartyID, Boolean state)
    {
        try
        { 
            concentrationSiteStub.setAssigedAPid(assaultPartyID, state);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on setAssigedAPid: " + e.getMessage ());
            System.exit (1);
        }
    }

    /**
     *  Transition sum up results in the life cycle of the Master Thief.
     *      
     *  Remote operation. 
     */
    public void sumUpResults() 
    {
        try
        { 
            MTState = concentrationSiteStub.sumUpResults(collectedCanvas); 
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Master Thief remote exception on sumUpResults: " + e.getMessage ());
            System.exit (1);
        }
    }
    
}
