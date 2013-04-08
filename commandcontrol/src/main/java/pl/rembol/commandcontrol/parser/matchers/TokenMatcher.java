package pl.rembol.commandcontrol.parser.matchers;

import pl.rembol.commandcontrol.parser.CommandElement;

public interface TokenMatcher {
	
	boolean match(CommandElement element);
	
}
