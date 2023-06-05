package clientSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import clientSide.entities.*;
import serverSide.main.*;
import interfaces.*;
import genclass.GenericIO;

/**
 *    Client side of the Heist To The Museum (Master).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class ClientHeistToTheMuseumMaster
{
    /**
     *  Main method.
     *
     *    @param args runtime arguments
     *        args[0] - name of the platform where is located the RMI registering service
     *        args[1] - port number where the registering service is listening to service requests
     */
    public static void main (String [] args)
    {
        String rmiRegHostName;          // name of the platform where is located the RMI registering service
        int rmiRegPortNumb = -1;        // port number where the registering service is listening to service requests

        /* getting problem runtime parameters */

        if (args.length != 2)
        { 
            GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
        }
        rmiRegHostName = args[0];
        try
        { rmiRegPortNumb = Integer.parseInt (args[1]);
        }
        catch (NumberFormatException e)
        { 
            GenericIO.writelnString ("args[1] is not a number!");
            System.exit (1);
        }
        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536))
        { 
            GenericIO.writelnString ("args[1] is not a valid port number!");
            System.exit (1);
        }

        /* problem initialization */

        String nameEntryGeneralRepos = "GeneralRepository";            // public name of the general repository object
        GeneralReposInterface reposStub = null;                        // remote reference to the general repository object

        String nameEntryConcSite = "ConcentrationSite";                // public name of the concentration site object
        ConcSiteInterface concSiteStub = null;                         // remote reference to the concentration site object

        String nameEntryCollecSite = "ControlCollectionSite";           // public name of the control and collection site object
        ControlCollecSiteInterface collecSiteStub = null;              // remote reference to the control and collection site object

        String [] nameEntryAssaultParty = new String[SimulPar.NUM_ASSAULT_PARTIES];     
        nameEntryAssaultParty[0] = "AssaultParty0";                     // public name of the assault party 0 object
        nameEntryAssaultParty[1] = "AssaultParty1";                     // public name of the assault party 1 object
        AssaultPartyInterface [] assaultPartyStub = new AssaultPartyInterface[SimulPar.NUM_ASSAULT_PARTIES];    // remote reference to the assault party 0 and 1 object
        // assaultPartyStub[0] = null;                                     // remote reference to the assault party 0 object
        // assaultPartyStub[1] = null;                                     // remote reference to the assault party 0 object

        Registry registry = null;                                      // remote reference for registration in the RMI registry service
        MasterThief masterThief;                                       // master thief thread

        try
        {   registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb); // location of the naming service
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        // get a remote reference to the general repository
        try
        {   reposStub = (GeneralReposInterface) registry.lookup (nameEntryGeneralRepos);
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("GeneralRepos lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        {   GenericIO.writelnString ("GeneralRepos not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        // get a remote reference to the concentration site
        try
        {   concSiteStub = (ConcSiteInterface) registry.lookup (nameEntryConcSite);
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("ConcentrationSite lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        {   GenericIO.writelnString ("ConcentrationSite not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        // get a remote reference to the control and collection site
        try
        {   collecSiteStub = (ControlCollecSiteInterface) registry.lookup (nameEntryCollecSite);
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("ControlColectionSite lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        {   GenericIO.writelnString ("ControlColectionSite not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        // get a remote reference to the assault party 0
        try
        {   assaultPartyStub[0] = (AssaultPartyInterface) registry.lookup (nameEntryAssaultParty[0]);
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("AssaultParty0 lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        {   GenericIO.writelnString ("AssaultParty0 not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        // get a remote reference to the assault party 1
        try
        {   assaultPartyStub[1] = (AssaultPartyInterface) registry.lookup (nameEntryAssaultParty[1]);
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("AssaultParty1 lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        {   GenericIO.writelnString ("AssaultParty1 not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        // Initializing the assault party ids
        for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
        {
            try{
                assaultPartyStub[i].initAssaultPartyID(i);
            }catch (RemoteException e)
            { GenericIO.writelnString ("Master generator remote exception on initAssaultPartyID: " + e.getMessage ());
              System.exit (1);
            }
        }

        masterThief = new MasterThief("MasterThief", concSiteStub, collecSiteStub, assaultPartyStub);

        /* start of the simulation */
        masterThief.start();
        GenericIO.writelnString("Lauching Master Thief Thread");

        /* waiting for the end of the simulation */
        GenericIO.writelnString ();
        try
        {
            masterThief.join();

        }catch(InterruptedException e) { }
        GenericIO.writelnString("The Master Thief has terminated.");

        /* end of simulation */
        GenericIO.writelnString("End of Simulation");

        // concentration site shutdown
        try
        {   concSiteStub.shutdown ();
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Master generator remote exception on ConcentrationSite shutdown: " + e.getMessage ());
            System.exit (1);
        }

        // control and collection site shutdown
        try
        {   collecSiteStub.shutdown ();
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Master generator remote exception on Control and Collection Site shutdown: " + e.getMessage ());
            System.exit (1);
        }

        // assault party 0 shutdown
        try
        {   assaultPartyStub[0].shutdown ();
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Master generator remote exception on Assault Party 0 shutdown: " + e.getMessage ());
            System.exit (1);
        }

        // assault party 1 shutdown
        try
        {   assaultPartyStub[1].shutdown ();
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Master generator remote exception on Assault Party 1 shutdown: " + e.getMessage ());
            System.exit (1);
        }

        // general repository shutdown
        try
        {   reposStub.shutdown ();
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Master generator remote exception on General Repository shutdown: " + e.getMessage ());
            System.exit (1);
        }
    }
}   