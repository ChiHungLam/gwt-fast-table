package com.jwh.gwt.fasttable.client.exception;

/**
 * Indicates not able to supply the expected result
 * 
 * @author jheyne
 */
public class NotFound extends Exception {

	private static final long serialVersionUID = 1L;

	final static NotFound instance = new NotFound();

	private NotFound() {
		super();
	}

	public static NotFound getInstance() {
		return instance;
	}

}
