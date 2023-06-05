package interfaces;

import java.io.Serializable;

/**
 *  Data type to return both an integer value and an integer array.
 *
 *  Used in calls on remote objects.
 */
public class ReturnArrayBoolean implements Serializable
{
    /**
     *  Serialization key.
     */
    public static final long serialVersionUID = 2021L;

    /**
     *  assault parties for reseting
     */
    private Boolean [] arrayBoolean;

    /**
     *  Integer state value.
     */
    private int val;

    /**
     *  ReturnArrayBoolean instantiation.
     *
     *     @param arrayBoolean  assault party with 3 thieves
     *     @param val      integer value
     */
    public ReturnArrayBoolean (Boolean [] arrayBoolean, int val)
    {
        this.arrayBoolean = arrayBoolean;
        this.val = val;
    }

    /**
     *  Getting assault party with 3 thieves
     *
     *     @return assault party with 3 thieves
     */
    public Boolean [] getArrayBoolean ()
    {
        return (arrayBoolean);
    }

    /**
     *  Getting integer state value.
     *
     *     @return integer state value
     */
    public int getIntVal ()
    {
        return (val);
    }
}