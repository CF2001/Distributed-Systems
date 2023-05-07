package entities;

import sharedRegions.*;

/**
 *   Ordinary Thief thread.
 *
 *   It simulates the Ordinary Thief life cycle.
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
     *  Reference to the Concentration Site.
     */
    private final ConcentrationSite concentrationSite;

    /**
     *  Reference to the Control & Collection Site.
     */
    private final ControlCollectionSite controlCollectionSite;

    /**
     *  Reference to the Assault Party.
     */
    private final AssaultParty [] assaultParties;

    /**
     *  Reference to the Museum.
     */
    private final Museum museum;


    /**
     *  Ordinary Thief Thread instantiation.
     * 
     * @param name  Thread name
     * @param thiefID   Ordinary Thief id 
     * @param thiefMaxDisp  Ordinary Thief max displacement
     * @param concentrationSite Reference to the Concentration Site
     * @param controlCollectionSite Reference to the Control & Collection Site
     * @param assaultParties    Reference to the Assault Party
     * @param museum    Reference to the museum
     */

    public OrdinaryThief(String name, int thiefID, int thiefMaxDisp, 
                            ConcentrationSite concentrationSite, 
                            ControlCollectionSite controlCollectionSite, 
                            AssaultParty [] assaultParties,
                            Museum museum)
    {
        super(name);
        this.OTState = OrdinaryThiefStates.CONCENTRATION_SITE;
        this.thiefID = thiefID;
        this.thiefMaxDisp = thiefMaxDisp; 
        this.AParty_ID = -1;
        this.AParty_position = 0;
        this.AParty_hasCanvas = 0;


        this.concentrationSite = concentrationSite;
        this.controlCollectionSite = controlCollectionSite;
        this.assaultParties = assaultParties;
        this.museum = museum;
    }

    /**
     *  Get Ordinary Thief state.
     * 
     *  @return Ordinary Thief state
     */
    public int getOTState() 
    {
        return OTState;
    }

    /**
     *  Set Ordinary Thief state.
     * 
     *  @param OTState  New Ordinary Thief state
     */
    public void setOTState(int OTState) 
    {
        this.OTState = OTState;
    }

    /**
     *  Get Ordinary Thief id.
     * 
     *  @return Ordinary Thief id
     */
    public int getThiefID() 
    {
        return thiefID;
    }

    /**
     * Set Ordinary Thief id.
     * 
     *  @param thiefID  New Ordinary Thief id
     */
    public void setThiefID(int thiefID) 
    {
        this.thiefID = thiefID;
    }

    /**
     *  Get Ordinary Thief maximum displacement.
     * 
     *  @return Ordinary Thief maximum displacement
     */
    public int getThiefMaxDisp() 
    {
        return thiefMaxDisp;
    }

    /**
     *  Set Ordinary Thief maximum displacement.
     * 
     *  @param thiefMaxDisp New Ordinary Thief maximum displacement
     */
    public void setThiefMaxDisp(int thiefMaxDisp) 
    {
        this.thiefMaxDisp = thiefMaxDisp;
    }

    /**
     *  Get Ordinary Thief assault party id.
     * 
     *  @return Ordinary Thief assault party id
     */
    public int getAParty_ID() 
    {
        return AParty_ID;
    }

    /**
     *  Set Ordinary Thief assault party id.
     * 
     *  @param AParty_ID New Ordinary Thief assault party id
     */
    public void setAParty_ID(int AParty_ID) 
    {
        this.AParty_ID = AParty_ID;
    }

    /**
     *  Get Ordinary Thief assault party position.
     * 
     *  @return Ordinary Thief assault party position
     */
    public int getAParty_position() 
    {
        return AParty_position;
    }

    /**
     *  Set Ordinary Thief assault party position.
     * 
     *  @param AParty_position New Ordinary Thief assault party position
     */
    public void setAParty_position(int AParty_position) 
    {
        this.AParty_position = AParty_position;
    }

    /**
     *  Get Ordinary Thief assault party hasCanvas.
     * 
     *  @return Ordinary Thief assault party hasCanvas
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
        while(concentrationSite.amINeeded())
        {   
            // try {
            // Thread.sleep(100);
            // } catch (InterruptedException e) {
            // }
            
            concentrationSite.prepareExcursion();
            assaultParties[AParty_ID].moveFirstPartyMember(true); 
        
            //System.out.println("Crawl in ..... thread -> " + thiefID + "\n");
            assaultParties[this.AParty_ID].crawlIN();

            //System.out.println("\n Cheguei ao quarto!!! partyID: " + AParty_ID + " thiefID: " + thiefID + " AParty_position: " + AParty_position);
            museum.rollACanvas(assaultParties[AParty_ID].getAssaultP_roomID());

            //System.out.println("\n A sair do ROOM !! partyID: " + AParty_ID + " thiefID: " + thiefID + " AParty_hasCanvas: " + AParty_hasCanvas + "\n ");
            assaultParties[this.AParty_ID].reverseDirection();

            assaultParties[this.AParty_ID].crawlOUT();  
            //System.out.println("Crawl out ..... thread -> " + thiefID + "\n");

            int roomID = assaultParties[AParty_ID] .getAssaultP_roomID();
            int currentThiefIndex = assaultParties[AParty_ID].getThiefIndex();
            if (AParty_hasCanvas == 0)
            {
                concentrationSite.roomStates().set(roomID, false); 
            }
            controlCollectionSite.handACanvas(roomID, currentThiefIndex);
        }  
    }
    
}