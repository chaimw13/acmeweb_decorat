package statusmgr;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import statusmgr.beans.*;

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
                                            @RequestParam(value = "details") List<String> detailTypes) {

        // Start off with creating a basic status object by calling the usual creator of that
        ServerStatus sStatus = getStatus(name);

        /**
         * Enhance the status based on the requested details, by successively decorating it with additional classes
         */
        for (String detailtype : detailTypes) {
            switch (detailtype.toLowerCase()) {
                case "operations":
                    sStatus = new OperationsServerStatus(sStatus);
                    break;
                case "extensions":
                    sStatus = new ExtensionsServerStatus(sStatus);
                    break;
                case "memory":
                    sStatus = new MemoryServerStatus(sStatus);
                    break;
                default:
                    throw new BadRequestException("Invalid details option: " + detailtype);
            }
        }

        return  sStatus;   // return the most recently created decorated status
    }

}
