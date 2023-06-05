package interfaces;

import java.io.Serializable;

/**
 *  Data type to return both an integer state value and an integer array.
 *
 *  Used in calls on remote objects.
 */
public class ReturnArrayInt implements Serializable
{
    /**
     *  Serialization key.
     */
    public static final long serialVersionUID = 2021L;

    /**
     *  Assault party with 3 thieves
     */
    private int [] arrayInt;

    /**
     *  Integer state value.
     */
    private int state;

    /**
     *  ReturnArrayInt instantiation.
     *
     *     @param arrayInt  assault party with 3 thieves
     *     @param state      integer state value
     */
    public ReturnArrayInt (int [] arrayInt, int state)
    {
        this.arrayInt = arrayInt;
        this.state = state;
    }

    /**
     *  Getting assault party with 3 thieves
     *
     *     @return assault party with 3 thieves
     */
    public int [] getArrayInt ()
    {
        return (arrayInt);
    }

    /**
     *  Getting integer state value.
     *
     *     @return integer state value
     */
    public int getIntStateVal ()
    {
        return (state);
    }
}
