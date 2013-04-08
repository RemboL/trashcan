package pl.rembol.commandcontrol.basic;

import java.io.Serializable;

import pl.rembol.commandcontrol.Command;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.commandcontrol.var.VariablesRegistry;

/**
 * Basic command for retrieving variable from registry.
 * 
 * @author RemboL
 * 
 */
public class GetCommand extends Command {

	@Override
	protected Serializable performInvoke(Serializable[] arguments)
			throws CommandException {
		String name = (String) arguments[0];

		return VariablesRegistry.get(name);
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

		if (!(arguments[0] instanceof String)) {
			return false;
		}

		return true;
	}

	@Override
	public String usage() {
		return "Serializable get(String name)";
	}

}
