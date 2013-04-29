package pl.rembol.commandcontrol.parser.patterns;

import java.util.ArrayList;
import java.util.List;

import pl.rembol.commandcontrol.parser.CommandElement;

public class PatternFinder {

	private static final List<PatternMatcher> patternOrder = new ArrayList<PatternMatcher>();

	static {
		patternOrder.add(new EmptyBracketedPatternMatcher());
		patternOrder.add(new BracketedPatternMatcher());
		patternOrder.add(new InvocationPatternMatcher());
		patternOrder.add(new ListPatternMatcher());
		patternOrder.add(new ListWithUndefinedElementsPatternMatcher());
		
	}

	public static void translate(List<CommandElement> elements) {
		boolean changed;

		do {
			changed = doTranslate(elements);

		} while (changed);
	}

	private static boolean doTranslate(List<CommandElement> elements) {
		

		boolean changed;

		do {
			changed = false;
			
			for(CommandElement element : elements) {
				if(element.getChildren() != null && element.getChildren().size() > 0) {
					changed = doTranslate(element.getChildren());
				}
				
				if(changed) {
					return true;
				}
			}
			
			for (PatternMatcher matcher : patternOrder) {
				changed = matcher.translate(elements);
				if (changed) {
					return true;
				}
			}

		} while (changed);

		return false;
	}

}
