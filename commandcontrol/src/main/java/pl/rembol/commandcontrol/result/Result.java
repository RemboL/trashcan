package pl.rembol.commandcontrol.result;

import java.io.Serializable;

public class Result {

	private Serializable value = null;

	private ErrorCode errorCode = null;

	private Result() {
	}

	public static Result positive() {
		return positive(null);
	}

	public static Result positive(Serializable value) {
		Result result = new Result();
		result.value = value;
		return result;
	}
	
	public static Result negative(ErrorCode errorCode) {
		Result result = new Result();
		result.errorCode = errorCode;
		return result;
	}
	
	public static Result negative(ErrorCode errorCode, Serializable value) {
		Result result = new Result();
		result.errorCode = errorCode;
		result.value = value;
		return result;
	}

	public Serializable get() throws CommandException {
		if (errorCode != null) {
			throw new CommandException(errorCode, value);
		}

		return value;
	}

}
