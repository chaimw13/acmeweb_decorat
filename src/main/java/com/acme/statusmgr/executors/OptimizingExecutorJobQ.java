package com.acme.statusmgr.executors;

import com.acme.statusmgr.commands.ExecutableWebCommands;
import org.slf4j.*;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ConcurrentLinkedQueue;
import static com.acme.statusmgr.commands.ExecutableWebCommands.CmdState.*;

/**
 * Job queue with execution thread, that attempts to optimize overall throughput by
 * grouping duplicate commands and running just one within some acceptable interval
 */
public class OptimizingExecutorJobQ extends ConcurrentLinkedQueue<EJob> implements Runnable {
    static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Thread jobThread;   // Thread that will execute all items enqueued to this class
    String commandKey;          // Type of command flavor this queue handles


    /**
     * Creates a {@code ConcurrentLinkedQueue} that is initially empty.
     */
    public OptimizingExecutorJobQ(String commandKey) {
        super();    // just to remind us that we are a Java data structure
        this.commandKey = commandKey;

        // We are a Java queue capable of concurrent ops, create thread to remove & process elements
        jobThread = new Thread(this, commandKey);
        LOGGER.info("New Job Queue thread created: {}", jobThread.getName());
        jobThread.start();
    }

    /**
     * Continuously try to take a Job from the queue, and execute it, signalling all waiting
     * threads (the web server requestors) when job is done
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        long contigSleeps = 0;
        EJob lastJob = null;    // keep track of last job run
        try {
            while (!jobThread.isInterrupted()) {
                // See if there is a job to run, else sleep a while and try again
                EJob job = poll();          // get next job if any from queue
                if (job == null) {
                    if (contigSleeps++ == 0) {
                        LOGGER.info("Job Queue {} empty, going to sleep...", jobThread.getName());
                    }
                    Thread.sleep(500);  // no jobs to do right now
                    continue;
                }

                // got a job to run, execute its command
                ExecutableWebCommands cmd = (ExecutableWebCommands) job.command;
                LOGGER.info("Job Queue {} processing cmd {} queued {} mSecs ago", jobThread.getName(),
                        cmd.toString(), System.currentTimeMillis() - job.msTimeEnqueued);

                // if we have a previous job that's not stale, use its result
                if (lastJob != null && lastJob.isRecent())
                {
                    LOGGER.info("Job Queue {} re-using last command result", jobThread.getName());
                    cmd.execute(lastJob.command);   // use last jobs result to quick-execute current one
                }
                else {
                    cmd.execute();
                    lastJob = job;  // keep track of last similar job run, may end up re-using its result
                }

                // Let requester know this job is done
                synchronized (job) {
                    job.notifyAll();
                }
                contigSleeps = 0;   // reset contiguous sleep counter, as we did some work
            }
        } catch (Exception e) {
            LOGGER.error("Job Queue thread caught error", e);
        }
    }
}
