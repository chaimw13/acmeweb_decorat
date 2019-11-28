package com.acme.statusmgr.beans;


/**
 * Represent the status of a disk
 */
public class DiskStatus {

    private final long id;
    private final String contentHeader;
    private String diskCommand;         // The command that was used to evaluate disk status
    private String diskCommandOutput;   // the output of executing 'diskCommand'

    /**
     * Constructor that takes the info already known about the request.
     *
     * @param id numeric id of request
     * @param contentHeader info about request
     */
    public DiskStatus(long id, String contentHeader) {
        this.id = id;
        this.contentHeader = contentHeader;
    }


    /**
     * set the disk command used
     *
     * @param diskCommand String containi8ng the command and paramaters used for checking disk status
     */
    public void setDiskCommand(String diskCommand) {
        this.diskCommand = diskCommand;
    }

    /**
     * set the output of 'diskCommand' execution
     *
     * @param diskCommandOutput - a possibly long String containing output of the 'diskCommand'
     */
    public void setDiskCommandOutput(String diskCommandOutput) {
        this.diskCommandOutput = diskCommandOutput;
    }


    public long getId() {
        return id;
    }

    public String getContentHeader() {
        return contentHeader;
    }

    public String getDiskCommand() {
        return diskCommand;
    }

    public String getDiskCommandOutput() {
        return diskCommandOutput;
    }
}
