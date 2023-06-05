package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import serverSide.objects.*;
import interfaces.*;
import genclass.GenericIO;

/**
 *    Instantiation and registering of a control and collection site object.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class ServerHeistMuseumControlSite
{
    /**
     *  Flag signaling the end of operations.
     */
    private static boolean end = false;

    /**
     *  Main method.
     *
     *        @param args[0] - port number for listening to service requests
     *        @param args[1] - name of the platform where is located the RMI registering service
     *        @param args[2] - port number where the registering service is listening to service requests
     */

    public static void main (String[] args)
    {
        int portNumb = -1;                            // port number for listening to service requests
        String rmiRegHostName;                        // name of the platform where is located the RMI registering service
        int rmiRegPortNumb = -1;                      // port number where the registering service is listening to service requests
    
        if (args.length != 3)
        { 
            GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
        }
        
        try
        {   portNumb = Integer.parseInt (args[0]);
        }
        catch (NumberFormatException e)
        {   GenericIO.writelnString ("args[0] is not a number!");
            System.exit (1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536))
        {   GenericIO.writelnString ("args[0] is not a valid port number!");
            System.exit (1);
        }
        rmiRegHostName = args[1];
        try
        {   rmiRegPortNumb = Integer.parseInt (args[2]);
        }
        catch (NumberFormatException e)
        {   GenericIO.writelnString ("args[2] is not a number!");
            System.exit (1);
        }
        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536))
        {   GenericIO.writelnString ("args[2] is not a valid port number!");
            System.exit (1);
        }

        /* create and install the security manager */

        if (System.getSecurityManager () == null)
            System.setSecurityManager (new SecurityManager ());
        GenericIO.writelnString ("Security manager was installed!");

        /* get a remote reference to the general repository object */

        String nameEntryGeneralRepos = "GeneralRepository";            // public name of the general repository object
        GeneralReposInterface reposStub = null;                        // remote reference to the general repository object
        Registry registry = null;                                      // remote reference for registration in the RMI registry service

        try
        { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        GenericIO.writelnString ("RMI registry was created!");

        try
        { reposStub = (GeneralReposInterface) registry.lookup (nameEntryGeneralRepos);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("GeneralRepos lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("GeneralRepos not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }


        /* instantiate a control and collection site object */

        ControlCollectionSite  controlCollectionSite = new ControlCollectionSite (reposStub);                 
        ControlCollecSiteInterface collecSiteStub = null;                          

        try
        { 
            collecSiteStub = (ControlCollecSiteInterface) UnicastRemoteObject.exportObject (controlCollectionSite, portNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Control and Collection Site stub generation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        GenericIO.writelnString ("Stub was generated!");



        /* register it with the general registry service */

        String nameEntryBase = "RegisterHandler";             // public name of the object that enables the registration of other remote objects
                                                                        
        String nameEntryObject = "ControlCollectionSite";              // public name of the ControlCollectionSite object
        Register reg = null;                                           // remote reference to the object that enables the registration
                                                                        // of other remote objects

        try
        { reg = (Register) registry.lookup (nameEntryBase);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RegisterRemoteObject lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("RegisterRemoteObject not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { reg.bind (nameEntryObject, collecSiteStub);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Control and Collection Site registration exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString ("Control and Collection Site already bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        GenericIO.writelnString ("Control and Collection Site object was registered!");

        /* wait for the end of operations */

        GenericIO.writelnString ("Control and Collection Site is in operation!");
        try
        { while (!end)
            synchronized (Class.forName ("serverSide.main.ServerHeistMuseumControlSite"))
            { try
                { (Class.forName ("serverSide.main.ServerHeistMuseumControlSite")).wait ();
                }
                catch (InterruptedException e)
                { GenericIO.writelnString ("Control and Collection Site main thread was interrupted!");
                }
            }
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("The data type ServerHeistMuseumControlSite was not found (blocking)!");
            e.printStackTrace ();
            System.exit (1);
        }

        /* server shutdown */

        boolean shutdownDone = false;        // flag signalling the shutdown of the ControlCollectionSite service

        try
        { reg.unbind (nameEntryObject);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Control and Collection deregistration exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Control and Collection not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        GenericIO.writelnString ("Control and Collection was deregistered!");

        try
        { 
            shutdownDone = UnicastRemoteObject.unexportObject (controlCollectionSite, true);
        }
        catch (NoSuchObjectException e)
        { GenericIO.writelnString ("Control and Collection unexport exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        if (shutdownDone)
            GenericIO.writelnString ("Control and Collection was shutdown!");

    }

    /**
     *  Close of operations.
     */
    public static void shutdown ()
    {
        end = true;
        try
        { synchronized (Class.forName ("serverSide.main.ServerHeistMuseumControlSite"))
            { (Class.forName ("serverSide.main.ServerHeistMuseumControlSite")).notify ();
            }
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("The data type ServerHeistMuseumControlSite was not found (waking up)!");
        e.printStackTrace ();
        System.exit (1);
        }
    }
}



