/**
 * Copyright (c) 2011 Jim Heyne. All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which is 
 * available at {@link http://www.eclipse.org/legal/epl-v10.html}
 */

package com.jwh.gwt.fasttable.client.element;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.jwh.gwt.fasttable.client.CellEvent;
import com.jwh.gwt.fasttable.client.CellHandlerWrapper;
import com.jwh.gwt.fasttable.client.CellListener;
import com.jwh.gwt.fasttable.client.CellEvent.OnEvent;
import com.jwh.gwt.fasttable.client.exception.NotFound;
import com.jwh.gwt.fasttable.client.util.IdGenerator;
import com.jwh.gwt.fasttable.client.util.Style;

public class Table<T> extends HtmlElement {

	@Deprecated
	static int instanceCounter = 1;

	/**
	 * Used for generating unique row ids
	 */
	int currentId = 0;

	/**
	 * Cache the current row so it can be cleaned up if a new row is added, or
	 * we are done adding rows.
	 */
	Row currentRow;

	/**
	 * Table to locate cell handlers
	 */
	private final HashMap<Integer, CellHandlerWrapper<T>> handlerLookup = new HashMap<Integer, CellHandlerWrapper<T>>();

	/**
	 * Identifier for the table. Each table on a page should have a unique
	 * identifier. It is used as a prefix for HTML id attributes, so it must
	 * begin with a alphanumeric character, and brevity is encouraged.
	 */
	private final String identifier;

	/**
	 * Table to locate model objects for cell event handlers
	 */
	private final HashMap<String, T> lookup = new HashMap<String, T>();

	public Table() {
		super(new StringBuilder());
		this.identifier = IdGenerator.getNextId();
		setId(getId());
		setStyle(Style.BORDER_NONE);
	}

	public void cleanupCurrentRow() {
		if (currentRow != null) {
			currentRow.cleanup();
			currentRow = null;
		}
	}

	/**
	 * A little magic happens here. All cell event handlers call a function
	 * named "fnct"[+digit]. Because GWT will obfuscate function names, we must
	 * dynamically generate the expected function using JSNI.
	 * 
	 * @return
	 */
	private int defineCellHandler() {
		final int current = instanceCounter++;
		// assert current <= 3 :
		// "currently only 4 cell handlers are supported per page";
		switch (current) {
		case 0:
			defineCellHandler0(this);
			return 0;
			// case 1:
			// defineCellHandler1(this);
			// return 1;
			// case 2:
			// defineCellHandler2(this);
			// return 2;
			// case 3:
			// defineCellHandler3(this);
			// return 3;
		default:
			// should never get here
			defineCellHandler0(this);
			return 0;
		}
	}

	public native void defineCellHandler0(Table<T> x)/*-{
														if($wnd.fctn0) return;
														$wnd.fctn0 = function (stubId, objectId, event, columnIndex) {
														x.@com.jwh.gwt.fasttable.client.element.Table::handleCellEvent(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)(stubId,objectId,event,columnIndex); 
														};
														}-*/
	;

	/**
	 * @return The element id for DOM manipulation
	 */
	public String getId() {
		return identifier + "_table";
	}

	// public native void defineCellHandler1(Table<T> x)/*-{
	// $wnd.fctn1 = function (stubId, objectId, field, event) {
	// x.@com.jwh.gwt.fasttable.client.Table::handleCellEvent(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(stubId,objectId,field,event);
	// };
	// }-*/
	// ;
	//
	// public native void defineCellHandler2(Table<T> x)/*-{
	// $wnd.fctn2 = function (stubId, objectId, field, event) {
	// x.@com.jwh.gwt.fasttable.client.Table::handleCellEvent(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(stubId,objectId,field,event);
	// };
	// }-*/
	// ;
	//
	// public native void defineCellHandler3(Table<T> x)/*-{
	// $wnd.fctn3 = function (stubId, objectId, field, event) {
	// x.@com.jwh.gwt.fasttable.client.Table::handleCellEvent(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(stubId,objectId,field,event);
	// };
	// }-*/
	// ;

	public String getRefId(T object) throws NotFound {
		final Set<Entry<String, T>> entrySet = lookup.entrySet();
		for (final Entry<String, T> entry : entrySet) {
			if (entry.getValue() == object) {
				return entry.getKey();
			}
		}
		throw new NotFound();
	}

	@Override
	public String getTag() {
		return "table";
	}

	/**
	 * @param wrapperId
	 *            Identifies the cell handler wrapper
	 * @param objectId
	 *            Identifies the model object, and is the row element refId
	 * @param field
	 *            Identifies the column (by model field) which experienced the
	 *            event
	 * @param event
	 *            The event which was triggered
	 */
	public void handleCellEvent(String wrapperId, String objectId, String event, int columnIndex) {
		final T t = lookup.get(objectId);
		final CellHandlerWrapper<T> cellHandlerWrapper = handlerLookup.get(Integer.valueOf(wrapperId));
		final CellEvent<T> cellEvent = new CellEvent<T>(event, t, objectId, columnIndex);
		cellHandlerWrapper.cellListener.handlerCellEvent(cellEvent);
	}

	/**
	 * @return a new row, first cleaning up any existing row
	 */
	public Row newRow() {
		closeOpeningTag();
		if (currentRow != null) {
			currentRow.cleanup();
		}
		return currentRow = new Row(builder);
	}

	/**
	 * @return a unique row identifier for this table
	 */
	private int nextID() {
		return currentId++;
	}

	/**
	 * Associate the model object with the row
	 * 
	 * @param object
	 *            The model object represented in a row
	 * @param row
	 *            The row
	 * @return The unique identifier used as a row refId and key to finding the
	 *         model object for event handler callbacks
	 */
	public String register(T object, Row row) {
		final String id = identifier + nextID();
		lookup.put(id, object);
		row.setId(id);
		return id;
	}

	/**
	 * @param cellListener
	 *            Callback which responds to the event.
	 * @param onEvent
	 *            The event ("onClick", "onBlur") for which to listen
	 * @return a wrapper which encapsulates all handler details, and which has
	 *         been cached for later retrieval
	 */
	public CellHandlerWrapper<T> registerCellHandler(CellListener<T> cellListener, OnEvent... onEvent) {
		final CellHandlerWrapper<T> wrapper = new CellHandlerWrapper<T>(cellListener, onEvent);
		handlerLookup.put(wrapper.id, wrapper);
		wrapper.setFunctionId(defineCellHandler());
		return wrapper;
	}

	/**
	 * prepare to be reused to accommodate a change in the displayed objects
	 */
	public void reset() {
		builder.setLength(0);
		writeOpeningTag();
		currentRow = null;
		lookup.clear();
		currentState = State.StartTag;
	}

	@Override
	public String toString() {
		cleanupCurrentRow();
		cleanup();
		return builder.toString();
	}

}