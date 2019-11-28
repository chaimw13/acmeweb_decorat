package com.acme.diskmgr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Manage all disk drives being tracked by the Acme server tracking system.
 * For now just some simple  methods for use in school project
 */
public class DiskManager {
    // Disk command to be used to check disk status
    // This example is for windows- chkdsk on the C drive. For unix use {"du", "-c", "d1", "/"}
//    private final static String[] diskCommand = new String[]{"chkdsk", "c:"};
    private final static String[] diskCommand = new String[]{"cmd", "/C", "Dir", "/S", "C:*.java"};

    /**
     * Get the disk command that is being used to check disk status
     *
     * @return a String representation of the command
     */
    public String getDiskCommand() {
        return String.join(" ", diskCommand);
    }

    /**
     * Execute the disk command, wait for completion  and get its output
     *
     * @return The output of the disk status command, as a possibly long String
     */
    public String getDiskCommandOutput() {
        String result = "No Result Obtained "; // init based on default error
        Runtime rt = Runtime.getRuntime();

        try {
            // execute desired command, the command and its params must be in a string array
            Process chkProcess = rt.exec(diskCommand);
            // read output from the command, and collect into a string
          //  result = new BufferedReader(new InputStreamReader(chkProcess.getInputStream()))
            //        .lines().collect(Collectors.joining("\n"));

            BufferedReader rdr = new BufferedReader(new InputStreamReader(chkProcess.getInputStream()));
            for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
                System.out.println(line);
                result = result + line;
            }


        } catch (IOException e) {
            e.printStackTrace();
            result = result.concat(e.getLocalizedMessage());
        }

        return result;
    }
}