package pl.rembol.copernicus.commands;

import java.io.Serializable;

import pl.rembol.commandcontrol.Command;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.copernicus.system.AstralSystem;

public class SetRadiusCommand extends Command {

    private AstralSystem system;

    public SetRadiusCommand(AstralSystem system) {
        this.system = system;
    }

    @Override
    protected Serializable performInvoke(Serializable[] arguments)
            throws CommandException {

        String name = arguments[0].toString();
        Double radius = Double.parseDouble(arguments[1].toString());

        system.setRadius(name, radius);

        return null;
    }

    @Override
    protected boolean validateArguments(Serializable[] arguments) {

        if (arguments == null) {
            return false;
        }

        if (arguments.length != 2) {
            return false;
        }

        if (arguments[0] == null) {
            return false;
        }

        if ("".equals(arguments[0].toString())) {
            return false;
        }

        if (arguments[1] == null) {
            return false;
        }
        try {
            Double radius = Double.parseDouble(arguments[1].toString());
            if (radius < 0) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public String usage() {
        return "setRadius(String name, Double radius > 0)";
    }
}
