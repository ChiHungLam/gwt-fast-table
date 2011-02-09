package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

public class CellEvent<T> {
	public enum OnEvent {onClick, onDblClick, onKeyDown, onKeyPress, onKeyUp,onFocus, onBlur, onChange, onMouseDown, onMouseMove, onMouseOut, onMouseOver, onMouseUp, unknown}
	/**
	 * one-based. First column is 1.
	 */
	public int column;
	public String eventIdentifier;
	String refId;
	public T domainObject;
	
	public CellEvent(String eventIdentifier, T domainObject, String refId,
			int column) {
		super();
		this.eventIdentifier = eventIdentifier;
		this.domainObject = domainObject;
		this.refId = refId;
		this.column = column;
	}
	public OnEvent getOnEvent() {
		try {
			return OnEvent.valueOf(OnEvent.class, eventIdentifier);
		} catch (IllegalArgumentException e) {
			return OnEvent.unknown;
		} catch (NullPointerException e) {
			return OnEvent.unknown;
		}
	}
	public Element getRowElement(Document document) throws ElementNotFound {
		Element row = document.getElementById(refId);
		if (row == null) {
			throw new ElementNotFound();
		}
		return row;
	}
	public ArrayList<Element> getAllColumnElements(Document document) throws ElementNotFound {
		ArrayList<Element> answer = new ArrayList<Element>();
		Element rowElement = getRowElement(document);
		Element currentElement = rowElement.getFirstChildElement();
		while (currentElement != null) {
			answer.add(currentElement);
			currentElement = currentElement.getNextSiblingElement();
		}
		if (answer.isEmpty()) {
			throw new ElementNotFound();
		}
		return answer;
	}
	public Element getColumnElement(Document document) throws ElementNotFound {
		ArrayList<Element> all = getAllColumnElements(document);
		if (column > all.size()) {
			throw new ElementNotFound();
		}
		return all.get(column - 1);
	}
	public T getDomainObject() {
		return domainObject;
	} 
}
