/**
 * Copyright (c) 2011 Jim Heyne. All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which is 
 * available at {@link http://www.eclipse.org/legal/epl-v10.html}
 */

package com.jwh.gwt.html.shared.event;

import java.util.ArrayList;

import com.jwh.gwt.html.shared.event.CellEvent.OnEvent;

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
	public final CellListener<T> cellListener;

	/**
	 * Unique identifier
	 */
	public final Integer id;
	/**
	 * All the events for which the handler applies
	 */
	public final ArrayList<OnEvent> onEvents = new ArrayList<OnEvent>();
	public String functionId;

	public CellHandlerWrapper(final CellListener<T> cellListener, final OnEvent... onEvents) {
		this.id = currentId++;
		this.cellListener = cellListener;
		for (final OnEvent event : onEvents) {
			this.onEvents.add(event);
		}

	}

	/**
	 * @param functionId
	 *            Identifies the corresponding JSNI callback function to use. @see
	 *            Table.defineCellHandler
	 */
	public void setFunctionId(final String functionId) {
		this.functionId = functionId;
	}

}
