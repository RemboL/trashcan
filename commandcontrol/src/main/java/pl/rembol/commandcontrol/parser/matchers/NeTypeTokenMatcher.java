package pl.rembol.commandcontrol.parser.matchers;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.CommandElement.Type;

class NeTypeTokenMatcher implements TokenMatcher {
	
	private Type type;
	
	NeTypeTokenMatcher(Type type) {
		this.type = type;
	}
	
	public boolean match(CommandElement element) {
		return type != element.getType();
	}

}
