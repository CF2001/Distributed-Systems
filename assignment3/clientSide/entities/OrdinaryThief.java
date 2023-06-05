package clientSide.entities;

import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;

/**
 *    Ordinary Thief thread.
 *
 *      It simulates the ordinary thief life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on remote calls under Java RMI.
 */
public class OrdinaryThief extends Thread
{
    /**
     *  State of the Ordinary Thief.
     */
    private int OTState; 

    /**
    *  Ordinary Thief identification.
    */
    private int thiefID; 

    /**
     *  Maximum displacement of the Ordinary Thief
     */
    private int thiefMaxDisp;

    /**
     *  Id of the Assault Party to which the Ordinary Thief belongs.
     */
    private int AParty_ID;       

    /**
     *  Position of the Ordinary Thief in the Assault Party.
     */
    private int AParty_position;  

    /**
     *  hasCanvas = 1 if the Ordinary Thief has a canvas in the Assault Party.
     */
    private int AParty_hasCanvas; 

    /**
     *  Remote reference to the stub of the Concentration Site.
     */
    private final ConcSiteInterface concentrationSiteStub; 

    /**
     *  Remote reference to the stub of the Control & Collection Site.
     */
    private final ControlCollecSiteInterface controlCollectionSiteStub;

    /**
     *  Remote reference to the stub of the Assault Party.
     */
    private final AssaultPartyInterface [] assaultPartiesStub;

    /**
     *  Remote reference to the stub of the Museum.
     */
    private final MuseumInterface museumStub;

    /**
     *  Ordinary Thief Thread instantiation.
     * 
     *      @param name                         Thread name
     *      @param thiefID                      Ordinary Thief id 
     *      @param thiefMaxDisp                 Ordinary Thief max displacement
     *      @param concentrationSiteStub        Remote reference to the stub of the Concentration Site
     *      @param controlCollectionSiteStub    Remote reference to the stub of the Control and Collection Site
     *      @param assaultPartiesStub           Remote reference to the stub of the Assault Party
     *      @param museumStub                   Remote reference to the stub of the museum
     */
    public OrdinaryThief(String name, int thiefID, int thiefMaxDisp, 
                            ConcSiteInterface concentrationSiteStub, 
                            ControlCollecSiteInterface controlCollectionSiteStub, 
                            AssaultPartyInterface [] assaultPartiesStub,
                            MuseumInterface museumStub)
    {
        super(name);
        this.OTState = OrdinaryThiefStates.CONCENTRATION_SITE;
        this.thiefID = thiefID;
        this.thiefMaxDisp = thiefMaxDisp; 
        this.AParty_ID = -1;
        this.AParty_position = 0;
        this.AParty_hasCanvas = 0;


        this.concentrationSiteStub = concentrationSiteStub;
        this.controlCollectionSiteStub = controlCollectionSiteStub;
        this.assaultPartiesStub = assaultPartiesStub;
        this.museumStub = museumStub;
    }

    /**
     *   Life cycle of the Ordinary Thief.
     */
    @Override
    public void run() 
    {   
        while(amINeeded())
        {   
            prepareExcursion();
            moveFirstPartyMember(true); 
        
            //System.out.println("Thief ID = " + thiefID + " going to crawl in ... \n");
            while ( AParty_position != getDistFromOutside())
            {
                crawlIN();
            }

            //System.out.println("Thief ID = " + thiefID + " going to steal a painting ... \n");

            int roomID = getAssaultP_roomID();
            rollACanvas(roomID);

            //System.out.println("Thief ID = " + thiefID + " rollCV DONE --> going to reverse Direction ... \n");
            reverseDirection();

            //System.out.println("Thief ID = " + thiefID + " going to crawl out ... \n");
            while( AParty_position != 0)
            {
                crawlOUT();
            }

            //System.out.println("Thief ID = " + thiefID + " a sair do crawl out ... \n");

            roomID = getAssaultP_roomID();
            int currentThiefIndex = getThiefIndex(thiefID);
            if (AParty_hasCanvas == 0)
            {
                setRoomStates(roomID, false);
            }
            //System.out.println("Thief ID = " + thiefID + " going to hand a CV ... \n");

            handACanvas(roomID, currentThiefIndex);

            //System.out.println("Thief ID = " + thiefID + " thief CV " + AParty_hasCanvas + " \n");
        }  
    }


    /**
     *  Transition am I Needed in the life cycle of the Ordinary Thief.
     *   
     *  When thieves are needed they are added to the waiting queue and notify the master. (waitForThieves())
     *  
     *  Thieves are blocked until they are called to form the assault groups.
     *  Or Thieves are blocked until they are no longer needed. 
     * 
     *  Remote operation.
     * 
     *      @return  True if the heist is not finished and the ordinary thieves are still needed
     */
    public Boolean amINeeded()
    {
        ReturnBoolean ret = null;                            // return value

        try
        { 
            ret = concentrationSiteStub.amINeeded(thiefID);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Ordinary Thief " + thiefID + " remote exception on amINeeded: " + e.getMessage ());
            System.exit (1);
        }

        OTState = ret.getIntStateVal();
        AParty_ID = -1;

        return ret.getBooleanVal ();
    }

    /**
     *  Transition prepare Excursion in the life cycle of the Ordinary Thief.
     *  CONCENTRATION_SITE -> CRAWLING_INWARDS
     * 
     *  The thief notifies the master that the last member of the group has arrived.
     * 
     *  Remote operation.
     */
    public void prepareExcursion()
    {
        ReturnInt ret = null;     // return value

        try
        { 
            ret = concentrationSiteStub.prepareExcursion(thiefID);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Ordinary Thief " + thiefID + " remote exception on prepareExcursion: " + e.getMessage ());
            System.exit (1);
        }

        AParty_ID = ret.getIntVal();  //ordinaryThief.setAParty_ID(assaultPartyID); 
        OTState = ret.getIntStateVal(); //  ordinaryThief.setOTState(OrdinaryThiefStates.CRAWLING_INWARDS);
    }

