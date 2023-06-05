package serverSide.sharedRegions;

import serverSide.main.SimulPar;
import serverSide.entities.*;
import clientSide.entities.*;
import commInfra.*;

/**
 *  Interface to the Concentration Site.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Concentration Site and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ConcSiteInterface 
{
    /**
     *  Reference to the concentration site.
     */
    private final ConcentrationSite concSite;

    /**
    *  Instantiation of an interface to the concentration site.
    *
    *    @param concSite reference to the concentration site
    */
    public ConcSiteInterface (ConcentrationSite concSite)
    {
        this.concSite = concSite;
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
            case MessageType.STARTOP:   // startOperations()
                                    if ((inMessage.getMasterState() < MasterThiefStates.PLANNING_THE_HEIST) || (inMessage.getMasterState() > MasterThiefStates.PRESENTING_THE_REPORT))
                                        throw new MessageException ("Invalid Master Thief state!", inMessage);
                                    break;
            case MessageType.APPRAISESIT:   // appraiseSit()
                                    if ((inMessage.getMasterState() < MasterThiefStates.PLANNING_THE_HEIST) || (inMessage.getMasterState() > MasterThiefStates.PRESENTING_THE_REPORT))
                                        throw new MessageException ("Invalid Master Thief state!", inMessage);
                                    break;
            case MessageType.WAITTHIEVES:   // waitForThieves() -- check nothing
                                    break;
            case MessageType.AMINEEDED:     // amINeeded()
                                    if ((inMessage.getOrdinaryID() < 0) || (inMessage.getOrdinaryID() >= SimulPar.NUM_ORD_THIEVES))
                                        throw new MessageException ("Invalid ordinary thief id!", inMessage);
                                    else if ((inMessage.getOrdinaryTState() < OrdinaryThiefStates.CONCENTRATION_SITE) || (inMessage.getOrdinaryTState() > OrdinaryThiefStates.COLLECTION_SITE))
                                        throw new MessageException ("Invalid ordinary thief state!", inMessage);
                                    break;
            case MessageType.ASSAPID:       // assignAssaultPartyID() -- check nothing
                                    break;
            case MessageType.ASSROOMID:     // assignRoomID() -- check nothing
                                    break;
            case MessageType.SETASSRID:     // setAssignedRoomID
                                    if ((inMessage.getAssignAssaultPId() < 0 ) || inMessage.getAssignAssaultPId() >= SimulPar.NUM_ASSAULT_PARTIES)
                                        throw new MessageException ("Invalid assault party id!", inMessage);
                                    // else if ((inMessage.getAssignRoomId() < 0) || (inMessage.getAssignRoomId() >= SimulPar.NUM_ROOMS))
                                    //     throw new MessageException ("Invalid room id!", inMessage);  // pode ser -1 ao fazer o reset
                                    break;
            case MessageType.BUILDP:        // buildParty() 
                                    if ((inMessage.getMasterState() < MasterThiefStates.PLANNING_THE_HEIST) || (inMessage.getMasterState() > MasterThiefStates.PRESENTING_THE_REPORT))
                                        throw new MessageException ("Invalid Master Thief state!", inMessage);
                                    break;
            case MessageType.PREPAREAP:     // prepareAssaultParty()
                                   if (inMessage.getAssaultPBuild().length != SimulPar.K)
                                        throw new MessageException ("Invalid assault party size!", inMessage);
                                    else if ((inMessage.getAssignAssaultPId() < 0 ) || inMessage.getAssignAssaultPId() >= SimulPar.NUM_ASSAULT_PARTIES)
                                        throw new MessageException ("Invalid assault party id!", inMessage);
                                    else if ((inMessage.getAssignRoomId() < 0) || (inMessage.getAssignRoomId() >= SimulPar.NUM_ROOMS))
                                        throw new MessageException ("Invalid room id!", inMessage);
                                    break;
            case MessageType.PREPAREEX:     // prepareExcursion()
                                    if ((inMessage.getOrdinaryID() < 0) || (inMessage.getOrdinaryID() >= SimulPar.NUM_ORD_THIEVES))
                                    throw new MessageException ("Invalid ordinary thief id!", inMessage);
                                else if ((inMessage.getOrdinaryTState() < OrdinaryThiefStates.CONCENTRATION_SITE) || (inMessage.getOrdinaryTState() > OrdinaryThiefStates.COLLECTION_SITE))
                                    throw new MessageException ("Invalid ordinary thief state!", inMessage);
                                break;
            case MessageType.SENDAP:    //sendAssaultParty()
                                    break;
            case MessageType.SETROOMST:     // roomStates().set(roomID, false); 
                                    break;
            case MessageType.SETASSAPID:    // setAssigedAPid()
                                    break;
            case MessageType.SUMUPRES:      // sumUpResults()
                                    if ((inMessage.getMasterState() < MasterThiefStates.PLANNING_THE_HEIST) || (inMessage.getMasterState() > MasterThiefStates.PRESENTING_THE_REPORT))
                                        throw new MessageException ("Invalid Master Thief state!", inMessage);
                                    else if(inMessage.getnCanvasCollected() < 0)
                                        throw new MessageException ("Invalid number of canvas collected!", inMessage);
                                    break;
            case MessageType.SHUT:     // check nothing 
                                    break;
            default:                throw new MessageException ("Invalid message type!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType ())
        { 
            case MessageType.STARTOP:   // startOperations()
                                    ((ConcentrationSiteClientProxy) Thread.currentThread()).setMTState(inMessage.getMasterState());
                                    concSite.startOperations();
                                    outMessage = new Message (MessageType.STARTOPDONE, ((ConcentrationSiteClientProxy) Thread.currentThread()).getMTState());
                                    break;
            case MessageType.APPRAISESIT:   // appraiseSit()
                                    ((ConcentrationSiteClientProxy) Thread.currentThread()).setMTState(inMessage.getMasterState());
                                    int transition = concSite.appraiseSit();
                                    outMessage = new Message (MessageType.APPRAISESITDONE, 
                                                                ((ConcentrationSiteClientProxy) Thread.currentThread()).getMTState(), 
                                                                transition);
                                    break;
            case MessageType.WAITTHIEVES:   // waitForThieves()
                                    concSite.waitForThieves();
                                    outMessage = new Message (MessageType.WAITTHIEVESDONE);
                                    break;
            case MessageType.AMINEEDED:     // amINeeded()
                                    ((ConcentrationSiteClientProxy) Thread.currentThread()).setThiefID(inMessage.getOrdinaryID());
                                    ((ConcentrationSiteClientProxy) Thread.currentThread()).setOTState(inMessage.getOrdinaryTState());
                                    Boolean amINeeded = concSite.amINeeded();
                                    outMessage = new Message (MessageType.AMINEEDEDDONE, 
                                                                ((ConcentrationSiteClientProxy) Thread.currentThread()).getMTState(), 
                                                                ((ConcentrationSiteClientProxy) Thread.currentThread()).getThiefID(),
                                                                amINeeded);
                                    break;
            case MessageType.ASSAPID:       // assignAssaultPartyID() 
                                    int assaultPartyID = concSite.assignAssaultPartyID();
                                    outMessage = new Message(MessageType.ASSAPIDDONE, assaultPartyID);
                                    break;
            case MessageType.ASSROOMID:     // assignRoomID()
                                    int roomID = concSite.assignRoomID();
                                    outMessage = new Message(MessageType.ASSROOMIDDONE, roomID);
                                    break;
            case MessageType.SETASSRID:      // setAssignedRoomID
                                    concSite.setAssignedRoomID(inMessage.getAssignAssaultPId(), inMessage.getAssignRoomId());
                                    outMessage = new Message(MessageType.SETASSRIDDONE);
                                    break;
            case MessageType.BUILDP:        // buildParty() -- check nothing
                                    ((ConcentrationSiteClientProxy) Thread.currentThread()).setMTState(inMessage.getMasterState());
                                    int [] assaultP = concSite.buildParty();
                                    outMessage = new Message(MessageType.BUILDPDONE, assaultP, 
                                                            ((ConcentrationSiteClientProxy) Thread.currentThread()).getMTState()); 
                                    break;
            case MessageType.PREPAREAP:     // prepareAssaultParty()
                                    concSite.prepareAssaultParty(inMessage.getAssaultPBuild(), inMessage.getAssignAssaultPId(), inMessage.getAssignRoomId());
                                    outMessage = new Message(MessageType.PREPAREAPDONE);
                                    break;
            case MessageType.PREPAREEX:     // prepareExcursion()
                                    ((ConcentrationSiteClientProxy) Thread.currentThread()).setAParty_ID(inMessage.getOrdinaryAPID());
                                    ((ConcentrationSiteClientProxy) Thread.currentThread()).setThiefID(inMessage.getOrdinaryID());
                                    ((ConcentrationSiteClientProxy) Thread.currentThread()).setOTState(inMessage.getOrdinaryTState());
                                    concSite.prepareExcursion();
                                    outMessage = new Message (MessageType.PREPAREEXDONE, 
                                                                ((ConcentrationSiteClientProxy) Thread.currentThread()).getAParty_ID(), 
                                                                ((ConcentrationSiteClientProxy) Thread.currentThread()).getMTState(), 
                                                                ((ConcentrationSiteClientProxy) Thread.currentThread()).getThiefID());
                                    break;
            case MessageType.SENDAP:    //sendAssaultParty()
                                    concSite.sendAssaultParty();
                                    outMessage = new Message (MessageType.SENDAPDONE);
                                    break;
            case MessageType.SETROOMST:     // roomStates().set(roomID, false); 
                                    concSite.roomStates().set(inMessage.getRoomID(), inMessage.getRoomState());
                                    outMessage = new Message (MessageType.SETROOMSTDONE);
                                    break;
            case MessageType.SETASSAPID:    // setAssigedAPid()
                                    concSite.setAssigedAPid(inMessage.getAssignAssaultPId(), inMessage.getAssaultPState());
                                    outMessage = new Message (MessageType.SETASSAPIDDONE);
                                    break;
            case MessageType.SUMUPRES:      // sumUpResults()
                                    ((ConcentrationSiteClientProxy) Thread.currentThread()).setMTState(inMessage.getMasterState());
                                    ((ConcentrationSiteClientProxy) Thread.currentThread()).setCollectedCanvas(inMessage.getnCanvasCollected());
                                    concSite.sumUpResults();
                                    outMessage = new Message (MessageType.SUMUPRESDONE, 
                                                                ((ConcentrationSiteClientProxy) Thread.currentThread()).getMTState(),
                                                                ((ConcentrationSiteClientProxy) Thread.currentThread()).getCollectedCanvas());
                                    break;
            case MessageType.SHUT:  concSite.shutdown ();
                                    outMessage = new Message (MessageType.SHUTDONE);
                                    break;
        }

     return (outMessage);
    }
}
