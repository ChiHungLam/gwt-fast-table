package com.jwh.gwt.fasttable.client.util;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.jwh.gwt.fasttable.client.element.HtmlFactory;
import com.jwh.gwt.fasttable.client.element.HtmlFactory.HtmlElement;
import com.jwh.gwt.fasttable.client.element.HtmlFactory.Tag;

/**
 * Utility to quickly layout common GUI elements in an HTML table
 * 
 * @author jheyne
 * 
 */
public class LabelValueUtil {

	private HtmlElement currentRow = null;
	public final HtmlElement table = HtmlFactory.forRoot(Tag.table);

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

	/**
	 * @param label
	 *            Text shown on the button
	 * @return the id (for DOM access)
	 */
	public String checkBox(String group, String value, String label, boolean selected) {
		final String id = IdGenerator.getNextId();
		final HtmlElement cell = newCell().setId(id).setStyle(Style.CHECK_BOX, Style.BORDER_NONE);
		cell.addAttribute("type", "checkbox");
		cell.addAttribute("name", group);
		cell.addAttribute("value", value);
		if (selected) {
			cell.addAttribute("checked", "checked");
		}
		applyPreparedAttributes(cell);
		cell.addContents(label);
		return id;
	}
	
	/**
	 * @param label
	 *            Text shown on the button
	 * @return the id (for DOM access)
	 */
	public String radioButton(String group, String value, String label, boolean selected) {
		final String id = IdGenerator.getNextId();
		final HtmlElement cell = newCell().setId(id).setStyle(Style.RADIO_BUTTON, Style.BORDER_NONE);
		cell.addAttribute("type", "radio");
		cell.addAttribute("name", group);
		cell.addAttribute("value", value);
		if (selected) {
			cell.addAttribute("checked", "checked");
		}
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

	private HtmlElement getCurrentRow() {
		if (currentRow == null) {
			currentRow = table.addChild(Tag.tr);
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
		final HtmlElement first = newCell();
		first.setStyle(Style.LABEL, Style.BORDER_NONE);
		first.addChild(Tag.label).addAttribute("for", id).setStyle(Style.LABEL).addContents(label).cleanup();
		newCell().setId(id).setStyle(Style.VALUE_READ_ONLY, Style.BORDER_NONE).addContents(value);
		return id;
	}

	private HtmlElement newCell() {
		return getCurrentRow().addChild(Tag.td);
	}

	public void newRow() {
		currentRow = table.addChild(Tag.tr);
	}

	public String toHtml() {
		return table.toHtml();
	}
}
