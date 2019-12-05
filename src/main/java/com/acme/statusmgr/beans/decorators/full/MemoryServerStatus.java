package com.acme.statusmgr.beans.decorators.full;

import com.acme.servermgr.ServerManager;
import com.acme.statusmgr.beans.ServerStatus;

/**
 * A POJO that decorates a Server Status with Memory information.
 */
public class MemoryServerStatus extends ServerStatus {

    /**
     * Reference to the status that is not yet decorated.
     */
    ServerStatus undecoratedStatus;

    /**
     * Construct a Status that we can decorate, based on info from the undecorated status
     * Accumulate the cost from the undecorated object plus our cost.
     * @param undecoratedStatus a Status that we are to decorate
     */
    public MemoryServerStatus(ServerStatus undecoratedStatus) {
        super(undecoratedStatus.getId(), undecoratedStatus.getContentHeader());
        this.undecoratedStatus = undecoratedStatus;
        this.accumulatedCost = undecoratedStatus.getAccumulatedCost() + getDecorationCost();
    }

    /**
     * Decorate our status by adding more information to the undecorated status
     * @return a String that contains previously generated status, with addition of our decoration
     */
    @Override
    public String generateStatusDesc() {
        LOGGER.info("This - {} - is the original StatusDesc before Memory decoration", this.undecoratedStatus.generateStatusDesc());

        return this.undecoratedStatus.generateStatusDesc() +
                ", and its memory is " + ServerManager.getMemoryStatus();
    }

    @Override
    protected int getDecorationCost() {
        return 5;
    }

}