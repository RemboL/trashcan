package pl.rembol.commandcontrol.parser.matchers;

import pl.rembol.commandcontrol.parser.CommandElement.Type;

public class TokenMatchers {

	private TokenMatchers() {
	}

	public static TokenMatcher eq(Type type) {
		return new EqTypeTokenMatcher(type);
	}

	public static TokenMatcher ne(Type type) {
		return new NeTypeTokenMatcher(type);
	}

	public static TokenMatcher any() {
		return new AnyTokenMatcher();
	}
	
	public static TokenMatcher in(Type... types) {
		return new InTypeTokenMatcher(types);
	}
	
	public static TokenMatcher notIn(Type... types) {
		return new NotInTypeTokenMatcher(types);
	}

}
