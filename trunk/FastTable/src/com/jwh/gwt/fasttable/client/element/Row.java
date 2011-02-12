/**
 * Copyright (c) 2011 Jim Heyne. All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which is 
 * available at {@link http://www.eclipse.org/legal/epl-v10.html}
 */

package com.jwh.gwt.fasttable.client.element;

/**
 * Used for constructing an HTML row element
 * 
 * @author jheyne
 */
public class Row extends HtmlElement {

	/**
	 * Cache the current cell for it can be cleaned up if a new cell is added or
	 * the row ends
	 */
	Cell currentCell;

	public Row(StringBuilder builder) {
		super(builder);
	}

	@Override
	public void cleanup() {
		if (currentCell != null) {
			currentCell.cleanup();
			currentCell = null;
		}
		super.cleanup();
	}

	@Override
	public String getTag() {
		return "tr";
	}

	/**
	 * @return a new cell after cleaning up any current cell
	 */
	public Cell newCell() {
		closeOpeningTag();
		addContents("");
		if (currentCell != null) {
			currentCell.cleanup();
		}
		return currentCell = new Cell(builder);
	}
}