package interfaces;

import java.io.Serializable;

/**
 *  Data type to return both an transition or OT assault party id value and an integer state value.
 *
 *  Used in calls on remote objects.
 */
public class ReturnInt implements Serializable
{
   /**
   *  Serialization key.
   */
   public static final long serialVersionUID = 2021L;

   /**
   *  Integer transition or OT assault party id value.
   */
   private int val;

   /**
   *  Integer state value.
   */
   private int state;

   /**
   *  ReturnInt instantiation 1.
   *
   *     @param val integer transition value or OT assault party id
   *     @param state      integer state value
   */
   public ReturnInt (int val, int state)
   {
      this.val = val;
      this.state = state;
   }

   /**
   *  Getting integer transition value or OT assault party id
   *
   *     @return integer transition value or OT assault party id
   */
   public int getIntVal ()
   {
      return (val);
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
