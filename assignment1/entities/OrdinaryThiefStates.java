package entities;

/**
 *    Definition of the internal states of the ordinary thief during his life cycle.
 */
public final class OrdinaryThiefStates {
    
    public static final int CONCENTRATION_SITE = 0;

    public static final int CRAWLING_INWARDS   = 1;

    public static final int AT_A_ROOM  = 2;

    public static final int CRAWLING_OUTWARDS  = 3;

    public static final int COLLECTION_SITE    = 4;

    /**
   *   It can not be instantiated.
   */

   private OrdinaryThiefStates ()
   { }
}
