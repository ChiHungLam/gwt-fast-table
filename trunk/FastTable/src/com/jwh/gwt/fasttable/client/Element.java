/**
 * Copyright (c) 2011 Jim Heyne. All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which is 
 * available at {@link http://www.eclipse.org/legal/epl-v10.html}
 */

package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * A simple API for programmatically constructing HTML. Designed for efficiency,
 * the programmatic specs are converted to String format immediately. This means
 * that the programmer needs to be careful about constructing the HTML in a
 * reasonable order. Correct ordering is enforced: an assert error will be
 * raised if things are amiss.
 * 
 * @author jheyne
 */
public abstract class Element {

	enum State {
		StartTag, Contents, EndTag
	}

	final StringBuilder builder;

	State currentState = State.StartTag;

	public Element(StringBuilder builder) {
		super();
		this.builder = builder;
		this.builder.append('<');
		this.builder.append(getTag());
		this.builder.append(' ');
	}

	/**
	 * Add an attribute to the opening tag
	 * 
	 * @param key
	 *            Attribute name
	 * @param value
	 *            Attribute value
	 * @return The receiver
	 */
	public Element addAttribute(String key, String value) {
		assert isAppropriate(State.StartTag) : "attributes should be added before "
				+ currentState;
		builder.append(key);
		builder.append("=\"");
		builder.append(value);
		builder.append("\" ");
		return this;
	}

	/**
	 * Add content to the tag. Uses @see SafeHtmlUtils to mitigate security
	 * risks.
	 * 
	 * @param contents
	 *            The text to add.
	 * @return The receiver
	 */
	public Element addContents(String contents) {
		assert isAppropriate(State.Contents) : "contents should be set before "
				+ currentState;
		if (currentState != State.Contents) {
			builder.append('>');
		}
		currentState = State.Contents;
		if (contents != null) {
			builder.append(SafeHtmlUtils.fromString(contents).asString());
		}
		return this;
	}

	/**
	 * @param wrapper
	 *            Encapsulates what we need to know to find the handler when an
	 *            event occurs
	 * @param objectId
	 *            Identifier for the domain model instance
	 * @param field
	 *            The domain model field to which the handler corresponds
	 * @return
	 */
	public Element addHandler(CellHandlerWrapper<?> wrapper, String objectId,
			String field) {
		assert isAppropriate(State.StartTag) : "attributes should be added before "
				+ currentState;
		for (String onEvent : wrapper.onEvents) {
			builder.append(onEvent);
			builder.append("=\"window.fctn");
			builder.append(wrapper.functionId);
			builder.append("('");
			builder.append(wrapper.id);
			builder.append("','");
			builder.append(objectId);
			builder.append("','");
			builder.append(field);
			builder.append("','");
			builder.append(onEvent);
			builder.append("')\" ");
		}
		return this;
	}

	/**
	 * Assure that the entire element is manifested as a string
	 */
	public void cleanup() {
		if (currentState != State.EndTag) {
			builder.append("</");
			builder.append(getTag());
			builder.append('>');
		}
		currentState = State.EndTag;
	}

	/**
	 * @return The element's HTML tag
	 */
	public abstract String getTag();

	/**
	 * assure that components are not written in the wrong order
	 * 
	 * @param state
	 * @return true if the operation is appropriate now
	 */
	private boolean isAppropriate(State state) {
		return currentState.compareTo(state) <= 0;
	}

	/**
	 * Set the id attribute
	 * @param id
	 * @return
	 */
	public Element setId(String id) {
		addAttribute("id", id);
		return this;
	}

	/**
	 * Define a single style. Mutually exclusive with @see Element.setStyles. 
	 * @param style The class attribute
	 * @return The receiver
	 */
	public Element setStyle(String style) {
		assert isAppropriate(State.StartTag) : "style should be set before "
				+ currentState;
		return addAttribute("class", style);
	}

	/**
	 * Define multiple styles. Mutually exclusive with @see Element.setStyle. 
	 * @param styles
	 * @return
	 */
	public Element setStyles(ArrayList<String> styles) {
		assert isAppropriate(State.StartTag) : "style should be set before "
				+ currentState;
		if (!styles.isEmpty()) {
			builder.append("class=\"");
			for (final String style : styles) {
				builder.append(style);
				builder.append(' ');
			}
			builder.deleteCharAt(builder.length() - 1);
			builder.append("\" ");
		}
		return this;
	}
}
