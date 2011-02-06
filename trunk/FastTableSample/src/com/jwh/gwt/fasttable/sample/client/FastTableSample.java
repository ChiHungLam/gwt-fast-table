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
import com.jwh.gwt.fasttable.client.CellHandlerWrapper;
import com.jwh.gwt.fasttable.client.CellListener;
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
		final Table<SampleModel> table = new Table<SampleModel>("Simple");
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
					.addHandler(cellHandler, objectId, NAME)
					.addContents(sample.name);
			row.newCell().setStyle(STREET).addContents(sample.street);
			row.newCell().setStyle(CITY).addContents(sample.city);
			row.newCell().setStyle(STATE).addContents(sample.state);
			row.newCell().setStyle(ZIP).addHandler(cellHandler, objectId, ZIP)
					.addContents(sample.zip);
		}
		final String html = table.toString();
		// show the table
		body.setInnerHTML(html);
	}

	private CellListener<SampleModel> buildCellListener(final Element body) {
		return new CellListener<SampleModel>() {
			@Override
			public void handleCellEvent(SampleModel object, String event,
					String field, String refId) {
				if (ON_CLICK.equalsIgnoreCase(event)) {
					handleClick(object, event, field);
				} else {
					handleMouseOver(body, event, field, refId);
				}
			}

			/**
			 * The user clicked. Popup an alert showing what we know
			 * 
			 * @param object
			 *            The model object
			 * @param event
			 *            The event which was triggered
			 * @param field
			 *            identifier for the cell/field which triggered the
			 *            event
			 */
			private void handleClick(SampleModel object, String event,
					String field) {
				Window.alert("Event: " + event + "\nObject: "
						+ object.toString() + "\nField: " + field);
			}

			/**
			 * Toggle highlighting of the cells triggering the event
			 * @param body The root page element
			 * @param event The event triggered
			 * @param field Indicates the cell/field which triggered the event
			 * @param refId Identifier for the row and model object
			 */
			private void handleMouseOver(final Element body, String event,
					String field, String refId) {
				// demonstrates that we know what row the event was
				// on
				com.google.gwt.dom.client.Element elementForRow = body
						.getOwnerDocument().getElementById(refId);
				com.google.gwt.dom.client.Element firstChild = elementForRow
						.getFirstChildElement();
				// demonstrates field dependent behavior
				com.google.gwt.dom.client.Element elementForField = NAME
						.equalsIgnoreCase(field) ? firstChild : firstChild
						.getNextSiblingElement().getNextSiblingElement()
						.getNextSiblingElement().getNextSiblingElement();
				// demonstrates event dependent behavior
				if (ON_MOUSE_OVER.equalsIgnoreCase(event)) {
					elementForField.addClassName(HIGHLIGHT);
				} else {
					elementForField.removeClassName(HIGHLIGHT);
				}
			}
		};
	}

}
