package statusmgr.beans.decorators;

import org.springframework.stereotype.Service;
import statusmgr.BadRequestException;
import statusmgr.beans.DecoratorStyle;
import statusmgr.beans.ServerStatus;

/**
 * A factory for creating decorators that provide the full amount of available status information,
 * without removing any internal fields and without doing any localization of content.
 */
@Service
public class FullDecoratorFactory implements DecoratorStyle {
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
