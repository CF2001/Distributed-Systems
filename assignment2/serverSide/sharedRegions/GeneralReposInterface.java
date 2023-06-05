package serverSide.sharedRegions;

import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;

/**
 *  Interface to the General Repository of Information.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    General Repository and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class GeneralReposInterface 
{
    /**
     *  Reference to the general repository.
     */
    private final GeneralRepository repos;

    /**
    *  Instantiation of an interface to the general repository.
    *
    *    @param repos reference to the general repository
    */
    public GeneralReposInterface (GeneralRepository repos)
    {
        this.repos = repos;
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
            case MessageType.LOGFN:     // log file name
                                    if (inMessage.getLogFName () == null)
                                        throw new MessageException ("Name of the logging file is not present!", inMessage);
                                    break;
            case MessageType.SETMST:    // set master state
                                    if ((inMessage.getMasterState() < MasterThiefStates.PLANNING_THE_HEIST) || (inMessage.getMasterState() > MasterThiefStates.PRESENTING_THE_REPORT))
                                        throw new MessageException ("Invalid Master Thief state!", inMessage);
                                    break;
            case MessageType.SETOTST:   // set ordinary thief state
                                    if ((inMessage.getOrdinaryID() < 0) || (inMessage.getOrdinaryID() >= SimulPar.NUM_ORD_THIEVES))
                                        throw new MessageException ("Invalid Ordinary thief id!", inMessage);
                                    else if ((inMessage.getOrdinaryTState() < OrdinaryThiefStates.CONCENTRATION_SITE) || (inMessage.getOrdinaryTState() > OrdinaryThiefStates.COLLECTION_SITE))
                                        throw new MessageException ("Invalid Ordinary Thief state!", inMessage);
                                    break;
            case MessageType.SETMAXD:   // setOrdinartThief_MaxDisp()
                                    if ((inMessage.getMaxDispOT() < SimulPar.MIN_MDISPLACEMENT) || (inMessage.getMaxDispOT() > SimulPar.MAX_MDISPLACEMENT))
                                        throw new MessageException ("Invalid maximum displacement !", inMessage);
                                    else if ((inMessage.getOrdinaryID() < 0) || (inMessage.getOrdinaryID() >= SimulPar.NUM_ORD_THIEVES))
                                        throw new MessageException ("Invalid Ordinary thief id!", inMessage);
                                    break;
            case MessageType.SETROOMDIST:  // setMRoom_DT_outsideG()
                                    if ((inMessage.getRoomID() < 0) || (inMessage.getRoomID() >= SimulPar.NUM_ROOMS))
                                        throw new MessageException ("Invalid room identification", inMessage);
                                    else if ((inMessage.getDistFromOutside() < SimulPar.MIN_DistOut) || (inMessage.getDistFromOutside() > SimulPar.MAX_DistOut))
                                        throw new MessageException ("Invalid distance from outside of a room", inMessage);
                                    break;
            case MessageType.SETROOMPAINT:  // setMRoom_NPaintings()
                                    if ((inMessage.getRoomID() < 0) || (inMessage.getRoomID() >= SimulPar.NUM_ROOMS))
                                        throw new MessageException ("Invalid room identification", inMessage);
                                    else if ((inMessage.getNPaintings()) < 0)
                                        throw new MessageException ("Invalid number of paintings", inMessage);
                                    break;
            case MessageType.SETAPROOMID:   // setAssaultParty_RId()
                                    if ((inMessage.getAssaultPartyID() < 0) || (inMessage.getAssaultPartyID() >= SimulPar.NUM_ASSAULT_PARTIES))
                                        throw new MessageException ("Invalid asault party identification", inMessage);
                                    else if ((inMessage.getRoomID() < -1) || (inMessage.getRoomID() >= SimulPar.NUM_ROOMS))
                                        throw new MessageException ("Invalid room identification", inMessage);
                                    break;
            case MessageType.SETAPELEMID:   // setAParty_Elem_ID()
                                    if ((inMessage.getAssaultPartyID() < 0) || (inMessage.getAssaultPartyID() >= SimulPar.NUM_ASSAULT_PARTIES))
                                        throw new MessageException ("Invalid assault party identification", inMessage);
                                    else if ((inMessage.getElementID() < 0) || (inMessage.getElementID() >= SimulPar.K))
                                        throw new MessageException ("Invalid element identification", inMessage);
                                    else if ( (inMessage.getOrdinaryID() < -1) || (inMessage.getOrdinaryID() >= SimulPar.NUM_ORD_THIEVES) )
                                        throw new MessageException (" Invalid ordinary thief id! ", inMessage);
                                    break;
            case MessageType.SETOTSIT:    // setOTSituation()
                                    if ((inMessage.getOrdinaryID() < 0) || (inMessage.getOrdinaryID() >= SimulPar.NUM_ORD_THIEVES))
                                        throw new MessageException ("Invalid room identification", inMessage);
                                    break;
            case MessageType.SETAPELEMPOS:  // setAParty_Elem_POS()
                                    if ((inMessage.getAssaultPartyID() < 0) || (inMessage.getAssaultPartyID() >= SimulPar.NUM_ASSAULT_PARTIES))
                                        throw new MessageException ("Invalid assault party identification", inMessage);
                                    else if ((inMessage.getElementID() < 0) || (inMessage.getElementID() >= SimulPar.K))
                                        throw new MessageException ("Invalid element identification", inMessage);
                                    // else if (inMessage.getOrdinaryAPPos() < 0)
                                    //     throw new MessageException ("Invalid thief position", inMessage);
                                    break;
            case MessageType.SETAPELEMCV:   // setAParty_Elem_CV()
                                    if ((inMessage.getAssaultPartyID() < 0) || (inMessage.getAssaultPartyID() >= SimulPar.NUM_ASSAULT_PARTIES))
                                        throw new MessageException ("Invalid assault party identification", inMessage);
                                    else if ((inMessage.getElementID() < 0) || (inMessage.getElementID() >= SimulPar.K))
                                        throw new MessageException ("Invalid element identification", inMessage);
                                    break;
            case MessageType.SETSTOLENP:    // setStolenPaitings()
                                    if(inMessage.getnCanvasCollected() < 0)
                                        throw new MessageException ("Invalid number of canvas collected!", inMessage);
                                    break;
            case MessageType.SHUT:     // check nothing
                                    break;
            default:                throw new MessageException ("Invalid message type!", inMessage);
        }

        /* processing */

        switch (inMessage.getMsgType ())
        { 
            case MessageType.LOGFN: // log file name
                                    repos.initSimul (inMessage.getLogFName ());
                                    outMessage = new Message (MessageType.LOGFNDONE);
                                    break;
            case MessageType.SETMST:    // set master state
                                    repos.setMasterThiefState(inMessage.getMasterState());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
            case MessageType.SETOTST:   // set ordinary thief state
                                    repos.setOTState(inMessage.getOrdinaryID(), inMessage.getOrdinaryTState());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
            case MessageType.SETMAXD:   // setOrdinartThief_MaxDisp()
                                    repos.setOrdinartThief_MaxDisp(inMessage.getOrdinaryID(), inMessage.getMaxDispOT());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
            case MessageType.SETROOMDIST:  // setMRoom_DT_outsideG()
                                    repos.setMRoom_DT_outsideG(inMessage.getRoomID(), inMessage.getDistFromOutside());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
            case MessageType.SETROOMPAINT:  // setMRoom_NPaintings()
                                    repos.setMRoom_NPaintings(inMessage.getRoomID(), inMessage.getNPaintings());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
            case MessageType.SETAPROOMID:   // setAssaultParty_RId()
                                    repos.setAssaultParty_RId(inMessage.getAssaultPartyID(), inMessage.getRoomID());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
            case MessageType.SETAPELEMID:   // setAParty_Elem_ID()
                                    repos.setAParty_Elem_ID(inMessage.getAssaultPartyID(), inMessage.getElementID(), inMessage.getOrdinaryID());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
            case MessageType.SETOTSIT:    // setOTSituation()
                                    repos.setOTSituation(inMessage.getOrdinaryID(), inMessage.getOrdinarySit());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
            case MessageType.SETAPELEMPOS:  // setAParty_Elem_POS()
                                    repos.setAParty_Elem_POS(inMessage.getAssaultPartyID(), inMessage.getElementID(), inMessage.getOrdinaryAPPos());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
            case MessageType.SETAPELEMCV:   // setAParty_Elem_CV()
                                    repos.setAParty_Elem_CV(inMessage.getAssaultPartyID(), inMessage.getElementID(), inMessage.getOrdinaryTCV());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
            case MessageType.SETSTOLENP:    // setStolenPaitings()
                                    repos.setStolenPaitings(inMessage.getnCanvasCollected());
                                    outMessage = new Message (MessageType.SACK);
                                    break;
            case MessageType.SHUT:  repos.shutdown ();
                                    outMessage = new Message (MessageType.SHUTDONE);
                                    break;
        }

     return (outMessage);
   }
}
