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


    /**
     * Creates a {@code ConcurrentLinkedQueue} that is initially empty.
     */
    public OptimizingExecutorJobQ() {
        super();    // just to remind us that we are a Java data structure

        // We are a Java queue capable of concurrent ops, create thread to remove & process elements
        jobThread = new Thread(this, "AcmeWeb:OptimizingExecutorJobQ");
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

                // got a job to run, execute the command
                LOGGER.info("Job Queue {} processing cmd queued {} mSecs ago", jobThread.getName(),
                        System.currentTimeMillis() - job.msTimeEnqueued);
                ExecutableWebCommands cmd = job.command;
                cmd.setCmdState(INPROGRESS);
                cmd.execute();
                cmd.setCmdState(COMPLETED);

                // Let requester know this job is done
                synchronized (job) {
                    job.notifyAll();
                }
                contigSleeps = 0;   // reset contiguous sleep counter, as we did some work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
