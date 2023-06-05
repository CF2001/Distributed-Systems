package serverSide.objects;

import java.rmi.registry.*;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import genclass.GenericIO;
import genclass.TextFile;
import java.util.Objects;

/**
 *  General Repository of information.
 *
 *    It is responsible to keep the visible internal state of the problem and to provide means for it
 *    to be printed in the logging file.
 *    It is implemented as an implicit monitor.
 *    All public methods are executed in mutual exclusion.
 *    There are no internal synchronization points.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class GeneralRepository implements GeneralReposInterface
{
    /**
     *  Name of the logging file.
     */
    private String logFileName;

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     *  Number of paintings stolen by ordinary thieves at the end of the heist.
     */
    private int stolenPaitings;

    /*************** Master Thief / Ordinary Thief ***************/
    /**
     *  State of the Master Thief.
     */
    private int masterThiefState;

    /**
     *  State of the Ordinary Thief.
     */
    private int [] ordinayThiefState;

    /**
     *  Situation of the Ordinary Thief, either 'w' (waiting to join a party) or 'P' (in party).
     */
    private String [] ordinaryThiefSituation; 

    /**
     *  Maximum displacement of the Ordinary Thief.
     */
    private int [] ordinartThief_MaxDisp;


    /*************** Assault Party ***************/

    /**
     *  Room Identification in the assault party.
     */
    private int [] assaultParty_RId;

    /**
     *  Member Identification in the assault party.
     */
    private int [][] assaultParty_Elem_ID;

     /**
     *  Present Position of a member in the assault party.
     */
    private int [][] assaultParty_Elem_POS;

     /**
     *  If a member of the assault party  is carrying a canvas.
     */
    private int [][] assaultParty_Elem_CV;

    /*************** Museum Room ***************/

    /**
     *  Number of paintings presently hanging on the walls in a Room.
     */
    private int [] MRoom_NPaintings;

    /**
     *  Distance from outside gathering site of a Room.
     */
    private int [] MRoom_DT_outsideG;

    /**
     *   Instantiation of a general repository object.
     */
    public GeneralRepository ()
    {
        logFileName = "logger";
        
        nEntities = 0;
        stolenPaitings = 0;

        masterThiefState = MasterThiefStates.PLANNING_THE_HEIST;

        ordinayThiefState = new int[SimulPar.NUM_ORD_THIEVES];
        ordinaryThiefSituation = new String[SimulPar.NUM_ORD_THIEVES];
        ordinartThief_MaxDisp = new int[SimulPar.NUM_ORD_THIEVES];
        for (int i = 0; i < SimulPar.NUM_ORD_THIEVES; i++)
        {
            ordinayThiefState[i] = OrdinaryThiefStates.CONCENTRATION_SITE;
            ordinaryThiefSituation[i] = "W";      
            ordinartThief_MaxDisp[i] = 0;       
        }

        assaultParty_RId = new int[SimulPar.NUM_ASSAULT_PARTIES];
        for (int i = 0; i < assaultParty_RId.length; i++)
        {
            assaultParty_RId[i] = -1;     
        }

        assaultParty_Elem_ID = new int[SimulPar.NUM_ASSAULT_PARTIES][SimulPar.K];
        assaultParty_Elem_POS = new int[SimulPar.NUM_ASSAULT_PARTIES][SimulPar.K];
        assaultParty_Elem_CV = new int[SimulPar.NUM_ASSAULT_PARTIES][SimulPar.K];
        for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
        {
            for (int j = 0; j < SimulPar.K; j++)
            {
                assaultParty_Elem_ID [i][j] = -1;    
                assaultParty_Elem_POS [i][j] = 0;     
                assaultParty_Elem_CV [i][j] = 0;    
            }
        }

        MRoom_NPaintings = new int [SimulPar.NUM_ROOMS];
        MRoom_DT_outsideG = new int [SimulPar.NUM_ROOMS];
        for (int i = 0; i < SimulPar.NUM_ROOMS; i++)
        {
            MRoom_NPaintings[i] = -1;
            MRoom_DT_outsideG[i] = -1;
        }
    }

    /**
     *   Operation initialization of simulation.
     *
     *   New operation.
     *
     *      @param logFileName name of the logging file
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void initSimul (String logFileName)  throws RemoteException
    {
        if (!Objects.equals (logFileName, ""))
            this.logFileName = logFileName;
        
        reportInitialStatus ();
    }

   /**
     *   Operation server shutdown.
     *
     *   New operation.
     * 
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void shutdown ()    throws RemoteException
    {
        nEntities += 1;
        if (nEntities >= SimulPar.E_GENR)
        {
            ServerHeistMuseumGeneralRepos.shutdown();
            reportFinalStatus();
            reportLegend();
        }
    }

    /**
     *  Write in the logging file the stolen paitings.
     * 
     *      @param stolenPaitings number of stolen paintings
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void setStolenPaitings(int stolenPaitings) throws RemoteException
    {
        this.stolenPaitings = stolenPaitings;
    }

    /**
     *  Write in the logging file the state of the master Thief.
     * 
     *      @param masterThiefState  master thief state
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void setMasterThiefState(int masterThiefState) throws RemoteException
    {
        this.masterThiefState = masterThiefState;
        reportStatus();
    }

    /**
     *  Write in the logging file the state of the ordinary Thief.
     * 
     *      @param thiefID              ordinary thief id
     *      @param ordinaryThiefState    ordinary thief state
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void setOTState(int thiefID, int ordinaryThiefState) throws RemoteException
    {
        this.ordinayThiefState[thiefID] = ordinaryThiefState;
        reportStatus();
    }

    /**
     * Write in the logging file the situation of the ordinary Thief.
     * 
     *      @param thiefID   ordinary thief id
     *      @param ordinaryThiefSituation    ordinary thief situation
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void setOTSituation(int thiefID, String ordinaryThiefSituation) throws RemoteException
    {
        this.ordinaryThiefSituation[thiefID] = ordinaryThiefSituation;
        reportStatus();
    }

    /**
     *  Write in the logging file the max displacement of the ordinary Thief.
     *
     *      @param thiefID    ordinary thief id
     *      @param maxDisp    ordinary thief maximum displacement
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void setOrdinartThief_MaxDisp(int thiefID, int maxDisp) throws RemoteException
    {
        this.ordinartThief_MaxDisp[thiefID] = maxDisp;
        //reportStatus();
    }

    /**
     *  Write in the logging file the room id of an assault party.
     * 
     *      @param assaultParty_ID   assault party id
     *      @param roomID    room id
     *      @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    @Override
    public synchronized void setAssaultParty_RId(int assaultParty_ID, int roomID) throws RemoteException
    {
        this.assaultParty_RId[assaultParty_ID] = roomID;
        reportStatus();
    }

    /**
     *  Write in the logging file the id of an element in the assault party.
     * 
     *      @param assaultParty_ID   assault party id
     *      @param elemID    element id
     *      @param thiefID   thief id
     */
    @Override
    public synchronized void setAParty_Elem_ID(int assaultParty_ID, int elemID, int thiefID) throws RemoteException
    {
        this.assaultParty_Elem_ID[assaultParty_ID][elemID] = thiefID;
        reportStatus();
    }

    /**
     * Set the assault party element position 
     * 
     *      @param assaultParty_ID    assault party id
     *      @param elemID    element id
     *      @param thiefPos  thief id
     */
    @Override
    public synchronized void setAParty_Elem_POS(int assaultParty_ID, int elemID, int thiefPos) throws RemoteException
    {
        this.assaultParty_Elem_POS[assaultParty_ID][elemID] = thiefPos;
        reportStatus();
    }

    /**
     * Set the assault party element canvas state  
     * 
     *      @param assaultParty_ID   assault party id
     *      @param elemID    element id
     *      @param thiefCV   thief id
     */
    @Override
    public synchronized void setAParty_Elem_CV(int assaultParty_ID, int elemID, int thiefCV) throws RemoteException
    {
        this.assaultParty_Elem_CV[assaultParty_ID][elemID] = thiefCV;
        reportStatus();
    }

    /**
     *  Set the number of paintings in a museum Room.
     * 
     *      @param roomID        room identification
     *      @param nPaintings    number of pautings
     */
    @Override
    public synchronized void setMRoom_NPaintings(int roomID, int nPaintings) throws RemoteException
    { 
        this.MRoom_NPaintings[roomID] = nPaintings;
        reportStatus();
    }

    /**
     *  Set the distance from outside gathering site of a museum Room.
     * 
     *      @param roomID            room identification
     *      @param distFromOutside   distance from outside
     */
    @Override
    public synchronized void setMRoom_DT_outsideG(int roomID, int distFromOutside) throws RemoteException
    {
        this.MRoom_DT_outsideG[roomID] = distFromOutside;
        reportStatus();
    }

    /**
     *  Write the header to the logging file.
     *
     *  The Master Thief is planning the heist and the ordinary thieves are at the concentration site waiting to be needed.
     *  Internal operation.
     */
    private void reportInitialStatus ()
    {
        TextFile log = new TextFile ();                      // instantiation of a text file handler

        if (!log.openForWriting (".", logFileName))
        { 
            GenericIO.writelnString ("The operation of creating the file " + logFileName + " failed!");
            System.exit (1);
        }

        log.writelnString ("                                Heist to the Museum - Description of the internal state\n\n");
        log.writelnString (" MstT       Thief 1       Thief 2        Thief 3         Thief 4       Thief 5        Thief 6");
        log.writelnString(" Stat      Stat S MD      Stat S MD      Stat S MD      Stat S MD      Stat S MD      Stat S MD\n");
        log.writelnString("                         Assault party 1                        Assault party 2                       Museum");
        log.writelnString("             Elem 1      Elem 2       Elem 3          Elem 1      Elem 2       Elem 3        Room 1     Room 2    Room 3    Room 4    Room 5");
        log.writelnString("     RId  Id Pos Cv    Id Pos Cv    Id Pos Cv    RId  Id Pos Cv    Id Pos Cv    Id Pos Cv     NP DT     NP DT     NP DT     NP DT     NP DT\n");

        if (!log.close ())
        { 
            GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
            System.exit (1);
        }
            
        reportStatus ();
    }

    /**
     *  Write two state lines at the end of the logging file.
     *
     *  The current state of the master thief and the ordinary thieves is organized in a line to be printed.
     *  Internal operation.
     */
    private void reportStatus ()
    {
        TextFile log = new TextFile ();                      // instantiation of a text file handler

        String lineStatus1 = "";                              // state line 1 to be printed
        String lineStatus2 = "      ";                        // state line 2 to be printed

        if (!log.openForAppending (".", logFileName))
        { 
            GenericIO.writelnString ("The operation of opening for appending the file " + logFileName + " failed!");
            System.exit (1);
        }

        /*******  lineStatus1 ********/

        switch(masterThiefState)
        {
            case MasterThiefStates.PLANNING_THE_HEIST:      lineStatus1 +=  " PLAN_TH  ";
                                                            break;
            case MasterThiefStates.DECIDING_WHAT_TO_DO:     lineStatus1 +=  " DECD_WTD ";
                                                            break;
            case MasterThiefStates.ASSEMBLING_A_GROUP:      lineStatus1 +=  " ASSMB_AG ";
                                                            break;
            case MasterThiefStates.WAITING_FOR_ARRIVAL:     lineStatus1 +=  " WAIT_FA  ";
                                                            break;
            case MasterThiefStates.PRESENTING_THE_REPORT:   lineStatus1 +=  " PRESG_TR ";
                                                            break;
        }

        for (int i = 0; i < SimulPar.NUM_ORD_THIEVES; i++)
        {
            switch(ordinayThiefState[i])
            {
                case OrdinaryThiefStates.CONCENTRATION_SITE:  lineStatus1 +=  " CONCENC_S ";
                                                                break;
                case OrdinaryThiefStates.CRAWLING_INWARDS:    lineStatus1 +=  " CRAWL_IN  ";
                                                                break;
                case OrdinaryThiefStates.AT_A_ROOM:           lineStatus1 +=  " AT_A_ROOM ";
                                                                break;
                case OrdinaryThiefStates.CRAWLING_OUTWARDS:   lineStatus1 +=  " CRAWL_OUT ";
                                                                break;
                case OrdinaryThiefStates.COLLECTION_SITE:     lineStatus1 +=  " COLLECT_S ";
                                                                break;
            }

            if (ordinartThief_MaxDisp[i] == 0)
            {
                lineStatus1 += ordinaryThiefSituation[i] + " " + "-" + " ";
            }else{

                lineStatus1 += ordinaryThiefSituation[i] + " " + ordinartThief_MaxDisp[i] + " ";
            }
        }

        /*******  lineStatus2 ********/

        for (int i = 0; i < SimulPar.NUM_ASSAULT_PARTIES; i++)
        {
            if (assaultParty_RId[i] == -1)
            {
                lineStatus2 += "-" + "    ";
            }else{
                lineStatus2 += (assaultParty_RId[i]+1) + "    ";
            }
            
            for (int j = 0; j < SimulPar.K; j++)
            {
                if (assaultParty_Elem_ID[i][j] == -1)
                {
                    lineStatus2 += "-" + "  " + assaultParty_Elem_POS[i][j] + "  " + assaultParty_Elem_CV[i][j] + "      " ;
                }else{
                    lineStatus2 += (assaultParty_Elem_ID[i][j]+1) + "  " + assaultParty_Elem_POS[i][j] + "  " + assaultParty_Elem_CV[i][j] + "      " ;
                }
                
            }
        }

        for (int i = 0; i < SimulPar.NUM_ROOMS; i++)
        {
            if (MRoom_NPaintings[i] == -1 ||  MRoom_DT_outsideG[i] == -1)
            {
                lineStatus2 += "--" + "  " + "--" + "    "; 
            }else{
                lineStatus2 += MRoom_NPaintings[i] + "  " + MRoom_DT_outsideG[i] + "    "; 
            }   
        }

        log.writelnString (lineStatus1);
        log.writelnString (lineStatus2);
        if (!log.close ())
        { 
            GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
            System.exit (1);
        }
    }

    /**
	 *   Write in the logging file the final Status of the Heist.
	 */
    public void reportFinalStatus ()
    {
        TextFile log = new TextFile ();                  	// instantiation of a text file handler
        if (!log.openForAppending (".", logFileName))
		{ 
			GenericIO.writelnString ("The operation of opening for appending the file " + logFileName + " failed!");
			System.exit (1);
		}

        log.writelnString("\nMy friends, tonight's effort produced " + stolenPaitings + " priceless paintings!");

        if (!log.close ())
		{ 
			GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
			System.exit (1);
		}
    }

    /**
	 *   Write in the logging file the legend.
	 */
	public void reportLegend()
	{
		TextFile log = new TextFile ();                  	// instantiation of a text file handler
		if (!log.openForAppending (".", logFileName))
		{ 
			GenericIO.writelnString ("The operation of opening for appending the file " + logFileName + " failed!");
			System.exit (1);
		}
		
		log.writelnString("\nLegend:");
        log.writelnString("MstT Stat - state of the master thief");
        log.writelnString("Thief # Stat - state of the ordinary thief # (# - 1 .. 6)");
        log.writelnString("Thief # S - situation of the ordinary thief # (# - 1 .. 6) either 'W' (waiting to join a party) or 'P' (in party)");
        log.writelnString("Thief # MD - maximum displacement of the ordinary thief # (# - 1 .. 6) a random number between 2 and 6");
        log.writelnString("Assault party # RId - assault party # (# - 1,2) elem # (# - 1 .. 3) room identification (1 .. 5)");
        log.writelnString("Assault party # Elem # Id - assault party # (# - 1,2) elem # (# - 1 .. 3) member identification (1 .. 6)");
        log.writelnString("Assault party # Elem # Pos - assault party # (# - 1,2) elem # (# - 1 .. 3) present position (0 .. DT RId)");
        log.writelnString("Assault party # Elem # Cv - assault party # (# - 1,2) elem # (# - 1 .. 3) carrying a canvas (0,1)");
        log.writelnString("Museum Room # NP - room identification (1 .. 5) number of paintings presently hanging on the walls");
        log.writelnString("Museum Room # DT - room identification (1 .. 5) distance from outside gathering site, a random number between 15 and 30");

		if (!log.close ())
		{ 
			GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
			System.exit (1);
		}
	}
}