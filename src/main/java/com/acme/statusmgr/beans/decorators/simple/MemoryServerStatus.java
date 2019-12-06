package com.acme.statusmgr.beans.decorators.simple;

import com.acme.servermgr.ServerManager;
import com.acme.statusmgr.beans.ServerStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A POJO that decorates a Server Status with Memory information.
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT) // Friendly: only include when values are set.
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
        return 0;   // Friendly version return empty value that will not get JSON'ed
    }

    /**
     * Gets the sequential ID of the request, for tracking purposes. Not usually very important to have a correct value.
     *
     * @return an integer purported to be the sequential number of the request.
     */
    @Override
    public long getId() {
        return 0;    // Friendly version return empty value that will not get JSON'ed
    }

    @Override
    public String getContentHeader() {
        return null;  // Friendly version return empty value that will not get JSON'ed
    }

    @Override
    public long getAccumulatedCost() {
        return 0;
    }

    /**
     * returns the accumulated costs for decoration, only needed for output/return to browser
     *
     * @return
     */
    @Override
    public long getTotalRequestCost() {
        return 0;
    }
}