package clientSide.stubs;

import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Stub to the General Repository.
 *
 *    It instantiates a remote reference to the general repository.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class GeneralRepositoryStub 
{
    /**
    *  Name of the platform where is located the general repository server.
    */
    private String serverHostName;

    /**
    *  Port number for listening to service requests.
    */
    private int serverPortNumb;

    /**
     *   Instantiation of a stub to the general repository.
     *
     *     @param serverHostName name of the platform where is located the general repository server
     *     @param serverPortNumb port number for listening to service requests
     */
    public GeneralRepositoryStub (String serverHostName, int serverPortNumb)
    {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }    

    /**
     *   Operation initialization of the simulation.
     *
     *     @param fileName logging file name
     */
    public void initSimul (String fileName)
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

        outMessage = new Message (MessageType.LOGFN, fileName);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.LOGFNDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Write in the logging file the stolen paitings.
     * 
     *      @param stolenPaitings number of stolen paintings
     */
    public void setStolenPaitings(int stolenPaitings)
    {
        ClientCom com;                            // communication channel
        Message outMessage,                       // outgoing message
                inMessage;                        // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETSTOLENP, stolenPaitings);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        
        if (inMessage.getMsgType() != MessageType.SACK)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Set state of the master Thief.
     * 
     *      @param masterThiefState  master thief state
     */
    public void setMasterThiefState(int masterThiefState)
    {
        ClientCom com;                            // communication channel
        Message outMessage,                       // outgoing message
                inMessage;                        // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETMST, masterThiefState);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        
        if (inMessage.getMsgType() != MessageType.SACK)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *  Set state of the ordinary Thief.
     * 
     *      @param thiefID           ordinary thief id
     *      @param ordinaryThiefState    ordinary thief state
     */
    public void setOTState(int thiefID, int ordinaryThiefState)
    {
        ClientCom com;                            // communication channel
        Message outMessage,                       // outgoing message
                inMessage;                        // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETOTST, ordinaryThiefState, thiefID);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SACK)
        {   
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *  Write in the logging file the situation of the ordinary Thief.
     * 
     *      @param thiefID   ordinary thief id
     *      @param ordinaryThiefSituation    ordinary thief situation
     */
    public void setOTSituation(int thiefID, String ordinaryThiefSituation)
    {
        ClientCom com;                            // communication channel
        Message outMessage,                       // outgoing message
                inMessage;                        // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETOTSIT, thiefID, ordinaryThiefSituation);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SACK)
        {   
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *  Write in the logging file the maximum displacement of the ordinary Thief.
     * 
     *      @param thiefID    ordinary thief identification
     *      @param maxDisp    ordinary thief maximum displacement
     */
    public void setOrdinartThief_MaxDisp(int thiefID, int maxDisp)
    {
        ClientCom com;                            // communication channel
        Message outMessage,                       // outgoing message
                inMessage;                        // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETMAXD, maxDisp, thiefID);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SACK)
        {   
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *  Write in the logging file the room id of an assault party.
     * 
     * @param assaultParty_ID   assault party id
     * @param roomID    room id
     */
    public void setAssaultParty_RId(int assaultParty_ID, int roomID)
    {
        ClientCom com;                            // communication channel
        Message outMessage,                       // outgoing message
                inMessage;                        // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETAPROOMID, assaultParty_ID, roomID);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SACK)
        {   
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *  Write in the logging file the id of an element in the assault party.
     * 
     * @param assaultParty_ID   assault party id
     * @param elemID            element id
     * @param thiefID           thief id
     */
    public void setAParty_Elem_ID(int assaultParty_ID, int elemID, int thiefID)
    {
        ClientCom com;                            // communication channel
        Message outMessage,                       // outgoing message
                inMessage;                        // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETAPELEMID, assaultParty_ID, elemID, thiefID);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SACK)
        {   
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Set the assault party element position 
     * 
     *      @param assaultParty_ID    assault party id
     *      @param elemID    element id
     *      @param thiefPos  thief id
     */
    public void setAParty_Elem_POS(int assaultParty_ID, int elemID, int thiefPos)
    {
        ClientCom com;                            // communication channel
        Message outMessage,                       // outgoing message
                inMessage;                        // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETAPELEMPOS, assaultParty_ID, elemID, thiefPos);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SACK)
        {   
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Set the assault party element canvas state   
     * 
     *      @param assaultParty_ID  assault party id
     *      @param elemID           element id
     *      @param thiefCV          thief id
     */
    public void setAParty_Elem_CV(int assaultParty_ID, int elemID, int thiefCV)
    {
        ClientCom com;                            // communication channel
        Message outMessage,                       // outgoing message
                inMessage;                        // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETAPELEMCV, assaultParty_ID, elemID, thiefCV);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SACK)
        {   
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *  Set the number of paintings in a museum Room.
     * 
     *      @param roomID        room identification
     *      @param nPaintings    number of pautings
     */
    public void setMRoom_NPaintings(int roomID, int nPaintings)
    {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SETROOMPAINT, nPaintings, roomID);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SACK)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *  Set the distance from outside gathering site of a museum Room.
     * 
     *      @param roomID   room identification
     *      @param distFromOutside distance from outside of the room
     */
    public void setMRoom_DT_outsideG(int roomID, int distFromOutside)
    {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SETROOMDIST, distFromOutside, roomID);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.SACK)
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
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

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
            { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
            }
        com.close ();
    }
}
