package com.acme.statusmgr.executors;

import com.acme.statusmgr.commands.ExecutableWebCommands;

/**
 * Executor Job object represents a running instance of a command on the appropriate
 * queue, and is used for wait/notify to coordinate threads.
 */
public class EJob {
    ExecutableWebCommands command;      // The command to be ran
    long msTimeEnqueued;                // time it was added to queue to be run

    /**
     * Construct a job representing a command to be run on an appropriate thread.
     * @param command
     */
    public EJob(ExecutableWebCommands command) {
        this.command = command;
        msTimeEnqueued = System.currentTimeMillis();
    }
}
