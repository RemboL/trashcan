package pl.rembol.commandcontrol.parser.patterns;

import static pl.rembol.commandcontrol.parser.matchers.TokenMatchers.eq;
import static pl.rembol.commandcontrol.parser.matchers.TokenMatchers.in;

import java.util.Arrays;
import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement.Type;
import pl.rembol.commandcontrol.parser.matchers.TokenMatcher;

class ListWithUndefinedElementsPatternMatcher extends ListPatternMatcher {

	private static final List<TokenMatcher> pattern = Arrays.asList(
			in(Type.STRING, Type.LIST, Type.INVOCATION, Type.UNDEFINED), //
			eq(Type.COMMA), //
			in(Type.STRING, Type.LIST, Type.INVOCATION, Type.UNDEFINED)); //

	@Override
	public List<TokenMatcher> getPattern() {
		return pattern;
	}

}
