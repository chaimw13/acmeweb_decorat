package com.acme.statusmgr.beans.decorators.simple;

import com.acme.servermgr.ServerManager;
import com.acme.statusmgr.beans.ServerStatus;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * A POJO that represents the most basic Server Status and can be used to generate JSON for that status
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT) // Friendly: only include when values are set.
public class BasicServerStatus extends ServerStatus {

    /**
     * Construct a ServerStatus using info passed in about the request
     *
     * @param id            a numeric identifier/counter of which request this is
     * @param contentHeader info about the request
     */
    public BasicServerStatus(long id, String contentHeader) {
        super(id, contentHeader);
        this.accumulatedCost = getDecorationCost(); // set it to our cost, the most basic cost.
    }

    public String getStatusDesc() {
        // just get the basic status info
        String statusStr = ServerManager.getCurrentServerStatus();
        return statusStr;
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
