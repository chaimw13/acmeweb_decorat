package com.acme.statusmgr.commands;

/**
 * Base class for all Concrete web status commands, maintain state, and basic identifiers
 */
abstract class StatusCommand implements ExecutableWebCommands {
    CmdState cmdState;
    Long cmdId;
    String template;
    String requestorName;

    /**
     * Construct a new status command, mark as non yet started.
     * @param cmdId
     * @param template
     * @param name
     */
    StatusCommand(Long cmdId, String template, String name) {
        this.cmdId = cmdId;
        this.template = template;
        this.requestorName = name;

        this.cmdState = CmdState.NOTSTARTED;
    }


}
