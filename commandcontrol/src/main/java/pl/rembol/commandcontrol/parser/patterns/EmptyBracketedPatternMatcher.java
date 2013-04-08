package pl.rembol.commandcontrol.parser.patterns;

import static pl.rembol.commandcontrol.parser.matchers.TokenMatchers.eq;

import java.util.Arrays;
import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.CommandElement.Type;
import pl.rembol.commandcontrol.parser.matchers.TokenMatcher;

class EmptyBracketedPatternMatcher extends PatternMatcher {

	private static final List<TokenMatcher> pattern = Arrays.asList(
			eq(Type.LEFT_BRACKET), //
			eq(Type.RIGHT_BRACKET)); //

	public List<TokenMatcher> getPattern() {
		return pattern;
	}

	public List<CommandElement> translateSublist(List<CommandElement> elements) {

		return Arrays.asList(new CommandElement(Type.BRACKETED, ""));

	}

}
