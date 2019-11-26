package com.acme.statusmgr.beans;

import org.slf4j.*;
import java.lang.invoke.MethodHandles;

/**
 * Declare how a general Server Status return object should behave and what it must keep track of.
 * Items that concrete classes must provide are defined here as abstract methods.
 */
public abstract class ServerStatus implements StatusResponse {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected long id;
    protected String contentHeader;

    public long getAccumulatedCost() {
        return accumulatedCost;
    }

    protected long accumulatedCost = 0;     // Keep track of all costs to prepare this status

    /**
     * Constructor that takes the info already known about the request.
     * @param id
     * @param contentHeader
     */
    public ServerStatus(long id, String contentHeader) {
        this.id = id;
        this.contentHeader = contentHeader;
        LOGGER.info("Constructed a ServerStatus obj for {}", this.contentHeader);
    }


    /**
     * Gets the sequential ID of the request, for tracking purposes. Not usually very important to have a correct value.
     *
     * @return an integer purported to be the sequential number of the request.
     */
    @Override
    public long getId() {

        return this.id;
    }

    @Override
    public String getContentHeader() {

        return this.contentHeader;
    }


    /**
     * Status description to be obtained/modified and returned by the concrete class
     * @return A String describing the status of the server, possibly decorated by multiple concrete classes.
     */
    @Override
    public abstract String getStatusDesc();


    // New items for cost

    /**
     * Each concrete class is asked to determine and provide its own cost with this method.
     * @return a number representing the cost to perform that decoration
     */
    protected abstract int getDecorationCost();

    /**
     * returns the accumulated costs for decoration, only needed for output/return to browser
     * @return
     */
    public long getTotalRequestCost() {
        return this.accumulatedCost;
    }
}
