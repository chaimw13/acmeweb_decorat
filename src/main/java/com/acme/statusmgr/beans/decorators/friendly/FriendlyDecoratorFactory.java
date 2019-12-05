package com.acme.statusmgr.beans.decorators.friendly;

import com.acme.statusmgr.BadRequestException;
import com.acme.statusmgr.beans.DecoratorStyle;
import com.acme.statusmgr.beans.ServerStatus;
import org.springframework.stereotype.Service;

/**
 * A factory for creating decorators that provide the full amount of available status information,
 * without removing any internal fields and without doing any localization of content.
 */
@Service
public class FriendlyDecoratorFactory implements DecoratorStyle {
    @Override
    public ServerStatus createDecorator(String detailtype, ServerStatus undecoratedStatus) {
        ServerStatus newDecorator;
        switch (detailtype.toLowerCase()) {
            case "operations":
                newDecorator = new OperationsServerStatus(undecoratedStatus);
                break;
            case "extensions":
                newDecorator = new ExtensionsServerStatus(undecoratedStatus);
                break;
            case "memory":
                newDecorator = new MemoryServerStatus(undecoratedStatus);
                break;
            default:
                throw new BadRequestException("Invalid details option: " + detailtype);
        }

        return newDecorator;
    }
}
