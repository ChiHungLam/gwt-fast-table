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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.jwh.gwt.fasttable.client.CellEvent;
import com.jwh.gwt.fasttable.client.CellEvent.OnEvent;
import com.jwh.gwt.fasttable.client.CellHandlerWrapper;
import com.jwh.gwt.fasttable.client.CellListener;
import com.jwh.gwt.fasttable.client.TableBuilder;
import com.jwh.gwt.fasttable.client.exception.AbortOperation;
import com.jwh.gwt.fasttable.client.exception.NotFound;
import com.jwh.gwt.fasttable.client.selection.SelectionListener;
import com.jwh.gwt.fasttable.client.stream.HtmlFactory.HtmlElement;
import com.jwh.gwt.fasttable.client.stream.HtmlFactory.Tag;
import com.jwh.gwt.fasttable.client.util.LabelValueUtil;
import com.jwh.gwt.fasttable.client.util.Style;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * This builds the sample "Fast Table" web page
 */
public class FastTableSample implements EntryPoint, SampleStyle {

	private static final String COLUMN_ID = "ID";
	private static final String COLUMN_NAME = "Name";
	private static final String COLUMN_ZIP = "Zip";
	private static final String COLUMN_STATE = "State";
	private static final String COLUMN_CITY = "City";
	private static final String COLUMN_STREET = "Street";
	private RootPanel tablePanel;
	private TableBuilder<SampleModel> builder;

