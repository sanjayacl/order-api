package com.loldigital.orderapi.exceptions;

public class TenantNotSpecifiedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public static final String ERROR_MESSAGE = "Tenant is not specified.";

	public TenantNotSpecifiedException() {
		super(ERROR_MESSAGE);
	}
}
