package pl.rembol.copernicus.commands;

import java.io.Serializable;

import pl.rembol.commandcontrol.Command;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.copernicus.system.AstralSystem;

public class RenameCommand extends Command {

    private AstralSystem system;

    public RenameCommand(AstralSystem system) {
        this.system = system;
    }

    @Override
    protected Serializable performInvoke(Serializable[] arguments)
            throws CommandException {

        String oldName = arguments[0].toString();
        String newName = arguments[1].toString();

        system.rename(oldName, newName);

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

        for (int i = 0; i < 2; ++i) {
            if (arguments[i] == null) {
                return false;
            }

            if ("".equals(arguments[i].toString())) {
                return false;
            }
        }

        return true;

    }

    @Override
    public String usage() {
        return "rename(String oldName, String newName)";
    }
}
