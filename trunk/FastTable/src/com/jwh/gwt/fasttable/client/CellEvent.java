package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableRowElement;
import com.jwh.gwt.fasttable.client.exception.NotFound;

public class CellEvent<T> {
	public enum OnEvent {
		onClick, onDblClick, onKeyDown, onKeyPress, onKeyUp, onFocus, onBlur, onChange, onMouseDown, onMouseMove, onMouseOut, onMouseOver, onMouseUp, unknown
	}

	/**
	 * one-based. First column is 1.
	 */
	public int column;
	public String eventIdentifier;
	String refId;
	public T domainObject;

	public CellEvent(String eventIdentifier, T domainObject, String refId, int column) {
		super();
		this.eventIdentifier = eventIdentifier;
		this.domainObject = domainObject;
		this.refId = refId;
		this.column = column;
	}

	public ArrayList<Element> getAllColumnElements(Document document) throws NotFound {
		final ArrayList<Element> answer = new ArrayList<Element>();
		final Element rowElement = getRowElement(document);
		Element currentElement = rowElement.getFirstChildElement();
		while (currentElement != null) {
			answer.add(currentElement);
			currentElement = currentElement.getNextSiblingElement();
		}
		if (answer.isEmpty()) {
			throw new NotFound();
		}
		return answer;
	}

	public Element getColumnElement(Document document) throws NotFound {
		final ArrayList<Element> all = getAllColumnElements(document);
		if (column > all.size()) {
			throw new NotFound();
		}
		return all.get(column - 1);
	}

	public T getDomainObject() {
		return domainObject;
	}

	public OnEvent getOnEvent() {
		try {
			return Enum.valueOf(OnEvent.class, eventIdentifier);
		} catch (final IllegalArgumentException e) {
			return OnEvent.unknown;
		} catch (final NullPointerException e) {
			return OnEvent.unknown;
		}
	}

	public String getRefId() {
		return refId;
	}

	public Element getRowElement(Document document) throws NotFound {
		final Element row = document.getElementById(refId);
		if (row == null) {
			throw new NotFound();
		}
		return row;
	}

	public TableRowElement insertRowAfter(Document document) throws NotFound {
		final Element row = getRowElement(document);
		final TableRowElement newRow = document.createTRElement();
		row.getParentElement().insertAfter(newRow, row);
		return newRow;
	}
}
