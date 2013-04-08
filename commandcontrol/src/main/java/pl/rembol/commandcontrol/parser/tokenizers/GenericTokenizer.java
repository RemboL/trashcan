package pl.rembol.commandcontrol.parser.tokenizers;

import java.util.List;
import java.util.regex.Pattern;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.CommandElement.Type;

abstract class GenericTokenizer {
	
	abstract public void tokenize(List<CommandElement> list);
	
	protected void genericTokenize(List<CommandElement> list, String pattern,
			String token, int numberOfElements, ITokenizeAction action) {
		boolean changed = false;

		do {

			changed = false;

			// check if token exists in pattern
			for (CommandElement element : list) {
				if (Type.UNDEFINED == element.getType()) {
					if (element.getNode() != null) {
						String elementNode = element.getNode().toString();

						if (Pattern.matches(pattern, elementNode)) {
							String[] tokens = Pattern.compile(token).split(
									elementNode, numberOfElements);
							if (tokens.length >= numberOfElements) {

								List<CommandElement> newList = action
										.run(tokens);

								list.addAll(list.indexOf(element), newList);
								list.remove(element);

								changed = true;
								break;

							}
						}

					}
				}
			}

		} while (changed);
	}
}
