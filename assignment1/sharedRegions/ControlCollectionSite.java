package sharedRegions;

import main.*;
import entities.*;

public class ControlCollectionSite
{   
    /** 
     * flags to indicate if the Master Thief has to collect a canvas  and ordinary thief has to hand a canvas
     */
    private Boolean handACanvas;

    private int [] arrivedAP;   // index = 0-> AP0 (n de elementos q chegaram)
    private int [] arrivedThieves_APid; // id da AP dos ladroes que chegaram 
    private Boolean [] collectCV;   // true if thief delivered the cv
    
    /**
     * Number of thieves of an assaultParty that have arrived.
     */
    private int [] arrivedThievesFromAssault;

    /**
     * counter for the total number of canvas collected
     */
    private int collectedCanvas;

    /**
     *   Reference to the general repository.
     */
    private final GeneralRepository repos;


    /**
     *  Master Thief - Control & Collection Site instantiation.
     *
     *    @param repos reference to the general repository
     */
    public ControlCollectionSite(GeneralRepository repos)
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

        this.repos = repos;
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
        MasterThief masterThief = (MasterThief) Thread.currentThread();
        masterThief.setMTState(MasterThiefStates.WAITING_FOR_ARRIVAL);

        // update general repository    -- info state Master Thief
        repos.setMasterThiefState(masterThief.getMTState());   

        //System.out.println("Master - takeARest \n");

        while(!handACanvas)
        {
            try {
                //System.out.println("Master - takeARest waiting \n");
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
     */
    public synchronized void handACanvas(int roomID, int currentThiefIndex)
    {
        OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
        ordinaryThief.setOTState(OrdinaryThiefStates.COLLECTION_SITE);
        // update general repository    -- info state ordinary Thief 
        repos.setOTState(ordinaryThief.getThiefID(), ordinaryThief.getOTState());

        int thiefID = ordinaryThief.getThiefID();
        int assaultPartyID = ordinaryThief.getAParty_ID();

        //System.out.println("OT - handACanvas thief ID " + thiefID + " assaultPartyID " + assaultPartyID + "\n");

        arrivedThievesFromAssault[assaultPartyID]++;

        if(ordinaryThief.getAParty_hasCanvas() == 1)
        {
            collectedCanvas++;
            ordinaryThief.setAParty_hasCanvas(0); 

            // update general repository    -- info hasACanvas ordinary Thief 
            repos.setAParty_Elem_CV(assaultPartyID, currentThiefIndex , ordinaryThief.getAParty_hasCanvas());
        }

        arrivedAP[assaultPartyID]++;
        if (arrivedThieves_APid[thiefID] == -1) // 
        {
            arrivedThieves_APid[thiefID] = assaultPartyID;
        }
        //showarrivedAP();
        //showarrivedThieves_APid();

        //System.out.println("arrivedThievesFromAssault[ " + assaultPartyID + " ] " + arrivedThievesFromAssault[assaultPartyID] + "\n");
        if (arrivedThievesFromAssault[assaultPartyID] == SimulPar.K)
        {
            //System.out.println("Wake up MASTER takeARest !!!  \n");
            arrivedThievesFromAssault[assaultPartyID] = 0;
            handACanvas = true;     // wake up Master - takeARest()
            notifyAll();    // take a rest do master
        } 

        //while(!collectACanvas)
        while(!collectCV[thiefID])
        {
            try {
                //System.out.println("OT - handACanvas waiting thief ID " + thiefID + " assaultPartyID " + assaultPartyID + "\n");
                wait();
            } catch (InterruptedException e) {}
        }
        collectCV[thiefID] = false; 
        arrivedThieves_APid[thiefID] = -1;  

        //System.out.println("OT - handACanvas collectCV a sair thief ID "  + thiefID + " \n");
        //showcollectCV();
        // showarrivedAP();
        // showarrivedThieves_APid();
    }
    
    /**
     *  Transition collect a canvas in the life cycle of the Master Thief.
     *  (WAITING_FOR_ARRIVAL -> DECIDING_WHAT_TO_DO)
     * 
     *  The Master Thief collects the canvas from the ordinary thief.
     *  The Master notify the ordinary thief that has the canvas.
     * 
     */
    public synchronized Boolean [] collectACanvas()
    {
        MasterThief masterThief = (MasterThief) Thread.currentThread();

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
        //showcollectCV();
        notifyAll();    // notify -> hand a canvas 

        return resetAP;
    }

    public synchronized void showarrivedAP()
    {
        System.out.print("OT - handACanvas arrivedAP   ");
        for (int i = 0; i < 2; i++)
        {
            System.out.print(arrivedAP[i] + " ");
        }
        System.out.println("\n");
    }

    public synchronized void showarrivedThieves_APid()
    {
        System.out.print("OT - handACanvas arrivedThieves_APid    ");
        for (int i = 0; i < 6; i++)
        {
            System.out.print(arrivedThieves_APid[i] + " ");
        }
        System.out.println("\n");
    }

    public synchronized void showcollectCV()
    {
        System.out.print("OT - handACanvas collectCV  ");
        for (int i = 0; i < 6; i++)
        {
            System.out.print(collectCV[i] + " ");
        }
        System.out.println("\n");
    }

    /**
     *  Get the collected canvas. 
     * 
     * @return collected canvas
     */
    public synchronized int getCollectedCanvas()
    {
        return collectedCanvas;
    }

}