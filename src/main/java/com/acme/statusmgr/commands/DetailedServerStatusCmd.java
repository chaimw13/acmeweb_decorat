package com.acme.statusmgr.commands;

import com.acme.statusmgr.beans.DecoratorStyle;
import com.acme.statusmgr.beans.ServerStatus;
import com.acme.statusmgr.beans.decorators.full.BasicServerStatus;

import java.util.List;

/**
 * Command for handling Detailed ServerStatus (e.g. "Server is up, and is Running low on memory")
 */
public class DetailedServerStatusCmd extends StatusCommand implements ExecutableWebCommands {
    private ServerStatus result = null; // keep track of the commands result
    List<String> detailTypes;
    DecoratorStyle decoratorStyle;

    /**
     * Construct a Detailed Status Command, with all needed inputs
     *
     * @param cmdId          numeric unique request ID
     * @param template       template to use for creating contentHeader
     * @param name           name of requestor
     * @param detailTypes    types of details requested, e.g. memory
     * @param decoratorStyle The desired decorator style to use (determines decorator factory)
     */
    public DetailedServerStatusCmd(Long cmdId, String template, String name, List<String> detailTypes, DecoratorStyle decoratorStyle) {
        super(cmdId, template, name);
        this.detailTypes = detailTypes;
        this.decoratorStyle = decoratorStyle;
    }


    /**
     * Execute the command by constructing a Detailed/decorated ServerStatus object
     */
    @Override
    public void execute() {
        this.cmdState = CmdState.INPROGRESS;

        // Start off with creating a basic status object by calling the usual creator of that
        result = new BasicServerStatus(this.cmdId, String.format(this.template, this.requestorName));

        /**
         * Enhance the status based on the requested details, by successively decorating it with additional classes
         */
        for (String detailtype : detailTypes) {
            result = decoratorStyle.createDecorator(detailtype, result);
        }

        result.setStatusDesc(result.generateStatusDesc());
        this.cmdState = CmdState.COMPLETED;
    }


    @Override
    public int getEstimatedTime() {
        return 1;
    }

    public ServerStatus getResult() {
        return result;
    }
}
