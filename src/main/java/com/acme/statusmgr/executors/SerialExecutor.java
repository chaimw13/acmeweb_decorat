package com.acme.statusmgr.executors;

import com.acme.statusmgr.commands.ExecutableWebCommands;
import org.slf4j.*;

import java.lang.invoke.MethodHandles;

/**
 * Serial command executor, will execute commands single file, but could have optimizations for order, etc.
 */
public class SerialExecutor implements IExecutor{
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Construct an executor
     */
    public SerialExecutor() {
    }

    /**
     * Handle the command immediately, e.g. with no optimizations
     * @param command command to be executed
     */
    public void handleCommand(ExecutableWebCommands command)
    {
        if (command.getEstimatedTime() > 120) {
            LOGGER.warn("Requested to process long {} sec cmd synchronously", command.getEstimatedTime());
        }

        command.execute();  // do whatever the command know how to do
    }



}
