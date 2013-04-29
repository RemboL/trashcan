package pl.rembol.copernicus.commands;

import java.io.Serializable;

import pl.rembol.commandcontrol.Command;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.copernicus.system.AstralSystem;

public class SetScaleDistanceCommand extends Command {

    private AstralSystem system;

    public SetScaleDistanceCommand(AstralSystem system) {
        this.system = system;
    }

    @Override
    protected Serializable performInvoke(Serializable[] arguments)
            throws CommandException {

        Double scaleDistance = Double.parseDouble(arguments[0].toString());

        system.setScaleDistance(scaleDistance);

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
            Double scaleDistance = Double.parseDouble(arguments[0].toString());
            if (scaleDistance < 0) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public String usage() {
        return "setScaleDistance(double scaleDistance > 0)";
    }
}
