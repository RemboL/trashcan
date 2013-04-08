package pl.rembol.commandcontrol.basic;

import java.io.Serializable;

import pl.rembol.commandcontrol.Command;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.commandcontrol.var.VariablesRegistry;

/**
 * Basic command for saving variable into registry
 * 
 * @author RemboL
 * 
 */
public class SetCommand extends Command {

	@Override
	protected Serializable performInvoke(Serializable[] arguments)
			throws CommandException {

		String name = (String) arguments[0];
		Serializable value = arguments[1];

		VariablesRegistry.set(name, value);

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

		if (arguments[1] == null) {
			return false;
		}

		if (!(arguments[0] instanceof String)) {
			return false;
		}

		return true;
	}

	@Override
	public String usage() {
		return "set(String name, Serializable value)";
	}

}
