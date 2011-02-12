package com.jwh.gwt.fasttable.client;

/**
 * Tests whether a domain model should be shown in the table
 * 
 * @author jheyne
 * @param <T>
 *            Class of domain objects in a table
 */
public interface Filter<T> {

	/**
	 * @param candidate
	 * @return true if candidate should be included in the table, false to
	 *         exclude
	 */
	public boolean isIncluded(T candidate);
}
