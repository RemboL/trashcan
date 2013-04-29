package pl.rembol.commandcontrol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.rembol.commandcontrol.basic.GetCommand;
import pl.rembol.commandcontrol.basic.RunScriptCommand;
import pl.rembol.commandcontrol.basic.SetCommand;
import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.CommandElement.Type;
import pl.rembol.commandcontrol.parser.CommandParser;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.commandcontrol.result.ErrorCode;

/**
 * Static class used to parse and execute commands given by a user.
 *
 * @author RemboL
 *
 */
public final class CommandControl {

    public static final int REGISTER_OK = 0;
    public static final int REGISTER_COMMAND_NAME_REGISTERED = -1;
    private static final Map<String, Command> commands = new HashMap<String, Command>();

    static {
        registerCommand("get", new GetCommand());
        registerCommand("set", new SetCommand());
        registerCommand("runScript", new RunScriptCommand());
    }

    private CommandControl() {
    }

    /**
     * Registers a command within a registry.
     *
     * @param commandName name under which the command is registered
     * @param command command to be registered
     * @return
     */
    public static int registerCommand(String commandName, Command command) {
        if (commands.keySet().contains(commandName)) {
            return REGISTER_COMMAND_NAME_REGISTERED;
        }

        commands.put(commandName, command);
        return REGISTER_OK;
    }

    /**
     * Main invocation method of a Control. Takes string as an argument, then
     * parses the string and performs an execution.
     *
     * @param command String containing command execution.
     * @return
     * @throws CommandException
     */
    public static Serializable invoke(String command) throws CommandException {

        List<CommandElement> elements = CommandParser.parse(command);

        if (elements == null || elements.size() == 0) {
            return null;
        }

        if (elements.size() == 1) {
            CommandElement element = elements.get(0);

            if (element.getType() == Type.INVOCATION) {
                return invokeCommand(element);
            }

            return element.getNode();
        }

        return (Serializable) prepareArguments(elements);

    }

    private static Serializable invokeCommand(CommandElement element)
            throws CommandException {

        String commandName = element.getNode().toString();

        if (element.getType() != Type.INVOCATION) {
            return null;
        }

        if (!commands.containsKey(commandName)) {
            throw new CommandException(ErrorCode.UNKNOWN_COMMAND, commandName);
        }

        List<Serializable> arguments = prepareArguments(element.getChildren());

        Command command = commands.get(commandName);

        return command.invoke(arguments.toArray(new Serializable[0]));
    }

    private static List<Serializable> prepareArguments(
            List<CommandElement> elements) throws CommandException {

        List<Serializable> result = new ArrayList<Serializable>();

        if (elements == null || elements.size() == 0) {
            return result;
        }

        for (CommandElement element : elements) {

            switch (element.getType()) {
                case BRACKETED:
                case LIST:
                    result.add((Serializable) prepareArguments(element
                            .getChildren()));
                    break;
                case COMMA:
                case LEFT_BRACKET:
                case RIGHT_BRACKET:
                    throw new CommandException(ErrorCode.UNMATCHED_TOKEN_FOUND,
                            element.getType());
                case INVOCATION:
                    result.add(invokeCommand(element));
                    break;
                case STRING:
                case UNDEFINED:
                    result.add(element.getNode());
            }
        }
        return result;
    }
}
