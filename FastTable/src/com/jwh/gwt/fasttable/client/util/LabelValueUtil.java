package com.jwh.gwt.fasttable.client.util;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.jwh.gwt.fasttable.client.element.Cell;
import com.jwh.gwt.fasttable.client.element.HtmlElement;
import com.jwh.gwt.fasttable.client.element.Row;
import com.jwh.gwt.fasttable.client.element.Table;

/**
 * Utility to quickly layout common GUI elements in an HTML table
 * 
 * @author jheyne
 * 
 */
public class LabelValueUtil {

	private Row currentRow = null;
	private final Table<Object> table = new Table<Object>();

	final HashMap<String, String> preparedAttributes = new HashMap<String, String>();

	/**
	 * Experimental. Prepare some attributes to be consumed by the subsequent
	 * widget created. Only implemented for button so far.
	 * 
	 * @param key
	 * @param value
	 */
	public void prepareAttribute(String key, String value) {
		preparedAttributes.put(key, value);
	}

	/**
	 * @param label
	 *            Text shown on the button
	 * @return the id (for DOM access)
	 */
	public String button(String label) {
		final String id = IdGenerator.getNextId();
		final HtmlElement cell = newCell().setId(id).setStyle(Style.BUTTON, Style.BORDER_NONE);
		applyPreparedAttributes(cell);
		cell.addContents(label);
		return id;
	}

	private void applyPreparedAttributes(HtmlElement cell) {
		for (Entry<String, String> entry : preparedAttributes.entrySet()) {
			cell.addAttribute(entry.getKey(), entry.getValue());
		}
		preparedAttributes.clear();
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
