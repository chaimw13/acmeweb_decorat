package com.acme.statusmgr.executors;

import com.acme.statusmgr.commands.ExecutableWebCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ConcurrentHashMap;

import static com.acme.statusmgr.commands.ExecutableWebCommands.CmdState.COMPLETED;
import static com.acme.statusmgr.commands.ExecutableWebCommands.CmdState.ENQUEUED;


/**
 * Executor with optimization for coalescing duplicate commands to be executed once.
 * Optimization is accomplished by organizing requests into different queues, one
 * for each unique command type that is encountered during execution. A Map keeps track
 * of the queue for each unique command type. A unique command type would be a command like
 * /server/status/detailed combined with a particular list of details requested.
 */
public class OptimizingExecutor implements IExecutor {
    static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // A Map between unique command-types and the Queue that will handle those commands
    ConcurrentHashMap<String, OptimizingExecutorJobQ> jobQueuesMap;

    /**
     * Construct an executor object, with the map to keep track of all queues for all command-types
     */
    public OptimizingExecutor() {
        jobQueuesMap = new ConcurrentHashMap<>();   // allocate map of command-types to queues
    }


    /**
     * Handle duplicates optimally, by returning same results for duplicate commands issued in close proximity.
     * Run a separate thread for each command pattern encountered.
     *
     * @param command to be run
     */
    public void handleCommand(ExecutableWebCommands command) {
        // get the appropriate job queue for this command pattern, will create one if not already created.
        OptimizingExecutorJobQ thisCmdJobQ = getCmdJobQ(command);

        // enqueue a new job onto this command's job queue
        EJob job = new EJob(command);
        command.setCmdState(ENQUEUED);
        thisCmdJobQ.add(job);   // once on this queue, it is subject to be ran by the queue's thread

        // we are running on thread of web client, sleep until our cmd has been run by queue's thread
        synchronized (job) {
            try {
                LOGGER.info("handleCommand: Waiting to be notified ");
                job.wait();
                LOGGER.info("handleCommand: Successfully notified!");
            } catch (InterruptedException ex) {
                LOGGER.warn("handleCommand: An InterruptedException was caught: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        // must have returned, make sure our command completed properly
        if (command.getCmdState() != COMPLETED) {
            LOGGER.error("handleCommand: Problem with execution, cmd {} has con-completed state {}",
                    command.toString(), command.getCmdState());
        }
    }


    /**
     * Get appropriate job queue for this command pattern, create one if not already there.
     *
     * @param command the command to find or create an appropriate queue for
     */
    private OptimizingExecutorJobQ getCmdJobQ(ExecutableWebCommands command) {

        // every command-type has a unique string value used as Key for the queue map
        // get the queue for this exact command-type, if it exists already
        OptimizingExecutorJobQ thisCmdJobQ = jobQueuesMap.get(command.toString());

        if (thisCmdJobQ == null) {
            // Make new queue for this command-type, add to the map of cmd-types to queues
            thisCmdJobQ = new OptimizingExecutorJobQ();
            // add it to queue map
            jobQueuesMap.put(command.toString(), thisCmdJobQ);
            LOGGER.info("Created new queue for cmd-type {}", command.toString());
        }

        return thisCmdJobQ;
    }
}
