package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import commInfra.*;

/**
 *  Interface to the Museum.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Museum and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class MuseumInterface 
{
    /**
     *  Reference to the museum.
     */
    private final Museum museum;

    /**
    *  Instantiation of an interface to the museum.
    *
    *    @param museum reference to the concentration site
    */
    public MuseumInterface (Museum museum)
    {
        this.museum = museum;
    }

    /**
     *  Processing of the incoming messages.
     *
     *  Validation, execution of the corresponding method and generation of the outgoing message.
     *
     *    @param inMessage service request
     *    @return service reply
     *    @throws MessageException if the incoming message is not valid
     */
    public Message processAndReply (Message inMessage) throws MessageException
    {
        Message outMessage = null;           // mensagem de resposta

        /* validation of the incoming message */

        switch (inMessage.getMsgType ())
        { 
            case MessageType.SETINFOMUSEUM: // initInfoMuseum()
                                    if (inMessage.getNPaintingsRoomsArray().length != SimulPar.NUM_ROOMS)
                                        throw new MessageException ("Invalid array of paintings", inMessage);
                                    break;
            case MessageType.ROOLCV:    // rollACanvas()
                                    if ((inMessage.getOrdinaryID() < 0) || (inMessage.getOrdinaryID() >= SimulPar.NUM_ORD_THIEVES))
                                        throw new MessageException ("Invalid ordinary thief id!", inMessage);
                                    else if ((inMessage.getOrdinaryTState() < OrdinaryThiefStates.CONCENTRATION_SITE) || (inMessage.getOrdinaryTState() > OrdinaryThiefStates.COLLECTION_SITE))
                                        throw new MessageException ("Invalid ordinary thief state!", inMessage);
                                    else if (inMessage.getAssignRoomId() < 0 || inMessage.getAssignRoomId() >= SimulPar.NUM_ROOMS)
                                        throw new MessageException ("Invalid assigned room id", inMessage);
                                    break;
            case MessageType.SHUT:     // check nothing
                                    break;
            default:                throw new MessageException ("Invalid message type!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType ())
        { 
            case MessageType.SETINFOMUSEUM: // initInfoMuseum()
                                    museum.initInfoMuseum(inMessage.getNPaintingsRoomsArray());
                                    outMessage = new Message (MessageType.SETINFOMUSEUMDONE);
                                    break;
            case MessageType.ROOLCV:    // rollACanvas()
                                    ((MuseumClientProxy) Thread.currentThread()).setThiefID(inMessage.getOrdinaryID());
                                    ((MuseumClientProxy) Thread.currentThread()).setOTState(inMessage.getOrdinaryTState());
                                    ((MuseumClientProxy) Thread.currentThread()).setAParty_hasCanvas(inMessage.getOrdinaryTCV());
                                    museum.rollACanvas(inMessage.getAssignRoomId());
                                    outMessage = new Message (MessageType.ROOLCVDONE,
                                                            ((MuseumClientProxy) Thread.currentThread()).getAParty_hasCanvas(),
                                                            ((MuseumClientProxy) Thread.currentThread()).getOTState(),
                                                            ((MuseumClientProxy) Thread.currentThread()).getThiefID());
                                    break;
            case MessageType.SHUT:  museum.shutdown ();
                                    outMessage = new Message (MessageType.SHUTDONE);
                                    break;
        }

     return (outMessage);
    }    
}
