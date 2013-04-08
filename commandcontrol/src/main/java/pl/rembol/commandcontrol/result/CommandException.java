package pl.rembol.commandcontrol.result;

import java.io.Serializable;

public class CommandException extends Exception {

	private static final long serialVersionUID = 4341779890682428506L;

	private ErrorCode errorCode;

	private Serializable value = null;

	public CommandException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public CommandException(ErrorCode errorCode, Serializable value) {
		this.errorCode = errorCode;
		this.value = value;
	}

	@Override
	public String getMessage() {
		return "Command invocation resulted in error: " + errorCode
				+ (value == null ? "" : " (" + value.toString() + ")");
	}

	@Override
	public String getLocalizedMessage() {
		return getMessage();
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public Serializable getValue() {
		return value;
	}

}
