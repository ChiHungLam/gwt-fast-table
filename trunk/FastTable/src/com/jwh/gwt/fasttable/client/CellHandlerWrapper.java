/**
 * Copyright (c) 2011 Jim Heyne. All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which is 
 * available at {@link http://www.eclipse.org/legal/epl-v10.html}
 */

package com.jwh.gwt.fasttable.client;

import java.util.ArrayList;

/**
 * @author jheyne Does the bookkeeping required for invoking event handles
 * @param <T>
 */
public class CellHandlerWrapper<T> {

	/**
	 * A counter used to assure that each wrapper has a unique identifier
	 */
	static Integer currentId = 0;

	/**
	 * @see CellListener
	 */
	final CellListener<T> cellListener;

	/**
	 * Unique identifier
	 */
	final Integer id;
	/**
	 * All the events for which the handler applies
	 */
	public final ArrayList<String> onEvents = new ArrayList<String>();
	int functionId;

	public CellHandlerWrapper(String onEvent, CellListener<T> cellListener) {
		this.id = currentId++;
		this.cellListener = cellListener;
		this.onEvents.add(onEvent);

	}

	/**
	 * @param functionId
	 *            Identifies the corresponding JSNI callback function to use. @see
	 *            Table.defineCellHandler
	 */
	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}

	/**
	 * The same handler can be used to respond to multiple events. This helps
	 * mitigate the 4 handler limit.
	 * 
	 * @param onEvent
	 *            An event the handler should respond to
	 */
	public void addEvent(String onEvent) {
		onEvents.add(onEvent);
	}

}
