package pl.rembol.commandcontrol.parser.matchers;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.CommandElement.Type;

class EqTypeTokenMatcher implements TokenMatcher {

	private Type type;

	EqTypeTokenMatcher(Type type) {
		this.type = type;
	}

	public boolean match(CommandElement element) {
		return type == element.getType();
	}

}
