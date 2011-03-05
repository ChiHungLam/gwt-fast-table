package com.jwh.gwt.fasttable.client.selection;

import com.jwh.gwt.html.shared.exception.AbortOperation;


public class SelectionListener<T> {

	/**
	 * @param object the object that is about to be unselected
	 * @throws AbortOperation when we are not ready to change the selection (for example, if there are unsaved changes)
	 */
	public void aboutToUnselect(T object) throws AbortOperation {

	}

	/**
	 * @param listener
	 * @return A selectionListener which passes events to both listeners
	 */
	public SelectionListener<T> append(final SelectionListener<T> listener) {
		return new SelectionListener<T>() {
			@Override
			public void aboutToUnselect(T object) throws AbortOperation {
				SelectionListener.this.aboutToUnselect(object);
				listener.aboutToUnselect(object);
			};

			@Override
			public void reselect(T object) {
				SelectionListener.this.reselect(object);
				listener.reselect(object);
			};

			@Override
			public void select(T object) {
				SelectionListener.this.select(object);
				listener.select(object);
			};

			@Override
			public void unselect(T object) {
				SelectionListener.this.unselect(object);
				listener.unselect(object);
			};
		};
	}

	/**
	 * @param object The currently selected object (which is being reselected)
	 */
	public void reselect(T object) {

	}

	/**
	 * @param object The newly selected object
	 */
	public void select(T object) {

	}

	/**
	 * @param object The previously selected objected
	 */
	public void unselect(T object) {

	}
}
