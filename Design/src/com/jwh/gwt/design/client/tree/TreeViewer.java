package com.jwh.gwt.design.client.tree;

import java.util.HashMap;

import com.google.gwt.user.client.ui.HasTreeItems;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.jwh.gwt.html.shared.exception.NotFound;

public class TreeViewer<T> {

	final TreeNodeSupplier<T> supplier;

	final HashMap<T, TreeItem> lookup = new HashMap<T, TreeItem>();

	final Tree tree;

	public TreeViewer(final Tree tree, final TreeNodeSupplier<T> supplier) {
		super();
		this.tree = tree;
		this.supplier = supplier;
	}

	/**
	 * Add an item for the child in the parent
	 * 
	 * @param child
	 * @param parent
	 */
	public void add(final T child, final T parent) {
		getItem(child, parent);
	}

	private TreeItem createItem(final T child, final HasTreeItems parent) {
		final TreeItem childItem = new TreeItem();
		supplier.populate(childItem, child);
		parent.addItem(childItem);
		return childItem;
	}

	private TreeItem getItem(final T child) throws NotFound {
		final TreeItem treeItem = lookup.get(child);
		if (treeItem == null) {
			throw NotFound.getInstance();
		}
		return treeItem;
	}

	/**
	 * @param child
	 * @param parent
	 * @return an existing item or create it if not found
	 */
	private TreeItem getItem(final T child, final T parent) {
		try {
			return getItem(child);
		} catch (final NotFound e) {
			if (parent == null) {
				return createItem(child, tree);
			}
			try {
				final HasTreeItems parentItem = getItem(parent);
				return createItem(child, parentItem);
			} catch (final NotFound e1) {
				final HasTreeItems parentItem = createItem(parent, tree);
				return createItem(child, parentItem);
			}
		}
	}

	/**
	 * remove the child's item for the parent
	 * 
	 * @param child
	 * @param parent
	 */
	public void remove(final T child, final T parent) {
		try {
			final TreeItem childItem = getItem(child);
			final TreeItem parentItem = getItem(parent);
			parentItem.removeItem(childItem);
		} catch (final NotFound e) {
		}
	}

	public void select(final T child) throws NotFound {
		final TreeItem childItem = getItem(child);
		childItem.setSelected(true);
	}

	public void update(final T child) throws NotFound {
		final TreeItem childItem = getItem(child);
		supplier.update(childItem, child);
	}
}
