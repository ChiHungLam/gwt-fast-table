package com.jwh.gwt.design.client;

import com.google.gwt.user.client.Element;
import com.jwh.gwt.html.shared.Attribute;

public class InPlaceEditor {

	final private Element element;
	final boolean updateSample;

	public InPlaceEditor(Element element) {
		this.element = element;
		updateSample = element.getInnerText() != null && element.getInnerText().equalsIgnoreCase(element.getAttribute(Attribute.SAMPLE));
		System.err.println("Implement in place editor");
	}

}
