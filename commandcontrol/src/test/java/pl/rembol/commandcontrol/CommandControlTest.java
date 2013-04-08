package pl.rembol.commandcontrol;

import java.io.Serializable;

import junit.framework.Assert;

import org.junit.Test;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.commandcontrol.result.ErrorCode;

public class CommandControlTest {

	@Test
	public void basicSetGetTest() throws CommandException {

		CommandControl.invoke("set(a,b)");
		Serializable value = CommandControl.invoke("get(a)");

		Assert.assertEquals("b", value);

		CommandControl.invoke("set(get(a),c)");
		Assert.assertEquals("c", CommandControl.invoke("get(b)"));
		Assert.assertEquals("c", CommandControl.invoke("get(get(a))"));
	}

	@Test
	public void unmatchedTokensTest() {

		String errorMsg = "CommandException expected";

		try {
			CommandControl.invoke("get(b))");

			Assert.fail(errorMsg);
		} catch (CommandException e) {
			Assert.assertEquals(ErrorCode.UNMATCHED_TOKEN_FOUND,
					e.getErrorCode());
			Assert.assertEquals(CommandElement.Type.RIGHT_BRACKET, e.getValue());
		} catch (Throwable e) {
			Assert.fail(errorMsg);
		}

		try {
			CommandControl.invoke("get((b)");

			Assert.fail(errorMsg);
		} catch (CommandException e) {
			Assert.assertEquals(ErrorCode.UNMATCHED_TOKEN_FOUND,
					e.getErrorCode());
			Assert.assertEquals(CommandElement.Type.LEFT_BRACKET, e.getValue());
		} catch (Throwable e) {
			Assert.fail(errorMsg);
		}
	}

}
