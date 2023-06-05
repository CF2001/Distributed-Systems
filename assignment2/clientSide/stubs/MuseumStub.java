package clientSide.stubs;

import serverSide.main.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Stub to the Museum.
 *
 *    It instantiates a remote reference to the museum.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class MuseumStub 
{
    /**
    *  Name of the platform where is located the museum server.
    */
    private String serverHostName;

    /**
    *  Port number for listening to service requests.
    */
    private int serverPortNumb;
    
    /**
   *   Instantiation of a stub to the museum.
   *
   *     @param serverHostName name of the platform where is located the museum server
   *     @param serverPortNumb port number for listening to service requests
   */
   public MuseumStub (String serverHostName, int serverPortNumb)
   {
      this.serverHostName = serverHostName;
      this.serverPortNumb = serverPortNumb;
   }

   /**
   *  Initializing the number of paintings in the museum rooms.
   * 
   *      @param nPaintingsArray  number of paintings in the rooms of the museum
   */
   public synchronized void initInfoMuseum(int [] nPaintingsArray)
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

      outMessage = new Message (MessageType.SETINFOMUSEUM, nPaintingsArray);
      com.writeObject (outMessage);
      inMessage = (Message) com.readObject ();

      if (inMessage.getMsgType() != MessageType.SETINFOMUSEUMDONE)
         { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
           GenericIO.writelnString (inMessage.toString ());
           System.exit (1);
         }
      com.close ();
   }

   /**
    * Transition roll a canvas in the life cycle of the Ordinary Thief.
    *  (AT_A_ROOM -> AT_A_ROOM) 
    *
    *       @param roomID  room id
    */
   public void rollACanvas(int roomID)
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

      outMessage = new Message (MessageType.ROOLCV, 
                              ((OrdinaryThief) Thread.currentThread()).getThiefID(),
                              ((OrdinaryThief) Thread.currentThread()).getOTState(),
                              ((OrdinaryThief) Thread.currentThread()).getAParty_hasCanvas(),
                              roomID);
      com.writeObject (outMessage);
      inMessage = (Message) com.readObject();

      if (inMessage.getMsgType() != MessageType.ROOLCVDONE)
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

      ((OrdinaryThief) Thread.currentThread()).setAParty_hasCanvas(inMessage.getOrdinaryTCV());
      ((OrdinaryThief) Thread.currentThread()).setOTState(inMessage.getOrdinaryTState());
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
