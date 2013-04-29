package pl.rembol.copernicus.commands;

import java.io.Serializable;

import pl.rembol.commandcontrol.Command;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.copernicus.system.AstralSystem;

public class SetScaleSizeCommand extends Command {

    private AstralSystem system;

    public SetScaleSizeCommand(AstralSystem system) {
        this.system = system;
    }

    @Override
    protected Serializable performInvoke(Serializable[] arguments)
            throws CommandException {

        Double scaleSize = Double.parseDouble(arguments[0].toString());

        system.setScaleSize(scaleSize);

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
            Double scaleSize = Double.parseDouble(arguments[0].toString());
            if (scaleSize < 0) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public String usage() {
        return "setScaleSize(double scaleSize > 0)";
    }
}
