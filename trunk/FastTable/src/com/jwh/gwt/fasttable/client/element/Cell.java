/**
 * Copyright (c) 2011 Jim Heyne. All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which is 
 * available at {@link http://www.eclipse.org/legal/epl-v10.html}
 */

package com.jwh.gwt.fasttable.client.element;

/**
 * Used for constructing an HTML table cell element
 * 
 * @author jheyne
 */
@Deprecated
public class Cell extends HtmlElement {

	public Cell(StringBuilder builder) {
		super(builder);
	}

	@Override
	public String getTag() {
		return "td";
	}
	
	public GenericElement newLabel() {
		closeOpeningTag();
		return GenericElement.getLabel(builder);
	}

}