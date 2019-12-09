package com.acme.statusmgr.commands;

import com.acme.statusmgr.beans.ServerStatus;

/**
 * Base class for all Concrete web status commands, maintain state, and basic identifiers
 */
abstract class StatusCommand implements ExecutableWebCommands {
    CmdState cmdState;
    Long cmdId;
    String template;
    String requestorName;


    ServerStatus result = null; // keep track of the commands result

    /**
     * Construct a new status command, mark as non yet started.
     * @param cmdId
     * @param template
     * @param name
     */
    StatusCommand(Long cmdId, String template, String name) {
        this.cmdId = cmdId;
        this.template = template;
        this.requestorName = name;

        this.cmdState = CmdState.NOTSTARTED;
    }

    /**
     * get the execution state of command
     * @return CmdState
     */
    public CmdState getCmdState() {
        return cmdState;
    }

    /**
     * set the state of the command
     * @param cmdState
     */
    public void setCmdState(CmdState cmdState) {
        this.cmdState = cmdState;
    }

    /**
     * Require all concrete classes to provide a string representation that fully
     * and uniquely identified the command so it can be grouped with same commands
     * @return a String that uniquely identifies command and its relevant parameters
     */
    abstract public String toString();

    public ServerStatus getResult() {
        return result;
    }

}
