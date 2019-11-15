package com.acme.statusmgr.beans;

/**
 * Defines the basic contract for returning status of any item managed by the Acme server, such
 * as the status of servers, applications, users ...
 */
public interface StatusResponse {

    /**
     * Gets the sequential ID of the request, for tracking purposes. Not usually very important to have a correct value.
     * @return an integer purported to be the sequential number of the request.
     */
    long getId() ;

    /**
     * Get info about the request, such as who made the request
     * @return a String with some appropriate request info
     */
    String getContentHeader();

    /**
     * Get the actual status of the server
     * @return a String with information about the servers status.
     */
    String getStatusDesc();

}
