package pl.rembol.copernicus.commands;

import java.io.Serializable;

import pl.rembol.commandcontrol.Command;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.copernicus.system.AstralSystem;

public class SetScaleTimeCommand extends Command {

    private AstralSystem system;

    public SetScaleTimeCommand(AstralSystem system) {
        this.system = system;
    }

    @Override
    protected Serializable performInvoke(Serializable[] arguments)
            throws CommandException {

        Long scaleTime = Long.parseLong(arguments[0].toString());

        system.setScaleTime(scaleTime);

        return null;
    }

    @Override
    protected boolean validateArguments(Serializable[] arguments) {

        if (arguments == null) {
            return false;
        }

        if (arguments.length != 1) {
            return false;
        }

        if (arguments[0] == null) {
            return false;
        }

        try {
            Long scaleTime = Long.parseLong(arguments[0].toString());
            if (scaleTime < 0) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public String usage() {
        return "setScaleTime(long scaleTime > 0)";
    }
}
