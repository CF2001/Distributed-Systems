package entities;

/**
 *    Definition of the internal states of the master thief during his life cycle.
 */
public final class MasterThiefStates {
    
    public static final int PLANNING_THE_HEIST  = 0;

    public static final int DECIDING_WHAT_TO_DO = 1;

    public static final int ASSEMBLING_A_GROUP  = 2;

    public static final int WAITING_FOR_ARRIVAL = 3;

    public static final int PRESENTING_THE_REPORT = 4;

    /**
   *   It can not be instantiated.
   */

   private MasterThiefStates ()
   { }
}
