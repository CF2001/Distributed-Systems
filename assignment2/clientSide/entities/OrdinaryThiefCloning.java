package clientSide.entities;

/**
 *    Ordinary Thief cloning.
 *
 *      It specifies his own attributes.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */
public interface OrdinaryThiefCloning 
{
    /**
     *  Get Ordinary Thief state.
     * 
     *      @return Ordinary Thief state
     */
    public int getOTState();

    /**
     *  Set Ordinary Thief state.
     * 
     *      @param OTState  New Ordinary Thief state
     */
    public void setOTState(int OTState);

    /**
     *  Get Ordinary Thief id.
     * 
     *      @return Ordinary Thief id
     */
    public int getThiefID();

    /**
     * Set Ordinary Thief id.
     * 
     *      @param thiefID  New Ordinary Thief id
     */
    public void setThiefID(int thiefID);

    /**
     *  Get Ordinary Thief maximum displacement.
     * 
     *      @return Ordinary Thief maximum displacement
     */
    public int getThiefMaxDisp();

    /**
     *  Set Ordinary Thief maximum displacement.
     * 
     *      @param thiefMaxDisp New Ordinary Thief maximum displacement
     */
    public void setThiefMaxDisp(int thiefMaxDisp);

    /**
     *  Get Ordinary Thief assault party id.
     * 
     *      @return Ordinary Thief assault party id
     */
    public int getAParty_ID();

    /**
     *  Set Ordinary Thief assault party id.
     * 
     *      @param AParty_ID New Ordinary Thief assault party id
     */
    public void setAParty_ID(int AParty_ID);

    /**
     *  Get Ordinary Thief assault party position.
     * 
     *      @return Ordinary Thief assault party position
     */
    public int getAParty_position();

    /**
     *  Set Ordinary Thief assault party position.
     * 
     *      @param AParty_position New Ordinary Thief assault party position
     */
    public void setAParty_position(int AParty_position);

    /**
     *  Get Ordinary Thief assault party hasCanvas.
     * 
     *      @return Ordinary Thief assault party hasCanvas
     */
    public int getAParty_hasCanvas();

    /**
     *  Set Ordinary Thief assault party hasCanvas.
     * 
     *      @param AParty_hasCanvas New Ordinary Thief assault party hasCanvas
     */
    public void setAParty_hasCanvas(int AParty_hasCanvas);
}
