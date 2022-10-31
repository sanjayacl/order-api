package com.loldigital.orderapi.dto;

public class ExceptionMessageDTO  {

	private String exception;
	private String localizedException;

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public ExceptionMessageDTO(String exception) {
		super();
		this.exception = exception;
	}
	
	public ExceptionMessageDTO(String exception,String localizedException) {
		super();
		this.exception = exception;
		this.localizedException = localizedException;
	}

	/**
	 * @return the localizedException
	 */
	public String getLocalizedException() {
		return localizedException;
	}

	/**
	 * @param localizedException the localizedException to set
	 */
	public void setLocalizedException(String localizedException) {
		this.localizedException = localizedException;
	}

	
	
	
}
