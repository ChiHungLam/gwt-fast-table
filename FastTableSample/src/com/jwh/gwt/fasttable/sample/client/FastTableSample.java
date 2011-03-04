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
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.Window.ScrollHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.jwh.gwt.fasttable.client.CellEvent;
import com.jwh.gwt.fasttable.client.CellEvent.OnEvent;
import com.jwh.gwt.fasttable.client.CellHandlerWrapper;
import com.jwh.gwt.fasttable.client.CellListener;
import com.jwh.gwt.fasttable.client.IncrementalStrategy;
import com.jwh.gwt.fasttable.client.TableBuilder;
import com.jwh.gwt.fasttable.client.element.HtmlFactory.HtmlElement;
import com.jwh.gwt.fasttable.client.element.HtmlFactory.Tag;
import com.jwh.gwt.fasttable.client.exception.AbortOperation;
import com.jwh.gwt.fasttable.client.exception.NotFound;
import com.jwh.gwt.fasttable.client.selection.SelectionListener;
import com.jwh.gwt.fasttable.client.util.LabelValueUtil;
import com.jwh.gwt.fasttable.client.util.Logger;
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
	final Logger logger = new Logger();
	private RootPanel optionsPanel;

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
				td.addClassName(DROP_PANEL);
				// Note: "colSpan" must have uppercase 'S' for IE
				td.setAttribute("colSpan", "6");
				newRow.appendChild(td);
				try {
					LabelValueUtil util = new LabelValueUtil();
					util.table.setStyle(Style.BORDER_NONE);
					util.labelValue("Name", d.name);
					util.prepareAttribute("rowSpan", "2");
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

			public void logError(String message) {
				logger.logError(message);
			};

			public void logInfo(String message) {
				logger.logInfo(message);
			};

			@Override
			protected void populateRowCells(SampleModel t, HtmlElement row, String refId, int rowNumber) {
				row.setStyle(rowNumber % 2 == 0 ? Style.EVEN : Style.ODD);
				row.addHandler(getCellHandler(), refId, 0);
				row.addChild(Tag.td).setStyle(NAME, CURSOR_POINTER, UNSELECTED).addContents(t.name);
				row.addChild(Tag.td).setStyle(BORDER_OPEN_RIGHT, NO_WRAP).addContents(t.street);
				row.addChild(Tag.td).setStyle(BORDER_OPEN_LEFT_RIGHT).addContents(t.city);
				row.addChild(Tag.td).setStyle(BORDER_OPEN_LEFT).addContents(t.state);
				row.addChild(Tag.td).setStyle(BORDER).addContents(t.zip);
				row.addChild(Tag.td).setStyle(BORDER_OPEN_LEFT).addContents(String.valueOf(t.sequenceNumber));
				listenForFinishedBuilding = true;
			}

			@Override
			public void sort(ArrayList<SampleModel> sortMe, int column, TableBuilder.SortAction action) {
				final SampleModelComparator comparator = new SampleModelComparator(column,
						action == SortAction.Ascending);
				Collections.sort(sortMe, comparator);
			}

		};
		showSamples();
	}

	private void showSamples() {
		logger.clear();
		builder.logInfo("Start building samples");
		final ArrayList<SampleModel> samples = SampleModel.getSamples(sampleRowCount);
		builder.logInfo("Set table contents");
		builder.setContents(samples);
		builder.updateView();
	}

	int sampleRowCount = 100;
	boolean listenForFinishedBuilding = false;
	
	@Override
	public void onModuleLoad() {		
		//TODO need better switch
		if (Window.Location.getHref().indexOf("FastTableSample") < 0 && Window.Location.getHref().indexOf("fast-table-sample") < 0) {
			return;
		}
		tablePanel = RootPanel.get("tableContainer");
		optionsPanel = RootPanel.get("optionsContainer");
		Window.addWindowScrollHandler(new ScrollHandler() {			
			@Override
			public void onWindowScroll(ScrollEvent event) {
				if (listenForFinishedBuilding) {
					builder.logInfo("Scrolled. Done drawing?");
				}
				listenForFinishedBuilding = false;
			}
		});
		logger.setParent(RootPanel.get("loggerContainer"));
		buildTable();
		buildOptionsPanel();
	}

	private void buildOptionsPanel() {
		final VerticalPanel verticalPanel = new VerticalPanel();
		optionsPanel.add(verticalPanel);
		buildRowCountButtons(verticalPanel);
		buildIncrementalStrategy(verticalPanel);
		buildInitialSize(verticalPanel);
		buildIncrementSize(verticalPanel);
		buildPushButton(verticalPanel);
	}

	private void buildIncrementSize(VerticalPanel verticalPanel) {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		horizontalPanel.add(new Label("Increment row count"));
		final ListBox box = new ListBox();
		horizontalPanel.add(box);		
		int selection = builder.configuration.getSubsequentIncrement();
		Integer[] options = new Integer[] {25, 50, 75, 100, 125, 150, Integer.MAX_VALUE};
		for (Integer integer : options) {
			addOption(box, integer, selection);
		}
		box.addChangeHandler(new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = box.getSelectedIndex();
				if (selectedIndex >= 0) {
					Integer increment = Integer.valueOf(box.getValue(selectedIndex));
					builder.configuration.subsequentIncrement = increment;
				}				
			}
		});
	}

	private void addOption(ListBox box, Integer integer, int selection) {
		box.addItem(String.valueOf(integer));
		if (selection == integer) {
			box.setSelectedIndex(box.getItemCount() - 1);
		}		
	}

	private void buildInitialSize(VerticalPanel verticalPanel) {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		horizontalPanel.add(new Label("Initial increment row count"));
		final ListBox box = new ListBox();
		horizontalPanel.add(box);		
		int selection = builder.configuration.getInitialIncrement();
		Integer[] options = new Integer[] {25, 50, 75, 100, 125, 150, Integer.MAX_VALUE};
		for (Integer integer : options) {
			addOption(box, integer, selection);
		}
		box.addChangeHandler(new ChangeHandler() {			
			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = box.getSelectedIndex();
				if (selectedIndex >= 0) {
					Integer increment = Integer.valueOf(box.getValue(selectedIndex));
					builder.configuration.initialIncrement = increment;
				}				
			}
		});
	}

	private void buildPushButton(final VerticalPanel verticalPanel) {
		verticalPanel.add(new Label());
		final PushButton refreshButton = new PushButton("Refresh Table");
		refreshButton.setTitle("Build new sample objects and update the table, using options specified above");
		refreshButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				showSamples();
			}
		});
		refreshButton.addStyleName(BUTTON);
		refreshButton.addStyleName(CURSOR_POINTER);
		verticalPanel.add(refreshButton);
	}

	private void buildIncrementalStrategy(VerticalPanel verticalPanel) {
		verticalPanel.add(new Label());
		final String none = "Turn off incremental builder";
		final String replace = "Replace original rows with incremental rows";			
		final String append = "Append incremental rows";
		final ValueChangeHandler<Boolean> handler = new ValueChangeHandler<Boolean>() {	
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				RadioButton selected = (RadioButton) event.getSource();
				if (none.equals(selected.getFormValue())) {
					builder.configuration.incrementalStrategy = IncrementalStrategy.NONE;
				} else if (replace.equals(selected.getFormValue())) {
					builder.configuration.incrementalStrategy = IncrementalStrategy.REPLACE;
				} else if (append.equals(selected.getFormValue())) {
					builder.configuration.incrementalStrategy = IncrementalStrategy.APPEND;
				} 
			}
		};
		final String incrementalStrategy = "incrementalStrategy";
		{
			final RadioButton radio = new RadioButton(incrementalStrategy);
			radio.setHTML(none);
			radio.setValue(builder.configuration.incrementalStrategy == IncrementalStrategy.NONE, false);
			radio.setFormValue(none);
			radio.addValueChangeHandler(handler);
			verticalPanel.add(radio);
			radio.getElement().getParentElement().addClassName(BORDER_NONE);
		}
		{
			final RadioButton radio = new RadioButton(incrementalStrategy);
			radio.setHTML(append);
			radio.setValue(builder.configuration.incrementalStrategy == IncrementalStrategy.APPEND, false);
			radio.setFormValue(append);
			radio.addValueChangeHandler(handler);
			verticalPanel.add(radio);
			radio.getElement().getParentElement().addClassName(BORDER_NONE);
		}
		{
			final RadioButton radio = new RadioButton(incrementalStrategy);
			radio.setHTML(replace);
			radio.setValue(builder.configuration.incrementalStrategy == IncrementalStrategy.REPLACE, false);
			radio.setFormValue(replace);
			radio.addValueChangeHandler(handler);
			verticalPanel.add(radio);
			radio.getElement().getParentElement().addClassName(BORDER_NONE);
		}
	}

	private void buildRowCountButtons(VerticalPanel verticalPanel) {
		final String rows10 = "10 sample rows";
		final String rows100 = "100 sample rows";
		final String rows500 = "500 sample rows";
		final String rows1000 = "1000 sample rows";
		final String rowsToBuild = "rowsToBuild";
		final ValueChangeHandler<Boolean> handler = new ValueChangeHandler<Boolean>() {				
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				RadioButton selected = (RadioButton) event.getSource();
				if (rows10.equals(selected.getFormValue())) {
					sampleRowCount = 10;
				} else if (rows100.equals(selected.getFormValue())) {
					sampleRowCount = 100;
				} else if (rows500.equals(selected.getFormValue())) {
					sampleRowCount = 500;
				} else {
					sampleRowCount = 1000;
				}
			}
		};
		{
			final RadioButton radio = new RadioButton(rowsToBuild);
			radio.setHTML(rows10);
			radio.setValue(false, false);
			radio.setFormValue(rows10);
			radio.addValueChangeHandler(handler);
			verticalPanel.add(radio);
			radio.getElement().getParentElement().addClassName(BORDER_NONE);
		}
		{
			final RadioButton radio = new RadioButton(rowsToBuild);
			radio.setHTML(rows100);
			radio.setValue(true, false);
			radio.setFormValue(rows100);
			radio.addValueChangeHandler(handler);
			verticalPanel.add(radio);
			radio.getElement().getParentElement().addClassName(BORDER_NONE);
		}
		{
			final RadioButton radio = new RadioButton(rowsToBuild);
			radio.setHTML(rows500);
			radio.setValue(false, false);
			radio.setFormValue(rows500);
			radio.addValueChangeHandler(handler);
			verticalPanel.add(radio);
			radio.getElement().getParentElement().addClassName(BORDER_NONE);
		}
		{
			final RadioButton radio = new RadioButton(rowsToBuild);
			radio.setHTML(rows1000);
			radio.setValue(false, false);
			radio.setFormValue(rows1000);
			radio.addValueChangeHandler(handler);
			verticalPanel.add(radio);
			radio.getElement().getParentElement().addClassName(BORDER_NONE);
		}
	}
}
