package com.acme.statusmgr.commands;

/**
 * Interface for all executable command, the Invoker(Executor) will call this methods.
 */
public interface ExecutableWebCommands <prevCmdType>{

    // define the different states of a command
    enum CmdState {NOTSTARTED, ENQUEUED, INPROGRESS, COMPLETED, ERROR}

    // execute the command
    void execute();

    // execute the command, merging with status description available in the passed in command
    void execute(prevCmdType command);

    // return a metric indicating relative cost of command
    int getEstimatedTime();

    // return the state of the command
    CmdState getCmdState();

    // set the state of the command
    void setCmdState(CmdState cmdState);

}
