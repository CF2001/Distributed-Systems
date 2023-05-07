package main;

import entities.*;
import sharedRegions.*;
import genclass.GenericIO;

/**
 *   Simulation of the Problem of the Heist To The Museum.
 */
public class HeistToTheMuseum
{
    /**
    *    Main method.
    *
    *    @param args Runtime arguments
    */
    public static void main (String[] args)
    {
        MasterThief masterThief;                                              // master thief thread
        OrdinaryThief [] ordinaryThieves = new OrdinaryThief [SimulPar.M];    // array of ordinary thieves threads          
        ConcentrationSite  concentrationSite;                                 // reference to the concentration site
        AssaultParty [] assaultParties;                                       // reference to the assault party
        Museum museum;                                                        // reference to the museum
        ControlCollectionSite controlCollectionSite;                          // reference to the control and collection site
        GeneralRepository repos;                                              // reference to the general repository


        GenericIO.writelnString ("Start of Simulation");

        /* problem initialization */
        repos = new GeneralRepository("log.txt");
        museum = new Museum(repos);

        //Instantiation of Enteties and Shared Regions
        
        assaultParties = new AssaultParty[SimulPar.NUM_ASSAULT_PARTIES];
        int [] distances = new int[SimulPar.NUM_ROOMS];
        for (int i = 0; i < SimulPar.NUM_ROOMS; i++)
        {
            distances[i] = SimulPar.generateRandom(SimulPar.MAX_DistOut, SimulPar.MIN_DistOut);
        }
        for (int id = 0; id < SimulPar.NUM_ASSAULT_PARTIES; id++) 
        {
            assaultParties[id] = new AssaultParty(repos, id, distances);
        }

        //concentrationSite = new ConcentrationSite(repos, assaultParties);
        concentrationSite = new ConcentrationSite(repos);
        //controlCollectionSite = new ControlCollectionSite(repos, concentrationSite, assaultParties);
        controlCollectionSite = new ControlCollectionSite(repos);


        masterThief = new MasterThief("MasterThief", concentrationSite, controlCollectionSite, assaultParties);

        for (int i = 0; i < SimulPar.NUM_ORD_THIEVES; i++)
        {
            ordinaryThieves[i] = new  OrdinaryThief("OrdinaryT_" + (i), 
                                                    i, 
                                                    SimulPar.generateRandom(SimulPar.MAX_MDISPLACEMENT, SimulPar.MIN_MDISPLACEMENT),
                                                    concentrationSite, 
                                                    controlCollectionSite,
                                                    assaultParties,
                                                    museum);

            repos.setOrdinartThief_MaxDisp(i, ordinaryThieves[i].getThiefMaxDisp());  // ALTERAR ISTO AQUI !!!!! 
        }

        /* start of simulation */
        masterThief.start();
        GenericIO.writelnString("Lauching Master Thief Thread");

        for(int i = 0; i < SimulPar.NUM_ORD_THIEVES; i++)
		{
		    ordinaryThieves[i].start();
		    GenericIO.writelnString("Launching Ordinary Thieves Thread " + (i));
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
		
        GenericIO.writelnString ();
		try
		{
			masterThief.join();

		}catch(InterruptedException e) { }
		GenericIO.writelnString("The Master Thief has terminated.");
		
		
		/* end of simulation */
		GenericIO.writelnString("End of Simulation");


        repos.reportFinalStatus();
        repos.reportLegend();           // report Legend in log file
    }
}