package clientSide.entities;

import clientSide.stubs.*;
import serverSide.main.SimulPar;

/**
 *    Master Thief thread.
 *
 *      It simulates the Master Thief life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
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
     *   Reference to the Assault Party Stub.
     */
    private final AssaultPartyStub [] assaultPartiesStub;

    /**
     *  Reference to the Concentration Site Stub.
     */
    private final ConcentrationSiteStub concentrationSiteStub;

    /**
     *  Reference to the Control & Collection Site Stub.
     */
    private final ControlCollectionSiteStub controlCollectionSiteStub;

    /**
     *   Master Thief Thread instantiation.
     *
     *     @param name Thread name
     *     @param concentrationSiteStub     Reference to the Concentration Site Stub
     *     @param controlCollectionSiteStub Reference to the Control and Collection Site Stub
     *     @param assaultPartiesStub        Reference to the Assault Party Stub
     */
    public MasterThief(String name, ConcentrationSiteStub concentrationSiteStub, ControlCollectionSiteStub controlCollectionSiteStub, AssaultPartyStub [] assaultPartiesStub) 
    {
        super(name);
        this.collectedCanvas = 0;
        this.MTState = MasterThiefStates.PLANNING_THE_HEIST;
        this.concentrationSiteStub = concentrationSiteStub;
        this.controlCollectionSiteStub = controlCollectionSiteStub;
        this.assaultPartiesStub = assaultPartiesStub;
    }

    /**
     *  Get Master Thief state.
     *
     *      @return Master Thief state
     */
    public int getMTState() 
    {
        return MTState;
    }

    /**
     *   Set Master Thief state.
     *
     *      @param MTState New Master Thief state
     */
    public void setMTState(int MTState) 
    {
        this.MTState = MTState;

    }

    /**
     *  Get collected canvas    
     * 
     *      @return  number of collected canvas
     */
    public int getCollectedCanvas()
    {
        return collectedCanvas;
    }

    /**
     *  Set collected canvas.
     *    
     *      @param collectedCanvas  collected canvas
     */
    public void setCollectedCanvas(int collectedCanvas)
    {
        this.collectedCanvas = collectedCanvas;
    }

    /**
     *   Life cycle of the Master Thief.
     */
    @Override
    public void run() {
        
        Boolean heistFinished = false;
        
        System.out.println("Master Thief: PLANNING_THE_HEIST \n");
        concentrationSiteStub.startOperations();

        while(!heistFinished)
        {
            int transition = concentrationSiteStub.appraiseSit();

            if (transition == MasterThiefStates.DECIDING_WHAT_TO_DO)
            {
                System.out.println("Master Thief: DECIDING_WHAT_TO_DO \n");

                concentrationSiteStub.waitForThieves();
            }else if (transition == MasterThiefStates.ASSEMBLING_A_GROUP)
            {
                System.out.println("Master Thief: ASSEMBLING_A_GROUP \n");

                int assaultPartyID = concentrationSiteStub.assignAssaultPartyID();
                int roomID = concentrationSiteStub.assignRoomID();
                concentrationSiteStub.setAssignedRoomID(assaultPartyID, roomID);

                int [] assaultP = concentrationSiteStub.buildParty();
                assaultPartiesStub[assaultPartyID].addThievesToParty(assaultP, roomID);

                concentrationSiteStub.prepareAssaultParty(assaultP, assaultPartyID, roomID);

                concentrationSiteStub.sendAssaultParty();  

            }else if (transition == MasterThiefStates.WAITING_FOR_ARRIVAL){
                
                System.out.println("Master Thief: WAITING_FOR_ARRIVAL \n");

                controlCollectionSiteStub.takeARest();
                
                Boolean [] resetAP = controlCollectionSiteStub.collectACanvas();
                
                //System.out.println("Master Thief: Paintings collected = " + collectedCanvas + " \n");

                // resetAP
                for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
                {
                    if (resetAP[i] == true)
                    {
                        assaultPartiesStub[i].resetAssaultParty();
                        concentrationSiteStub.setAssigedAPid(i, false);
                        concentrationSiteStub.setAssignedRoomID(i, -1);
                    }
                }
            }else {
                System.out.println("Master Thief: PRESENTING_THE_REPORT \n");
                concentrationSiteStub.sumUpResults(); 
                System.out.println("Master thief: Total collected paintings = " + collectedCanvas + " \n"); 
                heistFinished = true;
            }
        }
    }
    
}
