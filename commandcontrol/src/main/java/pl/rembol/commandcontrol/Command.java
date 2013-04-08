package pl.rembol.commandcontrol;

import java.io.Serializable;

import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.commandcontrol.result.ErrorCode;

/**
 * Abstract class for Command.
 * 
 * @author RemboL
 * 
 */
public abstract class Command {

	static public final int VALIDATION_ERROR = -1;

	/**
	 * Main body of the command. Override this method with code that will be
	 * performed by command invocation.
	 * 
	 * @param arguments
	 *            command arguments
	 * @return return code
	 */
	protected abstract Serializable performInvoke(Serializable[] arguments)
			throws CommandException;

	/**
	 * Validation method for the arguments. This method is invoked before
	 * <code>performInvoke</code>. If validation returns true, command is
	 * invoked. Otherwise <code>VALIDATION_ERROR</code> is returned.
	 * 
	 * @param arguments
	 *            command arguments
	 * @return true if arguments are valid, false otherwise
	 */
	protected abstract boolean validateArguments(Serializable[] arguments);

	/**
	 * Main entry method for the command. This method validates the arguments,
	 * on positive validation command is performed, otherwise
	 * <code>VALIDATION_ERROR</code> code is returned.
	 * 
	 * @param arguments
	 *            command arguments
	 * @return return code
	 */
	public final Serializable invoke(Serializable[] arguments) throws CommandException {
		if (validateArguments(arguments)) {
			return performInvoke(arguments);
		} else {
			throw new CommandException(ErrorCode.VALIDATION_ERROR);
		}

	}

	/**
	 * Support method that prints out usage for the command.
	 * 
	 * @return usage string
	 */
	public abstract String usage();

}
