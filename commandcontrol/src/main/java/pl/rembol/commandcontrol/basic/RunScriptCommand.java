package pl.rembol.commandcontrol.basic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.rembol.commandcontrol.Command;
import pl.rembol.commandcontrol.CommandControl;
import pl.rembol.commandcontrol.result.CommandException;
import pl.rembol.commandcontrol.result.ErrorCode;

/**
 * Basic command for retrieving variable from registry.
 *
 * @author RemboL
 *
 */
public class RunScriptCommand extends Command {

    @Override
    protected Serializable performInvoke(Serializable[] arguments)
            throws CommandException {
        String fileName = (String) arguments[0];
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try {
            try {
                fileReader = new FileReader(fileName);
                bufferedReader = new BufferedReader(fileReader);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(RunScriptCommand.class.getName()).log(Level.SEVERE, null, ex);
                throw new CommandException(ErrorCode.FILE_READ_ERROR, fileName);
            }

            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    CommandControl.invoke(line);
                }
            } catch (IOException ex) {
                Logger.getLogger(RunScriptCommand.class.getName()).log(Level.SEVERE, null, ex);
                throw new CommandException(ErrorCode.FILE_READ_ERROR, fileName);
            }

        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(RunScriptCommand.class.getName()).log(Level.SEVERE, null, ex);
                    throw new CommandException(ErrorCode.FILE_READ_ERROR, fileName);
                }
            }

            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(RunScriptCommand.class.getName()).log(Level.SEVERE, null, ex);
                    throw new CommandException(ErrorCode.FILE_READ_ERROR, fileName);
                }
            }
        }

        return null;
    }

    @Override
    protected boolean validateArguments(Serializable[] arguments) {
        if (arguments == null) {
            return false;
        }

        if (arguments.length != 1) {
            return false;
        }

        if (arguments[0] == null) {
            return false;
        }

        if (!(arguments[0] instanceof String)) {
            return false;
        }

        return true;
    }

    @Override
    public String usage() {
        return "runScript(String name)";
    }
}
