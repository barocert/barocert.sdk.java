package com.barocert;

import kr.co.linkhub.auth.LinkhubException;

public class BarocertException extends Exception {
	
	private static final long serialVersionUID = 1L;

	private long code;
	
	public BarocertException(LinkhubException linkhubException) {
		super(linkhubException.getMessage(), linkhubException);
		this.code = linkhubException.getCode();
	}
	
	public BarocertException(long code, String Message) {
		super(Message);
		this.code = code;
	}

	public BarocertException(long code, String Message, Throwable innerException) {
		super(Message, innerException);
		this.code = code;
	}

	/**
	 * Return Barocert's result Error code. (ex. -31010009) In case of -99999999,
	 * check the getMessage() for detail.
	 * 
	 * @return error code.
	 */
	public long getCode() {
		return code;
	}
	
}
