package com.np.ifcp.repair.exception;

public class InfoException extends Exception {
	private static final long serialVersionUID = -5477920392065828878L;
	public int reason;
	public String pan = "";

	public InfoException(int reason, String pan) {
		super("Info Exception with reason: " + reason);
		this.reason = reason;
		this.pan = pan;
	}

	public InfoException(int reason) {
		this(reason, "");
	}
}
