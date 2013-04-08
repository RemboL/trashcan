package pl.rembol.swt.console;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import pl.rembol.swt.console.ConsoleLine.Style;

public class Console extends Composite {

	private int length = 10;

	private StyledText log;

	private Text commandLine;

	private List<ConsoleLine> lines = new LinkedList<ConsoleLine>();

	public Console(Composite parent, int style, ConsoleListener listener) {
		super(parent, style);

		this.setLayout(new GridLayout(1, true));

		log = new StyledText(this, SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		log.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		log.setSize(0, 100);

		log.setBackground(new Color(this.getDisplay(), Style.INFO
				.getBackgroundColor()));

		commandLine = new Text(this, SWT.SINGLE | SWT.BORDER);
		commandLine.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
				1, 1));

		commandLine.addKeyListener(listener);
		listener.setConsole(this);
		
	}

	public void info(String s) {
		addLine(new ConsoleLine(s, Style.INFO));
	}

	public void error(String s) {
		addLine(new ConsoleLine(s, Style.ERROR));
	}

	private void addLine(ConsoleLine line) {
		lines.add(line);
		while (lines.size() > length) {
			lines.remove(0);
		}

		redrawLog();
		
		if(this.getParent() != null) {
			this.getParent().layout();
		}
	}

	private void redrawLog() {

		String logString = StringUtils.join(lines, "\r\n");
		log.setText(logString);

		List<StyleRange> styleRanges = new ArrayList<StyleRange>();

		int offset = 0;

		for (ConsoleLine line : lines) {
			int length = line.getLine().length();
			Color foreground = new Color(this.getDisplay(),
					line.getForegroundColor());
			Color background = new Color(this.getDisplay(),
					line.getBackgroundColor());

			StyleRange styleRange = new StyleRange(offset, length, foreground,
					background);
			styleRanges.add(styleRange);

			offset += length + "\r\n".length();
		}

		log.setStyleRanges(styleRanges.toArray(new StyleRange[0]));

	}

	Text getCommandLine() {
		return commandLine;
	}
}
