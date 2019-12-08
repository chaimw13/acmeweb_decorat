package com.acme.statusmgr.executors;

import com.acme.statusmgr.commands.ExecutableWebCommands;

/**
 * Common interface for all executors to follow
 */
public interface IExecutor {

    /**
     * Tell executor to handle command however that executor is best able to.
     * @param command
     */
    public void handleCommand(ExecutableWebCommands command);
}
