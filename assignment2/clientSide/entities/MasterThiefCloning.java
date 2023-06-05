package clientSide.entities;

/**
 *    Master Thief cloning.
 *
 *      It specifies his own attributes.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */
public interface MasterThiefCloning {
    /**
     *  Get Master Thief state.
     *
     *      @return Master Thief state
     */
    public int getMTState();

    /**
     *   Set Master Thief state.
     *
     *      @param MTState New Master Thief state
     */
    public void setMTState(int MTState);

    /**
     *  Get collected canvas    
     * 
     *      @return  number of collected canvas
     */
    public int getCollectedCanvas();

    /**
     *  Set collected canvas.
     *    
     *      @param collectedCanvas  collected canvas
     */
    public void setCollectedCanvas(int collectedCanvas);
}