package com.jwh.gwt.fasttable.client.selection;

import com.jwh.gwt.fasttable.client.exception.AbortOperation;

public class SelectionListener<T> {

	public void aboutToUnselect(T object) throws AbortOperation {
		
	}
	
	public void select(T object) {
		
	}
	public void reselect(T object) {
		
	}

	public void unselect(T object) {
		
	}
	
	/**
	 * @param listener
	 * @return A selectionListener which passes events to both listeners
	 */
	public SelectionListener<T> append(final SelectionListener<T> listener) {
		return new SelectionListener<T>() {
			public void aboutToUnselect(T object) throws AbortOperation {
				SelectionListener.this.aboutToUnselect(object);
				listener.aboutToUnselect(object);
			};
			public void reselect(T object) {
				SelectionListener.this.reselect(object);
				listener.reselect(object);
			};
			public void select(T object) {
				SelectionListener.this.select(object);
				listener.select(object);};
			public void unselect(T object) {
				SelectionListener.this.unselect(object);
				listener.unselect(object);};
		};
	}
}
