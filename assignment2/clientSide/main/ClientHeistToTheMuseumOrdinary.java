package clientSide.main;

import clientSide.entities.*;
import clientSide.stubs.*;
import serverSide.main.SimulPar;
import commInfra.*;
import genclass.GenericIO;


/**
 *    Client side of the Heist To The Museum (Ordinary Thief).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ClientHeistToTheMuseumOrdinary 
{
    /**
     *  Main method.
     *
     *    @param args runtime arguments
     *        args[0] - name of the platform where is located the concentration site server
     *        args[1] - port number for listening to service requests
     *        args[2] - name of the platform where is located the control and collection site server
     *        args[3] - port number for listening to service requests
     *        args[4] - name of the platform where is located the assault party 0 server
     *        args[5] - port number for listening to service requests
     *        args[6] - name of the platform where is located the assault party 1 server
     *        args[7] - port number for listening to service requests
     *        args[8] - name of the platform where is located the museum server
     *        args[9] - port number for listening to service requests
     *        args[10] - name of the platform where is located the general repository server
     *        args[11] - port number for listening to service requests
     */
    public static void main (String [] args)
    {
        String concSiteServerHostName;                  // name of the platform where is located the concentration Site server
        int concSiteServerPortNumb = -1;                // port number for listening to service requests

        String controlCollecSiteServerHostName;         // name of the platform where is located the controlCollection Site server
        int controlCollecSitePortNumb = -1;             // port number for listening to service requests

        String [] assaultPServerHostName = new String[SimulPar.NUM_ASSAULT_PARTIES];  // name of the platform where is located the assaultParty servers
        int [] assaultPPortNumb = new int[SimulPar.NUM_ASSAULT_PARTIES];              // port number for listening to service requests
        for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
        {
            assaultPPortNumb[i] = -1;
        }

        String museumServerHostName;                    // name of the platform where is located the museum server
        int museumServerPortNumb = -1;                  // port number for listening to service requests

        String genReposServerHostName;                  // name of the platform where is located the general repository server
        int genReposServerPortNumb = -1;                // port number for listening to service requests


        OrdinaryThief [] ordinaryThieves = new OrdinaryThief [SimulPar.NUM_ORD_THIEVES];      // array of ordinary thieves threads 

        ConcentrationSiteStub concentrationSiteStub;                                                    // remote reference to the concentration Site
        ControlCollectionSiteStub  controlCollectionSiteStub;                                           // reference to the control and collection site
        AssaultPartyStub [] assaultPartiesStub = new AssaultPartyStub[SimulPar.NUM_ASSAULT_PARTIES];    // reference to the assault party
        MuseumStub museumStub;                                                                          // reference to the museum
        GeneralRepositoryStub genReposStub;                                                             // remote reference to the general repository


        /* getting problem runtime parameters */
        if (args.length != 12)  
        { 
            GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
        }

        // get concentraction site parameters 
        concSiteServerHostName = args[0];
        try
        { 
            concSiteServerPortNumb = Integer.parseInt (args[1]);
        }
        catch (NumberFormatException e)
        { 
            GenericIO.writelnString ("args[1] is not a number!");
            System.exit (1);
        }
        if ((concSiteServerPortNumb < 4000) || (concSiteServerPortNumb >= 65536))
        { 
            GenericIO.writelnString ("args[1] is not a valid port number!");
            System.exit (1);
        }

        // get control and collection site parameters 
        controlCollecSiteServerHostName = args[2];
        try
        { 
            controlCollecSitePortNumb = Integer.parseInt (args[3]);
        }
        catch (NumberFormatException e)
        { 
            GenericIO.writelnString ("args[3] is not a number!");
            System.exit (1);
        }
        if ((controlCollecSitePortNumb < 4000) || (controlCollecSitePortNumb >= 65536))
        { 
            GenericIO.writelnString ("args[3] is not a valid port number!");
            System.exit (1);
        }

        // get assault party 0 parameters 
        assaultPServerHostName[0] = args[4];
        try
        { 
            assaultPPortNumb[0] = Integer.parseInt (args[5]);
        }
        catch (NumberFormatException e)
        { 
            GenericIO.writelnString ("args[5] is not a number!");
            System.exit (1);
        }
        if ((assaultPPortNumb[0] < 4000) || (assaultPPortNumb[0] >= 65536))
        { 
            GenericIO.writelnString ("args[5] is not a valid port number!");
            System.exit (1);
        }

        // get assault party 1 parameters 
        assaultPServerHostName[1] = args[6];
        try
        { 
            assaultPPortNumb[1] = Integer.parseInt (args[7]);
        }
        catch (NumberFormatException e)
        { 
            GenericIO.writelnString ("args[7] is not a number!");
            System.exit (1);
        }
        if ((assaultPPortNumb[1] < 4000) || (assaultPPortNumb[1] >= 65536))
        { 
            GenericIO.writelnString ("args[7] is not a valid port number!");
            System.exit (1);
        }

        museumServerHostName = args[8];
        try
        { 
            museumServerPortNumb = Integer.parseInt (args[9]);
        }
        catch (NumberFormatException e)
        { 
            GenericIO.writelnString ("args[9] is not a number!");
            System.exit (1);
        }
        if ((museumServerPortNumb < 4000) || (museumServerPortNumb >= 65536))
        { 
            GenericIO.writelnString ("args[9] is not a valid port number!");
            System.exit (1);
        }

        // get general repository parameters 
        genReposServerHostName = args[10];
        try
        { 
            genReposServerPortNumb = Integer.parseInt (args[11]);
        }
        catch (NumberFormatException e)
        { 
            GenericIO.writelnString ("args[11] is not a number!");
            System.exit (1);
        }
        if ((genReposServerPortNumb < 4000) || (genReposServerPortNumb >= 65536))
        { 
            GenericIO.writelnString ("args[11] is not a valid port number!");
            System.exit (1);
        }


        /* problem initialization */
        concentrationSiteStub = new  ConcentrationSiteStub(concSiteServerHostName, concSiteServerPortNumb);   
        controlCollectionSiteStub = new ControlCollectionSiteStub(controlCollecSiteServerHostName, controlCollecSitePortNumb);
        for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
        {
            assaultPartiesStub[i] = new AssaultPartyStub(assaultPServerHostName[i], assaultPPortNumb[i]);
        }   
        museumStub = new MuseumStub(museumServerHostName, museumServerPortNumb);
        genReposStub = new GeneralRepositoryStub (genReposServerHostName, genReposServerPortNumb);
        genReposStub.initSimul("log.txt"); 
        
        int [] distances = new int[SimulPar.NUM_ROOMS];
        int [] rooms_Paintings = new int[SimulPar.NUM_ROOMS];
        for (int i = 0; i < SimulPar.NUM_ROOMS; i++)
        {
            distances[i] = SimulPar.generateRandom(SimulPar.MAX_DistOut, SimulPar.MIN_DistOut);
            rooms_Paintings[i] = SimulPar.generateRandom(SimulPar.MAX_NUM_PAITINGS, SimulPar.MIN_NUM_PATTINGS);
        }

        for (int id = 0; id < SimulPar.NUM_ASSAULT_PARTIES; id++) 
            assaultPartiesStub[id].initAssaultPartyDist(distances);

        museumStub.initInfoMuseum(rooms_Paintings);


        for (int i = 0; i < SimulPar.NUM_ORD_THIEVES; i++)
        {
            ordinaryThieves[i] = new  OrdinaryThief("OrdinaryT_" + (i), 
                                                    i, 
                                                    SimulPar.generateRandom(SimulPar.MAX_MDISPLACEMENT, SimulPar.MIN_MDISPLACEMENT),
                                                    concentrationSiteStub, 
                                                    controlCollectionSiteStub,
                                                    assaultPartiesStub,
                                                    museumStub);

            genReposStub.setOrdinartThief_MaxDisp(i, ordinaryThieves[i].getThiefMaxDisp());
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

        concentrationSiteStub.shutdown();
        controlCollectionSiteStub.shutdown();
        for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
        {
            assaultPartiesStub[i].shutdown();
        }
        museumStub.shutdown();
        genReposStub.shutdown();


    }
    
}
