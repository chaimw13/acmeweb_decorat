package com.acme.statusmgr.beans.decorators.full;

import com.acme.servermgr.ServerManager;
import com.acme.statusmgr.beans.ServerStatus;

/**
 * A POJO that decorates a Server Status with Extension information.
 */
public class ExtensionsServerStatus extends ServerStatus {

    /**
     * Reference to the status that is not yet decorated.
     */
    ServerStatus undecoratedStatus;

    /**
     * Construct a Status that we can decorate, based on info from the undecorated status.
     * Accumulate the cost from the undecorated object plus our cost.
     * @param undecoratedStatus a Status that we are to decorate
     */
    public ExtensionsServerStatus(ServerStatus undecoratedStatus) {
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
            return this.undecoratedStatus.generateStatusDesc() +
                    ", and is using these extensions - " + ServerManager.getExtensions();

    }

    @Override
    protected int getDecorationCost() {
        return 12;
    }

}