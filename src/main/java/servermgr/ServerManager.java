package servermgr;

/**
 * Manage all servers (service providers) being tracked by the Acme server tracking system
 * For now just some simple static methods for use in school project
 */
public class ServerManager {

    /**
     * Get the status of this server
     * @return a descriptive string about the servers status
     */
    static public String getCurrentServerStatus() {
        return "Server is up";
    }

    /**
     * Find out if this server is operating normally
     * @return Boolean indicating if server is operating normally
     */
    static public Boolean isOperatingNormally()
    {
        return true;
    }

    /**
     * Get a list of all extensions being used by the server
     * @return
     */
    public static String getExtensions() {
        return "[Hypervisor, Kubernetes, RAID-6]";
    }

    /**
     * Get info on memory status
     * @return
     */
    public static String getMemoryStatus() {
        return "Running low";
    }
}
