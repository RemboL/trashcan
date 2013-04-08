package pl.rembol.commandcontrol.parser.patterns;

import static pl.rembol.commandcontrol.parser.matchers.TokenMatchers.eq;

import java.util.Arrays;
import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.CommandElement.Type;
import pl.rembol.commandcontrol.parser.matchers.TokenMatcher;

class InvocationPatternMatcher extends PatternMatcher {

	private static final List<TokenMatcher> pattern = Arrays.asList(
			eq(Type.UNDEFINED), //
			eq(Type.BRACKETED)); //

	@Override
	public List<TokenMatcher> getPattern() {
		return pattern;
	}

	@Override
	public List<CommandElement> translateSublist(List<CommandElement> elements) {

		CommandElement command = elements.get(0);
		CommandElement argument = elements.get(1);

		CommandElement invocationElement = new CommandElement(Type.INVOCATION,
				command.getNode());

		invocationElement.setChildren(argument.getChildren());
		
		return Arrays.asList(invocationElement);

	}

}
