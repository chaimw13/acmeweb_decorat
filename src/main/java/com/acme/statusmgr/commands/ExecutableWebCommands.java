package com.acme.statusmgr.commands;

/**
 * Interface for all executable command, the Invoker(Executor) will call this methods.
 */
public interface ExecutableWebCommands {

    // define the different states of a command
    enum CmdState {NOTSTARTED, ENQUEUED, INPROGRESS, COMPLETED, ERROR}

    // execute the command
    void execute();

    // return a metric indicating relative cost of command
    int getEstimatedTime();

    // return the state of the command
    CmdState getCmdState();

    // set the state of the command
    void setCmdState(CmdState cmdState);

}