    /**
     *  Indicates whether the first member of the party has been notified by the master 
     *  on sendAssaultParty to proceed.
     * 
     *  Remote operation.
     * 
     *      @param firstPartyMember movement status of the first member of the assault party
     */
    public void moveFirstPartyMember(Boolean firstPartyMember)
    {
        try
        { 
            assaultPartiesStub[this.AParty_ID].moveFirstPartyMember(firstPartyMember); 
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Ordinary Thief " + thiefID + " remote exception on moveFirstPartyMember: " + e.getMessage ());
            System.exit (1);
        }
    }

    /**
     *  Transition crawling inwards in the life cycle of the Ordinary Thief.
     * 
     *  Remote operation.
     */
    public void crawlIN()
    {
        try
        { 
            AParty_position = assaultPartiesStub[this.AParty_ID].crawlIN(thiefID, thiefMaxDisp, AParty_position, AParty_ID);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Ordinary Thief " + thiefID + " remote exception on crawlIN: " + e.getMessage ());
            System.exit (1);
        }
    }

    /**
     *  Getting distance from outiside of a room.
     * 
     *  Remote operation.
     * 
     *      @return distance from outiside of a room.
     */
    public int getDistFromOutside ()
    {
        int distance = 0;

        try
        { 
            distance = assaultPartiesStub[this.AParty_ID].getDistFromOutside();
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Ordinary Thief " + thiefID + " remote exception on getDistFromOutside: " + e.getMessage ());
            System.exit (1);
        }

        return distance; 
    }

    /**
     *  Getting distance from outiside of a room.
     * 
     *  Remote operation.
     * 
     *      @return distance from outiside of a room.
     */
    public int getAssaultP_roomID ()
    {
        int roomID = 0;

        try
        { 
            roomID =  assaultPartiesStub[AParty_ID].getAssaultP_roomID();
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Ordinary Thief " + thiefID + " remote exception on getAssaultP_roomID: " + e.getMessage ());
            System.exit (1);
        }

        return roomID; 
    }


    /**
    *   Transition roll a canvas in the life cycle of the Ordinary Thief.
    *
    *   Remote operation.
    *
    *       @param roomID  room identification
    */
    public void rollACanvas(int roomID)
    {
        ReturnInt ret = null;

        try
        { 
            ret =   museumStub.rollACanvas(roomID, thiefID);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Ordinary Thief " + thiefID + " remote exception on rollACanvas: " + e.getMessage ());
            System.exit (1);
        }

        AParty_hasCanvas = ret.getIntVal();
        OTState =  ret.getIntStateVal();
    }

    /**
     *  Transition crawling inwards in the life cycle of the Ordinary Thief.
     * 
     *  Remote operation.
     *  
     */
    public void reverseDirection()
    {
        try
        { 
            OTState = assaultPartiesStub[this.AParty_ID].reverseDirection(thiefID, AParty_hasCanvas);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Ordinary Thief " + thiefID + " remote exception on reverseDirection: " + e.getMessage ());
            System.exit (1);
        }
    }

    /**
     *  Transition crawling outwards in the life cycle of the Ordinary Thief.
     *  
     *   Remote operation.
     */
    public void crawlOUT()
    {
        try
        { 
            AParty_position = assaultPartiesStub[this.AParty_ID].crawlOUT(thiefID, thiefMaxDisp, AParty_position, AParty_ID);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Ordinary Thief " + thiefID + " remote exception on crawlIN: " + e.getMessage ());
            System.exit (1);
        }
    }


    /**
     *  Get the assault party thief index.
     * 
     *  Remote operation.
     * 
     *      @param thiefID ordinary thief id 
     *      @return  index of array in the assault party that the thief belongs to
     */
    public int getThiefIndex(int thiefID) 
    {
        int currentThiefIndex = 0;

        try
        { 
            assaultPartiesStub[AParty_ID].getThiefIndex(thiefID);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Ordinary Thief " + thiefID + " remote exception on getThiefIndex: " + e.getMessage ());
            System.exit (1);
        }
        return currentThiefIndex;
    }

    /**
     *  Update room status. True if empty.
     * 
     *  Remote operation.
     * 
     *      @param roomID   room identification.
     *      @param roomState    room status.
     */
    public void setRoomStates(int roomID, Boolean roomState)
    {
        try
        { 
            concentrationSiteStub.setRoomStates(roomID, roomState);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Ordinary Thief " + thiefID + " remote exception on setRoomStates: " + e.getMessage ());
            System.exit (1);
        }
    }

    /**
     *  Transition hand a canvas in the life cycle of the ordinary Thief.
     *  
     *  Remote operation.
     * 
     *      @param roomID               room id
     *      @param currentThiefIndex    current thief index
     */
    public void handACanvas(int roomID, int currentThiefIndex)
    {
        try
        { 
            
            AParty_hasCanvas = controlCollectionSiteStub.handACanvas(roomID, currentThiefIndex, thiefID, AParty_ID, AParty_hasCanvas);
        }
        catch (RemoteException e)
        { 
            GenericIO.writelnString ("Ordinary Thief " + thiefID + " remote exception on handACanvas: " + e.getMessage ());
            System.exit (1);
        }
    }

}
