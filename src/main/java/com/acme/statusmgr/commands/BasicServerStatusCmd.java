package com.acme.statusmgr.commands;

import com.acme.statusmgr.beans.decorators.complex.BasicServerStatus;

/**
 * Command for handling Basic ServerStatus (e.g. "Server is up")
 */
public class BasicServerStatusCmd  extends StatusCommand   implements ExecutableWebCommands {
    private BasicServerStatus result = null;

    public BasicServerStatusCmd(Long cmdId, String template, String name) {
        super(cmdId, template, name);
    }


    /**
     * Execute the command by constructing a BasicServerStatus object
     */
    @Override
    public void execute() {
        this.cmdState = CmdState.INPROGRESS;
        result = new BasicServerStatus(cmdId, String.format(template, requestorName));
        result.setStatusDesc(result.generateStatusDesc());
        this.cmdState = CmdState.COMPLETED;
    }



    @Override
    public int getEstimatedTime() {
        return 1;
    }

    public BasicServerStatus getResult() {
        return result;
    }

    /**
     * Provide a string representation of this command that is unique across commands
     * @return String that identifies command
     */
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
