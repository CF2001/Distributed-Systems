package serverSide.entities;

import serverSide.sharedRegions.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Service provider agent for access to the Assault Party.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class AssaultPartyClientProxy extends Thread implements MasterThiefCloning, OrdinaryThiefCloning
{
    /**
     *  Number of instantiayed threads.
     */
    private static int nProxy = 0;

    /**
    *  Communication channel.
    */
    private ServerCom sconi;

    /**
    *  Interface to the Assault Party of Information.
    */
    private AssaultPartyInterface assaultPartyInter;

    /**
     *  State of the Master Thief.
     */
    private int MTState;

    /**
     *  Number of canvas collected by the Master Thief.
     */
    private int collectedCanvas;

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
    *  Instantiation of a client proxy.
    *
    *     @param sconi communication channel
    *     @param assaultPartyInter interface to the assault party of information
    */
    public AssaultPartyClientProxy (ServerCom sconi, AssaultPartyInterface assaultPartyInter)
    {
        super ("AssaultPartyClientProxy" + AssaultPartyClientProxy.getProxyId ());
        this.sconi = sconi;
        this.assaultPartyInter = assaultPartyInter;
    }

    /**
     *  Generation of the instantiation identifier.
     *
     *     @return instantiation identifier
     */
    private static int getProxyId ()
    {
        Class<?> cl = null;          // representation of the AssaultPartyClientProxy object in JVM
        int proxyId;                 // instantiation identifier

        try
        { cl = Class.forName ("serverSide.entities.AssaultPartyClientProxy");
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("Data type AssaultPartyClientProxy was not found!");
        e.printStackTrace ();
        System.exit (1);
        }
        synchronized (cl)
        { proxyId = nProxy;
        nProxy += 1;
        }
        return proxyId;
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
     *   @param MTState New Master Thief state
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
     *      @param collectedCanvas  collected canvas
     */
    public void setCollectedCanvas(int collectedCanvas)
    {
        this.collectedCanvas = collectedCanvas;
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
     *      @param AParty_hasCanvas New Ordinary Thief assault party hasCanvas
     */
    public void setAParty_hasCanvas(int AParty_hasCanvas) 
    {
        this.AParty_hasCanvas = AParty_hasCanvas;
    } 

    /**
     *  Life cycle of the service provider agent.
     */
    @Override
    public void run ()
    {
        Message inMessage = null,                                 // service request
                outMessage = null;                                // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject ();                // get service request
        try
        { outMessage = assaultPartyInter.processAndReply (inMessage);    // process it
        }
        catch (MessageException e)
        { GenericIO.writelnString ("Thread " + getName () + ": " + e.getMessage () + "!");
        GenericIO.writelnString (e.getMessageVal ().toString ());
        System.exit (1);
        }
        sconi.writeObject (outMessage);                                // send service reply
        sconi.close ();                                                // close the communication channel
    }    
}