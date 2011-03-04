package com.jwh.gwt.fasttable.client;

public class ConfigurationIE {

	public int initialIncrement = 50;
	public int subsequentIncrement = Integer.MAX_VALUE;
	public IncrementalStrategy incrementalStrategy = IncrementalStrategy.REPLACE;

	public int getInitialIncrement() {
		return initialIncrement;
	}

	public int getSubsequentIncrement() {
		return subsequentIncrement;
	}

	public IncrementalStrategy getIncrementalStrategy() {
		return incrementalStrategy;
	}
}
