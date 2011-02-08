/**
 * Copyright (c) 2011 Jim Heyne. All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which is 
 * available at {@link http://www.eclipse.org/legal/epl-v10.html}
 */

package com.jwh.gwt.fasttable.sample.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.jwh.gwt.fasttable.client.CellEvent;
import com.jwh.gwt.fasttable.client.CellHandlerWrapper;
import com.jwh.gwt.fasttable.client.CellListener;
import com.jwh.gwt.fasttable.client.ElementNotFound;
import com.jwh.gwt.fasttable.client.Row;
import com.jwh.gwt.fasttable.client.Table;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FastTableSample implements EntryPoint, Style {

	private static final String ON_MOUSE_OUT = "onMouseOut";
	private static final String ON_MOUSE_OVER = "onMouseOver";
	private static final String ON_CLICK = "onClick";

	@Override
	public void onModuleLoad() {
		final Element body = RootPanel.getBodyElement();
		final Table<SampleModel> table = new Table<SampleModel>();
		CellListener<SampleModel> cellListener = buildCellListener(body);
		final CellHandlerWrapper<SampleModel> cellHandler = table
				.registerCellHandler(ON_CLICK, cellListener);
		// note that the same call handler can handle multiple events
		cellHandler.addEvent(ON_MOUSE_OVER);
		cellHandler.addEvent(ON_MOUSE_OUT);
		// get a bunch of model objects
		final ArrayList<SampleModel> samples = SampleModel.getSamples(1000);
		// add rows and cells for the model objects
		for (final SampleModel sample : samples) {
			final Row row = table.newRow();
			// associate the model object with the row
			final String objectId = table.register(sample, row);
			// add style info, an event handler, and set the contents
			// here we only add the event handler to NAME and ZIP
			row.newCell().setStyle(NAME)
					.addHandler(cellHandler, objectId, 0)
					.addContents(sample.name);
			row.newCell().setStyle(STREET).addContents(sample.street);
			row.newCell().setStyle(CITY).addContents(sample.city);
			row.newCell().setStyle(STATE).addContents(sample.state);
			row.newCell().setStyle(ZIP).addHandler(cellHandler, objectId, 4)
					.addContents(sample.zip);
		}
		final String html = table.toString();
		// show the table
		body.setInnerHTML(html);
	}

	private CellListener<SampleModel> buildCellListener(final Element body) {
		return new CellListener<SampleModel>() {

			@Override
			public void handlerCellEvent(CellEvent<SampleModel> event) {
				switch (event.getOnEvent()) {
				case onClick:
					Window.alert("Event: " + event.getOnEvent() + "\nObject: "
							+ event.domainObject.toString() + "\nColumn: " + event.column);
					break;
				case onMouseOver:
					try {
						event.getColumnElement(body.getOwnerDocument()).addClassName(HIGHLIGHT);
					} catch (ElementNotFound e1) {
					}
					break;
				case onMouseOut:
					try {
						event.getColumnElement(body.getOwnerDocument()).removeClassName(HIGHLIGHT);
					} catch (ElementNotFound e1) {
					}
					break;
				default:
					break;
				}
				
			}
		};
	}

}
