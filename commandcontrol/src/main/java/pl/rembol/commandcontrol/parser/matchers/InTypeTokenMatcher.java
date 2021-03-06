package pl.rembol.commandcontrol.parser.matchers;

import java.util.Arrays;
import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.CommandElement.Type;

class InTypeTokenMatcher implements TokenMatcher {
	
	private List<Type> types;
	
	InTypeTokenMatcher(Type... types) {
		this.types = Arrays.asList(types);
	}
	
	public boolean match(CommandElement element) {
		return types.contains(element.getType());
	}

}
