package pl.rembol.commandcontrol.parser.patterns;

import static pl.rembol.commandcontrol.parser.matchers.TokenMatchers.eq;
import static pl.rembol.commandcontrol.parser.matchers.TokenMatchers.in;

import java.util.Arrays;
import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.CommandElement.Type;
import pl.rembol.commandcontrol.parser.matchers.TokenMatcher;

class BracketedPatternMatcher extends PatternMatcher {

	private static final List<TokenMatcher> pattern = Arrays.asList(
			eq(Type.LEFT_BRACKET), //
			in(Type.STRING, Type.LIST, Type.INVOCATION, Type.UNDEFINED), //
			eq(Type.RIGHT_BRACKET)); //

	public List<TokenMatcher> getPattern() {
		return pattern;
	}

	public List<CommandElement> translateSublist(List<CommandElement> elements) {

		CommandElement parameter = elements.get(1);
		
		CommandElement bracketedElement = new CommandElement(Type.BRACKETED, "");
		
		if(Type.LIST == parameter.getType()) {
			bracketedElement.setChildren(parameter.getChildren());
		} else {
			bracketedElement.setChildren(Arrays.asList(parameter));
		}
		
		return Arrays.asList(bracketedElement);

	}

}
