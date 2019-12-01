package com.acme.statusmgr.executors;

import com.acme.statusmgr.commands.ExecutableWebCommands;
import org.slf4j.*;

import java.lang.invoke.MethodHandles;

/**
 * Serial command executor, will execute commands single file, but could have optimizations for order, etc.
 */
public class SerialExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * The command to execute, TODO could be a list.
     */
    private ExecutableWebCommands command;

    /**
     * Construct an executor, initialize with the command to be executed.
     * @param command command to be executed
     */
    public SerialExecutor(ExecutableWebCommands command) {
        this.command = command;
    }

    /**
     * Execute the command immediately, e.g. with no optimizations
     */
    public void handleImmidiatly()
    {
        if (command.getEstimatedTime() > 120) {
            LOGGER.warn("Requested to process long {} sec cmd synchronously", command.getEstimatedTime());
        }

        command.execute();  // do whatever the command know how to do
    }



}
