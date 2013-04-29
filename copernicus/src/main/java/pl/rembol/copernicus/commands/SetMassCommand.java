package pl.rembol.copernicus.commands;

import java.io.Serializable;

import pl.rembol.commandcontrol.Command;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.copernicus.system.AstralSystem;

public class SetMassCommand extends Command {

    private AstralSystem system;

    public SetMassCommand(AstralSystem system) {
        this.system = system;
    }

    @Override
    protected Serializable performInvoke(Serializable[] arguments)
            throws CommandException {

        String name = arguments[0].toString();
        Double mass = Double.parseDouble(arguments[1].toString());

        system.setMass(name, mass);

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
            Double mass = Double.parseDouble(arguments[1].toString());
            if (mass < 0) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public String usage() {
        return "setMass(String name, Double mass > 0)";
    }
}
