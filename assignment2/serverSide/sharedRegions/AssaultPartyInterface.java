package serverSide.sharedRegions;

import serverSide.main.*;
import serverSide.entities.*;
import clientSide.entities.*;
import commInfra.*;

/**
 *  Interface to the Assault Party.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Assault Party and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class AssaultPartyInterface 
{   
     /**
     *  Reference to the assault party.
     */
    private final AssaultParty assaultParty;

    /**
    *  Instantiation of an interface to the assault party.
    *
    *    @param assaultParty reference to the concentration site
    */
    public AssaultPartyInterface (AssaultParty assaultParty)
    {
        this.assaultParty = assaultParty;
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
            case MessageType.INFOAPID:    // initAssaultPartyID()
                                    if ((inMessage.getAssaultPartyID() < 0) || (inMessage.getAssaultPartyID() >= SimulPar.NUM_ASSAULT_PARTIES))
                                        throw new MessageException ("Invalid assault party identification", inMessage);
                                    break;
            case MessageType.INFOAPDIST:    // initAssaultPartyDist()
                                    if (inMessage.getDistFromOutsideArray().length != SimulPar.NUM_ROOMS)
                                        throw new MessageException ("Invalid room distances", inMessage);
                                    break;
            case MessageType.ADDTPARTY: // addThievesToParty()
                                    if (inMessage.getAssaultPBuild().length != SimulPar.K)
                                        throw new MessageException ("Invalid assault party size", inMessage);
                                    else if ((inMessage.getAssignRoomId() < 0) || (inMessage.getAssignRoomId() >= SimulPar.NUM_ROOMS))
                                        throw new MessageException ("Invalid room id!", inMessage);
                                    break;
            case MessageType.MOVEFIRSTM:    // moveFirstPartyMember()
                                    break;  
            case MessageType.CRAWLIN:       // crawlIN()
                                    if ((inMessage.getOrdinaryID() < 0) || (inMessage.getOrdinaryID() >= SimulPar.NUM_ORD_THIEVES))
                                        throw new MessageException ("Invalid ordinary thief id!", inMessage);
                                    if ((inMessage.getOrdinaryAPID() < 0) || (inMessage.getOrdinaryAPID() >= SimulPar.NUM_ASSAULT_PARTIES))
                                        throw new MessageException ("Invalid ordinary thief assault party id", inMessage);
                                    if (inMessage.getOrdinaryAPPos() != 0)  // posicao inicial qd entra no crawl in
                                        throw new MessageException ("Invalid thief assault party position", inMessage);
                                    if ((inMessage.getMaxDispOT() < SimulPar.MIN_MDISPLACEMENT) || (inMessage.getMaxDispOT() > SimulPar.MAX_MDISPLACEMENT))
                                        throw new MessageException ("Invalid maximum displacement !", inMessage);
                                    break;
            case MessageType.GETAPROOMID:   // getAssaultP_roomID()
                                    break;
            case MessageType.REVERSEDIRECTION:   // reverseDirection()
                                    if ((inMessage.getOrdinaryTState() < OrdinaryThiefStates.CONCENTRATION_SITE) || (inMessage.getOrdinaryTState() > OrdinaryThiefStates.COLLECTION_SITE))
                                        throw new MessageException ("Invalid ordinary thief state!", inMessage);
                                    else if ((inMessage.getOrdinaryID() < 0) || (inMessage.getOrdinaryID() >= SimulPar.NUM_ORD_THIEVES))
                                        throw new MessageException ("Invalid ordinary thief id!", inMessage);
                                    break;
             case MessageType.CRAWLOUT:       // crawlIN()
                                    ((AssaultPartyClientProxy) Thread.currentThread()).setAParty_position(inMessage.getOrdinaryAPPos());
                                    if ((inMessage.getOrdinaryID() < 0) || (inMessage.getOrdinaryID() >= SimulPar.NUM_ORD_THIEVES))
                                        throw new MessageException ("Invalid ordinary thief id!", inMessage);
                                    if ((inMessage.getOrdinaryAPID() < 0) || (inMessage.getOrdinaryAPID() >= SimulPar.NUM_ASSAULT_PARTIES))
                                        throw new MessageException ("Invalid ordinary thief assault party id", inMessage);
                                    if (inMessage.getOrdinaryAPPos() != ((AssaultPartyClientProxy) Thread.currentThread()).getAParty_position())  // posicao inicial qd entra no crawl out
                                        throw new MessageException ("Invalid thief assault party position", inMessage);
                                    if ((inMessage.getMaxDispOT() < SimulPar.MIN_MDISPLACEMENT) || (inMessage.getMaxDispOT() > SimulPar.MAX_MDISPLACEMENT))
                                        throw new MessageException ("Invalid maximum displacement !", inMessage);
                                    break;
            case MessageType.GETTINDEX:     // getThiefIndex()
                                    break;
            case MessageType.RESETAP:   // resetAssaultParty()
                                    break;
            case MessageType.SHUT:     // check nothing
                                    break;
            default:                throw new MessageException ("Invalid message type!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType ())
        { 
            case MessageType.INFOAPID:    // initAssaultPartyID()
                                    assaultParty.initAssaultPartyID(inMessage.getAssaultPartyID());
                                    outMessage = new Message(MessageType.INFOAPIDDONE);
                                    break;
            case MessageType.INFOAPDIST:    // initAssaultPartyDist()
                                    assaultParty.initAssaultPartyDist(inMessage.getDistFromOutsideArray());
                                    outMessage = new Message(MessageType.INFOAPDISTDONE);
                                    break;
            case MessageType.ADDTPARTY: // addThievesToParty()
                                    assaultParty.addThievesToParty(inMessage.getAssaultPBuild(), inMessage.getAssignRoomId());
                                    outMessage = new Message(MessageType.ADDTPARTYDONE);
                                    break; 
            case MessageType.MOVEFIRSTM:    // moveFirstPartyMember()
                                    assaultParty.moveFirstPartyMember(inMessage.getMoveFirstM());
                                    outMessage = new Message(MessageType.MOVEFIRSTMDONE);
                                    break; 
            case MessageType.CRAWLIN:       // crawlIN()
                                    ((AssaultPartyClientProxy) Thread.currentThread()).setThiefID(inMessage.getOrdinaryID());
                                    ((AssaultPartyClientProxy) Thread.currentThread()).setAParty_ID(inMessage.getOrdinaryAPID());
                                    ((AssaultPartyClientProxy) Thread.currentThread()).setAParty_position(inMessage.getOrdinaryAPPos());
                                    ((AssaultPartyClientProxy) Thread.currentThread()).setThiefMaxDisp(inMessage.getMaxDispOT());

                                    assaultParty.crawlIN();
                                    outMessage = new Message(MessageType.CRAWLINDONE, 
                                                            ((AssaultPartyClientProxy) Thread.currentThread()).getThiefID(),
                                                            ((AssaultPartyClientProxy) Thread.currentThread()).getAParty_ID(),
                                                            ((AssaultPartyClientProxy) Thread.currentThread()).getAParty_position(),
                                                            ((AssaultPartyClientProxy) Thread.currentThread()).getThiefMaxDisp());
                                    break;
            case MessageType.GETAPROOMID:   // getAssaultP_roomID()
                                    int AP_roomID = assaultParty.getAssaultP_roomID();
                                    outMessage = new Message (MessageType.GETAPROOMIDDONE, AP_roomID);
                                    break;
            case MessageType.REVERSEDIRECTION:   // reverseDirection()
                                    ((AssaultPartyClientProxy) Thread.currentThread()).setThiefID(inMessage.getOrdinaryID());
                                    ((AssaultPartyClientProxy) Thread.currentThread()).setAParty_hasCanvas(inMessage.getOrdinaryTCV());
                                    ((AssaultPartyClientProxy) Thread.currentThread()).setOTState(inMessage.getOrdinaryTState());
                                    
                                    assaultParty.reverseDirection();
                                    outMessage = new Message (MessageType.REVERSEDIRECTIONDONE, 
                                                                ((AssaultPartyClientProxy) Thread.currentThread()).getAParty_hasCanvas(),
                                                                ((AssaultPartyClientProxy) Thread.currentThread()).getOTState(),
                                                                ((AssaultPartyClientProxy) Thread.currentThread()).getThiefID());
                                    break;
            case MessageType.CRAWLOUT:       // crawlOUT()
                                    ((AssaultPartyClientProxy) Thread.currentThread()).setThiefID(inMessage.getOrdinaryID());
                                    ((AssaultPartyClientProxy) Thread.currentThread()).setAParty_ID(inMessage.getOrdinaryAPID());
                                    ((AssaultPartyClientProxy) Thread.currentThread()).setThiefMaxDisp(inMessage.getMaxDispOT());

                                    assaultParty.crawlOUT();
                                    outMessage = new Message(MessageType.CRAWLOUTDONE, 
                                                            ((AssaultPartyClientProxy) Thread.currentThread()).getThiefID(),
                                                            ((AssaultPartyClientProxy) Thread.currentThread()).getAParty_ID(),
                                                            ((AssaultPartyClientProxy) Thread.currentThread()).getAParty_position(),
                                                            ((AssaultPartyClientProxy) Thread.currentThread()).getThiefMaxDisp());
                                    break;
            case MessageType.GETTINDEX:     // getThiefIndex()
                                    ((AssaultPartyClientProxy) Thread.currentThread()).setThiefID(inMessage.getOrdinaryID());
                                    int thiefIndex = assaultParty.getThiefIndex();
                                    outMessage = new Message (MessageType.GETTINDEXDONE, 
                                                            thiefIndex,
                                                            ((AssaultPartyClientProxy) Thread.currentThread()).getThiefID());
                                    break;
            case MessageType.RESETAP:
                                    assaultParty.resetAssaultParty();
                                    outMessage = new Message (MessageType.RESETAPDONE);
                                    break;
            case MessageType.SHUT:  assaultParty.shutdown ();
                                    outMessage = new Message (MessageType.SHUTDONE);
                                    break;
        }

     return (outMessage);
    }
}
