package clientSide.stubs;

import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Stub to the Control and Collection Site.
 *
 *    It instantiates a remote reference to the control and collection site.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ControlCollectionSiteStub 
{
    /**
    *  Name of the platform where is located the control and collection site server.
    */
    private String serverHostName;

    /**
    *  Port number for listening to service requests.
    */
    private int serverPortNumb;
    
    /**
   *   Instantiation of a stub to the control and collection site.
   *
   *     @param serverHostName name of the platform where is located the control and collection site server
   *     @param serverPortNumb port number for listening to service requests
   */
   public ControlCollectionSiteStub (String serverHostName, int serverPortNumb)
   {
      this.serverHostName = serverHostName;
      this.serverPortNumb = serverPortNumb;
   }

   /**
   *  Transition take A Rest in the life cycle of the Master Thief.
   *  (DECIDING_WHAT_TO_DO -> WAITING_FOR_ARRIVAL)
   * 
   *  Master thief is waken up by the operation handACanvas of one of
   *  the assault party members returning from the museum.
   * 
   */
   public void takeARest()
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

      outMessage = new Message (MessageType.TAKEREST, ((MasterThief) Thread.currentThread()).getMTState());
      com.writeObject (outMessage);
      inMessage = (Message) com.readObject ();

      if (inMessage.getMsgType() != MessageType.TAKERESTDONE)
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
   *  Transition hand a canvas in the life cycle of the ordinary Thief.
   *  (COLLECTION_SITE -> COLLECTION_SITE)
   * 
   *  The thief hands the master the paintings he stole.
   *  The ordinary thief is waken up by the operation of the master thief: collectACanvas.
   * 
   *     @param roomID  room id
   *     @param currentThiefIndex current thief index
   */
   public void handACanvas(int roomID, int currentThiefIndex)
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

      outMessage = new Message (MessageType.HANDACANVAS,
                                 ((OrdinaryThief) Thread.currentThread()).getThiefID(),
                                 ((OrdinaryThief) Thread.currentThread()).getAParty_ID(),
                                 ((OrdinaryThief) Thread.currentThread()).getOTState(),
                                 ((OrdinaryThief) Thread.currentThread()).getAParty_hasCanvas(),
                                 roomID, currentThiefIndex );
      com.writeObject (outMessage);
      inMessage = (Message) com.readObject ();

      if (inMessage.getMsgType() != MessageType.HANDACANVASDONE)
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

      ((OrdinaryThief) Thread.currentThread()).setOTState(inMessage.getOrdinaryTState());
      ((OrdinaryThief) Thread.currentThread()).setAParty_hasCanvas(inMessage.getOrdinaryTCV());
   }

   /**
     *  Transition collect a canvas in the life cycle of the Master Thief.
     *  (WAITING_FOR_ARRIVAL -> DECIDING_WHAT_TO_DO)
     * 
     *  The Master Thief collects the canvas from the ordinary thief.
     *  The Master notify the ordinary thief that has the canvas.
     * 
     *   @return assaul parties reset
     * 
     */
   public Boolean[] collectACanvas()
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

      outMessage = new Message (MessageType.COLLECTACANVAS,((MasterThief) Thread.currentThread()).getCollectedCanvas());
      com.writeObject (outMessage);
      inMessage = (Message) com.readObject ();

      if (inMessage.getMsgType() != MessageType.COLLECTACANVASDONE)
      { 
         GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
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

      ((MasterThief) Thread.currentThread()).setCollectedCanvas(inMessage.getnCanvasCollected());

      return inMessage.getResetAPArray();
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
