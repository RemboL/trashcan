package pl.rembol.commandcontrol.parser.tokenizers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.CommandElement.Type;

public class WhiteSpaceTokenizer extends GenericTokenizer {
	public static final List<String> WHITE_SPACE_SEPARATORS = Arrays.asList(
			" ", "\t");

	@Override
	public void tokenize(List<CommandElement> list) {
		for (String token : WHITE_SPACE_SEPARATORS) {

			ITokenizeAction parseWhiteSpacesAction = new ITokenizeAction() {

				public List<CommandElement> run(String[] elements) {
					String first = elements[0];
					String second = elements[1];
					List<CommandElement> newList = new ArrayList<CommandElement>();

					if (first.length() > 0) {
						newList.add(new CommandElement(Type.UNDEFINED, first));
					}

					if (second.length() > 0) {
						newList.add(new CommandElement(Type.UNDEFINED, second));
					}
					return newList;
				}

			};

			genericTokenize(list, ".*" + token + ".*", token, 2,
					parseWhiteSpacesAction);

		}

	}

}
