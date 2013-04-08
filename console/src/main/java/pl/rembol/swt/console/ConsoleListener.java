package pl.rembol.swt.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Text;

abstract public class ConsoleListener extends KeyAdapter {

	protected Console console;

	@Override
	final public void keyReleased(KeyEvent e) {

		if (SWT.CR != e.keyCode) {
			return;
		}

		Text text = console.getCommandLine();

		String string = text.getText();
		text.setText("");

		process(string);

	}

	public void setConsole(Console console) {
		this.console = console;
	}

	abstract protected void process(String string);

}