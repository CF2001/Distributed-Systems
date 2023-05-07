package entities;

import main.SimulPar;
import sharedRegions.*;

/**
 *   Master Thief thread.
 *
 *   It simulates the Master Thief life cycle.
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
     *   Reference to the Assault Party.
     */
    private final AssaultParty [] assaultParties;

    /**
     *  Reference to the Concentration Site.
     */
    private final ConcentrationSite concentrationSite;

    /**
     *  Reference to the Control & Collection Site.
     */
    private final ControlCollectionSite controlCollectionSite;


    /**
     *   Master Thief Thread instantiation.
     *
     *     @param name Thread name
     *     @param concentrationSite Reference to the Concentration Site
     *     @param controlCollectionSite Reference to the Control & Collection Site
     */
    public MasterThief(String name, ConcentrationSite concentrationSite, ControlCollectionSite controlCollectionSite, AssaultParty [] assaultParties) 
    {
        super(name);
        this.collectedCanvas = 0;
        this.MTState = MasterThiefStates.PLANNING_THE_HEIST;
        this.concentrationSite = concentrationSite;
        this.controlCollectionSite = controlCollectionSite;
        this.assaultParties = assaultParties;
    }

   
    /**
     *  Get Master Thief state.
     *
     *  @return Master Thief state
     */
    public int getMTState() 
    {
        return MTState;
    }

    /**
     *   Set Master Thief state.
     *
     *   @param state New Master Thief state
     */
    public void setMTState(int MTState) 
    {
        this.MTState = MTState;

    }

    /**
     *  Get collected canvas    
     * 
     *  @return  number of collected canvas
     */
    public int getCollectedCanvas()
    {
        return collectedCanvas;
    }

    /**
     *  Set collected canvas.
     *    
     *  @param collectedCanvas  collected canvas
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
        concentrationSite.startOperations();

        while(!heistFinished)
        {
            int transition = concentrationSite.appraiseSit();

            if (transition == MasterThiefStates.DECIDING_WHAT_TO_DO)
            {
                concentrationSite.waitForThieves();
            }else if (transition == MasterThiefStates.ASSEMBLING_A_GROUP)
            {
                int assaultPartyID = concentrationSite.assignAssaultPartyID();
                int roomID = concentrationSite.assignRoomID();
                concentrationSite.setAssignedRoomID(assaultPartyID, roomID);

                int [] assaultP = concentrationSite.buildParty();
                assaultParties[assaultPartyID].addThievesToParty(assaultP, roomID);
                concentrationSite.prepareAssaultParty(assaultP, assaultPartyID, roomID); 

                concentrationSite.sendAssaultParty();

            }else if (transition == MasterThiefStates.WAITING_FOR_ARRIVAL){
                controlCollectionSite.takeARest();          
                Boolean [] resetAP = controlCollectionSite.collectACanvas(); 
                
                // resetAP
                for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
                {
                    if (resetAP[i] == true)
                    {
                        assaultParties[i].resetAssaultParty();
                        concentrationSite.setAssigedAPid(i, false);
                        concentrationSite.setAssignedRoomID(i, -1);
                    }
                }

            }else { 
                concentrationSite.sumUpResults();  
                heistFinished = true;
            }
        }
    }
}