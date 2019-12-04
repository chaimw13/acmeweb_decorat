package com.acme.statusmgr;

import com.acme.diskmgr.DiskManager;
import com.acme.statusmgr.beans.*;

import com.acme.statusmgr.beans.decorators.full.BasicServerStatus;
import com.acme.statusmgr.commands.*;
import com.acme.statusmgr.executors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Controller for all web/REST requests about the status of servers
 * <p>
 * For initial school project - just handles info about this server
 * Syntax for URLS:
 * All start with /server
 * /status  will give back status of server
 * a param of 'name' specifies a requestor name to appear in response
 * <p>
 * Examples:
 * http://localhost:8080/server/status
 * <p>
 * http://localhost:8080/server/status?name=Noach
 */

@RestController
@RequestMapping("/server")
public class StatusController {

    protected static final String template = "Server Status requested by %s";
    protected final AtomicLong counter = new AtomicLong();

    /**
     * Accept injection of what factory to use when creating decorators for details
     */
    @Autowired
    DecoratorStyle decoratorStyle;

    @RequestMapping("/status")
    public BasicServerStatus getStatus(@RequestParam(value = "name", defaultValue = "Anonymous") String name) {
        BasicServerStatusCmd cmd = new BasicServerStatusCmd(counter.incrementAndGet(), template, name);
        SerialExecutor exc = new SerialExecutor(cmd);
        exc.handleImmidiatly();
        return cmd.getResult();
    }

    /**
     * Handle request for detailed status, with return data based on the types of "details" requested
     * @param name          optional URL param with default for name of executor
     * @param detailTypes   mandatory list of one or more types of detail desired
     * @return
     */
    @RequestMapping(value = "/status/detailed", produces = {"application/json"})
    public StatusResponse getDetailedStatus(@RequestParam(value = "name", defaultValue = "Anonymous") String name,
                                            @RequestParam(value = "details") List<String> detailTypes) {

        DetailedServerStatusCmd cmd = new DetailedServerStatusCmd(counter.incrementAndGet(),
                template, name, detailTypes, decoratorStyle);
        SerialExecutor exc = new SerialExecutor(cmd);
        exc.handleImmidiatly();
        return cmd.getResult();
    }


    /**
     * Handle request for disk status
     * @param name          optional URL param with default for name of executor
     * @return a DiskStatus object with info about disk that will be converted to JSON
     */
    @RequestMapping(value = "/disk/status", produces = {"application/json"})
    public DiskStatus getDiskStatus(@RequestParam(value = "name", defaultValue = "Anonymous") String name) {

        // Start off with creating a basic status object by calling the usual creator of that
        DiskStatus dStatus = new DiskStatus (counter.incrementAndGet(), String.format(template, name));

        // Create a DiskManager object, and ask it to run the disk status command, get its output
        DiskManager dMgr = new DiskManager();
        dStatus.setDiskCommand(dMgr.getDiskCommand());
        dStatus.setDiskCommandOutput(dMgr.getDiskCommandOutput());

        return  dStatus;   // return the disk status object, which will be converted by Spring, e.g. into JSON
    }


}
