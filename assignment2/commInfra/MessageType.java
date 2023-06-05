package commInfra;

/**
 *   Type of the exchanged messages.
 *
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */
public class MessageType 
{
    /******************** General repository messages ********************/

    /**
    *  Initialization of the logging file name (service request).
    */
    public static final int LOGFN = 1;

    /**
    *  Logging file was initialized (reply).
    */
    public static final int LOGFNDONE = 2;

    /**
     *  Set master state (service request).
     */
    public static final int SETMST = 3;

    /**
     *  Set ordinary thief (service request).
     */
    public static final int SETOTST = 4;

    /**
     *  Set maximum displacement of the ordinary Thief (service request).
     */
    public static final int SETMAXD = 5;

    /**
     *  Set the distance from outside gathering site of a museum Room (service request). 
     */
    public static final int SETROOMDIST = 6;

    /**
     *  Set the number of paintings in a museum Room (service request).
     */
    public static final int SETROOMPAINT = 7;

    /**
     *  Set the room identification of an assault party (service request).
     */
    public static final int SETAPROOMID = 8;

    /**
     *  Set the identification of an element in the assault party (service request).
     */
    public static final int SETAPELEMID = 9;

    /**
     *  Set situation of the ordinary Thief (service request).
     */
    public static final int SETOTSIT = 10;

    /**
     *  Set the position of an element in the assault party (service request).
     */
    public static final int SETAPELEMPOS = 11;

    /**
     *  Set the assault party element canvas state (service request).
     */
    public static final int SETAPELEMCV = 12;

    /**
     *  Set the stolen paitings (service request).
     */
    public static final int SETSTOLENP = 13;

    /**
     *  Setting acknowledged (reply).
     */
    public static final int SACK = 14;

    /******************** Concentration Site messages ********************/

    /**
     *  Master start operations (service request).
     */
    public static final int STARTOP = 15;

    /**
     *  Operations started (reply).
     */
    public static final int STARTOPDONE = 16;

    /**
     *  Master appraises the situation (service request).
     */
    public static final int APPRAISESIT = 17;

    /**
     *  Situation of the operation appraised (reply) .
     */
    public static final int APPRAISESITDONE = 18;

    /**
     *  Master needs to wait for the thieves (service request).
     */
    public static final int WAITTHIEVES = 19;

    /**
     *  Thieves arrived (reply)  
     */
    public static final int WAITTHIEVESDONE = 20;

    /**
     *  Thieves are needed in the concentration site (service request).
     */
    public static final int AMINEEDED = 21;

    /**
     *  Thieves arrived at the concetration site (reply).
     */
    public static final int AMINEEDEDDONE = 22;

    /**
     *  New state of the assault party (service request).
     */
    public static final int ASSAPID = 23;

    /**
     *  New state of the assault party (reply).
     */
    public static final int ASSAPIDDONE = 24;

    /**
     *  Get a room that has not yet been assigned and still has rooms (service request).
     */
    public static final int ASSROOMID = 25;

    /** 
     *  Get a room that has not yet been assigned and still has rooms (reply).
     */
    public static final int ASSROOMIDDONE = 26;

    /**
     *  New room assigned to the assault party (service request).
     */
    public static final int SETASSRID = 27;

    /**
    *  New room assigned to the assault party (reply).
    */
    public static final int SETASSRIDDONE = 28;

    /**
     *  Master wants to build an assault party (service request).
     */
    public static final int BUILDP = 29;

    /**
     *  Assault Party Built (reply).
     */
    public static final int BUILDPDONE = 30;

    /**
     *  Master intends to notify thieves already added to the assault party (service request).
     */
    public static final int PREPAREAP = 31;

    /**
     *  Master notifies thieves already added to the assault party (reply).
     */
    public static final int PREPAREAPDONE = 32;

    /**
     *  The thieves want to notify the master that the last member of the group has arrived (service request).
     */
    public static final int PREPAREEX = 33;

    /**
     *  The thieves want to notify the master that the last member of the group has arrived (reply).
     */
    public static final int PREPAREEXDONE = 34;

    /**
     *  Master wants to send the assault party (service request).
     */
    public static final int SENDAP = 35;

    /**
     *  Master is sending the assault party (reply).
     */
    public static final int SENDAPDONE = 36;

    /**
     *  Update Room Status  (service request).
     */
    public static final int SETROOMST = 37;

    /**
     *  Room status Updated (reply).
     */
    public static final int SETROOMSTDONE = 38;

