package pl.rembol.copernicus.commands;

import java.io.Serializable;

import pl.rembol.commandcontrol.Command;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.copernicus.system.AstralSystem;

public class SetColorCommand extends Command {

    private AstralSystem system;

    public SetColorCommand(AstralSystem system) {
        this.system = system;
    }

    @Override
    protected Serializable performInvoke(Serializable[] arguments)
            throws CommandException {

        String name = arguments[0].toString();
        Integer red = Integer.parseInt(arguments[1].toString());
        Integer green = Integer.parseInt(arguments[2].toString());
        Integer blue = Integer.parseInt(arguments[3].toString());

        system.setColor(name, red, green, blue);

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
                Integer number = Integer.parseInt(arguments[i].toString());
                if (number < 0) {
                    return false;
                }

                if (number > 255) {
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
        return "add(String name)";
    }
}
