package clientSide.stubs;

import serverSide.main.SimulPar;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;



/**
 *  Stub to the concentration site.
 *
 *    It instantiates a remote reference to the concentration site.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ConcentrationSiteStub
{
    /**
    *  Name of the platform where is located the concentration site server.
    */
    private String serverHostName;

    /**
    *  Port number for listening to service requests.
    */
    private int serverPortNumb;

    /**
     *  Instantiation of a concentration site stub.
     *
     *    @param hostName name of the platform where is located the control and concentration site server
     *    @param port port number where the server is listening to service requests
     */
    public ConcentrationSiteStub(String hostName, int port){
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /** 
    *   New state of the assault party. True if assigned, false otherwise.
    *
    *       @param assaultPartyID   assault party id
    *       @param state            status of the assault party
    */
    public void setAssigedAPid(int assaultPartyID, Boolean state)
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETASSAPID, assaultPartyID, state);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SETASSAPIDDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *  New room assigned to the assault party.
     * 
     *      @param assaultPartyID   assault party id
     *      @param roomID           assigned room id 
     */
    public void setAssignedRoomID(int assaultPartyID, int roomID)
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETASSRID, assaultPartyID, roomID);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SETASSRIDDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *  Transition start operations in the life cycle of the Master Thief.
     *  (PLANNING_THE_HEIST -> DECIDING_WHAT_TO_DO)
     * 
     *  It is called by the Master Thief when he wants to start the heist operation in the museum.
     */
    public void startOperations ()
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message
        
        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        
        outMessage = new Message (MessageType.STARTOP, ((MasterThief) Thread.currentThread()).getMTState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.STARTOPDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getMasterState() < MasterThiefStates.PLANNING_THE_HEIST) || (inMessage.getMasterState() > MasterThiefStates.PRESENTING_THE_REPORT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid Master Thief state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        
        ((MasterThief) Thread.currentThread()).setMTState(inMessage.getMasterState());
    }

    /**
     * Transition appraise situation in the life cycle of the Master Thief.
     * 
     * As long as the master thief remains in this transition -> state : DECIDING_WHAT_TO_DO
     * 
     *      @return  next transition (DECIDING_WHAT_TO_DO or ASSEMBLING_A_GROUP or WAITING_FOR_ARRIVAL or PRESENTING_THE_REPORT)
     */
    public int appraiseSit()
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.APPRAISESIT, ((MasterThief) Thread.currentThread()).getMTState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.APPRAISESITDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getMasterState() < MasterThiefStates.PLANNING_THE_HEIST) || (inMessage.getMasterState() > MasterThiefStates.PRESENTING_THE_REPORT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid Master Thief state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if((inMessage.getMasterTransition() < MasterThiefStates.DECIDING_WHAT_TO_DO) || (inMessage.getMasterTransition() > MasterThiefStates.PRESENTING_THE_REPORT))
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid Master Thief Transition!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close();

        //System.out.println("Appraise Situation: Master Trans = " + inMessage.getMasterTransition());
        ((MasterThief) Thread.currentThread()).setMTState(inMessage.getMasterState());

        return inMessage.getMasterTransition();
    }

    /**
     *      Master blocks until enough ordinary thieves have arrived the concentration site.
     *      Master is notified whenever a thief arrives at the concentration site (amINeeded()).
     */
    public void waitForThieves()
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.WAITTHIEVES);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        
        if (inMessage.getMsgType() != MessageType.WAITTHIEVESDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close();
    }

    /**
     *  Transition am I Needed in the life cycle of the Ordinary Thief.
     *   
     *  When thieves are needed they are added to the waiting queue and notify the master. (waitForThieves())
     *  
     *  CONCENTRATION_SITE -> CONCENTRATION_SITE : 
     *  Thieves are blocked until they are called to form the assault parties.
     *  Or Thieves are blocked until they are no longer needed. 
     * 
     *      @return  True if the heist is not finished and the ordinary thieves are still needed
     */
    public Boolean amINeeded()
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.AMINEEDED, ((OrdinaryThief) Thread.currentThread()).getOTState(), 
                                                        ((OrdinaryThief) Thread.currentThread()).getThiefID());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.AMINEEDEDDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if (inMessage.getOrdinaryID() != ((OrdinaryThief) Thread.currentThread ()).getThiefID())
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid ordinary thief id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getOrdinaryTState() < OrdinaryThiefStates.CONCENTRATION_SITE) || (inMessage.getOrdinaryTState() > OrdinaryThiefStates.COLLECTION_SITE))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid ordinary thief state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();

        ((OrdinaryThief) Thread.currentThread ()).setOTState(inMessage.getOrdinaryTState());

        return inMessage.getAmINeeded();
    }

    /**
     *  Master creates a 3-member assault party
     *      
     *      @return assault party with 3 thieves
     */
    public int [] buildParty()
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.BUILDP, ((MasterThief) Thread.currentThread()).getMTState());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        
        if (inMessage.getMsgType() != MessageType.BUILDPDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if (inMessage.getAssaultPBuild().length != SimulPar.K)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid assault party size!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getMasterState() < MasterThiefStates.PLANNING_THE_HEIST) || (inMessage.getMasterState() > MasterThiefStates.PRESENTING_THE_REPORT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid Master Thief state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();

        ((MasterThief) Thread.currentThread()).setMTState(inMessage.getMasterState());

        return inMessage.getAssaultPBuild();
    }

    /**
     *  Transition prepare Assault Party in the life cycle of the Master Thief.
     *  DECIDING_WHAT_TO_DO -> ASSEMBLING_A_GROUP
     * 
     *  The master starts by creating a group of 3 elements, then notifies the thieves waiting 
     *  on the queue. 
     * 
     *      @param assaultParty     assault party elements
     *      @param assaultPartyID   assault party id
     *      @param roomID           room id
     */
    public void prepareAssaultParty(int [] assaultParty, int assaultPartyID, int roomID)
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.PREPAREAP, assaultParty, assaultPartyID, roomID);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.PREPAREAPDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *  Transition send Assault Party in the life cycle of the Master Thief.
     *  ASSEMBLING_A_GROUP -> DECIDING_WHAT_TO_DO
     * 
     *  After creating a group of 3, the master blocks and waits for the last thief to reach the group to wake it up. 
     *  He is notified by the thief's prepare Excursion function.
     */
    public void sendAssaultParty()
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SENDAP);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SENDAPDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *  Transition prepare Excursion in the life cycle of the Ordinary Thief.
     *  CONCENTRATION_SITE -> CRAWLING_INWARDS
     * 
     *  The thief notifies the master that the last member of the group has arrived.
     */
    public void prepareExcursion()
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        
        outMessage = new Message (MessageType.PREPAREEX, ((OrdinaryThief) Thread.currentThread()).getAParty_ID(), 
                                                        ((OrdinaryThief) Thread.currentThread()).getOTState(),
                                                        ((OrdinaryThief) Thread.currentThread()).getThiefID());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.PREPAREEXDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getOrdinaryAPID() < 0) || (inMessage.getOrdinaryAPID() >= SimulPar.NUM_ASSAULT_PARTIES))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid ordinary thief assault party id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if (inMessage.getOrdinaryID() != ((OrdinaryThief) Thread.currentThread ()).getThiefID())
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid ordinary thief id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getOrdinaryTState() < OrdinaryThiefStates.CONCENTRATION_SITE) || (inMessage.getOrdinaryTState() > OrdinaryThiefStates.COLLECTION_SITE))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid ordinary thief state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();

        ((OrdinaryThief) Thread.currentThread ()).setAParty_ID(inMessage.getOrdinaryAPID());
        ((OrdinaryThief) Thread.currentThread ()).setOTState(inMessage.getOrdinaryTState());
        
        com.close ();
    }

    /**
     *  Transition sum Up Results  in the life cycle of the Master Thief.
     * 
     *  Master collects the canvas.
     *  Last function to be called by the master thief kills threads and ends the simulation.
     */
    public void sumUpResults()
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SUMUPRES, 
                                ((MasterThief) Thread.currentThread()).getMTState(), 
                                ((MasterThief) Thread.currentThread()).getCollectedCanvas());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        
        if (inMessage.getMsgType() != MessageType.SUMUPRESDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getMasterState() < MasterThiefStates.PLANNING_THE_HEIST) || (inMessage.getMasterState() > MasterThiefStates.PRESENTING_THE_REPORT))
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid Master Thief state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if (inMessage.getnCanvasCollected() < 0)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid number of canvas collected!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();

        ((MasterThief) Thread.currentThread()).setMTState(inMessage.getMasterState());
        ((MasterThief) Thread.currentThread()).setCollectedCanvas(inMessage.getnCanvasCollected());
    }

    /**
     *  Function that checks if assault parties have already been created. 
     *  If not, returns an id of the group that has not yet been created.
     * 
     *      @return assault party ID or -1 if groups have already been created.
     */
    public int assignAssaultPartyID()
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.ASSAPID);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.ASSAPIDDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getAssignAssaultPId() < 0 ) || inMessage.getAssignAssaultPId() >= SimulPar.NUM_ASSAULT_PARTIES)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid assault party id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.getAssignAssaultPId();
    }


    /**
     *  Get a room that has not yet been assigned and still has rooms.
     * 
     *      @return  room ID or -1 if all used or empty
     */
    public int assignRoomID()
    { 
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.ASSROOMID);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.ASSROOMIDDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if ((inMessage.getAssignRoomId() < 0) || (inMessage.getAssignRoomId() >= SimulPar.NUM_ROOMS))
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid room id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.getAssignRoomId();
    }

    /**
     *  Update room status. True if empty.
     * 
     *      @param roomID   room identification.
     *      @param roomState    room status.
     */
    public void setRoomStates(int roomID, Boolean roomState)
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETROOMST, roomID, roomState);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SETROOMSTDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     */
    public void shutdown ()
    {
        ClientCom com;                // communication channel
        Message outMessage,           // outgoing message
                inMessage;            // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SHUT);

        com.writeObject (outMessage);

        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SHUTDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }
}
