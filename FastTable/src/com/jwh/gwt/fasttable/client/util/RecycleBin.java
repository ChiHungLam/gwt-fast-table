package com.jwh.gwt.fasttable.client.util;

import com.jwh.gwt.fasttable.client.exception.NotFound;

public class RecycleBin<T> {

	final T[] contents;
	int index = 0;

	public RecycleBin(T[] contents) {
		super();
		this.contents = contents;
	}

	public T get() throws NotFound {
		try {
			final T answer = contents[index];
			contents[index++] = null;
			return answer;
		} catch (IndexOutOfBoundsException e) {
			throw new NotFound();
		}
	}

	public void put(T recycled) {
		try {
			index--;
			contents[index] = recycled;
		} catch (IndexOutOfBoundsException e) {
			index++;
		}
	}
}
