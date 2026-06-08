package com.demo.bankapp.v1.account_service.exceptions.classes;

public class RemoteServiceException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RemoteServiceException(String message) {
		super(message);
	}
}