package clientSide.entities;

/**
 *    Definition of the internal states of the ordinary thief during his life cycle.
 */
public final class OrdinaryThiefStates {
    
    /**
     *  The Ordinary thieves are gathering at the concentration site.
     */
    public static final int CONCENTRATION_SITE = 0;

    /**
     *  The Ordinary thieves are moving into the museum room.
     */
    public static final int CRAWLING_INWARDS   = 1;

    /**
     *  The Ordinary thieves are moving into the museum room.
     */
    public static final int AT_A_ROOM  = 2;

    /**
     *  The Ordinary Thieves are stealing the paintings from the museum room.
     */
    public static final int CRAWLING_OUTWARDS  = 3;

    /**
     *  The Ordinary thieves are moving to the painting collection site.
     */
    public static final int COLLECTION_SITE    = 4;


    /**
     *   It can not be instantiated.
     */
    private OrdinaryThiefStates ()
    { }
}