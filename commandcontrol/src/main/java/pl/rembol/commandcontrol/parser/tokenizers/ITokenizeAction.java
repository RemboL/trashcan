package pl.rembol.commandcontrol.parser.tokenizers;

import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement;

interface ITokenizeAction {
	
	List<CommandElement> run(String[] elements);

}
