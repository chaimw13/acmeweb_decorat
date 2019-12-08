package com.acme.statusmgr.executors;

import com.acme.statusmgr.commands.ExecutableWebCommands;

public interface IExecutor {
    public void handleCommand(ExecutableWebCommands command);
}
