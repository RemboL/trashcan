package pl.rembol.commandcontrol.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Element of a parse tree.
 * @author RemboL
 *
 */
public class CommandElement {
	
	/**
	 * Type of a token stored in this element.
	 * @author RemboL
	 *
	 */
	public static enum Type {
		STRING, UNDEFINED, COMMA, LIST, LEFT_BRACKET, RIGHT_BRACKET, INVOCATION, BRACKETED
	}
	
	private List<CommandElement> children = new ArrayList<CommandElement>();
	
	private Serializable node;
	
	private Type type;
	
	public CommandElement() {
	}
	
	public CommandElement(Type type, Serializable node) {
		this.type = type;
		this.node = node;
	}
	
	public List<CommandElement> getChildren() {
		return children;
	}
	
	public void setChildren(List<CommandElement> children) {
		this.children = children;
	}
	
	public Serializable getNode() {
		return node;
	}
	
	public void setNode(Serializable node) {
		this.node = node;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public String toString() {
		String result = "";
		result+= type;
		
		result += ":" + node.toString()+"(" + StringUtils.join(children, ", ") + ")";
		
		return result;
	}
}
