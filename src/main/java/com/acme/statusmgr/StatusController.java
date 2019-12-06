package com.acme.statusmgr;

import com.acme.diskmgr.DiskManager;
import com.acme.statusmgr.beans.DecoratorStyle;
import com.acme.statusmgr.beans.DiskStatus;
import com.acme.statusmgr.beans.ServerStatus;
import com.acme.statusmgr.beans.StatusResponse;
import com.acme.statusmgr.beans.decorators.complex.BasicServerStatus;
import com.acme.statusmgr.beans.decorators.complex.ComplexDecoratorFactory;
import com.acme.statusmgr.beans.decorators.simple.SimpleDecoratorFactory;
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
    DecoratorStyle defaultDecoratorStyle;

    @RequestMapping("/status")
    public BasicServerStatus getStatus(@RequestParam(value = "name", defaultValue = "Anonymous") String name) {
        return new BasicServerStatus(counter.incrementAndGet(),
                String.format(template, name));
    }

    /**
     * Handle request for detailed status, with return data based on the types of "details" requested
     * @param name          optional URL param with default for name of executor
     * @param detailTypes   mandatory list of one or more types of detail desired
     * @return
     */
    @RequestMapping(value = "/status/detailed", produces = {"application/json"})
    public StatusResponse getDetailedStatus(@RequestParam(value = "name", defaultValue = "Anonymous") String name,
                                            @RequestParam(value = "details") List<String> detailTypes,
                                            @RequestParam(value = "levelofdetail", defaultValue = "&DEFAULT") String detailLevel) {

        // Start off with creating a basic status object by calling the usual creator of that
        ServerStatus sStatus = getStatus(name);

        if (detailLevel.equalsIgnoreCase("simple"))
            defaultDecoratorStyle = new SimpleDecoratorFactory();


        DecoratorStyle decoratorStyle =
                detailLevel.equalsIgnoreCase("simple") ? new SimpleDecoratorFactory() :
                        detailLevel.equalsIgnoreCase("complex") ? new ComplexDecoratorFactory() :
                                defaultDecoratorStyle;


        /**
         * Enhance the status based on the requested details, by successively decorating it with additional classes
         */
        for (String detailtype : detailTypes) {
            sStatus = decoratorStyle.createDecorator(detailtype, sStatus);
        }

        return  sStatus;   // return the most recently created decorated status
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
