package serverSide.sharedRegions;

import serverSide.entities.*;
import serverSide.main.*;
import clientSide.main.*;
import clientSide.stubs.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Control and Collection Site.
 *
 *    It is responsible to ...
 *    Is implemented as an implicit monitor.
 *    All public methods are executed in mutual exclusion.
 *    There are ... internal synchronization points: .....
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ControlCollectionSite 
{
    /** 
     * flags to indicate if the Master Thief has to collect a canvas  and ordinary thief has to hand a canvas
     */
    private Boolean handACanvas;

    /**
     *  Identification of the assault party that arrived
     */
    private int [] arrivedAP;   // index = 0-> AP0 (n de elementos q chegaram)
    
    /**
     * Identification of the thieves who arrived.
     */
    private int [] arrivedThieves_APid; 
    
    /**
     *  True if thief delivered the cv
     */
    private Boolean [] collectCV; 
    
    /**
     * Number of thieves of an assaultParty that have arrived.
     */
    private int [] arrivedThievesFromAssault;

    /**
     * counter for the total number of canvas collected
     */
    private int collectedCanvas;

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
    public ControlCollectionSite (GeneralRepositoryStub reposStub)
    {
        this.handACanvas = false;
        this.collectedCanvas = 0;

        arrivedAP = new int [SimulPar.NUM_ASSAULT_PARTIES];
        collectCV = new Boolean[SimulPar.NUM_ORD_THIEVES];
        arrivedThieves_APid = new int [SimulPar.NUM_ORD_THIEVES];
        for (int i = 0; i < SimulPar.NUM_ORD_THIEVES; i++)
        {
            arrivedThieves_APid[i] = -1; 
            collectCV[i] = false;
        }
        
        this.arrivedThievesFromAssault = new int[SimulPar.NUM_ASSAULT_PARTIES];
        for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
        {
            arrivedThievesFromAssault[i] = 0;
        }

        nEntities = 0;
        this.reposStub = reposStub;
    }

    /**
     *  Transition take A Rest in the life cycle of the Master Thief.
     *  (DECIDING_WHAT_TO_DO -> WAITING_FOR_ARRIVAL)
     * 
     *  Master thief is waken up by the operation handACanvas of one of
     *  the assault party members returning from the museum.
     * 
     */
    public synchronized void takeARest()
    {
        ControlCollecSiteClientProxy masterThief = (ControlCollecSiteClientProxy) Thread.currentThread();     
        masterThief.setMTState(MasterThiefStates.WAITING_FOR_ARRIVAL);

        // update general repository    -- info state Master Thief
        reposStub.setMasterThiefState(masterThief.getMTState());   
        
        // System.out.println("Control and Collection Site - master is takeARest \n");

        while(!handACanvas)
        {
            try {
                // System.out.println("Master - takeARest waiting \n");
                wait();
            } catch (InterruptedException e) {}
        }

        handACanvas = false;
    }
    
    /**
     *  Transition hand a canvas in the life cycle of the ordinary Thief.
     *  (COLLECTION_SITE -> COLLECTION_SITE)
     * 
     *  The thief hands the master the paintings he stole.
     *  The ordinary thief is waken up by the operation of the master thief: collectACanvas.
     * 
     *      @param roomID               room id
     *      @param currentThiefIndex    current thief index
     */
    public synchronized void handACanvas(int roomID, int currentThiefIndex)
    {   
        ControlCollecSiteClientProxy ordinaryThief = (ControlCollecSiteClientProxy) Thread.currentThread();  
        ordinaryThief.setOTState(OrdinaryThiefStates.COLLECTION_SITE);

        // update general repository    -- info state ordinary Thief 
        reposStub.setOTState(ordinaryThief.getThiefID(), ordinaryThief.getOTState());

        int thiefID = ordinaryThief.getThiefID();
        int assaultPartyID = ordinaryThief.getAParty_ID();

        // System.out.println("OT - handACanvas thief ID " + thiefID + " assaultPartyID " + assaultPartyID + "\n");
        arrivedThievesFromAssault[assaultPartyID]++;

        if(ordinaryThief.getAParty_hasCanvas() == 1)
        {
            collectedCanvas++;
            ordinaryThief.setAParty_hasCanvas(0); 

            // update general repository    -- info hasACanvas ordinary Thief 
            reposStub.setAParty_Elem_CV(assaultPartyID, currentThiefIndex , ordinaryThief.getAParty_hasCanvas());
        }

        arrivedAP[assaultPartyID]++;
        if (arrivedThieves_APid[thiefID] == -1) // 
        {
            arrivedThieves_APid[thiefID] = assaultPartyID;
        }

        if (arrivedThievesFromAssault[assaultPartyID] == SimulPar.K)
        {
            //System.out.println("Wake up MASTER takeARest !!!  \n");
            arrivedThievesFromAssault[assaultPartyID] = 0;
            handACanvas = true;     // wake up Master - takeARest()
            notifyAll();    // take a rest do master
        } 

        while(!collectCV[thiefID])
        {
            try {
                // System.out.println("OT - handACanvas waiting thief ID " + thiefID + " assaultPartyID " + assaultPartyID + "\n");
                wait();
            } catch (InterruptedException e) {}
        }
        collectCV[thiefID] = false; 
        arrivedThieves_APid[thiefID] = -1;
    }


    /**
     *  Transition collect a canvas in the life cycle of the Master Thief.
     *  (WAITING_FOR_ARRIVAL -> DECIDING_WHAT_TO_DO)
     * 
     *  The Master Thief collects the canvas from the ordinary thief.
     *  The Master notify the ordinary thief that has the canvas.
     * 
     *      @return     assault parties for reseting
     */
    public synchronized Boolean [] collectACanvas()
    {
        ControlCollecSiteClientProxy masterThief = (ControlCollecSiteClientProxy) Thread.currentThread();

        masterThief.setCollectedCanvas(collectedCanvas);

        //System.out.println("Master - collect a CV \n");

        Boolean [] resetAP = new Boolean[SimulPar.NUM_ASSAULT_PARTIES];

        for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
        {
            resetAP[i] = false; // not reset AP
            if (arrivedAP[i] == 3)  // AP que chegou = i
            {
                for (int j = 0; j < SimulPar.NUM_ORD_THIEVES; j++)
                {
                    if (arrivedThieves_APid[j] == i)    // wake up thief AP i
                    {
                        collectCV[j] = true;
                    }
                }  

                arrivedAP[i] = 0; // reset
                resetAP[i] = true; // reset AP
            }
        }
        notifyAll();    // notify -> hand a canvas 

        return resetAP;
    }

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     */
    public synchronized void shutdown ()
    {
        nEntities += 1;
        if (nEntities >= SimulPar.E_CONTROLS)
            ServerHeistMuseumControlSite.waitConnection = false;
    }

    /**
     *  Get the collected canvas. 
     * 
     *      @return collected canvas
     */
    public synchronized int getCollectedCanvas()
    {
        return collectedCanvas;
    }

    // public void showarrivedAP()
    // {
    //     System.out.print("OT - handACanvas arrivedAP   ");
    //     for (int i = 0; i < 2; i++)
    //     {
    //         System.out.print(arrivedAP[i] + " ");
    //     }
    //     System.out.println("\n");
    // }

    // public void showarrivedThieves_APid()
    // {
    //     System.out.print("OT - handACanvas arrivedThieves_APid    ");
    //     for (int i = 0; i < 6; i++)
    //     {
    //         System.out.print(arrivedThieves_APid[i] + " ");
    //     }
    //     System.out.println("\n");
    // }

    // public void showcollectCV()
    // {
    //     System.out.print("OT - handACanvas collectCV  ");
    //     for (int i = 0; i < 6; i++)
    //     {
    //         System.out.print(collectCV[i] + " ");
    //     }
    //     System.out.println("\n");
    // }
}
