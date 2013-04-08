package pl.rembol.commandcontrol.parser.patterns;

import static pl.rembol.commandcontrol.parser.matchers.TokenMatchers.eq;
import static pl.rembol.commandcontrol.parser.matchers.TokenMatchers.in;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.CommandElement.Type;
import pl.rembol.commandcontrol.parser.matchers.TokenMatcher;

class ListPatternMatcher extends PatternMatcher {

	private static final List<TokenMatcher> pattern = Arrays.asList(
			in(Type.STRING, Type.LIST, Type.INVOCATION), //
			eq(Type.COMMA), //
			in(Type.STRING, Type.LIST, Type.INVOCATION)); //

	public List<TokenMatcher> getPattern() {
		return pattern;
	}

	public List<CommandElement> translateSublist(List<CommandElement> elements) {

		List<CommandElement> list = new ArrayList<CommandElement>();

		for (CommandElement element : elements) {
			if(Type.COMMA == element.getType()) {
				continue;
			}
			
			if (Type.LIST == element.getType()) {
				list.addAll(element.getChildren());
			} else {
				list.add(element);
			}
		}

		CommandElement listElement = new CommandElement(Type.LIST, "");
		listElement.setChildren(list);
		
		return Arrays.asList(listElement);

	}

}
