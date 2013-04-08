package pl.rembol.commandcontrol.parser.matchers;

import pl.rembol.commandcontrol.parser.CommandElement;

class AnyTokenMatcher implements TokenMatcher {
	
	public boolean match(CommandElement element) {
		return true;
	}

}
