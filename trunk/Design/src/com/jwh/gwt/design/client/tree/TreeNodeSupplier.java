package com.jwh.gwt.design.client.tree;

import com.google.gwt.user.client.ui.TreeItem;

/**
 * Callback to set tree contents (label, widget, etc)
 * 
 * @author jheyne
 * 
 * @param <T>
 */
public interface TreeNodeSupplier<T> {

	/**
	 * Set the contents of the tree item for the underlying object
	 * 
	 * @param item
	 *            set my contents
	 * @param obj
	 *            source for contents
	 */
	public void populate(TreeItem item, T obj);

	/**
	 * Update the display for the obj
	 * 
	 * @param item
	 * @param obj
	 */
	public void update(TreeItem item, T obj);
}
