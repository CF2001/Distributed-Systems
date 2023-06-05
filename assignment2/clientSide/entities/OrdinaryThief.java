package clientSide.entities;

import clientSide.stubs.*;

/**
 *    Ordinary Thief thread.
 *
 *      It simulates the ordinary thief life cycle.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
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
     *  Reference to the stub of the Concentration Site.
     */
    private final ConcentrationSiteStub concentrationSiteStub; 

    /**
     *  Reference to the stub of the Control & Collection Site.
     */
    private final ControlCollectionSiteStub controlCollectionSiteStub;

    /**
     *  Reference to the stub of the Assault Party.
     */
    private final AssaultPartyStub [] assaultPartiesStub;

    /**
     *  Reference to the stub of the Museum.
     */
    private final MuseumStub museumStub;

    /**
     *  Ordinary Thief Thread instantiation.
     * 
     *      @param name  Thread name
     *      @param thiefID   Ordinary Thief id 
     *      @param thiefMaxDisp  Ordinary Thief max displacement
     *      @param concentrationSiteStub Reference to the stub of the Concentration Site
     *      @param controlCollectionSiteStub Reference to the stub of the Control and Collection Site
     *      @param assaultPartiesStub    Reference to the stub of the Assault Party
     *      @param museumStub    Reference to the stub of the museum
     */
    public OrdinaryThief(String name, int thiefID, int thiefMaxDisp, 
                            ConcentrationSiteStub concentrationSiteStub, 
                            ControlCollectionSiteStub controlCollectionSiteStub, 
                            AssaultPartyStub [] assaultPartiesStub,
                            MuseumStub museumStub)
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
     *  Get Ordinary Thief state.
     * 
     *      @return Ordinary Thief state
     */
    public int getOTState() 
    {
        return OTState;
    }

    /**
     *  Set Ordinary Thief state.
     * 
     *      @param OTState  New Ordinary Thief state
     */
    public void setOTState(int OTState) 
    {
        this.OTState = OTState;
    }

    /**
     *  Get Ordinary Thief id.
     * 
     *      @return Ordinary Thief id
     */
    public int getThiefID() 
    {
        return thiefID;
    }

    /**
     * Set Ordinary Thief id.
     * 
     *      @param thiefID  New Ordinary Thief id
     */
    public void setThiefID(int thiefID) 
    {
        this.thiefID = thiefID;
    }

    /**
     *  Get Ordinary Thief maximum displacement.
     * 
     *      @return Ordinary Thief maximum displacement
     */
    public int getThiefMaxDisp() 
    {
        return thiefMaxDisp;
    }

    /**
     *  Set Ordinary Thief maximum displacement.
     * 
     *      @param thiefMaxDisp New Ordinary Thief maximum displacement
     */
    public void setThiefMaxDisp(int thiefMaxDisp) 
    {
        this.thiefMaxDisp = thiefMaxDisp;
    }

    /**
     *  Get Ordinary Thief assault party id.
     * 
     *      @return Ordinary Thief assault party id
     */
    public int getAParty_ID() 
    {
        return AParty_ID;
    }

    /**
     *  Set Ordinary Thief assault party id.
     * 
     *      @param AParty_ID New Ordinary Thief assault party id
     */
    public void setAParty_ID(int AParty_ID) 
    {
        this.AParty_ID = AParty_ID;
    }

    /**
     *  Get Ordinary Thief assault party position.
     * 
     *      @return Ordinary Thief assault party position
     */
    public int getAParty_position() 
    {
        return AParty_position;
    }

    /**
     *  Set Ordinary Thief assault party position.
     * 
     *      @param AParty_position New Ordinary Thief assault party position
     */
    public void setAParty_position(int AParty_position) 
    {
        this.AParty_position = AParty_position;
    }

    /**
     *  Get Ordinary Thief assault party hasCanvas.
     * 
     *      @return Ordinary Thief assault party hasCanvas
     */
    public int getAParty_hasCanvas() 
    {
        return AParty_hasCanvas;
    }

    /**
     *  Set Ordinary Thief assault party hasCanvas.
     * 
     *  @param AParty_hasCanvas New Ordinary Thief assault party hasCanvas
     */
    public void setAParty_hasCanvas(int AParty_hasCanvas) 
    {
        this.AParty_hasCanvas = AParty_hasCanvas;
    } 

   /**
     *   Life cycle of the Ordinary Thief.
     */
    @Override
    public void run() 
    {   
        while(concentrationSiteStub.amINeeded())
        {   
            concentrationSiteStub.prepareExcursion();
            assaultPartiesStub[AParty_ID].moveFirstPartyMember(true); 
        
            // System.out.println("Thief ID = " + thiefID + " going to crawl in ... \n");
            assaultPartiesStub[this.AParty_ID].crawlIN();

            // System.out.println("Thief ID = " + thiefID + " going to steal a painting ... \n");
            museumStub.rollACanvas(assaultPartiesStub[AParty_ID].getAssaultP_roomID());

            // System.out.println("Thief ID = " + thiefID + " rollCV DONE --> going to reverse Direction ... \n");
            assaultPartiesStub[this.AParty_ID].reverseDirection();

            // System.out.println("Thief ID = " + thiefID + " going to crawl out ... \n");
            assaultPartiesStub[this.AParty_ID].crawlOUT();  

            int roomID = assaultPartiesStub[AParty_ID] .getAssaultP_roomID();
            int currentThiefIndex = assaultPartiesStub[AParty_ID].getThiefIndex();
            if (AParty_hasCanvas == 0)
            {
                concentrationSiteStub.setRoomStates(roomID, false);
            }
            // System.out.println("Thief ID = " + thiefID + " going to hand a CV ... \n");
            controlCollectionSiteStub.handACanvas(roomID, currentThiefIndex);
            // System.out.println("Thief ID = " + thiefID + " thief CV " + AParty_hasCanvas + " \n");
        }  
    }
    
}