    /**
     *  Master wants to set the assault parties status (service request). 
     */
    public static final int SETASSAPID = 39;

    /**
     *  Master wants to set the assault parties status (reply). 
     */
    public static final int SETASSAPIDDONE = 40;

    /**
     *  Master wants to end the heist after collecting all the paintings (service request).
     */
    public static final int SUMUPRES = 41;
    
    /**
     *  Master is ending the heist after collecting all the paintings (reply).
     */
    public static final int SUMUPRESDONE = 42;


    /******************** Assault Party messages ********************/

    /**
     *  Initialization assault party id and the distances of the rooms (service request).
     */
    public static final int INFOAPID = 43;

    /**
     *  The assault party id and the distances of the rooms has been initialized (reply).
     */
    public static final int INFOAPIDDONE = 44;

    /**
     * Initialization of the assault party  and the distance from the rooms to outside (service request).
     */
    public static final int INFOAPDIST = 45;

    /**
     *  Initialization of the assault party  and the distance from the rooms to outside (reply).
     */
    public static final int INFOAPDISTDONE = 46;

    /**
     *  Add the thieves group to your assault party (service request).
     */
    public static final int ADDTPARTY = 47;

    /**
     *  Thieves added to the group (reply).
     */
    public static final int ADDTPARTYDONE = 48;

    /**
     *  Master notifies the first member of the group to move (service request).
     */
    public static final int MOVEFIRSTM = 49;

    /**
     *  First member moves (reply).
     */
    public static final int MOVEFIRSTMDONE = 50;

    /**
     *  The thief wants to start crawling into the museum room (service request).
     */
    public static final int CRAWLIN = 51;

    /**
     *  The thief starts to crawl into the museum room (reply).
     */
    public static final int CRAWLINDONE = 52;

    /**
     *  The ordinary thief wants to know the room id assigned to his assault party (service request).
     */
    public static final int GETAPROOMID = 53;

    /**
     *  The ordinary thief knows the room id assigned to his assault party (reply).
     */
    public static final int GETAPROOMIDDONE = 54;

    /**
     *  The ordinary thief wants to reverse direction (service request).
     */
    public static final int REVERSEDIRECTION = 55;

    /**
     *  The ordinary thief reversed direction (reply).
     */
    public static final int REVERSEDIRECTIONDONE = 56;

    /**
    *   The thief wants to start crawling into the outside gathering site (service request).
    */
    public static final int CRAWLOUT = 57;

    /**
    *   The thief starts crawling into the outside gathering site (reply).
    */
    public static final int CRAWLOUTDONE = 58;

   /**
    *   Get the assault party thief index (Service request)
    */
    public static final int GETTINDEX =  59;

   /**
    *   Get the assault party thief index done (reply)
    */
    public static final int GETTINDEXDONE = 60;

   /**
    *   Master wants to reset the assault parties (service request).
    */
    public static final int RESETAP = 61;

    /**
     *  Master is resetting the assault parties (reply).
     */
    public static final int RESETAPDONE = 62;

    /******************** Museum messages ********************/

    /**
     *  Initializing the number of paintings in the museum rooms (service request).
     */
    public static final int SETINFOMUSEUM = 63;

    /**
     *  Number of paintings in the museum rooms initialized (reply).
     */
    public static final int SETINFOMUSEUMDONE = 64;

    /**
     *  The thief wants to steal a painting from the museum (service request).
     */
    public static final int ROOLCV = 65;

    /**
     *  The thief is stealing a painting from the museum (reply).
     */
    public static final int ROOLCVDONE = 66;

    /******************** Control and Collection Site messages ********************/
    
    /**
     *  Master needs to wait for the assault party in action (service request).
     */
    public static final int TAKEREST = 67;

    /**
     *  Master is waiting for the assault party in action (reply).
     */
    public static final int TAKERESTDONE = 68;

    /**
     *  Hand a canvas to the master (service request).
     */
    public static final int HANDACANVAS = 69;

    /**
     *  Hand a canvas to the master (reply).
     */
    public static final int HANDACANVASDONE = 70;

    /**
     * Collect a canvas from the ordinary thief (service request).
     */
    public static final int COLLECTACANVAS = 71;

    /**
     * Collect a canvas from the ordinary thief (reply).
     */
    public static final int COLLECTACANVASDONE = 72;

    /******************** shutdown messages ********************/

    /**
    *  Server shutdown (service request).
    */
    public static final int SHUT = 73;

   /**
    *  Server was shutdown (reply).
    */
    public static final int SHUTDONE = 74; 
}
