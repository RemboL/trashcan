package pl.rembol.commandcontrol.var;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class VariablesRegistry {

	private VariablesRegistry() {
	}

	private static final Map<String, Serializable> variables = new HashMap<String, Serializable>();

	public static Serializable get(String name) {
		return variables.get(name);
	}

	public static void set(String name, Serializable value) {
		variables.put(name, value);
	}
}
