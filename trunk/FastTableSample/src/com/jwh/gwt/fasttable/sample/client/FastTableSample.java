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
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.requestfactory.shared.impl.FindRequest;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.jwh.gwt.fasttable.client.CellEvent;
import com.jwh.gwt.fasttable.client.CellEvent.OnEvent;
import com.jwh.gwt.fasttable.client.CellHandlerWrapper;
import com.jwh.gwt.fasttable.client.CellListener;
import com.jwh.gwt.fasttable.client.ElementNotFound;
import com.jwh.gwt.fasttable.client.Row;
import com.jwh.gwt.fasttable.client.Style;
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
	private TableBuilder<SampleModel> builder;

	@Override
	public void onModuleLoad() {
		tablePanel = RootPanel.get("tableContainer");
		RootPanel.get("helpContainer").add(new Label("Under Construction"));
		buildTable();
	}

	private void buildTable() {
		builder = new TableBuilder<SampleModel>() {

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

			public void buildHeader(TableBuilder<SampleModel>.SortableHeaderRow headerRow) {
				headerRow.newHeaderCell().addContents(COLUMN_NAME);
				headerRow.newHeaderCell(BORDER).addContents(COLUMN_STREET);
				headerRow.newHeaderCell().addContents(COLUMN_CITY);
				headerRow.newHeaderCell(BORDER).addContents(COLUMN_STATE);
				headerRow.newHeaderCell().addContents(COLUMN_ZIP);
			}

			@Override
			protected void populateRowCells(SampleModel t, Row row, String refId) {
				row.newCell().setStyle(NAME, CURSOR_POINTER, UNSELECTED)
						.addHandler(getCellHandler(), refId, 1).addContents(t.name);
				row.newCell().setStyle(BORDER_OPEN_RIGHT).addContents(t.street);
				row.newCell().setStyle(BORDER_OPEN_LEFT_RIGHT).addContents(t.city);
				row.newCell().setStyle(BORDER_OPEN_LEFT).addContents(t.state);
				row.newCell().setStyle(BORDER, CURSOR_POINTER).addHandler(getCellHandler(), refId, 5)
						.addContents(t.zip);
			}

			CellHandlerWrapper<SampleModel> cellHandler;

			private CellHandlerWrapper<SampleModel> getCellHandler() {
				if (cellHandler == null) {
					CellListener<SampleModel> listener = buildCellListener();
					cellHandler = table.registerCellHandler(listener, OnEvent.onClick, OnEvent.onMouseOver,
							OnEvent.onMouseOut);
				}
				return cellHandler;
			}

			@Override
			public void sort(ArrayList<SampleModel> sortMe, int column, TableBuilder.SortAction action) {
				SampleModelComparator comparator = new SampleModelComparator(column,
						action == SortAction.Ascending);
				Collections.sort(sortMe, comparator);
			}

			@Override
			public Panel getContainingElement() {
				return tablePanel;
			}

		};
		builder.setContents(SampleModel.getSamples(500));
		builder.updateView();
	}

	private CellListener<SampleModel> buildCellListener() {
		return new CellListener<SampleModel>() {

			@Override
			public void handlerCellEvent(CellEvent<SampleModel> event) {
				final Document document = tablePanel.getElement().getOwnerDocument();
				switch (event.getOnEvent()) {
				case onClick:
					try {
						final Element rowElement = event.getRowElement(document);
						final Element columnElement = rowElement.getFirstChildElement();
						SampleModel previousSelection = builder.singleSelection(columnElement,
								event.domainObject);
						if (previousSelection == null || previousSelection != event.getDomainObject()) {
							showDetailPanel(event, document);
						}
						if (previousSelection != null && previousSelection != event.getDomainObject()) {
							builder.findRowElement(previousSelection).getNextSibling().removeFromParent();
						}
					} catch (ElementNotFound e) {
					}
					break;
				case onMouseOver:
					try {
						event.getRowElement(document).addClassName(HIGHLIGHT);
					} catch (ElementNotFound e1) {
					}
					break;
				case onMouseOut:
					try {
						event.getRowElement(document).removeClassName(HIGHLIGHT);
					} catch (ElementNotFound e1) {
					}
					break;
				default:
					break;
				}

			}

			/**
			 * Show a panel under the selected item
			 * 
			 * @param event
			 * @param document
			 * @throws ElementNotFound
			 */
			private void showDetailPanel(CellEvent<SampleModel> event, final Document document)
					throws ElementNotFound {
				final TableRowElement newRow = event.insertRowAfter(document);
				final TableCellElement td = document.createTDElement();
				td.setAttribute("colspan", "5");
				newRow.appendChild(td);
				final SampleModel domainObject = event.getDomainObject();
				VerticalPanel v = new VerticalPanel();
				td.appendChild(v.getElement());
				v.add(new Label("Name: " + domainObject.name));
				v.add(new Label("Street: " + domainObject.street));
				v.add(new Label("City/State: " + domainObject.city + ", " + domainObject.state));
				v.add(new Label("Zip: " + domainObject.zip));
			}
		};
	}

}
