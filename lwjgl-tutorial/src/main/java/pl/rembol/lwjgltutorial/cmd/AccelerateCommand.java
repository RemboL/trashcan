package pl.rembol.lwjgltutorial.cmd;

import java.io.Serializable;

import pl.rembol.commandcontrol.Command;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.lwjgltutorial.SWTExample;

public class AccelerateCommand extends Command {

	@Override
	protected Serializable performInvoke(Serializable[] arguments)
			throws CommandException {

		SWTExample.speed+=1;
		
		return null;
	}

	@Override
	protected boolean validateArguments(Serializable[] arguments) {
		if(arguments.length == 0) {
			return true;
		}
		return false;
	}

	@Override
	public String usage() {
		return "acc()";
	}

}
