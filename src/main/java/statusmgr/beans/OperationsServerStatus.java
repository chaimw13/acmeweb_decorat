package statusmgr.beans;

import servermgr.ServerManager;

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
     * @param undecoratedStatus a Status that we are to decorate
     */
    public OperationsServerStatus(ServerStatus undecoratedStatus) {
        super(undecoratedStatus.getId(), undecoratedStatus.getContentHeader());
        this.undecoratedStatus = undecoratedStatus;
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

}