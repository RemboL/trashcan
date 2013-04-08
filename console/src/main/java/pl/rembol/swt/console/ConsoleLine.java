package pl.rembol.swt.console;

import org.eclipse.swt.graphics.RGB;

class ConsoleLine {

	static enum Style {

		INFO(new RGB(255, 255, 255), new RGB(0, 0, 0)), //
		ERROR(new RGB(255, 0, 0), new RGB(0, 0, 0));

		Style(RGB foregroundColor, RGB backbroundColor) {
			this.foregroundColor = foregroundColor;
			this.backgroundColor = backbroundColor;
		}

		private RGB foregroundColor;

		private RGB backgroundColor;

		RGB getForegroundColor() {
			return foregroundColor;
		}

		RGB getBackgroundColor() {
			return backgroundColor;
		}

	}

	private String line;
	
	private Style style;
	
	ConsoleLine(String line, Style style) {
		this.line = line;
		this.style = style;
	}
	
	String getLine() {
		return line;
	}
	
	RGB getForegroundColor() {
		return style.getForegroundColor();
	}
	
	RGB getBackgroundColor() {
		return style.getBackgroundColor();
	}
	
	@Override
	public String toString() {
		return line;
	}
}
