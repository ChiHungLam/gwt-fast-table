package com.jwh.gwt.fasttable.client.selection;

import java.util.ArrayList;
import java.util.HashSet;

import com.jwh.gwt.fasttable.client.exception.AbortOperation;

public class SelectionTracker<T> {
	final HashSet<T> selections = new HashSet<T>();

	/**
	 * All listeners with be advised of all selection events
	 */
	final ArrayList<SelectionListener<T>> selectionListeners = new ArrayList<SelectionListener<T>>();

	/**
	 * Add a listener who will be notified of all selection events
	 * 
	 * @param selectionListener
	 */
	public void addSelectionListener(SelectionListener<T> selectionListener) {
		this.selectionListeners.add(selectionListener);
	}

	/**
	 * @return all selections
	 */
	public HashSet<T> getSelections() {
		return selections;
	}

	/**
	 * Useful for multiple selection. Select an object, or unselect if already
	 * selected.
	 * 
	 * @param object
	 * @param selectionListener
	 *            TODO
	 * @return true if newly selected, false if unselected
	 * @throws AbortOperation
	 *             if some cleanup work is required first
	 */
	public boolean multiSelect(T object, SelectionListener<T> selectionListener) throws AbortOperation {
		final boolean removed = unselect(object, selectionListener);
		if (!removed) {
			select(object, selectionListener);
		}
		return !removed;
	}

	/**
	 * @param object
	 * @param selectionListener
	 *            TODO
	 * @return true if the item is newly added
	 */
	private boolean select(T object, SelectionListener<T> selectionListener) {
		final boolean added = selections.add(object);
		selectionListener.select(object);
		for (final SelectionListener<T> listener : selectionListeners) {
			listener.select(object);
		}
		return added;
	}

	/**
	 * Use for single selection. Select an object, unselect any current
	 * selection (unless it is the object)
	 * 
	 * @param object
	 *            To be selected
	 * @param listener
	 *            TODO
	 * @return any existing selection, or null if none
	 * @throws AbortOperation
	 */
	public T singleSelect(T object, SelectionListener<T> selectionListener) throws AbortOperation {
		T existing = null;
		if (!selections.isEmpty()) {
			existing = new ArrayList<T>(selections).get(0);
		}
		if (object == existing) {
			selectionListener.reselect(object);
			for (final SelectionListener<T> listener : selectionListeners) {
				listener.reselect(object);
			}
		} else {
			unselect(existing, selectionListener);
			select(object, selectionListener);
		}
		return existing;
	}

	/**
	 * @param object
	 * @param selectionListener
	 *            TODO
	 * @return true if the item was removed
	 * @throws AbortOperation
	 *             If some cleanup work is required first
	 */
	private boolean unselect(T object, SelectionListener<T> selectionListener) throws AbortOperation {
		selectionListener.aboutToUnselect(object);
		for (final SelectionListener<T> listener : selectionListeners) {
			listener.aboutToUnselect(object);
		}
		final boolean removed = selections.remove(object);
		selectionListener.unselect(object);
		for (final SelectionListener<T> listener : selectionListeners) {
			listener.unselect(object);
		}
		return removed;
	}
}