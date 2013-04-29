package pl.rembol.commandcontrol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.junit.Test;

import pl.rembol.commandcontrol.parser.CommandElement;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.commandcontrol.result.ErrorCode;
import pl.rembol.commandcontrol.var.VariablesRegistry;

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
    public void basicScriptTest() throws CommandException {

        String fileName = "testScript" + System.currentTimeMillis() + ".txt";
        String variable = "variable" + System.currentTimeMillis();

        Assert.assertNull(VariablesRegistry.get(variable));

        try {
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter(fileName);
                bw = new BufferedWriter(fw);
                bw.write("set(" + variable + ",test)\n");
                bw.flush();
                bw.close();
                fw.close();


            } catch (IOException ex) {
                Logger.getLogger(CommandControlTest.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (bw != null) {
                    try {
                        bw.close();
                    } catch (IOException ex) {
                        Logger.getLogger(CommandControlTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException ex) {
                        Logger.getLogger(CommandControlTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }


            CommandControl.invoke("runScript(" + fileName + ")");

            Object actualVariable = CommandControl.invoke("get(" + variable + ")");

            Assert.assertNotNull(actualVariable);
            Assert.assertEquals("test", actualVariable.toString());
        } finally {
            File file = new File(fileName);
            file.delete();
        }

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
