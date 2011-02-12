package com.jwh.gwt.fasttable.client.util;

import com.jwh.gwt.fasttable.client.element.Cell;
import com.jwh.gwt.fasttable.client.element.Row;
import com.jwh.gwt.fasttable.client.element.Table;

/**
 * Utility to quickly layout common GUI elements in an HTML table
 * @author jheyne
 *
 */
public class LabelValueUtil {

	private Row currentRow = null;
	private final Table<Object> table = new Table<Object>();

	/**
	 * @param label
	 *            Text shown on the button
	 * @return the id (for DOM access)
	 */
	public String button(String label) {
		final String id = IdGenerator.getNextId();
		newCell().setId(id).setStyle(Style.BUTTON).addContents(label);
		return id;
	}

	private Row getCurrentRow() {
		if (currentRow == null) {
			currentRow = table.newRow();
		}
		return currentRow;
	}

	/**
	 * Add a label/value pair
	 * 
	 * @param label
	 * @param value
	 * @return the value id (for DOM access)
	 */
	public String labelValue(String label, String value) {
		final String id = IdGenerator.getNextId();
		final Cell first = newCell();
		first.setStyle(Style.LABEL, Style.BORDER_NONE);
		first.newLabel().addAttribute("for", id).setStyle(Style.LABEL).addContents(label).cleanup();
		newCell().setId(id).setStyle(Style.VALUE_READ_ONLY, Style.BORDER_NONE).addContents(value);
		return id;
	}

	private Cell newCell() {
		return getCurrentRow().newCell();
	}

	public void newRow() {
		currentRow = table.newRow();
	}

	public String toHtml() {
		return table.toString();
	}
}
