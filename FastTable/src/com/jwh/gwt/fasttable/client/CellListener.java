/**
 * Copyright (c) 2011 Jim Heyne. All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which is 
 * available at {@link http://www.eclipse.org/legal/epl-v10.html}
 */

package com.jwh.gwt.fasttable.client;

/**
 * @author jheyne
 * Serves as a callback handler for events
 * @param <T> The class of the model object shown in a table
 */
public interface CellListener<T> {

	/**
	 * @param object The model object affected by the event
	 * @param event The event which was triggered
	 * @param field The column/model field indicator
	 * @param refId The id of the table row
	 */
	public void handleCellEvent(T object, String event, String field, String refId);
}
