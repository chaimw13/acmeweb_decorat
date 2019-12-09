package com.acme.statusmgr.commands;

import com.acme.statusmgr.beans.decorators.complex.BasicServerStatus;

/**
 * Command for handling Basic ServerStatus (e.g. "Server is up")
 */
public class BasicServerStatusCmd  extends StatusCommand   implements ExecutableWebCommands {

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

    /**
     * Execute the command but use results (statusDesc) from the previous command
     * @param prevCommand a previously/recently executed command of same flavor
     */
    @Override
    public void execute(Object prevCommand) {
        this.cmdState = CmdState.INPROGRESS;
        result = new BasicServerStatus(cmdId, String.format(template, requestorName));

        // use the status desc generated by the previous command
        result.setStatusDesc(((BasicServerStatusCmd)prevCommand).getResult().getStatusDesc() );
        this.cmdState = CmdState.COMPLETED;
    }


    @Override
    public int getEstimatedTime() {
        return 1;
    }


    /**
     * Provide a string representation of this command that is unique across commands
     * @return String that identifies command
     */
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
