package commInfra;

import java.io.*;
import genclass.GenericIO;

/**
 *   Internal structure of the exchanged messages.
 *
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */
public class Message implements Serializable{
    
    /**
    *  Serialization key.
    */
    private static final long serialVersionUID = 2021L;

    /**
     *  Message type.
     */
    private int msgType = -1;

    /**
     *  Name of the logging file.
     */
    private String fName = null;

    /**
     *  Index of the thief in the assault party for general repository.
     */
    private int elementID = -1;

    /**
     *  Master Thief state.
     */
    private int masterState = -1;

    /**
     *  Number of collected paintings by the master thief.
     */
    private int nPaintingsCollected = -1;

    /**
     *  Decision of the master thief when appraising the situation
     */
    private int transitionMaster = -1;

    /**
     *  Ordinary Thief state.
     */
    private int ordinaryTState = -1;

    /**
     *  Ordinary Thief identifucation.
     */
    private int ordinaryThiefID = -1;

    /**
     *  Maximum displacement of an ordinary thief.
     */
    private int maxDispOT = -1;

    /**
     *  Ordinary Thief Assault party identification.
     */
    private int ordinaryTAssaultPID = -1;

    /**
     *  Ordinary Thief Assault Party Position.
     */
    private int ordinaryTAP_Pos = -1;

    /**
     *  If ordinary thief has a canvas.
     */
    private int ordinaryTCV = 0;

    /**
     *  Index of the thief in the assault party for general repository.
     */
    private int ordinaryTAP_index;

    /**
     *  Ordinary Thief Situation.
     */
    private String ordinaryThiefSit = "W";

    /**
     *  Assault party identification.
     */
    private int assaultPartyID = -1;

    /**
     *  Room identification.
     */
    private int roomID = -1;

    /**
     *  Distances of the rooms from outside. 
     */
    private int [] distFromOutsideArray;

    /**
     *  Distance of an room from outside.
     */
    private int distFromOutside = -1;

    /**
     *  Number of paintings in the museum rooms. 
     */
    private int [] nPaintingsRooms;

    /**
     *  Number of paintings in a museum Room.
     */
    private int nPaintings = -1;

    /**
     *  Am I Needed (Ordinary thief)
     */
    private Boolean amINeeded = false;

    /**
     *  Movement status of the first member of the assault party
     */
    private Boolean moveFirstMember = false;

    /**
     *  Identification of the assigned assault party (Master)
     */
    private int assignAssaultPartyID = -1;

    /**
     *  Identification of the assigned room (Master)
     */
    private int assignRoomID = -1; 

    /**
     *  State of the room. False if not empty.
     */
    private Boolean roomState = false;

    /**
     *  Assault party state. True if is already assigned.
     */
    private Boolean assaultPState = false;

    /**
     *  Assault party of 3 thieves built by master.
     */
    private int [] assaultPartyBuild;

    /**
     *  Assault party for reset.
     */
    private Boolean [] resetAPArray;


    /**
     *  Message instantiation (form 1).
     *
     *     @param type message type
     */
    public Message (int type)
    {
        msgType = type;
    }

    /**
     *  Message instantiation (form 2).
     *
     *     @param type message type
     *     @param name name of the logging file
     */
   public Message (int type, String name)
   {
        msgType = type;
        fName = name;
   }

