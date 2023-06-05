package clientSide.entities;

/**
 *    Definition of the internal states of the master thief during his life cycle.
 */
public final class MasterThiefStates {
    
    /**
     *  Master Thief is planning the heist.
     */
    public static final int PLANNING_THE_HEIST  = 0;

    /**
     *  Master Thief is deciding what to do.
     */
    public static final int DECIDING_WHAT_TO_DO = 1;

    /**
     *  Master Thief is assembling a group
     */
    public static final int ASSEMBLING_A_GROUP  = 2;

    /**
     *  Master Thief is waiting for the assault parties
     */
    public static final int WAITING_FOR_ARRIVAL = 3;

    /**
     *  Master Thief is reporting the report.
     */
    public static final int PRESENTING_THE_REPORT = 4;

    /**
    *   It can not be instantiated.
    */
   private MasterThiefStates ()
   { }
}
