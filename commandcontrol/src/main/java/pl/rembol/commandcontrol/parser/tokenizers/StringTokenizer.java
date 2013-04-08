package pl.rembol.commandcontrol.parser.tokenizers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.CommandElement.Type;

public class StringTokenizer extends GenericTokenizer {
	public static final List<String> STRING_TOKENS = Arrays.asList("'''",
			"\"\"\"", "'", "\"");

	@Override
	public void tokenize(List<CommandElement> list) {
		for (String token : STRING_TOKENS) {

			ITokenizeAction parseStringsAction = new ITokenizeAction() {

				public List<CommandElement> run(String[] elements) {
					String first = elements[0];
					String second = elements[1];
					String third = elements[2];
					List<CommandElement> newList = new ArrayList<CommandElement>();

					if (first.length() > 0) {
						newList.add(new CommandElement(Type.UNDEFINED, first));
					}

					newList.add(new CommandElement(Type.STRING, second));

					if (third.length() > 0) {
						newList.add(new CommandElement(Type.UNDEFINED, third));
					}
					
					return newList;
				}

			};

			genericTokenize(list, ".*" + token + ".*" + token + ".*", token, 3,
					parseStringsAction);
		}

	}

}
