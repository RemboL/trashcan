package pl.rembol.commandcontrol.parser;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import pl.rembol.commandcontrol.parser.CommandElement.Type;
import pl.rembol.commandcontrol.parser.tokenizers.StringTokenizer;

public class CommandParserTest {

	@Test
	public void testBasicStringParsers() {

		for (String token : StringTokenizer.STRING_TOKENS) {

			List<CommandElement> list = CommandParser.parse(token + "abc"
					+ token);

			Assert.assertNotNull(list);
			Assert.assertEquals(1, list.size());

			CommandElement element = list.get(0);

			Assert.assertNotNull(element);
			Assert.assertEquals(Type.STRING, element.getType());
			Assert.assertEquals("abc", element.getNode().toString());

			list = CommandParser.parse(token + "first string" + token + token
					+ "second string" + token);

			Assert.assertNotNull(list);
			Assert.assertEquals(2, list.size());

			element = list.get(0);

			Assert.assertNotNull(element);
			Assert.assertEquals(Type.STRING, element.getType());
			Assert.assertEquals("first string", element.getNode().toString());

			element = list.get(1);

			Assert.assertNotNull(element);
			Assert.assertEquals(Type.STRING, element.getType());
			Assert.assertEquals("second string", element.getNode().toString());
		}

	}

	@Test
	public void testMixedStringParsers() {
		List<CommandElement> list = CommandParser
				.parse("'''String containing \"\"\"embedded triple-double-quote\"\"\", 'embedded single-quote' and \"embedded double-quote\"'''");

		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());

		CommandElement element = list.get(0);

		Assert.assertNotNull(element);
		Assert.assertEquals(Type.STRING, element.getType());
		Assert.assertEquals(
				"String containing \"\"\"embedded triple-double-quote\"\"\", 'embedded single-quote' and \"embedded double-quote\"",
				element.getNode().toString());

		list = CommandParser
				.parse("\"\"\"String containing 'embedded single-quote' and \"embedded double-quote\" \"\"\"");

		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());

		element = list.get(0);

		Assert.assertNotNull(element);
		Assert.assertEquals(Type.STRING, element.getType());
		Assert.assertEquals(
				"String containing 'embedded single-quote' and \"embedded double-quote\" ",
				element.getNode().toString());

		list = CommandParser
				.parse("'String containing \"embedded double-quote\"'");

		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());

		element = list.get(0);

		Assert.assertNotNull(element);
		Assert.assertEquals(Type.STRING, element.getType());
		Assert.assertEquals("String containing \"embedded double-quote\"",
				element.getNode().toString());
	}

	@Test
	public void testWhiteSpaces() {

		List<CommandElement> list = CommandParser
				.parse(" element1 element2  \t  element3  ");

		Assert.assertNotNull(list);
		Assert.assertEquals(3, list.size());

		Assert.assertEquals("element1", list.get(0).getNode());
		Assert.assertEquals("element2", list.get(1).getNode());
		Assert.assertEquals("element3", list.get(2).getNode());
	}

	@Test
	public void testList() {

		List<CommandElement> list = CommandParser
				.parse("'element1', 'element2', \t'element3'");

		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
		Assert.assertNotNull(list.get(0));
		Assert.assertEquals(Type.LIST, list.get(0).getType());

		List<CommandElement> children = list.get(0).getChildren();
		Assert.assertNotNull(children);
		Assert.assertEquals(3, children.size());

		Assert.assertEquals("element1", children.get(0).getNode());
		Assert.assertEquals("element2", children.get(1).getNode());
		Assert.assertEquals("element3", children.get(2).getNode());
	}

	@Test
	public void testEmptyBrackets() {
		List<CommandElement> list = CommandParser.parse("()");

		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
		Assert.assertNotNull(list.get(0));
		Assert.assertEquals(Type.BRACKETED, list.get(0).getType());
	}

	@Test
	public void testBrackets() {
		List<CommandElement> list = CommandParser.parse("(\"a\",\"b\")");

		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
		Assert.assertNotNull(list.get(0));
		Assert.assertEquals(Type.BRACKETED, list.get(0).getType());

		List<CommandElement> children = list.get(0).getChildren();
		Assert.assertNotNull(children);
		Assert.assertEquals(2, children.size());
		
		Assert.assertNotNull(children.get(0));
		Assert.assertEquals(Type.STRING, children.get(0).getType());
		Assert.assertNotNull(children.get(0).getNode());
		Assert.assertEquals("a", children.get(0).getNode().toString());
		
		Assert.assertNotNull(children.get(1));
		Assert.assertEquals(Type.STRING, children.get(1).getType());
		Assert.assertNotNull(children.get(1).getNode());
		Assert.assertEquals("b", children.get(1).getNode().toString());
	}
	
	@Test
	public void testInvocation() {
		List<CommandElement> list = CommandParser.parse("get('cat')");
		
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
		Assert.assertNotNull(list.get(0));
		Assert.assertEquals(Type.INVOCATION, list.get(0).getType());
		Assert.assertEquals("get", list.get(0).getNode().toString());
		
		List<CommandElement> children = list.get(0).getChildren();
		Assert.assertNotNull(children);
		Assert.assertEquals(1, children.size());
		
		Assert.assertNotNull(children.get(0));
		Assert.assertEquals(Type.STRING, children.get(0).getType());
		Assert.assertNotNull(children.get(0).getNode());
		Assert.assertEquals("cat", children.get(0).getNode().toString());
		
		list = CommandParser.parse("get('cat',embedded('invo','cation'))");
		
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
		Assert.assertNotNull(list.get(0));
		Assert.assertEquals(Type.INVOCATION, list.get(0).getType());
		Assert.assertEquals("get", list.get(0).getNode().toString());
		
		children = list.get(0).getChildren();
		Assert.assertNotNull(children);
		Assert.assertEquals(2, children.size());
		
		Assert.assertNotNull(children.get(0));
		Assert.assertEquals(Type.STRING, children.get(0).getType());
		Assert.assertNotNull(children.get(0).getNode());
		Assert.assertEquals("cat", children.get(0).getNode().toString());
		
		Assert.assertNotNull(children.get(1));
		Assert.assertEquals(Type.INVOCATION, children.get(1).getType());
		Assert.assertNotNull(children.get(1).getNode());
		Assert.assertEquals("embedded", children.get(1).getNode().toString());
		
		
	}
}
