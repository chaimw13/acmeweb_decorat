package com.acme.statusmgr.commands;

/**
 * Interface for all executable command, the Invoker(Executor) will call this methods.
 */
public interface ExecutableWebCommands {

    enum CmdState {NOTSTARTED, INPROGRESS, COMPLETED, ERROR}

    void execute();

    int getEstimatedTime();

}
