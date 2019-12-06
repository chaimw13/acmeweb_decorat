package com.acme.statusmgr.beans.decorators.complex;

import com.acme.servermgr.ServerManager;
import com.acme.statusmgr.beans.ServerStatus;

/**
 * A POJO that represents the most basic Server Status and can be used to generate JSON for that status
 */
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
        return 1;
    }


}
