package pl.rembol.commandcontrol.parser.patterns;

import java.util.ArrayList;
import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.matchers.TokenMatcher;

abstract class PatternMatcher {

	abstract List<TokenMatcher> getPattern();

	abstract List<CommandElement> translateSublist(List<CommandElement> elements);

	public boolean translate(List<CommandElement> elements) {

		int index = matches(elements);

		if (index == -1) {
			return false;
		}

		List<CommandElement> oldElements = new ArrayList<CommandElement>();
		oldElements
				.addAll(elements.subList(index, index + getPattern().size()));
		List<CommandElement> newElements = translateSublist(oldElements);

		elements.removeAll(oldElements);
		elements.addAll(index, newElements);

		return true;
	}

	private int matches(List<CommandElement> elements) {

		for (int i = 0; i < elements.size() - getPattern().size() + 1; ++i) {

			boolean match = true;

			for (int j = 0; j < getPattern().size(); ++j) {

				if (!getPattern().get(j).match(elements.get(i + j))) {
					match = false;
					break;
				}

			}

			if (match) {
				return i;
			}
		}

		return -1;
	}
}