package statusmgr.beans;

import servermgr.ServerManager;

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
    }

    public String getStatusDesc() {
        // just get the basic status info
        String statusStr = ServerManager.getCurrentServerStatus();
        return statusStr;
    }


}