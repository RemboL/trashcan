package pl.rembol.commandcontrol.parser.tokenizers;

import java.util.ArrayList;
import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.parser.CommandElement.Type;

public class SimpleTokenizer extends GenericTokenizer {

	private Type type;

	private String string;

	public SimpleTokenizer(Type type, String string) {
		this.type = type;
		this.string = string;
	}

	@Override
	public void tokenize(List<CommandElement> list) {
		ITokenizeAction parseCommasAction = new ITokenizeAction() {

			public List<CommandElement> run(String[] elements) {
				String first = elements[0];
				String second = elements[1];

				List<CommandElement> newList = new ArrayList<CommandElement>();

				if (first.length() > 0) {
					newList.add(new CommandElement(Type.UNDEFINED, first));
				}

				newList.add(new CommandElement(type, string));

				if (second.length() > 0) {
					newList.add(new CommandElement(Type.UNDEFINED, second));
				}
				return newList;
			}

		};

		genericTokenize(list, ".*" + string + ".*", string, 2,
				parseCommasAction);
	}

}
