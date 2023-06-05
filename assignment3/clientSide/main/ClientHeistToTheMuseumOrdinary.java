package clientSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import clientSide.entities.*;
import serverSide.main.*;
import interfaces.*;
import genclass.GenericIO;

/**
 *    Client side of the Heist To The Museum (Ordinary Thieves).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class ClientHeistToTheMuseumOrdinary
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
        AssaultPartyInterface [] assaultPartyStub = new AssaultPartyInterface[SimulPar.NUM_ASSAULT_PARTIES];              
        // assaultPartyStub[0] = null;                                     // remote reference to the assault party 0 object
        // assaultPartyStub[1] = null;                                     // remote reference to the assault party 0 object

        String nameEntryMuseum = "Museum";                              // public name of the museum object
        MuseumInterface museumStub = null;                              // remote reference to the museum object

        Registry registry = null;                                      // remote reference for registration in the RMI registry service

        OrdinaryThief [] ordinaryThieves = new OrdinaryThief [SimulPar.NUM_ORD_THIEVES];      // array of ordinary thieves threads 


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

        // get a remote reference to the museum
        try
        {   museumStub = (MuseumInterface) registry.lookup (nameEntryMuseum);
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Museum lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        {   GenericIO.writelnString ("Museum not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        // Initializing the name of the general repository
        try
        {   reposStub.initSimul("log.txt"); 
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Ordinary Thief generator remote exception on initSimul: " + e.getMessage ());
            System.exit (1);
        }

        // Initializing the distances and number of paintings 
        int [] distances = new int[SimulPar.NUM_ROOMS];
        int [] rooms_Paintings = new int[SimulPar.NUM_ROOMS];
        for (int i = 0; i < SimulPar.NUM_ROOMS; i++)
        {
            distances[i] = SimulPar.generateRandom(SimulPar.MAX_DistOut, SimulPar.MIN_DistOut);
            rooms_Paintings[i] = SimulPar.generateRandom(SimulPar.MAX_NUM_PAITINGS, SimulPar.MIN_NUM_PATTINGS);
        }
        for (int id = 0; id < SimulPar.NUM_ASSAULT_PARTIES; id++)
        {
            try{
                assaultPartyStub[id].initAssaultPartyDist(distances);
            }catch (RemoteException e)
            { GenericIO.writelnString ("Ordinary Thief generator remote exception on initAssaultPartyDist: " + e.getMessage ());
              System.exit (1);
            }
        }
        try{
            museumStub.initInfoMuseum(rooms_Paintings);
        }catch (RemoteException e)
        { GenericIO.writelnString ("Ordinary Thief generator remote exception on initInfoMuseum: " + e.getMessage ());
          System.exit (1);
        }

        int [] maxDispl = new int[SimulPar.NUM_ORD_THIEVES];

        for (int i = 0; i < SimulPar.NUM_ORD_THIEVES; i++)
        {
            maxDispl[i] = SimulPar.generateRandom(SimulPar.MAX_MDISPLACEMENT, SimulPar.MIN_MDISPLACEMENT);
            ordinaryThieves[i] = new  OrdinaryThief("OrdinaryT_" + (i), 
                                                    i, 
                                                    maxDispl[i],
                                                    concSiteStub, 
                                                    collecSiteStub,
                                                    assaultPartyStub,
                                                    museumStub);

            //reposStub.setOrdinartThief_MaxDisp(i, ordinaryThieves[i].getThiefMaxDisp());
            try{
                reposStub.setOrdinartThief_MaxDisp(i, maxDispl[i]);
            }catch (RemoteException e)
            { GenericIO.writelnString ("Ordinary Thief generator remote exception on setOrdinartThief_MaxDisp: " + e.getMessage ());
              System.exit (1);
            }
        }     

        /* start of the simulation */
        for(int i = 0; i < SimulPar.NUM_ORD_THIEVES; i++)
		{
		    ordinaryThieves[i].start();
		    GenericIO.writelnString("\nLaunching Ordinary Thieves Thread " + (i));
		}   
 
         /* wait for the end of simulation */
         GenericIO.writelnString ();
         for(int i = 0; i < SimulPar.NUM_ORD_THIEVES; i++) {
			try
			{
				ordinaryThieves[i].join();

			}catch(InterruptedException e) { }
			GenericIO.writelnString("The Ordinary Thief " + (i) + " has terminated.");
		}
 
        /* end of simulation */
        GenericIO.writelnString("End of Simulation");

        // concentration site shutdown
        try
        {   concSiteStub.shutdown ();
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Ordinary Thief generator remote exception on ConcentrationSite shutdown: " + e.getMessage ());
            System.exit (1);
        }

        // control and collection site shutdown
        try
        {   collecSiteStub.shutdown ();
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Ordinary Thief generator remote exception on Control and Collection Site shutdown: " + e.getMessage ());
            System.exit (1);
        }

        // assault party 0 shutdown
        try
        {   assaultPartyStub[0].shutdown ();
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Ordinary Thief generator remote exception on Assault Party 0 shutdown: " + e.getMessage ());
            System.exit (1);
        }

        // assault party 1 shutdown
        try
        {   assaultPartyStub[1].shutdown ();
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Ordinary Thief generator remote exception on Assault Party 1 shutdown: " + e.getMessage ());
            System.exit (1);
        }

        // museum shutdown
        try
        {   museumStub.shutdown ();
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Ordinary Thief generator remote exception on Museum shutdown: " + e.getMessage ());
            System.exit (1);
        }

        // general repository shutdown
        try
        {   reposStub.shutdown ();
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("Ordinary Thief generator remote exception on General Repository shutdown: " + e.getMessage ());
            System.exit (1);
        }
        
    }
}