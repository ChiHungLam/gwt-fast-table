package com.jwh.gwt.design.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.jwh.gwt.design.shared.html.TagUtil;
import com.jwh.gwt.fasttable.client.element.HtmlFactory.Tag;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Design implements EntryPoint, ValueChangeHandler<String>, EventListener, Style {

	public enum Operation  {tableInsert, tableHeaderRow, tableRow, tableFooterRow, tableCell, moveUp, moveDown, label, value, labelValue, binderId, styleAdd, styleRemove}
	
	private RootPanel statesPanel;
	private RootPanel targetPanel;
	private RootPanel toolsPanel;

	Element currentElement;
	private SuggestBox commandBox;
	
	@Override
	public void onModuleLoad() {
		statesPanel = RootPanel.get("statesPanel");
		targetPanel = RootPanel.get("targetPanel");
		toolsPanel = RootPanel.get("toolsPanel");
		buildToolsPanel();
		initializeTargetPanel();
	}

	private void initializeTargetPanel() {
		currentElement = targetPanel.getElement();
		addOnClick(currentElement);
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (event.getTypeInt()) {
		case Event.ONCLICK:
			EventTarget eventTarget = event.getEventTarget();
			// TODO revisit cast
			Element newElement = (Element) Element.as(eventTarget);
			setCurrentElement(newElement);
			commandBox.setFocus(true);
			break;

		default:
			break;
		}				
	}

	private void setCurrentElement(Element newElement) {
		if (newElement != currentElement) {
			currentElement.removeClassName(CURRENT_TARGET);
			newElement.addClassName(CURRENT_TARGET);
			currentElement = newElement;
		}
	}			

	
	private void addOnClick(Element element) {
		DOM.setEventListener(element, this);
	}

	private void buildToolsPanel() {
		final VerticalPanel verticalPanel = new VerticalPanel();
		toolsPanel.add(verticalPanel);
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		for (Operation operation : Operation.values()) {
			oracle.add(operation.name());
		}
		commandBox = new SuggestBox(oracle);
		verticalPanel.add(commandBox);
		commandBox.addValueChangeHandler(this);
	}

	/* 
	 * An operation has been selected
	 * 
	 * (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
	 */
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		try {
			doOperation(Operation.valueOf(event.getValue()));
		} catch (Exception e) {
			System.out.println(event.getValue());
			e.printStackTrace();
		}
		
	}

	private void doOperation(final Operation op) {
		switch (op) {
		case tableInsert:
			Element table = DOM.createTable();
			currentElement.insertAfter(table, null);
			setCurrentElement(table);
			break;
		case tableRow:
			Element tbody = TagUtil.assureParents(currentElement, Tag.tbody, Tag.table);
			TagUtil.appendChild(tbody, Tag.tr);
			break;
		default:
			break;
		}
	}


}