   /**
    *   Message instantiation (form 3).
    *
    *       @param type   message type
    *       @param info  assault party ID / master thief state / assigned AP id / 
    *                   assigned room id  / thief id  / collect cv master / number of stolen paintings
    */
   public Message (int type, int info)
   {
        msgType = type;
        if (msgType == MessageType.INFOAPID)
        {
            assaultPartyID = info;
        }else if (msgType == MessageType.STARTOP || msgType == MessageType.STARTOPDONE || msgType == MessageType.APPRAISESIT || 
                msgType == MessageType.SETMST || msgType == MessageType.BUILDP || msgType == MessageType.TAKEREST || msgType == MessageType.TAKERESTDONE)
        {
            masterState = info;
        }else if (msgType == MessageType.ASSAPIDDONE)
        {
            assignAssaultPartyID = info;
        }else if (msgType == MessageType.ASSROOMIDDONE || msgType == MessageType.GETAPROOMIDDONE){
            assignRoomID = info;
        }else if (msgType == MessageType.GETTINDEX)
        {
            ordinaryThiefID = info;
        }else if(msgType== MessageType.COLLECTACANVAS){
            nPaintingsCollected = info;
        }else if (msgType == MessageType.SETSTOLENP)
        {
            this.nPaintingsCollected = info;
        }else{
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
   }

   /**
    *   Message instantiation (form 4).
    *
    *       @param type   message type
    *       @param info1  master thief state/ ordinary thief state / assign assaultPartyID / 
    *                     max displacement OT / distFromOutside / nPaintings / assault Party id / assault party thief index
    *       @param info2  master thief transition/ ordinary thief id / assign roomID / room ID  
    */
    public Message (int type, int info1, int info2)
    {
        this.msgType = type;
        if (msgType == MessageType.SETMAXD)
        {
            this.maxDispOT = info1;
            this.ordinaryThiefID = info2;
        }else if (msgType == MessageType.SETROOMDIST)
        {
            this.distFromOutside = info1;
            this.roomID = info2;
        }else if (msgType == MessageType.SETROOMPAINT)
        {
            this.nPaintings = info1;
            this.roomID = info2;
        }else if (msgType == MessageType.APPRAISESITDONE)
        {
            this.masterState = info1;
            this.transitionMaster = info2; 
        }else if (msgType == MessageType.AMINEEDED || msgType == MessageType.SETOTST)
        {
            this.ordinaryTState = info1;
            this.ordinaryThiefID = info2;
        }else if (msgType == MessageType.SETASSRID)
        {
            this.assignAssaultPartyID = info1;
            this.assignRoomID = info2;
        }else if (msgType == MessageType.SETAPROOMID)
        {
            this.assaultPartyID = info1;
            this.roomID = info2;
        }else if (msgType == MessageType.GETTINDEXDONE)
        {
            this.ordinaryTAP_index = info1;
            this.ordinaryThiefID = info2;

        }else if (msgType == MessageType.SUMUPRES || msgType == MessageType.SUMUPRESDONE)
        {
            this.masterState = info1;
            this.nPaintingsCollected = info2;
        }else{
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
    }

    /**
    *   Message instantiation (form 5).     -- AMINEEDEDDONE
    *
    *       @param type   message type
    *       @param state  ordianry thief state
    *       @param thiefID ordinary thief identification
    *       @param amINeeded true if ordinary thief is needed
    */
    public Message (int type, int state, int thiefID, Boolean amINeeded)
    {
        this.msgType = type;
        this.ordinaryTState = state;
        this.ordinaryThiefID = thiefID;
        this.amINeeded = amINeeded;
    }

    /**
     *  Message instantiation (form 6).
     * 
     *      @param type     message type
     *      @param info    array of room paintings / room distances from outside
     */
    public Message (int type, int [] info)
    {
        this.msgType = type;
        if (msgType == MessageType.INFOAPDIST)
        {
            this.distFromOutsideArray = info;
        }else if (msgType == MessageType.SETINFOMUSEUM)
        {
            this.nPaintingsRooms = info;
        }else {
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
        
    }

    /**
     *  Message instantiation (form 7).     -- BUILDPDONE | ADDTPARTY
     * 
     *      @param type    message type
     *      @param info1   assault party build by master / set of thieves that is added to the assault party
     *      @param info2   identification of the room assigned to the set of thieves / master state
     */
    public Message (int type, int [] info1, int info2)
    {
        this.msgType = type;
        if (msgType == MessageType.BUILDPDONE)
        {
            this.assaultPartyBuild = info1;
            this.masterState = info2;
        }else if (msgType == MessageType.ADDTPARTY)
        {
            this.assaultPartyBuild = info1;
            this.assignRoomID = info2;
        }else{
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
        
    }

    /**
     *  Message instantiation (form 8).     -- PREPAREAP
     * 
     *      @param type     message type
     *      @param assaultParty     assault party
     *      @param assaultPartyID   assault party identification
     *      @param roomID           room identification assigned to the assault
     */
    public Message (int type, int [] assaultParty, int assaultPartyID, int roomID)
    {
        this.msgType = type;
        this.assaultPartyBuild = assaultParty;
        this.assignAssaultPartyID = assaultPartyID;
        this.assignRoomID = roomID;
    }

    /**
     *  Message instantiation (form 9).   
     * 
     *      @param type     message type
     *      @param info1    assault party identification  / ordinary thief assault party identification / ordinary CV
     *      @param info2    index of the thief in the assault party (for repos) / ordinary thief state 
     *      @param info3    ordinary thief id / ordinary thief position /  ordinary CV (SETAPELEMCV)
     */
    public Message(int type, int info1, int info2, int info3)
    {
        this.msgType = type;
        if (msgType == MessageType.SETAPELEMID)
        {
            this.assaultPartyID = info1;
            this.elementID = info2;
            this.ordinaryThiefID = info3;
        }else if (msgType == MessageType.SETAPELEMPOS)
        {   
            this.assaultPartyID = info1;
            this.elementID = info2;
            this.ordinaryTAP_Pos = info3;
        }else if (msgType == MessageType.PREPAREEX || msgType == MessageType.PREPAREEXDONE)
        {
            this.ordinaryTAssaultPID = info1;
            this.ordinaryTState = info2;
            this.ordinaryThiefID = info3;
        }else if (msgType == MessageType.ROOLCVDONE || msgType == MessageType.REVERSEDIRECTION || msgType == MessageType.REVERSEDIRECTIONDONE)
        {
            this.ordinaryTCV = info1;
            this.ordinaryTState = info2;
            this.ordinaryThiefID = info3;
        }else if (msgType == MessageType.SETAPELEMCV)
        {
            this.assaultPartyID = info1;
            this.elementID = info2;
            this.ordinaryTCV = info3;
        }else {
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
    }

    /**
     *  Message instantiation (form 10).    -- SETOTSIT
     * 
     *      @param type             message type
     *      @param thiefID          ordinary thief identification
     *      @param thiefSituation   ordinary thief situation
     */
    public Message (int type, int thiefID, String thiefSituation)
    {
        this.msgType = type;
        this.ordinaryThiefID = thiefID;
        this.ordinaryThiefSit = thiefSituation; 
    }

    /**
     *  Message instantiation (form 10).    -- SETOTSIT
     * 
     *      @param type message type
     *      @param moveFirstMember  movement status of the first member of the assault party
     */
    public Message (int type, Boolean moveFirstMember)
    {
        this.msgType = type;
        this.moveFirstMember = moveFirstMember;
    }

    /**
     *  Message instantiation (form 11).   
     * 
     *      @param type     message type
     *      @param info1    ordinary thief id
     *      @param info2    ordinary thief assault party id / ordinary thief state
     *      @param info3    ordinary thief assault party position  / ordinary thief assault party id 
     *      @param info4    ordinary thief max displacement / has canvas
     */
    public Message (int type, int info1, int info2, int info3, int info4)
    {
        this.msgType = type;
        
        if (msgType == MessageType.CRAWLIN || msgType == MessageType.CRAWLINDONE || msgType==MessageType.CRAWLOUT || msgType == MessageType.CRAWLOUTDONE)
        {
            this.ordinaryThiefID = info1;
            this.ordinaryTAssaultPID = info2;
            this.ordinaryTAP_Pos = info3;
            this.maxDispOT = info4;

        } else if (msgType == MessageType.ROOLCV)
        {
            this.ordinaryThiefID = info1;
            this.ordinaryTState = info2;
            this.ordinaryTCV = info3;
            this.assignRoomID = info4;

        }else if(msgType == MessageType.HANDACANVASDONE)
        {
            this.ordinaryThiefID = info1;
            this.ordinaryTState = info2;
            this.ordinaryTAssaultPID = info3;
            this.ordinaryTCV = info4;
        }else{
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
    }

    /**
     *  Message instantiation (form 12) 
     * 
     *      @param type     message type
     *      @param info1    room id / assigned assault party id
     *      @param info2    room state  / assault party status
     */
    public Message (int type, int info1, Boolean info2)
    {
        this.msgType = type;
        
        if (msgType == MessageType.SETROOMST)
        {
            this.roomID = info1;
            this.roomState = info2;
        }else if (msgType == MessageType.SETASSAPID)
        {
            this.assignAssaultPartyID = info1;
            this.assaultPState = info2;
        }else{
            GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
    }

    /**
     *   Message instantiation (form 13) - HANDACANVAS
     * 
     *      @param type     message type
     *      @param info1    ordinary thief id
     *      @param info2    assault party thief id
     *      @param info3    ordinary thief state
     *      @param info4    ordinary thief canvas state
     *      @param info5    room id
     *      @param info6    ordinary thief assault party index
     */
    public Message (int type, int info1, int info2, int info3, int info4 , int info5 , int info6){
        
        this.msgType = type;
        this.ordinaryThiefID = info1;
        this.ordinaryTAssaultPID = info2;
        this.ordinaryTState = info3;
        this.ordinaryTCV = info4;
        this.roomID = info5;
        this.ordinaryTAP_index = info6;
    }

    /**
    * Message instantiation (form 14).    -- COLLECTACANVASDONE     
    *
    *     @param type   message type
    *     @param info   collected canvas by master 
    *     @param info2  assault party for reset
    */
    public Message (int type, int info, Boolean[] info2)
    {
        this.msgType = type;
        this.nPaintingsCollected = info;
        this.resetAPArray=info2;
    }

    /**
     *  Getting message type.
     *
     *     @return message type
     */
    public int getMsgType ()
    {
        return (msgType);
    }

    /**
     *  Getting name of logging file.
     *
     *     @return name of the logging file
     */
    public String getLogFName ()
    {
        return (fName);
    }

    /**
     *  Getting the index of the thief in the assault party for general repository
     * 
     *      @return index of the thief in the assault party.
     */
    public int getElementID()
    {
        return (elementID);
    }

    /**
     *   Getting master thief state.
     * 
     *      @return master state
     */
    public int getMasterState()
    {
        return (masterState);
    }

    /**
     *  Getting the number of canvas collected by the master thief
     * 
     *      @return number of canvas collected by the master thief
     */
    public int getnCanvasCollected()
    {
        return (nPaintingsCollected);
    }

    /**
     *   Getting ordinary thief state.
     * 
     *      @return ordinary thief state
     */
    public int getOrdinaryTState()
    {
        return (ordinaryTState);
    }

    /**
     *  Getting ordinary thief identification.
     * 
     *      @return  ordinary thief identification 
     */
    public int getOrdinaryID()
    {
        return (ordinaryThiefID);
    }

    /**
     *  Getting the ordinary Thief Assault party identification.
     * 
     *      @return ordinary Thief Assault party identification.
     */
    public int getOrdinaryAPID()
    {
        return (ordinaryTAssaultPID);
    }

    /**
     *  Getting the ordinary Thief Assault party position.
     *  
     *      @return ordinary Thief Assault party position
     */
    public int getOrdinaryAPPos()
    {
        return (ordinaryTAP_Pos);
    }

    /**
     *  Getting if ordinary thief has a canvas.
     * 
     *      @return 1 if ordinary thief has a canvas.
     */
    public int getOrdinaryTCV()
    {
        return (ordinaryTCV);
    }

    /** 
     *   Getting the index of the thief in the assault party for general repository.
     * 
     *      @return index of the thief in the assault party
     */
    public int getOrdinaryTAP_index()
    {
        return (ordinaryTAP_index);
    }

    /**
     *  Gettig ordinary thief situation. 
     * 
     *      @return ordinary thief situation.
     */
    public String getOrdinarySit()
    {
        return (ordinaryThiefSit);
    }

    /**
     *  Getting max displacement of an ordinary thief.
     * 
     *      @return  max displacement of an ordinary thief.
     */
    public int getMaxDispOT()
    {
        return (maxDispOT);
    }

    /**
     *  Getting assault party identification.
     * 
     *      @return assault party identification.
     */
    public int getAssaultPartyID()
    {
        return (assaultPartyID);
    }

    /**
     *  Getting room identification.
     * 
     *      @return room identification.
     */
    public int getRoomID()
    {
        return (roomID);
    }

    /**
     *  Getting distances of the rooms from outside.
     * 
     *      @return distances of the rooms from outside.
     */
    public int [] getDistFromOutsideArray()
    {
        return (distFromOutsideArray);
    }

    /**
     *  Getting distance from outside of a room.
     * 
     *      @return distance from outside of a room.
     */
    public int getDistFromOutside()
    {
        return (distFromOutside);
    }

    /**
     *  Getting the number of paintings in the museum rooms. 
     * 
     *      @return number of paintings in the museum rooms. 
     */
    public int [] getNPaintingsRoomsArray()    
    {
        return (nPaintingsRooms);
    }

    /**
     *  Getting the number of paintings in a museum Room.
     * 
     *      @return number of paintings in a museum Room.
     */
    public int getNPaintings()
    {
        return (nPaintings);
    }

    /**
     *  Getting master thief transition. 
     * 
     *      @return master transition
     */
    public int getMasterTransition()
    {
        return (transitionMaster);
    }

    /**
     *  Getting am i nedeeded flag (OT)
     * 
     *      @return am i needed flag
     */
    public Boolean getAmINeeded()
    {
        return (amINeeded);
    }

    /**
     *  Getting movement status of the first member of the assault party
     * 
     *      @return movement status of the first member of the assault party
     */
    public Boolean getMoveFirstM()
    {
        return (moveFirstMember);
    }

    /**
     *  Getting assigned assault party identification (Master)
     * 
     *      @return assigned assualt party identification 
     */
    public int getAssignAssaultPId()
    {
        return (assignAssaultPartyID);
    }

    /**
     *  Getting assigned room identification (Master)
     * 
     *      @return assigned room identification
     */
    public int getAssignRoomId()
    {
        return (assignRoomID);
    }

    /**
     *  Getting the state of the room.
     * 
     *      @return True if room empty
     */
    public Boolean getRoomState()
    {
        return (roomState);
    }

    /**
     *  Getting assault party status.
     * 
     *      @return True if is already assigned.
     */
    public Boolean getAssaultPState()
    {
        return (assaultPState);
    }

    /**
     *  Getting assault party built by master
     * 
     *      @return assault party built by master
     */
    public int [] getAssaultPBuild()
    {
        return (assaultPartyBuild);
    }

    /**
     *  Getting the assault party for reset. 
     * 
     *  @return assault party for reset
     */
    public Boolean[] getResetAPArray()
    {
        return (resetAPArray);
    }

    /**
     *  Printing the values of the internal fields.
     *
     *  It is used for debugging purposes.
     *
     *     @return string containing, in separate lines, the pair field name - field value
     */
    @Override
    public String toString ()
    {
        return ("Message type = " + msgType +
                "\nAssault Party ID = " + assaultPartyID + 
                "\nDistances from Outside = " + distFromOutsideArray +
                "\nRoom ID = " + roomID +
                "\nDistance from outside of a Room ID = " + distFromOutside +
                "\nNumber of paintings in the museum rooms = " + nPaintingsRooms +
                "\nNumber of Paintings in a Room = " + nPaintings +
                "\nMaster Thief State = " + masterState +
                "\nMaster Transition = " + transitionMaster +
                "\nAssigned Assault Party id (MT) = " + assignAssaultPartyID +
                "\nAssigned Room id (MT) = " + assignRoomID + 
                "\nAssault Party (MT) = " + assaultPartyBuild +
                "\nOrdinary Thief Id = " + ordinaryThiefID + 
                "\nOrdinary Thief State = " + ordinaryTState +
                "\nMax Displac OT = " + maxDispOT + 
                "\nAm I Needed (OT) = " + amINeeded +  
                "\nName of logging file = " + fName);
    }
}
