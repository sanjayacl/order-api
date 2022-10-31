package com.loldigital.orderapi.exceptions.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.loldigital.orderapi.dto.ExceptionMessageDTO;
import com.loldigital.orderapi.exceptions.LolDigitalServerSideException;
import com.loldigital.orderapi.exceptions.ResourceNotFoundException;
import com.loldigital.orderapi.exceptions.TenantNotSpecifiedException;

/**
 * @author sanjaya This is for returning proper error messages with the proper
 *         http status when exception occurs
 */
@ControllerAdvice
public class LolDigitalExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ResponseEntity<Object> exceptionObj = null;

		if (request.getParameter("tenant") == null) {
			exceptionObj = new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return exceptionObj;
	}

	@ExceptionHandler(value = { LolDigitalServerSideException.class })
	public ResponseEntity<Object> getResponseForLolDigitalServerSideException(LolDigitalServerSideException exception) {
		return new ResponseEntity<>(
				new ExceptionMessageDTO(LolDigitalServerSideException.ERROR_MESSAGE,
						getLocalizedErrorMessage(LolDigitalServerSideException.class.getName())),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> gerResponseForResourceNotFoundException(ResourceNotFoundException exception,
			WebRequest request) {
		return new ResponseEntity<>(new ExceptionMessageDTO(ResourceNotFoundException.ERROR_MESSAGE,
				getLocalizedErrorMessage(ResourceNotFoundException.class.getName())), HttpStatus.NOT_FOUND);
	}



	@ExceptionHandler(value = { TenantNotSpecifiedException.class })
	public ResponseEntity<Object> getResponseForTenantNotSpecifiedException(TenantNotSpecifiedException exception) {
		return new ResponseEntity<>(new ExceptionMessageDTO(TenantNotSpecifiedException.ERROR_MESSAGE,
				getLocalizedErrorMessage(TenantNotSpecifiedException.class.getName())), HttpStatus.BAD_REQUEST);
	}

	private String getLocalizedErrorMessage(String exceptionClass) {
		return messageSource.getMessage(exceptionClass, null, LocaleContextHolder.getLocale());
	}
}