	private CellListener<SampleModel> buildCellListener() {
		return new CellListener<SampleModel>() {

			/**
			 * @param event
			 */
			@Override
			public void handlerCellEvent(final CellEvent<SampleModel> event) {
				final Document document = tablePanel.getElement().getOwnerDocument();
				switch (event.getOnEvent()) {
				case onClick:
					SelectionListener<SampleModel> listener = new SelectionListener<SampleModel>() {
						@Override
						public void select(SampleModel object) {
							try {
								showDetailPanel(event, document);
							} catch (NotFound e) {
							}
						}

						@Override
						public void unselect(SampleModel object) {
							try {
								builder.findRowElement(object).getNextSibling().removeFromParent();
							} catch (NotFound e) {
							}
						}
					};
					try {
						final Element rowElement = event.getRowElement(document);
						final Element columnElement = rowElement.getFirstChildElement();
						// Test multi vs single select by changing this flag
						boolean isMultiSelect = true;
						isMultiSelect = false;
						if (isMultiSelect) {
							builder.multiSelect(columnElement, event.domainObject, listener);
						} else {
							builder.singleSelect(columnElement, event.domainObject, listener);
						}
					} catch (NotFound e) {
					} catch (AbortOperation e) {
					}
					break;
				case onMouseOver:
					try {
						event.getRowElement(document).addClassName(HIGHLIGHT);
					} catch (final NotFound e1) {
					}
					break;
				case onMouseOut:
					try {
						event.getRowElement(document).removeClassName(HIGHLIGHT);
					} catch (final NotFound e1) {
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
			 * @throws NotFound
			 */
			private void showDetailPanel(CellEvent<SampleModel> event, final Document document)
					throws NotFound {
				final SampleModel d = event.getDomainObject();
				final TableRowElement newRow = event.insertRowAfter(document);
				final TableCellElement td = document.createTDElement();
				td.setAttribute("colspan", "6");
				newRow.appendChild(td);
				try {
					LabelValueUtil util = new LabelValueUtil();
					util.table.setStyle(Style.BORDER_NONE);
					util.labelValue("Name", d.name);
					util.prepareAttribute("rowspan", "2");
					final String buttonId = util.button("OK");
					util.newRow();
					util.labelValue("Street", d.street);
					util.newRow();
					util.labelValue("City, State ", d.city + ", " + d.state);
					util.newRow();
					util.labelValue("Zip", d.zip);
					util.newRow();
					final String html = util.toHtml();
					td.setInnerHTML(html);
					final Element okButton = document.getElementById(buttonId);
					DOM.setEventListener((com.google.gwt.user.client.Element) okButton, new EventListener() {
						@Override
						public void onBrowserEvent(Event event) {
							switch (event.getTypeInt()) {
							case Event.ONMOUSEDOWN:
								Window.alert("This demonstrates how to attach events to content created with setInnerHtml()");
								break;
							case Event.ONMOUSEOVER:
								okButton.addClassName(Style.BUTTON_OVER);
								break;
							case Event.ONMOUSEOUT:
								okButton.removeClassName(Style.BUTTON_OVER);
								break;
							default:
								break;
							}
						}
					});
					DOM.sinkEvents((com.google.gwt.user.client.Element) okButton, Event.ONMOUSEOUT
							| Event.ONMOUSEDOWN | Event.ONMOUSEOVER);
				} catch (Exception e) {
				}
			}
		};
	}

	private void buildTable() {
		builder = new TableBuilder<SampleModel>() {

			CellHandlerWrapper<SampleModel> cellHandler;

			@Override
			public void buildFooter(HtmlElement row) {
				row.addChild(Tag.td).addContents(COLUMN_NAME);
				row.addChild(Tag.td).addContents(COLUMN_STREET);
				row.addChild(Tag.td).addContents(COLUMN_CITY);
				row.addChild(Tag.td).addContents(COLUMN_STATE);
				row.addChild(Tag.td).addContents(COLUMN_ZIP);
				row.addChild(Tag.td).addContents(COLUMN_ID);
			}

			@Override
			public void buildHeader(TableBuilder<SampleModel>.SortableHeaderRow headerRow) {
				headerRow.newHeaderCell().addContents(COLUMN_NAME);
				headerRow.newHeaderCell(BORDER).addContents(COLUMN_STREET);
				headerRow.newHeaderCell().addContents(COLUMN_CITY);
				headerRow.newHeaderCell(BORDER).addContents(COLUMN_STATE);
				headerRow.newHeaderCell().addContents(COLUMN_ZIP);
				headerRow.newHeaderCell(BORDER).addContents(COLUMN_ID);
			}

			private CellHandlerWrapper<SampleModel> getCellHandler() {
				if (cellHandler == null) {
					final CellListener<SampleModel> listener = buildCellListener();
					cellHandler = table.registerCellHandler(listener, OnEvent.onClick, OnEvent.onMouseOver,
							OnEvent.onMouseOut);
				}
				return cellHandler;
			}

			@Override
			public Panel getContainingElement() {
				return tablePanel;
			}

			@Override
			public SortAction getSortAction(int column) {
				return SortAction.Ascending;
			}

			@Override
			protected void populateRowCells(SampleModel t, HtmlElement row, String refId) {
				row.addChild(Tag.td).setStyle(NAME, CURSOR_POINTER, UNSELECTED)
						.addHandler(getCellHandler(), refId, 1).addContents(t.name);
				row.addChild(Tag.td).setStyle(BORDER_OPEN_RIGHT).addContents(t.street);
				row.addChild(Tag.td).setStyle(BORDER_OPEN_LEFT_RIGHT).addContents(t.city);
				row.addChild(Tag.td).setStyle(BORDER_OPEN_LEFT).addContents(t.state);
				row.addChild(Tag.td).setStyle(BORDER, CURSOR_POINTER).addHandler(getCellHandler(), refId, 5)
						.addContents(t.zip);
				row.addChild(Tag.td).setStyle(BORDER_OPEN_LEFT).addContents(String.valueOf(t.sequenceNumber));
			}

			@Override
			public void sort(ArrayList<SampleModel> sortMe, int column, TableBuilder.SortAction action) {
				final SampleModelComparator comparator = new SampleModelComparator(column,
						action == SortAction.Ascending);
				Collections.sort(sortMe, comparator);
			}

		};
		builder.setContents(SampleModel.getSamples(1000));
		builder.updateView();
	}

	@Override
	public void onModuleLoad() {
		tablePanel = RootPanel.get("tableContainer");
		RootPanel.get("helpContainer").add(new Label("Under Construction"));
		buildTable();
	}

}
