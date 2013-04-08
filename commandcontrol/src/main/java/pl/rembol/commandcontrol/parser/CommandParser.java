package pl.rembol.commandcontrol.parser;

import java.util.LinkedList;
import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement.Type;
import pl.rembol.commandcontrol.parser.patterns.PatternFinder;
import pl.rembol.commandcontrol.parser.tokenizers.SimpleTokenizer;
import pl.rembol.commandcontrol.parser.tokenizers.StringTokenizer;
import pl.rembol.commandcontrol.parser.tokenizers.WhiteSpaceTokenizer;

public final class CommandParser {

	private CommandParser() {
	}

	public static List<CommandElement> parse(String command) {

		List<CommandElement> list = new LinkedList<CommandElement>();

		CommandElement main = new CommandElement();
		main.setType(Type.UNDEFINED);
		main.setNode(command);
		list.add(main);

		tokenize(list);
		parse(list);

		return list;
	}

	private static void tokenize(List<CommandElement> list) {
		new StringTokenizer().tokenize(list);
		new WhiteSpaceTokenizer().tokenize(list);
		new SimpleTokenizer(Type.COMMA, ",").tokenize(list);
		new SimpleTokenizer(Type.LEFT_BRACKET, "\\(").tokenize(list);
		new SimpleTokenizer(Type.RIGHT_BRACKET, "\\)").tokenize(list);
	}

	private static void parse(List<CommandElement> list) {
		PatternFinder.translate(list);
	}
	
}
