package pl.rembol.lwjgltutorial.console;

import java.io.Serializable;

import pl.rembol.commandcontrol.CommandControl;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.swt.console.ConsoleListener;

public class CommandConsoleListener extends ConsoleListener {

	@Override
	protected void process(String string) {
		try {
			Serializable result = CommandControl.invoke(string);
			if (result == null) {
				console.info("null");
				return;
			} else {
				console.info(result.toString());
			}
		} catch (CommandException e1) {
			console.error(e1.getLocalizedMessage());
		}
	}

}
