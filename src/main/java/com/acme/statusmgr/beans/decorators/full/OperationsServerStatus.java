package com.acme.statusmgr.beans.decorators.full;

import com.acme.servermgr.ServerManager;
import com.acme.statusmgr.beans.ServerStatus;
import com.acme.statusmgr.beans.StatusResponse;

/**
 * A POJO that decorates a Server Status with Operational status information.
 */
public class OperationsServerStatus extends ServerStatus {

    /**
     * Reference to the status that is not yet decorated.
     */
    StatusResponse undecoratedStatus;

    /**
     * Construct a Status that we can decorate, based on info from the undecorated status
     * Accumulate the cost from the undecorated object plus our cost.
     * @param undecoratedStatus a Status that we are to decorate
     */
    public OperationsServerStatus(ServerStatus undecoratedStatus) {
        super(undecoratedStatus.getId(), undecoratedStatus.getContentHeader());
        this.undecoratedStatus = undecoratedStatus;
        this.accumulatedCost = undecoratedStatus.getAccumulatedCost() + getDecorationCost();
    }

    /**
     * Decorate our status by adding more information to the undecorated status
     * @return a String that contains previously generated status, with addition of our decoration
     */
    @Override
    public String getStatusDesc() {
        return this.undecoratedStatus.getStatusDesc() +
                ", and is operating " +
                (ServerManager.isOperatingNormally() ? "normally" : " abnormally");
    }

    @Override
    protected int getDecorationCost() {
        return 7;
    }

}