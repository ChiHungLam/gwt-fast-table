/**
 * Copyright (c) 2011 Jim Heyne. All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which is 
 * available at {@link http://www.eclipse.org/legal/epl-v10.html}
 */

package com.jwh.gwt.fasttable.sample.client;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.jwh.gwt.fasttable.client.CellEvent;
import com.jwh.gwt.fasttable.client.CellEvent.OnEvent;
import com.jwh.gwt.fasttable.client.CellHandlerWrapper;
import com.jwh.gwt.fasttable.client.CellListener;
import com.jwh.gwt.fasttable.client.ElementNotFound;
import com.jwh.gwt.fasttable.client.Row;
import com.jwh.gwt.fasttable.client.TableBuilder;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FastTableSample implements EntryPoint, SampleStyle {


	private static final String COLUMN_NAME = "Name";
	private static final String COLUMN_ZIP = "Zip";
	private static final String COLUMN_STATE = "State";
	private static final String COLUMN_CITY = "City";
	private static final String COLUMN_STREET = "Street";
	private RootPanel tablePanel;

	@Override
	public void onModuleLoad() {
		tablePanel = RootPanel.get("tableContainer");
		RootPanel.get("helpContainer").add(new Label("Under Construction"));
		buildTable();
	}

	private void buildTable() {
		TableBuilder<SampleModel> builder = new TableBuilder<SampleModel>() {

			@Override
			public SortAction getSortAction(int column) {
				return SortAction.Ascending;
			}

			@Override
			public void buildFooter(Row row) {
				row.newCell().addContents(COLUMN_NAME);
				row.newCell().addContents(COLUMN_STREET);
				row.newCell().addContents(COLUMN_CITY);
				row.newCell().addContents(COLUMN_STATE);
				row.newCell().addContents(COLUMN_ZIP);
			}

			public void buildHeader(
					TableBuilder<SampleModel>.SortableHeaderRow headerRow) {
				headerRow.newHeaderCell(COLUMN_NAME);
				headerRow.newHeaderCell(COLUMN_STREET);
				headerRow.newHeaderCell(COLUMN_CITY);
				headerRow.newHeaderCell(COLUMN_STATE);
				headerRow.newHeaderCell(COLUMN_ZIP);
			}

			@Override
			protected void populateRowCells(SampleModel t, Row row, String refId) {
				row.newCell().setStyle(NAME)
						.addHandler(getCellHandler(), refId, 1).addContents(t.name);
				row.newCell().setStyle(BORDER_OPEN_RIGHT).addContents(t.street);
				row.newCell().setStyle(BORDER_OPEN_LEFT_RIGHT).addContents(t.city);
				row.newCell().setStyle(BORDER_OPEN_LEFT).addContents(t.state);
				row.newCell().setStyle(BORDER)
						.addHandler(getCellHandler(), refId, 5).addContents(t.zip);
			}

			CellHandlerWrapper<SampleModel> cellHandler;

			private CellHandlerWrapper<SampleModel> getCellHandler() {
				if (cellHandler == null) {
					CellListener<SampleModel> listener = buildCellListener();
					cellHandler = table.registerCellHandler(listener,
							OnEvent.onClick, OnEvent.onMouseOver,
							OnEvent.onMouseOut);
				}
				return cellHandler;
			}

			@Override
			public void sort(ArrayList<SampleModel> sortMe, int column,
					TableBuilder.SortAction action) {
				SampleModelComparator comparator = new SampleModelComparator(
						column, action == SortAction.Ascending);
				Collections.sort(sortMe, comparator);
			}

			@Override
			public Panel getContainingElement() {
				return tablePanel;
			}
			
		};
		builder.setContents(SampleModel.getSamples(1000));
		builder.updateView();
	}

	private CellListener<SampleModel> buildCellListener() {
		return new CellListener<SampleModel>() {

			@Override
			public void handlerCellEvent(CellEvent<SampleModel> event) {
				switch (event.getOnEvent()) {
				case onClick:
					Window.alert("Event: " + event.getOnEvent() + "\nObject: "
							+ event.domainObject + "\nColumn: "
							+ event.column);
					break;
				case onMouseOver:
					try {
						event.getColumnElement(
								tablePanel.getElement().getOwnerDocument())
								.addClassName(HIGHLIGHT);
					} catch (ElementNotFound e1) {
					}
					break;
				case onMouseOut:
					try {
						event.getColumnElement(
								tablePanel.getElement().getOwnerDocument())
								.removeClassName(HIGHLIGHT);
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
