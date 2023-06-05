package serverSide.sharedRegions;

import serverSide.entities.*;
import serverSide.main.*;
import clientSide.main.*;
import clientSide.stubs.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;
/**
 *  Interface to the Control and Collection Site.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Control and Collection Site and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ControlCollecSiteInterface 
{
    /**
     *  Reference to the control and collection site.
     */
    private final ControlCollectionSite controlCollecSite;

    /**
    *  Instantiation of an interface to the control and collection site.
    *
    *    @param controlCollecSite reference to the concentration site
    */
    public ControlCollecSiteInterface (ControlCollectionSite controlCollecSite)
    {
        this.controlCollecSite = controlCollecSite;
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
            case MessageType.TAKEREST:  // takeARest()
                                    if ((inMessage.getMasterState() < MasterThiefStates.PLANNING_THE_HEIST) || (inMessage.getMasterState() > MasterThiefStates.PRESENTING_THE_REPORT))
                                        throw new MessageException ("Invalid Master Thief state!", inMessage);
                                    break;
            case MessageType.HANDACANVAS: // handACanvas()
                                    if ((inMessage.getOrdinaryID() < 0) || (inMessage.getOrdinaryID() >= SimulPar.NUM_ORD_THIEVES))
                                        throw new MessageException ("Invalid ordinary thief id!", inMessage);
                                    else if ((inMessage.getOrdinaryAPID() < 0) || (inMessage.getOrdinaryAPID() >= SimulPar.NUM_ASSAULT_PARTIES))
                                        throw new MessageException ("Invalid ordinary thief assault party id", inMessage);
                                    else if ((inMessage.getOrdinaryTState() < OrdinaryThiefStates.CONCENTRATION_SITE) || (inMessage.getOrdinaryTState() > OrdinaryThiefStates.COLLECTION_SITE))
                                        throw new MessageException ("Invalid ordinary thief state!", inMessage);
                                    break;
            case MessageType.COLLECTACANVAS: // collectACanvas()
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
            case MessageType.TAKEREST:  // takeARest()
                                    ((ControlCollecSiteClientProxy) Thread.currentThread()).setMTState(inMessage.getMasterState());
                                    controlCollecSite.takeARest();
                                    outMessage = new Message (MessageType.TAKERESTDONE, 
                                                            ((ControlCollecSiteClientProxy) Thread.currentThread()).getMTState());
                                    break;
            case MessageType.HANDACANVAS:  // handACanvas()
                                    ((ControlCollecSiteClientProxy) Thread.currentThread()).setThiefID(inMessage.getOrdinaryID());
                                    ((ControlCollecSiteClientProxy) Thread.currentThread()).setOTState(inMessage.getOrdinaryTState());
                                    ((ControlCollecSiteClientProxy) Thread.currentThread()).setAParty_ID(inMessage.getOrdinaryAPID());
                                    ((ControlCollecSiteClientProxy) Thread.currentThread()).setAParty_hasCanvas(inMessage.getOrdinaryTCV());

                                    controlCollecSite.handACanvas(inMessage.getRoomID(),inMessage.getOrdinaryTAP_index());
                                    outMessage = new Message (MessageType.HANDACANVASDONE, 
                                                            ((ControlCollecSiteClientProxy) Thread.currentThread()).getThiefID(),
                                                            ((ControlCollecSiteClientProxy) Thread.currentThread()).getOTState(),
                                                            ((ControlCollecSiteClientProxy) Thread.currentThread()).getAParty_ID(),
                                                            ((ControlCollecSiteClientProxy) Thread.currentThread()).getAParty_hasCanvas()
                                                            );
                                    break;
            case MessageType.COLLECTACANVAS:  // collectACanvas()
                                    ((ControlCollecSiteClientProxy) Thread.currentThread()).setCollectedCanvas(inMessage.getnCanvasCollected());

                                    Boolean[] resetAP =controlCollecSite.collectACanvas();
                                    outMessage = new Message (MessageType.COLLECTACANVASDONE, 
                                                                ((ControlCollecSiteClientProxy) Thread.currentThread()).getCollectedCanvas(),
                                                                resetAP);
                                    break;
            case MessageType.SHUT:  controlCollecSite.shutdown();
                                    outMessage = new Message (MessageType.SHUTDONE);
                                    break;
        }

     return (outMessage);
    }    
}
