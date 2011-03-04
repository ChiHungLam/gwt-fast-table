package com.jwh.gwt.fasttable.client;

/**
 * Specifies the incremental build strategy
 * 
 * @author jheyne
 * 
 */
public enum IncrementalStrategy {
	/**
	 * Do not use incremental builder
	 */
	NONE,
	/**
	 * Append incremental results to previous results
	 */
	APPEND,
	/**
	 * Replace initial results with subsequent results
	 */
	REPLACE
}
