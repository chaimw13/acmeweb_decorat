package com.acme.servermgr;

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

        burnTime(2000);
        return "[Hypervisor, Kubernetes, RAID-6]";
    }

    /**
     * Get info on memory status
     * @return
     */
    public static String getMemoryStatus() {

        burnTime(2000);
        return "Running low";
    }


    /**
     * Burn time for benchmark purposes, consuming some CPU alternating with short sleeps.
     * The 'load' constant is the desired percentage load, assuming only CPU.
     * @param duration
     */
    static private void burnTime(long duration) {
        final double load = 0.8;   // good number for multi-cpu system, lower it for fewer cores.
        long startTime = System.currentTimeMillis();

        try {
            // Loop for the given duration
            while (System.currentTimeMillis() - startTime < duration) {
                // Every 100ms, sleep for the percentage of unladen time
                if (System.currentTimeMillis() % 100 == 0) {
                    Thread.sleep((long) Math.floor((1 - load) * 100), 20);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
