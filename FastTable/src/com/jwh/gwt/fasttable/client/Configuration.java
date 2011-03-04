package com.jwh.gwt.fasttable.client;

public class Configuration {

	public int initialIncrement = 50;
	public int subsequentIncrement = 50;
	public IncrementalStrategy incrementalStrategy = IncrementalStrategy.APPEND;

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
