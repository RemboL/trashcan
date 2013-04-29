package pl.rembol.copernicus.commands;

import java.io.Serializable;

import pl.rembol.commandcontrol.Command;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.copernicus.system.AstralSystem;

public class SetVelocityCommand extends Command {

    private AstralSystem system;

    public SetVelocityCommand(AstralSystem system) {
        this.system = system;
    }

    @Override
    protected Serializable performInvoke(Serializable[] arguments)
            throws CommandException {

        String name = arguments[0].toString();
        Double x = Double.parseDouble(arguments[1].toString());
        Double y = Double.parseDouble(arguments[2].toString());
        Double z = Double.parseDouble(arguments[3].toString());

        system.setVelocity(name, x, y, z);

        return null;
    }

    @Override
    protected boolean validateArguments(Serializable[] arguments) {

        if (arguments == null) {
            return false;
        }

        if (arguments.length != 4) {
            return false;
        }

        if (arguments[0] == null) {
            return false;
        }

        if ("".equals(arguments[0].toString())) {
            return false;
        }

        for (int i = 1; i < 4; ++i) {
            if (arguments[i] == null) {
                return false;
            }
            try {
                Double number = Double.parseDouble(arguments[i].toString());
                if (number < 0) {
                    return false;
                }
            } catch (NumberFormatException nfe) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String usage() {
        return "setVelocity(String name, Double x >= 0, Double y >= 0, Double z >= 0)";
    }
}
