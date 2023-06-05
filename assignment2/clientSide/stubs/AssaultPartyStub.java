package clientSide.stubs;

import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Stub to the Assault Party.
 *
 *    It instantiates a remote reference to the assault party.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class AssaultPartyStub
{
    /**
    *  Name of the platform where is located the assault party server.
    */
    private String serverHostName;

    /**
    *  Port number for listening to service requests.
    */
    private int serverPortNumb;

    /**
     *  Instantiation of a Assault Party Stub
     *
     *    @param hostName name of the platform where is located the assault party server
     *    @param port port number where the server is listening to service requests
     */
    public AssaultPartyStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     *  Initialize the assault party identification
     * 
     *      @param assaultParty_ID  assault party identification.
     */
    public void initAssaultPartyID(int assaultParty_ID)
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

        outMessage = new Message(MessageType.INFOAPID, assaultParty_ID);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.INFOAPIDDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     *  Initialization of the assault party id and the distance from the rooms to outside.
     * 
     *      @param distFromOutside  distances of the rooms from outside 
     */
    public void initAssaultPartyDist(int [] distFromOutside)
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

        outMessage = new Message(MessageType.INFOAPDIST, distFromOutside);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.INFOAPDISTDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     *  Indicates whether the first member of the party has been notified by the master 
     *  on sendAssaultParty to proceed.
     * 
     *      @param firstPartyMember movement status of the first member of the assault party
     */
    public void moveFirstPartyMember(Boolean firstPartyMember)
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

        outMessage = new Message(MessageType.MOVEFIRSTM, firstPartyMember);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.MOVEFIRSTMDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     *  Add the thieves chosen by the master to the created assault party.
     * 
     *      @param assaultParty     set of thieves to be added to the assault party
     *      @param roomID       identification of the room assigned to the assault party        
     */
    public void addThievesToParty(int [] assaultParty, int roomID)
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

        outMessage = new Message(MessageType.ADDTPARTY, assaultParty, roomID);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.ADDTPARTYDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     *  Reset the values of the created assault party and the room associated with it.
     */
    public void resetAssaultParty()
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

        outMessage = new Message(MessageType.RESETAP);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.RESETAPDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

    /**
     *  Transition crawling inwards in the life cycle of the Ordinary Thief.
     *  (CRAWLING_INWARDS -> AT_A_ROOM)
     */
    public void crawlIN()
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

        outMessage = new Message(MessageType.CRAWLIN, ((OrdinaryThief) Thread.currentThread()).getThiefID(),
                                    ((OrdinaryThief) Thread.currentThread()).getAParty_ID(),
                                    ((OrdinaryThief) Thread.currentThread()).getAParty_position(),
                                    ((OrdinaryThief) Thread.currentThread()).getThiefMaxDisp());
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.CRAWLINDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryID() != ((OrdinaryThief) Thread.currentThread ()).getThiefID())
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid ordinary thief id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if (inMessage.getOrdinaryAPID() != ((OrdinaryThief) Thread.currentThread ()).getAParty_ID())
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid ordinary thief assault party id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close();

        ((OrdinaryThief) Thread.currentThread ()).setAParty_position(inMessage.getOrdinaryAPPos());
    }

    /**
     *  Reverse Direction of the Ordinary Thief.
     */
    public void reverseDirection()
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

        outMessage = new Message(MessageType.REVERSEDIRECTION, 
                                    ((OrdinaryThief) Thread.currentThread()).getAParty_hasCanvas(),
                                    ((OrdinaryThief) Thread.currentThread()).getOTState(),
                                    ((OrdinaryThief) Thread.currentThread()).getThiefID());
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.REVERSEDIRECTIONDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
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
        com.close();

        ((OrdinaryThief) Thread.currentThread ()).setOTState(inMessage.getOrdinaryTState());
    }


    /**
     *  Transition crawling inwards in the life cycle of the Ordinary Thief.
     *  (CRAWLING_OUTWARDS -> COLECTION_SITE)
     */
    public void crawlOUT()
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

        outMessage = new Message(MessageType.CRAWLOUT, ((OrdinaryThief) Thread.currentThread()).getThiefID(),
                                    ((OrdinaryThief) Thread.currentThread()).getAParty_ID(),
                                    ((OrdinaryThief) Thread.currentThread()).getAParty_position(),
                                    ((OrdinaryThief) Thread.currentThread()).getThiefMaxDisp());
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if(inMessage.getMsgType() != MessageType.CRAWLOUTDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        if (inMessage.getOrdinaryID() != ((OrdinaryThief) Thread.currentThread ()).getThiefID())
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid ordinary thief id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if (inMessage.getOrdinaryAPID() != ((OrdinaryThief) Thread.currentThread ()).getAParty_ID())
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid ordinary thief assault party id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // FALTA VERIFICAR getAParty_position + getThiefMaxDisp

        com.close();

        ((OrdinaryThief) Thread.currentThread ()).setAParty_position(inMessage.getOrdinaryAPPos());
    }

    /**
     *   Get Assault Party Room id.
     * 
     *     @return Assault Party Room id
     */
    public int getAssaultP_roomID() 
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

        outMessage = new Message (MessageType.GETAPROOMID);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.GETAPROOMIDDONE)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if (inMessage.getAssignRoomId() < 0 || inMessage.getAssignRoomId() >= SimulPar.NUM_ROOMS)
        { 
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid room id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();

        return inMessage.getAssignRoomId();
    }

    /**
     *  Get the assault party thief index.
     * 
     *      @return  index of array in the assault party that the thief belongs to
     */
    public int getThiefIndex()
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

        outMessage = new Message (MessageType.GETTINDEX, ((OrdinaryThief) Thread.currentThread()).getThiefID());
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();

        if (inMessage.getMsgType() != MessageType.GETTINDEXDONE)
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
        com.close ();

        return inMessage.getOrdinaryTAP_index();
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
