package serverSide.objects;

import java.rmi.registry.*;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import genclass.GenericIO;
import java.util.*;

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
public class ControlCollectionSite implements ControlCollecSiteInterface
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
    public ControlCollectionSite (GeneralReposInterface reposStub)
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
     *      @return new master thief state
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized int takeARest() throws RemoteException
    {
        int masterThiefState = MasterThiefStates.WAITING_FOR_ARRIVAL;
        // ControlCollecSiteClientProxy masterThief = (ControlCollecSiteClientProxy) Thread.currentThread();     
        // masterThief.setMTState(MasterThiefStates.WAITING_FOR_ARRIVAL);

        // update general repository    -- info state Master Thief
        reposStub.setMasterThiefState(masterThiefState);   
        
        // System.out.println("Control and Collection Site - master is takeARest \n");

        while(!handACanvas)
        {
            try {
                // System.out.println("Master - takeARest waiting \n");
                wait();
            } catch (InterruptedException e) {}
        }

        handACanvas = false;

        return masterThiefState;
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
     *      @param thiefID  ordinary thief identification
     *      @param AParty_ID    ordinary thief assault party id
     *      @param AParty_hasCanvas true if ordinary thief has a canvas
     *      @return true if ordinary thief has a canvas
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized int handACanvas(int roomID, int currentThiefIndex, int thiefID, int AParty_ID, int AParty_hasCanvas) throws RemoteException
    {   
        int ordinaryThiefState,
        OT_hasCV;

        OT_hasCV = 1;
        ordinaryThiefState = OrdinaryThiefStates.COLLECTION_SITE;

        // try {
        //     Thread.sleep(100);
        // } catch (InterruptedException e) { }


        // update general repository    -- info state ordinary Thief 
        reposStub.setOTState(thiefID, ordinaryThiefState);

        //int ordianryThiefID = thiefID;
        //int assaultPartyID = AParty_ID;

        // System.out.println("OT - handACanvas thief ID " + thiefID + " assaultPartyID " + assaultPartyID + "\n");
        arrivedThievesFromAssault[AParty_ID]++;

        //if(ordinaryThief.getAParty_hasCanvas() == 1)
        if(AParty_hasCanvas == 1)
        {
            collectedCanvas++;
            // ordinaryThief.setAParty_hasCanvas(0); 
            OT_hasCV = 0;

            // update general repository    -- info hasACanvas ordinary Thief 
            reposStub.setAParty_Elem_CV(AParty_ID, currentThiefIndex , OT_hasCV);
        }

        arrivedAP[AParty_ID]++;
        if (arrivedThieves_APid[thiefID] == -1) // 
        {
            arrivedThieves_APid[thiefID] = AParty_ID;
        }

        if (arrivedThievesFromAssault[AParty_ID] == SimulPar.K)
        {
            //System.out.println("Wake up MASTER takeARest !!!  \n");
            arrivedThievesFromAssault[AParty_ID] = 0;
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

        return OT_hasCV;
    }

    /**
     *  Transition collect a canvas in the life cycle of the Master Thief.
     *  (WAITING_FOR_ARRIVAL -> DECIDING_WHAT_TO_DO)
     * 
     *  The Master Thief collects the canvas from the ordinary thief.
     *  The Master notify the ordinary thief that has the canvas.
     * 
     *      @return  assault parties for reseting and collectedCanvas
     *       @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized ReturnArrayBoolean collectACanvas() throws RemoteException
    {
        //ControlCollecSiteClientProxy masterThief = (ControlCollecSiteClientProxy) Thread.currentThread();

        //masterThief.setCollectedCanvas(collectedCanvas);

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

        return new ReturnArrayBoolean (resetAP, collectedCanvas);
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
        if (nEntities >= SimulPar.E_CONTROLS)
            ServerHeistMuseumControlSite.shutdown();
    }
}